
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.plaf.FontUIResource;

public class MainFrame extends JFrame {

    static String storageLocation = "C:/ReceivedFiles/";
    static MainFrame frame;

    Point initialClick;

    static Settings set = new Settings();

    static JButton exitButton;
    static JButton SettingsButton;
    static JButton RetryConButton;
    static JButton SendButton;

    static JLabel ImageLabel;
    static JLabel HeaderLabel;
    static JLabel StatusLabel;
    static JLabel FileNameLabel;
    static JLabel PercentLabel;

    JPanel MainPanel;
    GUIPanel guiPanel;

    static JProgressBar ProgressBar;

    JOptionPane jop;

    DotPanels dot[] = new DotPanels[5];

    //for networking
    static String ConnectedIP = null;

    public MainFrame() {

        //Variabe Initialization begins...
        MainPanel = new JPanel();
        guiPanel = new GUIPanel();

        HeaderLabel = new JLabel();
        StatusLabel = new JLabel();
        FileNameLabel = new JLabel();
        PercentLabel = new JLabel();

        SettingsButton = new JButton();
        exitButton = new JButton();

        SendButton = new JButton();
        RetryConButton = new JButton();

        ImageLabel = new JLabel();
        ProgressBar = new JProgressBar();
        //Variable initialization Ends...

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new Dimension(600, 300));
        getContentPane().setLayout(null);

        for (int i = 0; i < 5; i++) {
            dot[i] = new DotPanels();
            dot[i].setVisible(false);
            add(dot[i]);
            dot[i].setBounds(0, 0, 600, 300);
        }

        MainPanel.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        MainPanel.setOpaque(false);
        MainPanel.setLayout(null);
        MainPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                initialClick = evt.getPoint();
            }
        });
        MainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {

                // get location of Window
                int thisX = frame.getLocation().x;
                int thisY = frame.getLocation().y;

                // Determine how much the mouse moved since the initial click
                int xMoved = (thisX + evt.getX()) - (thisX + initialClick.x);
                int yMoved = (thisY + evt.getY()) - (thisY + initialClick.y);

                // Move window to this position
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                frame.setLocation(X, Y);

            }
        });

        HeaderLabel.setFont(new Font("Tahoma", 0, 24));
        HeaderLabel.setForeground(new Color(255, 255, 255));
        HeaderLabel.setText("Click to Connect!");
        MainPanel.add(HeaderLabel);
        HeaderLabel.setBounds(10, 10, 470, 29);

        StatusLabel.setFont(new Font("Tahoma", 0, 18));
        StatusLabel.setForeground(new Color(255, 255, 255));
        MainPanel.add(StatusLabel);
        StatusLabel.setBounds(10, 270, 440, 22);

        RetryConButton.setFont(new Font("Tahoma", 0, 15));
        RetryConButton.setText("Retry Connection");
        RetryConButton.setForeground(new Color(255, 255, 255));
        RetryConButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        RetryConButton.setContentAreaFilled(false);
        RetryConButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retryConButtonActionPerformed(evt);
            }
        });
        RetryConButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                RetryConButton.setForeground(new Color(0, 0, 0));
                StatusLabel.setText("Change Settings...");
                guiPanel.y = 70;
                guiPanel.flag = true;
                guiPanel.repaint();
                RetryConButton.setContentAreaFilled(true);
            }

            public void mouseExited(MouseEvent evt) {
                RetryConButton.setForeground(new Color(255, 255, 255));
                StatusLabel.setText("");
                guiPanel.flag = false;
                guiPanel.repaint();
                RetryConButton.setContentAreaFilled(false);
            }

        });
        RetryConButton.setVisible(true);
        MainPanel.add(RetryConButton);
        RetryConButton.setBounds(445, 70, 140, 40);

        SettingsButton.setFont(new Font("Tahoma", 0, 15));
        SettingsButton.setText("Settings");
        SettingsButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        SettingsButton.setContentAreaFilled(false);
        SettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                set.sett();
            }
        });
        SettingsButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                StatusLabel.setText("Change Settings...");
                guiPanel.y = 115;
                guiPanel.flag = true;
                guiPanel.repaint();
                SettingsButton.setContentAreaFilled(true);
            }

            public void mouseExited(MouseEvent evt) {
                StatusLabel.setText("");
                guiPanel.flag = false;
                guiPanel.repaint();
                SettingsButton.setContentAreaFilled(false);
            }

        });
        SettingsButton.setVisible(true);
        MainPanel.add(SettingsButton);
        SettingsButton.setBounds(445, 115, 140, 40);

        exitButton.setFont(new Font("Tahoma", 0, 15));
        exitButton.setText("Exit");
        exitButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        //exitButton.setContentAreaFilled(false);
        exitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                StatusLabel.setText("Leave the Application...");
                guiPanel.y = 250;
                guiPanel.flag = true;
                guiPanel.repaint();
                //exitButton.setContentAreaFilled(true);
            }

            public void mouseExited(MouseEvent evt) {
                StatusLabel.setText("");
                guiPanel.flag = false;
                guiPanel.repaint();
                //exitButton.setContentAreaFilled(false);
            }

        });
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (AndroidClient.connectedStatus) {
                    try {
                        AndroidClient.connectedStatus = false;
                        AndroidClient.oos.writeObject((String) "exit");
                        AndroidClient.oos.flush();

                        System.out.println("All streams closed, GoodBye!");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });
        MainPanel.add(exitButton);
        exitButton.setBounds(445, 250, 140, 40);

        SendButton.setFont(new Font("Tahoma", 0, 15)); // NOI18N
        SendButton.setForeground(new Color(0, 0, 0));
        SendButton.setText("Send");
        SendButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        //SendButton.setContentAreaFilled(false);
        SendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });
        SendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                StatusLabel.setText("Click to Send files...");
                guiPanel.y = 160;
                guiPanel.flag = true;
                guiPanel.repaint();
                SendButton.setContentAreaFilled(true);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                StatusLabel.setText("");
                guiPanel.flag = false;
                guiPanel.repaint();
                SendButton.setContentAreaFilled(false);
            }
        });
        SendButton.setEnabled(true);
        SendButton.setVisible(true);
        MainPanel.add(SendButton);
        SendButton.setBounds(445, 160, 140, 40);

        PercentLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        PercentLabel.setForeground(new java.awt.Color(255, 255, 255));
        PercentLabel.setText(" ");
        MainPanel.add(PercentLabel);
        PercentLabel.setBounds(20, 100, 200, 22);

        FileNameLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        FileNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        FileNameLabel.setText(" ");
        MainPanel.add(FileNameLabel);
        FileNameLabel.setBounds(20, 77, 300, 22);

        getContentPane().add(MainPanel);
        MainPanel.setBounds(0, 0, 600, 300);

        guiPanel.setOpaque(false);
        guiPanel.setLayout(null);
        getContentPane().add(guiPanel);
        guiPanel.setBounds(0, 0, 600, 300);

        //ProgressBar.setVisible(false);
        ProgressBar.setForeground(new java.awt.Color(102, 255, 0));
        ProgressBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        MainPanel.add(ProgressBar);
        ProgressBar.setBounds(10, 255, 420, 14);

        ImageLabel.setForeground(new java.awt.Color(255, 255, 255));
        ImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("Arrow.jpg"))); // NOI18N
        ImageLabel.setText("ImageLabel");
        getContentPane().add(ImageLabel);
        ImageLabel.setBounds(0, 0, 600, 300);

    }

    private void retryConButtonActionPerformed(ActionEvent evt) {
        Thread thread = new Thread() {
            public void run() {
                AndroidClient.start();
                if (AndroidClient.connectedStatus) {
                    MainFrame.HeaderLabel.setText("Connected to Android");
                    MainFrame.RetryConButton.setEnabled(false);
                    MainFrame.RetryConButton.setText("Connected");
                }
            }
        };
        thread.start();
    }

    private void sendButtonActionPerformed(ActionEvent evt) {
        if (SendButton.getText().equals("Send")) {
            Thread sending = new Thread() {
                public void run() {
                    try {
                        SendFile.initiate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            sending.start();

        }
        if (SendButton.getText().equals("Cancel Sending")) {
            cancelSendButtonActionPerformed();
        }
    }

    private void cancelSendButtonActionPerformed() {
        SendButton.setText("Send");
        SendFile.AbortTransfer = true;
        SendFile.stoploop = true;

        destroyFileServerSocket();
    }

    private void destroyFileServerSocket() {
        try {
            AndroidClient.oos.writeObject("Cancel Receiving");
            AndroidClient.oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start() throws Exception {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame = new MainFrame();
                frame.setLocationRelativeTo(null);
                frame.setUndecorated(true);
                frame.setVisible(true);
            }
        });

    }

    public static void main(String args[]) throws Exception {
        start();
    }

    //For GUI besides the JButtons...
    public class GUIPanel extends JPanel {

        boolean flag = false;
        int y;

        public void paint(Graphics g) {
            if (flag == true) {
                super.paint(g);
                g.setColor(Color.white);
                g.fillRect(590, y, 10, 40);
            }
        }

    }
    //end...

    //DotPanels classes and functions...
    public static void pause() {
        try {
            Thread.sleep(150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //end...

    //here starts frequently used functions...
    static boolean StopDots = false;

    public void startDots() {
        StopDots = false;
        Thread DotMove = new Thread() {
            public void run() {
                Thread moveDot[] = new Thread[5];
                for (int i = 0; i < 5; i++) {
                    dot[i].setVisible(true);
                    moveDot[i] = new Threads(dot[i]);
                    moveDot[i].start();
                    pause();
                }
            }
        };
        DotMove.start();
    }

    public void stopDots() {
        StopDots = true;
    }
}
