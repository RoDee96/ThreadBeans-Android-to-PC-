
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AndroidClient {

    public static Socket socket;
    public static boolean connectedStatus;

    static ObjectInputStream ois;
    static ObjectOutputStream oos;
    public static String rec;

    public static void start() {
        try {
            System.out.println("AndroidClient attempting con. at 5000.");
            socket = new Socket("192.168.43.1", 5000);
            //always create the ObjectOutputStream first both sides since ois sends sone byte ...see bookamrk on chrome
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            initAndroidStream();
            connectedStatus = true;
            System.out.println("AndroidClient: Connected at 5000.");

            InBuiltReceiver.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initAndroidStream() throws Exception {
        //sendind inet to android
        InetAddress inet = InetAddress.getLocalHost();
        System.out.println("Sent: "+inet);
        
        oos.writeObject(inet);
        oos.flush();
        
        //receiving android name from android device
        rec = (String) ois.readObject();
        System.out.println("Object Streams are up: "+rec);
    }

    public static void kill_and_leave() {
        try {
            connectedStatus = false;
            oos.close();
            ois.close();
            AndroidClient.socket.close();
            System.out.println("AndroidClient: kill_and_leave: Sockets and streams closed\nGoodBye");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public static class InBuiltReceiver {

        public static void start() {
            Thread com = new Thread() {
                public void run() {
                    while (connectedStatus) {
                        System.out.println("Receiver started at 5000!");
                        String read = "";
                        try {
                            read = (String) ois.readObject();
                            System.out.println(new String(read).toUpperCase() + " received");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("AndroidClient: " + "Cannot read from 5000 stream");
                        }

                        if (read.equals("sending")) {
                            try {
                                ReceiveFile.initiate();
                                String str = (String) AndroidClient.ois.readObject();
                                System.out.println("    ReceiveFile: Received:"+str);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }

                        if (read.equals("Cancel Sending")) {
                            SendFile.AbortTransfer = true;
                            SendFile.stoploop = true;
                        }

                        if (read.equals("exit")) {
                            kill_and_leave();
                        }

                        System.out.println("Receiver: end of an iteration!");
                    }
                }

            };
            com.start();
        }

    }
}
