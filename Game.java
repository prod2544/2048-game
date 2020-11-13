import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Game {

    //คลาสเอนทิตีสำหรับจัดเก็บสี
    private static class Color {
        public Color(int fc, int bgc) {
            fontColor = fc;//สีตัวอักษร
            bgColor = bgc;//สีพื้นหลัง
        }

        public int fontColor;//สีตัวอักษร
        public int bgColor;//สีพื้นหลัง
    }

    JFrame mainFrame;//วัตถุหน้าต่างหลัก
    JLabel[] jLabels;//Square ให้ใช้ jlabel แทน
    int[] datas = new int[]{0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0};//มูลค่าในแต่ละตาราง
    int[] temp = new int[4];//อาร์เรย์ชั่วคราวที่ดึงออกมาจากอัลกอริทึมการเคลื่อนที่ของบล็อก
    int[] temp2 = new int[16];//ใช้เพื่อตรวจสอบว่ามีการผสานสี่เหลี่ยมจัตุรัสหรือไม่


    List emptyBlocks = new ArrayList<Integer>(16);//รายการชั่วคราวที่ใช้เมื่อสร้างบล็อกใหม่เพื่อจัดเก็บบล็อกว่าง

    //สีของmap
    static HashMap<Integer, Color> colorMap = new HashMap<Integer, Color>() {{
        put(0, new Color(0x776e65, 0xCDC1B4));
        put(2, new Color(0x776e65, 0xeee4da));
        put(4, new Color(0x776e65, 0xede0c8));
        put(8, new Color(0xf9f6f2, 0xf2b179));
        put(16, new Color(0xf9f6f2, 0xf59563));
        put(32, new Color(0xf9f6f2, 0xf67c5f));
        put(64, new Color(0xf9f6f2, 0xf65e3b));
        put(128, new Color(0xf9f6f2, 0xedcf72));
        put(256, new Color(0xf9f6f2, 0xedcc61));
        put(512, new Color(0xf9f6f2, 0xe4c02a));
        put(1024, new Color(0xf9f6f2, 0xe2ba13));
        put(2048, new Color(0xf9f6f2, 0xecc400));
    }};

    public Game() {
        initGameFrame();
        initGame();
        refresh();
    }

    //สร้าง 2 2 สแควร์สและ 4 สแควร์เริ่มต้น
    private void initGame() {
        for (int i = 0; i < 2; i++) {
            generateBlock(datas, 2);
        }
        generateBlock(datas, 4);
    }

    //สร้างสี่เหลี่ยม 4 หรือ 2 แบบสุ่ม
    private void randomGenerate(int arr[]) {
        int ran = (int) (Math.random() * 10);
        if (ran > 5) {
            generateBlock(arr, 4);
        } else {
            generateBlock(arr, 2);
        }

    }

    //สุ่มสร้างบล็อกใหม่พารามิเตอร์: มูลค่าของบล็อกที่จะสร้าง
    private void generateBlock(int arr[], int num) {
        emptyBlocks.clear();

        for (int i = 0; i < 16; i++) {
            if (arr[i] == 0) {
                emptyBlocks.add(i);
            }
        }
        int len = emptyBlocks.size();
        if (len == 0) {
            return;
        }
        int pos = (int) (Math.random() * 100) % len;
        arr[(int) emptyBlocks.get(pos)] = num;
        refresh();

    }


    //ชนะหรือแพ้และจัดการกับเกม
    private void judge(int arr[]) {

        if (isWin(arr)) {
            JOptionPane.showMessageDialog(null, "ขอแสดงความยินดีคุณสร้างสี่เหลี่ยม2048สำเร็จ "," คุณชนะแล้ว", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }
        if (isEnd(arr)) {
            int max = getMax(datas);
            JOptionPane.showMessageDialog(null, "ขออภัยคุณไม่สามารถประกอบเป็นสี่เหลี่ยมจัตุรัส2048ได้ คะแนนสูงสุดของคุณคือ" + max, "จบเกม", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }

    }

    //ตัดสินว่าผู้เล่นชนะตราบใดที่มีสี่เหลี่ยมมากกว่าหรือเท่ากับ 2048 นั่นคือชัยชนะ
    private boolean isWin(int arr[]) {
        for (int i : arr) {
            if (i >= 2048) {
                return true;
            }
        }
        return false;

    }

    //ฟังก์ชั่นนี้ใช้เพื่อตัดสินว่าเกมจบลงหรือไม่หากไม่สามารถสร้างบล็อกว่างได้หลังจากเลื่อนขึ้นลงซ้ายและขวานั่นหมายความว่าบล็อกนั้นเต็มแล้วจะส่งคืนจริงซึ่งหมายความว่าเกมจะจบลง
    private boolean isEnd(int arr[]) {

        int[] tmp = new int[16];
        int isend = 0;

        System.arraycopy(arr, 0, tmp, 0, 16);
        left(tmp);
        if (isNoBlank(tmp)) {
            isend++;
        }

        System.arraycopy(arr, 0, tmp, 0, 16);
        right(tmp);
        if (isNoBlank(tmp)) {
            isend++;
        }

        System.arraycopy(arr, 0, tmp, 0, 16);
        up(tmp);
        if (isNoBlank(tmp)) {
            isend++;
        }

        System.arraycopy(arr, 0, tmp, 0, 16);
        down(tmp);
        if (isNoBlank(tmp)) {
            isend++;
        }

        return isend == 4;
    }

    //ตรวจสอบว่าไม่มีสี่เหลี่ยมว่างหรือไม่
    private boolean isNoBlank(int arr[]) {

        for (int i : arr) {
            if (i == 0) {
                return false;
            }
        }
        return true;
    }

    //รับค่าบล็อกที่ใหญ่ที่สุด
    private int getMax(int arr[]) {
        int max = arr[0];
        for (int i : arr) {
            if (i >= max) {
                max = i;
            }
        }
        return max;
    }

    //รีเฟรชข้อมูลที่แสดงในแต่ละตาราง
    private void refresh() {
        JLabel j;
        for (int i = 0; i < 16; i++) {
            int arr = datas[i];
            j = jLabels[i];
            if (arr == 0) {
                j.setText("");
            } else if (arr >= 1024) {
                j.setFont(new java.awt.Font("Dialog", 1, 42));
                j.setText(String.valueOf(datas[i]));
            } else {
                j.setFont(new java.awt.Font("Dialog", 1, 50));
                j.setText(String.valueOf(arr));
            }

            Color currColor = colorMap.get(arr);
            j.setBackground(new java.awt.Color(currColor.bgColor));
            j.setForeground(new java.awt.Color(currColor.fontColor));
        }
    }

    //เริ่มต้นหน้าต่างเกมและดำเนินการบางอย่างที่ซับซ้อน
    private void initGameFrame() {

        //สร้าง JFrame และทำการตั้งค่าบางอย่าง
        mainFrame = new JFrame("2048 Game");
        mainFrame.setSize(500, 500);
        mainFrame.setResizable(false);//ขนาดหน้าต่างคงที่
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        mainFrame.setLayout(new GridLayout(4, 4));
        mainFrame.getContentPane().setBackground(new java.awt.Color(0xCDC1B4));
        //เพิ่มการตรวจสอบปุ่ม
        mainFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

                System.arraycopy(datas, 0, temp2, 0, 16);

                //เรียกใช้ฟังก์ชันการประมวลผลที่แตกต่างกันตามคีย์ต่างๆ
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        up(datas);
                        break;

                    case KeyEvent.VK_DOWN:
                        down(datas);
                        break;

                    case KeyEvent.VK_LEFT:
                        left(datas);
                        break;

                    case KeyEvent.VK_RIGHT:
                        right(datas);
                        break;

                }


                //ตรวจสอบว่ามีการผสานบล็อกหลังการย้ายหรือไม่หากมีบล็อกใหม่หากไม่มีจะไม่มีการสร้างบล็อกใหม่
                if (!Arrays.equals(datas, temp2)) {
                    randomGenerate(datas);
                }

                refresh();
                judge(datas);
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        });

        //ใช้สไตล์ UI เริ่มต้นของระบบ
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        //ใช้ 16 JLabel เพื่อแสดง 16 ช่องสี่เหลี่ยม
        jLabels = new JLabel[16];
        JLabel j; //ใช้การอ้างอิงซ้ำหลีกเลี่ยงการสร้างการอ้างอิงมากเกินไปสำหรับ
        for (int i = 0; i < 16; i++) {
            jLabels[i] = new JLabel("0", JLabel.CENTER);
            j = jLabels[i];
            j.setOpaque(true);
            // ตั้งค่าเส้นขอบพารามิเตอร์: ด้านบนซ้ายล่างขวาสีของเส้นขอบ
            j.setBorder(BorderFactory.createMatteBorder(6, 6, 6, 6, new java.awt.Color(0xBBADA0)));

            //j.setForeground(new java.awt.Color(0x776E65));
            j.setFont(new java.awt.Font("Dialog", 1, 52));
            mainFrame.add(j);
        }
        mainFrame.setVisible(true);
    }

    private void left(int arr[]) {
        moveLeft(arr);

        combineLeft(arr);

        moveLeft(arr);//หลังจากผสานแล้วจะมีช่องว่างให้เลื่อนไปทางซ้ายอีกครั้ง


    }

    //รวมช่องทางซ้าย
    private void combineLeft(int arr[]) {
        for (int l = 0; l < 4; l++) {
            //0 1 2
            for (int i = 0; i < 3; i++) {
                if ((arr[l * 4 + i] != 0 && arr[l * 4 + i + 1] != 0) && arr[l * 4 + i] == arr[l * 4 + i + 1]) {
                    arr[l * 4 + i] *= 2;
                    arr[l * 4 + i + 1] = 0;
                }
            }
        }
    }

    //ย้ายกล่องไปทางซ้ายใช้อาร์เรย์ชั่วคราวสำหรับแต่ละแถวเพื่อเลื่อนไปทางซ้าย
    private void moveLeft(int arr[]) {
        for (int l = 0; l < 4; l++) {


            int z = 0, fz = 0;//z (0); fz (ไม่ใช่0)
            for (int i = 0; i < 4; i++) {
                if (arr[l * 4 + i] == 0) {
                    z++;
                } else {
                    temp[fz] = arr[l * 4 + i];
                    fz++;
                }
            }
            for (int i = fz; i < 4; i++) {
                temp[i] = 0;
            }
            for (int j = 0; j < 4; j++) {
                arr[l * 4 + j] = temp[j];
            }
        }
    }

    private void right(int arr[]) {

        moveRight(arr);
        combineRight(arr);
        moveRight(arr);

    }

    private void combineRight(int arr[]) {
        for (int l = 0; l < 4; l++) {
            //3 2 1
            for (int i = 3; i > 0; i--) {
                if ((arr[l * 4 + i] != 0 && arr[l * 4 + i - 1] != 0) && arr[l * 4 + i] == arr[l * 4 + i - 1]) {
                    arr[l * 4 + i] *= 2;
                    arr[l * 4 + i - 1] = 0;
                }
            }
        }
    }

    private void moveRight(int arr[]) {

        for (int l = 0; l < 4; l++) {

            int z = 3, fz = 3;//z (0); fz (ไม่ใช่0)
            for (int i = 3; i >= 0; i--) {
                if (arr[l * 4 + i] == 0) {
                    z--;
                } else {
                    temp[fz] = arr[l * 4 + i];
                    fz--;
                }
            }
            for (int i = fz; i >= 0; i--) {
                temp[i] = 0;
            }
            for (int j = 3; j >= 0; j--) {
                arr[l * 4 + j] = temp[j];
            }
        }
    }


    private void up(int arr[]) {
        moveUp(arr);
        combineUp(arr);
        moveUp(arr);

    }

    private void combineUp(int arr[]) {


        for (int r = 0; r < 4; r++) {
            for (int i = 0; i < 3; i++) {
                if ((arr[r + 4 * i] != 0 && arr[r + 4 * (i + 1)] != 0) && arr[r + 4 * i] == arr[r + 4 * (i + 1)]) {
                    arr[r + 4 * i] *= 2;
                    arr[r + 4 * (i + 1)] = 0;
                }
            }
        }
    }

    private void moveUp(int arr[]) {

        for (int r = 0; r < 4; r++) {

            int z = 0, fz = 0;//z (0); fz (ไม่ใช่0)
            for (int i = 0; i < 4; i++) {
                if (arr[r + 4 * i] == 0) {
                    z++;
                } else {
                    temp[fz] = arr[r + 4 * i];
                    fz++;
                }
            }
            for (int i = fz; i < 4; i++) {
                temp[i] = 0;
            }
            for (int j = 0; j < 4; j++) {
                arr[r + 4 * j] = temp[j];
            }
        }
    }


    private void down(int arr[]) {
        moveDown(arr);
        combineDown(arr);
        moveDown(arr);
    }

    private void combineDown(int arr[]) {
        for (int r = 0; r < 4; r++) {
            for (int i = 3; i > 0; i--) {
                if ((arr[r + 4 * i] != 0 && arr[r + 4 * (i - 1)] != 0) && arr[r + 4 * i] == arr[r + 4 * (i - 1)]) {
                    arr[r + 4 * i] *= 2;
                    arr[r + 4 * (i - 1)] = 0;
                }
            }
        }
    }

    private void moveDown(int arr[]) {
        for (int r = 0; r < 4; r++) {

            int z = 3, fz = 3;//z (0); fz (ไม่ใช่0)
            for (int i = 3; i >= 0; i--) {
                if (arr[r + 4 * i] == 0) {
                    z--;
                } else {
                    temp[fz] = arr[r + 4 * i];
                    fz--;
                }
            }
            for (int i = fz; i >= 0; i--) {
                temp[i] = 0;
            }
            for (int j = 3; j >= 0; j--) {
                arr[r + 4 * j] = temp[j];
            }
        }
    }

}
