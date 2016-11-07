import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;

public class GUITest {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Push Me");
        frame.setSize(200, 100);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JButton button = centeredButton(frame);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton b = (JButton) e.getSource();
                if (b.getActionCommand().equals("done"))
                    frame.dispose();
                System.out.printf("Modifiers: %d\n", e.getModifiers());
                System.out.printf("Params: %s\n", e.paramString());
                System.out.printf("When: %d\n", e.getWhen());
                System.out.printf("Action: %s\n", e.getActionCommand());
                System.out.printf("Source: %s\n", e.getSource());
                b.setActionCommand("done");
                b.setText("Exit");
            }
        });
        frame.setVisible(true);
    }

    static JButton centeredButton(JFrame frame) {
        String[] location = {BorderLayout.NORTH, BorderLayout.EAST, BorderLayout.SOUTH, BorderLayout.WEST};
        for (String s : location) {
            frame.add(new JLabel(""), s);
        }
        JButton jb = new JButton("Push Me");
        jb.setActionCommand("doit");
        frame.add(jb);
        return jb;
    }
}


