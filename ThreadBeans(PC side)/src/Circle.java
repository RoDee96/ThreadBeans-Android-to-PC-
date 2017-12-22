import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Circle extends JPanel{
    int x=0;
    public Circle(){
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
    }
    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman", Font.PLAIN+Font.BOLD, 24));
        g.drawString("Starting Threads", 40, 50);
        g.setFont(new Font("TimesRoman", Font.PLAIN+Font.BOLD, 18));
        g.drawString("Please Wait...", 40, 150);
        
        g.setColor(Color.black);
        g.fillOval(x   , 70, 7, 7);
       
    }
    
    public void call(){
        int x;
        while(true)
            for(x=0; x<=600; x++){
                this.x=x;
                
                if(x>=200 && x<=300){
                    try{
                    Thread.sleep(9);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    try{
                    Thread.sleep(4);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                repaint();
            }
    }
    
}
