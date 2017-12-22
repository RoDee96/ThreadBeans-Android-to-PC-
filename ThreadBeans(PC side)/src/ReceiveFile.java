
import com.threadbeans2.beans.FileBean;
import java.io.*;
import javax.swing.*;
import java.net.*;

public class ReceiveFile {

    static String storageLocation = MainFrame.storageLocation;
    static boolean cancelTransfer;

    static InputStream is;
    static OutputStream os;
    static FileOutputStream fos;

    static ObjectOutputStream oos;
    static ObjectInputStream ois;

    static Socket socket;

    public static void initiate() throws Exception {
        System.out.println("    ReceiveFile: initiate: ServerIP: " + "192.168.43.1");
        socket = new Socket("192.168.43.1", 6000);

        is = socket.getInputStream();
        os = socket.getOutputStream();

        oos = new ObjectOutputStream(os);
        ois = new ObjectInputStream(is);

        prepareToReceive();
    }

    private static void prepareToReceive() throws Exception {
        cancelTransfer = false;
        FileBean fileBean = (FileBean) ois.readObject();

        System.out.println("    prepareToReceive: At Location: " + storageLocation);
        System.out.println("    prepareToReceive: FileName: " + fileBean.getFilename());
        System.out.println("    prepareToReceive: FileSIze: " + fileBean.getFilesize());

        fos = new FileOutputStream(MainFrame.storageLocation + fileBean.getFilename());

        updateUI(fileBean);
    }

    private static void updateUI(FileBean fileBean) {
        MainFrame.FileNameLabel.setText("Receiving file: " + fileBean.getFilename());
        MainFrame.SendButton.setText("Cancel Receiving");
        MainFrame.exitButton.setEnabled(false);

        receive(fileBean);
    }

    private static void receive(FileBean fileBean) {
        try {

            byte[] bytes = new byte[1024];
            int count;
            for (double bytesRead = 1; true; bytesRead += 1024) {
                if (cancelTransfer == true) {
                    break;
                }

                int percent = (int) (int) ((bytesRead / fileBean.getFilesize()) * 100);
                updateProgressBar(percent);

                if ((count = is.read(bytes)) > 0) {
                    fos.write(bytes, 0, count);
                    fos.flush();
                } else {
                    break;
                }

            }

            if (cancelTransfer == false) {//sucessfull receving
                try {
                    System.out.println("    ReceiveFile: File received Successfully!");
                    MainFrame.PercentLabel.setText("100% completed!");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (cancelTransfer == true) {//unsucessful receving
                try {
                    System.out.println("    ReceiveFile: Error Receiving file!");
                    MainFrame.PercentLabel.setText("Error receeiving file!");
                    MainFrame.StatusLabel.setText("Tranfer Aborted!");

                    Thread.sleep(3000);
                    MainFrame.StatusLabel.setText(" ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("    ReceiveFile: File not received!");
        } finally {
            try {
                postUIActions();

                oos.close();
                ois.close();
                fos.close();
                is.close();
                os.close();
                socket.close();
                System.out.println("    ReceiveFile: Streams closed Successfully");
                
                
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("    ReceiveFile: Streams not closed!");
            }
        }
    }

    //utility methods 
    private static void updateProgressBar(int percent) {
        MainFrame.ProgressBar.setValue(percent);
        MainFrame.PercentLabel.setText(percent+" % complete");
    }

    private static void postUIActions() {
        MainFrame.exitButton.setEnabled(true);
        MainFrame.SendButton.setText("Send");
        MainFrame.PercentLabel.setText(" ");
        MainFrame.FileNameLabel.setText(" ");
        MainFrame.ProgressBar.setValue(0);
    }
}
