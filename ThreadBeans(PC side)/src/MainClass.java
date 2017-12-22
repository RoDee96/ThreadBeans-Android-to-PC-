
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import static javax.swing.SwingUtilities.invokeLater;

public class MainClass {

    static StartFrame startFrame;

    public static void main(String args[]) throws Exception {
        invokeLater(new Runnable() {
            public void run() {
                startFrame = new StartFrame();
                try {
                    startFrame.start();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread.sleep(2000);

        //to make visible that network established!
        startFrame.pwp.setVisible(true);
        //setting the IP of PC...
        //SetIP_ToUse siptu = new SetIP_ToUse();

        new File("C:/ReceivedFiles").mkdir();
        AndroidClient.start();
        Thread.sleep(1000);
        //First CheckPoint...
        startFrame.dispose();

        MainFrame.start();
        Thread.sleep(1000);
        if (AndroidClient.connectedStatus) {

            MainFrame.HeaderLabel.setText("Connected to Android: "+AndroidClient.rec);
            MainFrame.RetryConButton.setEnabled(false);
            MainFrame.RetryConButton.setText("Connected");
            
        }

    }

}
