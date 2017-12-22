public class Threads extends Thread{
        DotPanels s;
        public Threads(DotPanels s){
            this.s = s;
        }
            
        public void run(){
            s.call();
        }    
    }