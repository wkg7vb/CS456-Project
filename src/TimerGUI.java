import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class TimerGUI extends JFrame {

    private JLabel time;
    private JButton start, startOver, breakButton;
    private Timer timer;
    private int secondsLeft = 0;
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

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startWorkTimer();
            }
        });

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

    private void startWorkTimer(){
        if (timer != null){
            timer.cancel();
        }

        timer = new Timer();
        secondsLeft = 25*60;
        time.setText(formatTime(secondsLeft));

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                secondsLeft--;
                if (secondsLeft >= 0) {
                    time.setText(formatTime(secondsLeft));
                } else {
                    stopTimer();
                }
            }
        }, 1000, 1000);
    }

    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds% 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}

// timer function from:
// https://stackoverflow.com/questions/37517420/how-to-make-count-down-timer-in-java
