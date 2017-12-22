
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class Settings extends javax.swing.JFrame {
    
        
    Point initialClick;
    static Boolean modIp=false;
    static String IP;
    static JFileChooser jfc;              
    
    private JButton browseButton;
    private JLabel headerLabel;
    private JLabel imageLabel;
    private JLabel storageLabel;
    private JButton okayButton;

    public Settings() {
        browseButton = new javax.swing.JButton();
        okayButton = new javax.swing.JButton();
        
        storageLabel = new javax.swing.JLabel();
        headerLabel = new javax.swing.JLabel();
        imageLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });
        browseButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        //browseButton.setContentAreaFilled(false);
        
        getContentPane().add(browseButton);
        browseButton.setBounds(250, 50, 110, 40);

        okayButton.setText("Okay");
        okayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okayButtonActionPerformed(evt);
            }
        });/*
        okayButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                okayButton.setContentAreaFilled(true);
            }
            public void mouseExited(MouseEvent evt) {
                okayButton.setContentAreaFilled(false);
            }
        });*/
        okayButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        getContentPane().add(okayButton);
        okayButton.setBounds(20, 100, 340, 40);

        storageLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        storageLabel.setText("Change storage location:");
        getContentPane().add(storageLabel);
        storageLabel.setBounds(20, 60, 220, 22);

        headerLabel.setFont(new java.awt.Font("Tahoma", 1, 26)); // NOI18N
        headerLabel.setText("Settings");
        getContentPane().add(headerLabel);
        headerLabel.setBounds(20, 10, 120, 30);

        imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Lan.jpg"))); // NOI18N
        getContentPane().add(imageLabel);
        imageLabel.setBounds(0, 0, 390, 160);
        
        getContentPane().addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent evt){
                initialClick = evt.getPoint();
            }
        });
        getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {

                // get location of Window
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                // Determine how much the mouse moved since the initial click
                int xMoved = (thisX + evt.getX()) - (thisX + initialClick.x);
                int yMoved = (thisY + evt.getY()) - (thisY + initialClick.y);

                // Move window to this position
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
    
            }
        });


        setSize(new java.awt.Dimension(390, 160));
        setLocationRelativeTo(null);
        setUndecorated(true);
    }                    

                                        

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        jfc = new JFileChooser();
        jfc.setCurrentDirectory(new java.io.File("."));
        jfc.setDialogTitle("Choose a Folder");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setAcceptAllFileFilterUsed(false);

        if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getSelectedFile() : " + jfc.getSelectedFile());
            MainFrame.storageLocation = jfc.getSelectedFile()+"/";
        }
        else{
            System.out.println("No Selection");
        }
    }                                   
    
    private void okayButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        this.setVisible(false);
    }       
    

    public void sett() {
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Settings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        

        
        setVisible(true);
        
        
    }
   
                
}
