import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.io.File;
import javax.sound.sampled.*;


public class TimerGUI extends JFrame {

    private JPanel panel;
    private JLabel time;
    private JButton start, startOver, breakButton;
    private JSpinner hourSpinner, minuteSpinner, secondSpinner;
    private JButton breakFlash;
    private Clip clip;
    private Timer timer;
    private javax.swing.Timer flashTimer;
    private int secondsLeft = 0;
    private boolean flashWhite = true;
    private static boolean playSound = false;
    private static JLabel soundLabel;
    private static Color alarmColor = Color.RED;
    public TimerGUI() {

        setTitle("Study Timer");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //timer.schedule(new App(), 0, 1000);

        panel = new JPanel(new BorderLayout());

        flashTimer = new javax.swing.Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flashScreen();
            }
        });

        // adding elements to panel
        time = new JLabel("  00:30");
        time.setFont(new Font("Serif", Font.BOLD, 200));
        panel.add(time, BorderLayout.CENTER);

        hourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        secondSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 59, 1));

        

        start = new JButton("START");
        start.setSize(new Dimension(400, 200));
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startWorkTimer();
            }
        });

        breakFlash = new JButton("STOP");
        breakFlash.setSize(new Dimension(400, 200));
        breakFlash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flashTimer.stop();
                panel.setBackground(Color.WHITE);
                breakFlash.setVisible(true);
                stopTimer();
            }
        });
        breakFlash.setVisible(true);

        JPanel buttonPane = new JPanel();
        buttonPane.add(new JLabel("Set Time:"));
        buttonPane.add(hourSpinner);
        buttonPane.add(new JLabel("h"));
        buttonPane.add(minuteSpinner);
        buttonPane.add(new JLabel("m"));
        buttonPane.add(secondSpinner);
        buttonPane.add(new JLabel("s     "));
        buttonPane.add(start);
        buttonPane.add(breakFlash);
        
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

        soundLabel = new JLabel("Sound: OFF");
        var soundBtn = new JButton(soundIcon);
        soundBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (playSound)
                {
                    soundBtn.setIcon(soundIcon);
                    playSound = false;
                    soundLabel.setText("Sound: OFF");

                }
                else
                {
                    soundBtn.setIcon(bellIcon);
                    playSound = true;
                    soundLabel.setText("Sound: ON");
                }

            }
        });
        //var bellBtn = new JButton(bellIcon);
        //var colorBtn = new JButton(colorIcon);
        /*colorBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {

            }
        });*/
        var pictureBtn = new JButton(pictureIcon);
        var timerValue = new JTextField("25:00");
        timerValue.setPreferredSize(new Dimension(100,10));
        var timerReset = new JButton("Reset");

        JPanel menu = new JPanel();
        menu.add(new JSeparator());
        menu.add(soundLabel);
        menu.add(soundBtn);
        menu.add(new JSeparator());
        //menu.add(bellBtn);
        menu.add(new JLabel("FlashColor:"));
        String[] colors = {"Red", "Blue", "Yellow"};
        var colorComboBox = new JComboBox<String>(colors);
        colorComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
                switch ((String) colorComboBox.getSelectedItem())
                {
                    case "Red" : alarmColor = Color.RED; break;
                    case "Blue" : alarmColor = Color.BLUE; break;
                    case "Yellow" : alarmColor = Color.YELLOW; break;
                }
            }
        });
        menu.add(colorComboBox);
        //menu.add(colorBtn);
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
        secondsLeft = (((Integer) hourSpinner.getValue()) * 3600)
            + (((Integer) minuteSpinner.getValue()) * 60) 
            + (((Integer) secondSpinner.getValue()))  ; //CHANGED TO 5 SECONDS FOR TESTING
        time.setText(formatTime(secondsLeft));

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                secondsLeft--;
                if (secondsLeft >= 0) {
                    time.setText(formatTime(secondsLeft));
                } else {
                    stopTimer();
                    playSound();
                }
            }
        }, 1000, 1000);
    }

    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
            flashTimer.start();
            
        }
    }

    private void flashScreen() {
        breakFlash.setVisible(true);
        if (flashWhite) {
            panel.setBackground(alarmColor);
        } else {
            panel.setBackground(Color.WHITE);
        }
        flashWhite = !flashWhite; 
    }
    

    private void playSound()
    {
        if (playSound)
        {
            try
            {
                clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));
        
                clip.addLineListener(new LineListener()
                {
                    @Override
                    public void update(LineEvent event)
                    {
                        if (event.getType() == LineEvent.Type.STOP)
                            clip.close();
                    }
                });
                
                var soundFile = new File("src/alarm.wav");
        
                clip.open(AudioSystem.getAudioInputStream(soundFile));
                clip.start();
            }
            catch (Exception exc)
            {
                exc.printStackTrace(System.out);
            }
        }
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds% 60;
        return String.format("  %02d:%02d", minutes, remainingSeconds);
    }
}

// timer function from:
// https://stackoverflow.com/questions/37517420/how-to-make-count-down-timer-in-java
// alarm sound from
// https://mixkit.co/free-sound-effects/alarm/
// playSound from:
// https://stackoverflow.com/a/17277981