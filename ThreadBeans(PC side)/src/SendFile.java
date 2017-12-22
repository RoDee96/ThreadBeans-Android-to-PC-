
import com.threadbeans2.beans.FileBean;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.*;
import java.net.*;

public class SendFile {

    static boolean AbortTransfer;
    static boolean stoploop = false;

    static ObjectInputStream ois;
    static ObjectOutputStream oos;

    static InputStream is;
    static OutputStream os;
    static FileInputStream fis;

    static JFileChooser jfc;

    static ServerSocket serverSocket;
    static Socket socket;

    public static void initiate() throws Exception {
        openDialog();
    }

    public static void openDialog() {
        stoploop = false;

        jfc = new JFileChooser();
        jfc.setMultiSelectionEnabled(true);
        jfc.showOpenDialog(null);

        File files[];

        if ((files = jfc.getSelectedFiles()) != null) {
            int n = files.length;
            if (n == 0) {
                MainFrame.SendButton.setEnabled(true);
            }

            System.out.println(n + " files selected.");

            if (n > 0) {
                for (int i = 0; i < n; i++) {
                    if (stoploop == true) {
                        break;
                    }
                    try {
                        prepareToSend(files[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    private static void prepareToSend(File file) throws Exception {
        System.out.println("    SendFile: prepareToSend: Android nortified for receiving");
        AbortTransfer = false;
        AndroidClient.oos.writeObject((String) "sending");
        AndroidClient.oos.flush();
        
        serverSocket = new ServerSocket(6000);
        socket = serverSocket.accept();
        System.out.println("\n    SendFile: prepareToSend: server started at port 6000");
        
       
        

        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("    SendFile: prepareToSend: streams up at 6000");

        FileBean fileBean = new FileBean();
        fileBean.setFilename(file.getName());
        fileBean.setFilesize(file.length());

        oos.writeObject(fileBean);
        oos.flush();
        System.out.println("    SendFile: prepareToSend: Filename and length sent");

        System.out.println("Transfer Initiated:");
        System.out.println("    File name is: " + file.getName());
        System.out.println("    File size is: " + file.length());

        fis = new FileInputStream(file);

        send(fileBean, file);
    }

    public static void send(FileBean filebean, File file) {
        try {
            System.out.println("    Sending...");

            os = socket.getOutputStream();
            is = socket.getInputStream();

            startingUIActions(file);
            
            int count;

            byte bytes[] = new byte[1024];
            for (double bytesRead = 1; true; bytesRead+=1024) {
                if (AbortTransfer == true) {
                    MainFrame.SendButton.setEnabled(false);
                    break;
                }

                if ((count = fis.read(bytes)) > 0) {
                    os.write(bytes, 0, count);
                    os.flush();
                } else {
                    break;
                }

                int percent = (int)(( bytesRead / file.length() ) * 100);
                
                updateProgressBar(percent);
            }

            if (AbortTransfer == true) {
                MainFrame.SendButton.setEnabled(false);
                System.out.println("    Transfer cancelled by user");
                
                try {
                    MainFrame.PercentLabel.setText("Sending Cancelled!");
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    MainFrame.PercentLabel.setText("100% completed!");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("    File Transferred Successfully!");

            }

            postUIActions();

        } catch (Exception e) {
            System.out.println("SendFile: "+AbortTransfer);
            e.printStackTrace();

            if (AbortTransfer == true) {
                System.out.println("    Transfer cancelled by user");

                try {
                    MainFrame.PercentLabel.setText("Sending Cancelled!");
                    Thread.sleep(3000);
                    MainFrame.SendButton.setEnabled(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } finally {
            try {
                postUIActions();

                fis.close();
                ois.close();
                oos.close();

                is.close();
                os.close();
                socket.close();
                serverSocket.close();

            } catch (Exception e) {
                System.out.println("Sender streams or server not closed!");
                e.printStackTrace();
            }
        }
    }

    //utility methods
    private static void startingUIActions(File file) {
        MainFrame.FileNameLabel.setText("Sending File: " + new String(file.getName()) + "...");
        MainFrame.SendButton.setText("Cancel Sending");
        MainFrame.exitButton.setEnabled(false);

        AbortTransfer = false;
    }

    private static void postUIActions() {
        MainFrame.SendButton.setText("Send");
        MainFrame.exitButton.setEnabled(true);
        MainFrame.SendButton.setEnabled(true);//for next version
        MainFrame.PercentLabel.setText(" ");
        MainFrame.FileNameLabel.setText(" ");
        MainFrame.ProgressBar.setValue(0);
    }

    private static void updateProgressBar(int percent) {
        MainFrame.ProgressBar.setValue(percent);
        MainFrame.PercentLabel.setText(percent + "%  completed!");
    }

}
