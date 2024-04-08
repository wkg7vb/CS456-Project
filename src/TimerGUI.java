import javax.swing.*;
import java.awt.*;

public class TimerGUI extends JFrame {

    private JLabel time;
    private JButton start, startOver, breakButton;

    public TimerGUI() {

        setTitle("Study Timer");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //timer.schedule(new App(), 0, 1000);

        JPanel panel = new JPanel(new BorderLayout());

        // adding elements to panel
        time = new JLabel("25:00");
        time.setFont(new Font("Serif", Font.BOLD, 200));
        panel.add(time, BorderLayout.CENTER);

        start = new JButton("START");
        start.setSize(new Dimension(400, 200));
        JPanel buttonPane = new JPanel();
        buttonPane.add(start);

        panel.add(buttonPane, BorderLayout.SOUTH);

        JPanel menu = getjPanel();

        panel.add(menu, BorderLayout.EAST);

        add(panel); // add panel to window

        setVisible(true);

        //System.exit(0);
    }

    private static JPanel getjPanel() {
        int iconSize = 30;
        var soundIcon = new ImageIcon(new ImageIcon("src/sound.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        var bellIcon = new ImageIcon(new ImageIcon("src/bell.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        var colorIcon = new ImageIcon(new ImageIcon("src/color.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        var pictureIcon = new ImageIcon(new ImageIcon("src/picture.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));

        var soundBtn = new JButton(soundIcon);
        var bellBtn = new JButton(bellIcon);
        var colorBtn = new JButton(colorIcon);
        var pictureBtn = new JButton(pictureIcon);

        JPanel menu = new JPanel();
        menu.add(new JSeparator());
        menu.add(soundBtn);
        menu.add(new JSeparator());
        menu.add(bellBtn);
        menu.add(new JSeparator());
        menu.add(colorBtn);
        menu.add(new JSeparator());
        menu.add(pictureBtn);
        menu.add(new JSeparator());

        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        return menu;
    }

}

// timer function from:
// https://stackoverflow.com/questions/37517420/how-to-make-count-down-timer-in-java
