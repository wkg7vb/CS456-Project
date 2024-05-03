import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.io.File;
import javax.sound.sampled.*;


public class TimerGUI extends JFrame {

    private JPanel panel;
    private JLabel time;
    private JLabel workBreak;
    private JButton start, startOver, breakButton;
    private JSpinner workHourSpinner, workMinuteSpinner, workSecondSpinner;
    private JSpinner breakHourSpinner, breakMinuteSpinner, breakSecondSpinner;
    private JButton breakFlash;
    private Clip clip;
    private Timer timer;
    private javax.swing.Timer flashTimer;
    private int secondsLeft = 0;
    private boolean flashWhite = true;
    private static boolean playSound = false;
    private Color chosenColor = Color.red;

    public TimerGUI() {

        setTitle("Study Timer");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //timer.schedule(new App(), 0, 1000);

        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        flashTimer = new javax.swing.Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flashScreen();
            }
        });

        // adding elements to panel
        workBreak = new JLabel("Press Start");
        workBreak.setFont(new Font("Serif", Font.BOLD, 100));
        panel.add(workBreak, BorderLayout.BEFORE_FIRST_LINE);

        time = new JLabel("  00:30");
        time.setFont(new Font("Serif", Font.BOLD, 200));
        panel.add(time, BorderLayout.CENTER);

        workHourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        workMinuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        workSecondSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 59, 1));
        breakHourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        breakMinuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        breakSecondSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 59, 1));

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
                if (timer != null){
                    timer.cancel();
                }
            }
        });
        breakFlash.setVisible(true);

        JPanel topSpinnerPanel = new JPanel();
        topSpinnerPanel.add(new JLabel("Set Work Time:"));
        topSpinnerPanel.add(workHourSpinner);
        topSpinnerPanel.add(new JLabel("h"));
        topSpinnerPanel.add(workMinuteSpinner);
        topSpinnerPanel.add(new JLabel("m"));
        topSpinnerPanel.add(workSecondSpinner);
        topSpinnerPanel.add(new JLabel("s"));
        topSpinnerPanel.add(start);

        JPanel bottomSpinnerPanel = new JPanel();        
        bottomSpinnerPanel.add(new JSeparator());
        bottomSpinnerPanel.add(new JLabel("Set Break Time:"));
        bottomSpinnerPanel.add(breakHourSpinner);
        bottomSpinnerPanel.add(new JLabel("h"));
        bottomSpinnerPanel.add(breakMinuteSpinner);
        bottomSpinnerPanel.add(new JLabel("m"));
        bottomSpinnerPanel.add(breakSecondSpinner);
        bottomSpinnerPanel.add(new JLabel("s"));
        bottomSpinnerPanel.add(breakFlash);



        JPanel spinnerPanel = new JPanel(new GridLayout(2,1));
        spinnerPanel.add(topSpinnerPanel);
        spinnerPanel.add(bottomSpinnerPanel);
        panel.add(spinnerPanel, BorderLayout.SOUTH);

        JPanel menu = getjPanel();

        panel.add(menu, BorderLayout.EAST);

        add(panel); // add panel to window

        setVisible(true);

        //System.exit(0);
    }

    private JPanel getjPanel() {
        int iconSize = 30;
        var soundIcon = new ImageIcon(new ImageIcon("src/sound.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        var bellIcon = new ImageIcon(new ImageIcon("src/bell.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));
        var colorIcon = new ImageIcon(new ImageIcon("src/color.png").getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH));

        var soundBtn = new JButton(soundIcon);
        soundBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (playSound)
                {
                    soundBtn.setIcon(soundIcon);
                    playSound = false;
                }
                else
                {
                    soundBtn.setIcon(bellIcon);
                    playSound = true;
                }

            }
        });

        var colorBtn = new JButton(colorIcon);
        colorBtn.setBackground(chosenColor);

        colorBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                chosenColor = JColorChooser.showDialog(null, "Select a color", chosenColor);
                colorBtn.setBackground(chosenColor);
            }
        });

        var timerValue = new JTextField("25:00");
        timerValue.setPreferredSize(new Dimension(100,10));
        var timerReset = new JButton("Reset");

        JPanel menu = new JPanel();
        menu.add(new JSeparator());
        menu.add(soundBtn);
        menu.add(new JSeparator());
        //menu.add(bellBtn);
        //menu.add(new JSeparator());
        menu.add(colorBtn);
        menu.add(new JSeparator());

        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        return menu;
    }

    private void startWorkTimer(){
        workBreak.setText("Work Time");

        if (timer != null){
            timer.cancel();
        }

        timer = new Timer();
        secondsLeft = (((Integer) workHourSpinner.getValue()) * 3600)
            + (((Integer) workMinuteSpinner.getValue()) * 60)
            + (((Integer) workSecondSpinner.getValue()))  ; //CHANGED TO 5 SECONDS FOR TESTING
        time.setText(formatTime(secondsLeft));

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                secondsLeft--;
                if (secondsLeft >= 0) {
                    time.setText(formatTime(secondsLeft));
                } else {
                    startBreakTimer();
                }
            }
        }, 1000, 1000);
    }

    private void startBreakTimer(){
        workBreak.setText("Break Time!");

        if (timer != null){
            timer.cancel();
        }

        timer = new Timer();
        secondsLeft = (((Integer) breakHourSpinner.getValue()) * 3600)
            + (((Integer) breakMinuteSpinner.getValue()) * 60)
            + (((Integer) breakSecondSpinner.getValue()))  ; //CHANGED TO 5 SECONDS FOR TESTING
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
            workBreak.setText("Press Start");
        }
    }

    private void flashScreen() {
        breakFlash.setVisible(true);
        if (flashWhite) {
            panel.setBackground(chosenColor);
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