import java.awt.*;
import javax.swing.*;
public class StartFrame extends JFrame{
    JLabel l1;
    Circle s[] = new Circle[5];
    PleaseWaitPanel pwp;
    
    public StartFrame(){
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(600,300);
        setLocationRelativeTo(null);
        setUndecorated(true);
        //setOpacity(0.9f);
        for(int i=0; i<5; i++){
            s[i] = new Circle();
            add(s[i]);
            s[i].setBounds(0,0,600,300);
        }
        
        pwp = new PleaseWaitPanel();
        add(pwp);
        pwp.setBounds(0, 0, 600, 300);
        pwp.setVisible(false);
        
        l1= new JLabel();
        l1.setIcon(new ImageIcon(getClass().getResource("Lan.jpg")));
        add(l1);
        l1.setBounds(0,0,600,300);
        
        setVisible(true);
        
    }
    public void start() throws InterruptedException{
       
        Threads td[] = new Threads[6];
            for(int i =0; i<5; i++){
                td[i] = new Threads(i);
                td[i].start();
                pause();
            }
        
    }
    
    public static void pause(){
        try{
            Thread.sleep(150);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public class Threads extends Thread{
        int i;        
        public Threads(int i){
            this.i = i;
        }
            
        public void run(){
            s[i].call();
        }    
    }
    
    public class PleaseWaitPanel extends JPanel{
        public PleaseWaitPanel(){
            setOpaque(false);
            setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        }
        
        public void paint(Graphics g){
            super.paint(g);
            g.setColor(Color.black);
            g.setFont(new Font("TimesRoman", Font.PLAIN+Font.BOLD, 14));
            g.drawString("Connecting to Android device", 40, 170);
            g.fillRect(0, 155, 5, 20);
            
        }
        
    }
    
}
