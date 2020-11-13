import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartFrame extends spider{

    JFrame mainFrame;
    final String gameRule = "เกม 2048 มี 16 กริดในตอนแรกจะมีการสร้างสี่เหลี่ยม 2 ช่องที่มีค่าเป็น 2 และสี่เหลี่ยมที่มีค่า 4\n" +
            "ผู้เล่นสามารถใช้ปุ่มลูกศรขึ้นลงซ้ายและขวาบนแป้นพิมพ์เพื่อควบคุมทิศทางการเลื่อนของบล็อก\n" +
            "ทุกครั้งที่คุณกดแป้นลูกศรสี่เหลี่ยมทั้งหมดจะเคลื่อนที่ไปในทิศทางเดียวและสี่เหลี่ยมที่มีค่าเดียวกันจะถูกเพิ่มและรวมเป็นสี่เหลี่ยมจัตุรัสเดียว\n" +
            "นอกจากนี้การกวาดนิ้วแต่ละครั้งจะสุ่มสร้างสี่เหลี่ยมที่มีค่า 2 หรือ 4\n" +
            "ผู้เล่นต้องหาวิธีสร้าง 2048 สี่เหลี่ยมใน 16 กริดนี้ถ้าเต็ม 16 กริดและไม่สามารถเคลื่อนที่ได้อีก\n" +
            "ผู้เล่นจะแพ้";
    public StartFrame() {
        initFrame();
    }

    private void initFrame() {
        mainFrame = new JFrame("2048 Game");
        mainFrame.setSize(500, 500);
        mainFrame.setResizable(false);//ขนาดหน้าต่างคงที่
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);//หน้าต่างอยู่ตรงกลาง


        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        jPanel.add(newLine(Box.createVerticalStrut(16)));//เพิ่มพื้นที่ว่าง

        JLabel jLabel = new JLabel("2048");
        jLabel.setForeground(new java.awt.Color(0x776e65));
        jLabel.setFont(new java.awt.Font("Dialog", 1, 92));
        jPanel.add(newLine(jLabel));

        /*
        JLabel author = new JLabel("by xxx");
        jPanel.add(newLine(author));
        */


        jPanel.add(newLine(Box.createVerticalStrut(50)));


        JButton btn1 = new JButton("เริ่มเกม");
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Game();
                mainFrame.dispose();
            }
        });
        jPanel.add(newLine(btn1));


        jPanel.add(newLine(Box.createVerticalStrut(50)));

        
        JButton btn2 = new JButton("กฎกติกา");
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, gameRule, "กฎกติกา", JOptionPane.PLAIN_MESSAGE);
            }
        });
        jPanel.add(newLine(btn2));


        jPanel.add(newLine(Box.createVerticalStrut(50)));


        JButton btn3 = new JButton("ออกจากเกม");
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        jPanel.add(newLine(btn3));


        mainFrame.add(jPanel);

        mainFrame.setVisible(true);
    }

    //เพิ่มบรรทัดใหม่ของตัวควบคุมที่อยู่ตรงกลางในแนวตั้งโดยการเติมวัตถุทั้งสองด้านของตัวควบคุม
    private JPanel newLine(Component c) {

        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
        jp.add(Box.createHorizontalGlue());
        jp.add(c);
        jp.add(Box.createHorizontalGlue());
        jp.setOpaque(false);//ตั้งค่าความทึบ

        return jp;
    }

}