import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DotPanels extends JPanel{
    int x=0;
    public DotPanels(){
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
    }
    
    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.white);
        g.fillOval(x   , 60, 7, 7);
    }
    
    public void call(){
        int x;
        while(true){
            //Conditions for MainFrame
            if(MainFrame.StopDots == true){
                break;
            }
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
    
    
}
