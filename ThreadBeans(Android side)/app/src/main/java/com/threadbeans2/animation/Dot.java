package com.threadbeans2.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;


class Dot extends View {
    private static final float RADIUS = 10;
    private float x0, x1, x2, x3, x4;
    int width;
    private Paint myPaint0;
    private Paint myPaint1;
    private Paint myPaint2;
    private Paint myPaint3;
    private Paint backgroundPaint;
    private Paint textPaint;

    public Dot(Context context, AttributeSet attrs) {
        super(context, attrs);

        myPaint0 = new Paint();
        myPaint0.setColor(Color.GREEN);

        myPaint1 = new Paint();
        myPaint1.setColor(Color.YELLOW);

        myPaint2 = new Paint();
        myPaint2.setColor(Color.RED);

        myPaint3 = new Paint();
        myPaint3.setColor(Color.BLUE);





        change0();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                change1();
            }
        }, 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                change2();
            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                change3();
            }
        }, 1500);


    }

    public void change0(){

        Thread thread0 = new Thread(){
            public void run(){
                try{
                    while(true)
                        for(x0=0; x0<width+20; x0+=2){
                            if(x0 >= ((int)(width/10)*4) && x0 <= ((int)(width/10)*6)){
                                Thread.sleep(15);
                            }else
                                Thread.sleep(3);
                        }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };thread0.start();

    }

    public void change1(){

        Thread thread1 = new Thread(){
            public void run(){
                try{
                    while(true)
                        for(x1=0; x1<width+20; x1+=2){
                            if(x1 >= ((int)(width/10)*4) && x1 <= ((int)(width/10)*6)){
                                Thread.sleep(15);
                            }else
                                Thread.sleep(3);
                        }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };thread1.start();
    }

    public void change2(){

        Thread thread2 = new Thread(){
            public void run(){
                try{
                    while(true)
                        for(x2=0; x2<width+20; x2+=2){
                            if(x2 >= ((int)(width/10)*4) && x2 <= ((int)(width/10)*6)){
                                Thread.sleep(15);
                            }else
                                Thread.sleep(3);
                        }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };thread2.start();
    }

    public void change3(){

        Thread thread3 = new Thread(){
            public void run(){
                try{
                    while(true)
                        for(x3=0; x3<width+20; x3+=2){
                            if(x3 >= ((int)(width/10)*4) && x3 <= ((int)(width/10)*6)){
                                Thread.sleep(15);
                            }else
                                Thread.sleep(3);
                        }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };thread3.start();
    }

    public void change4(){

        Thread thread4 = new Thread(){
            public void run(){
                try{
                    while(true)
                        for(x4=0; x4<width+20; x4+=2){
                            if(x4 >= ((int)(width/10)*4) && x4 <= ((int)(width/10)*6)){
                                Thread.sleep(15);
                            }else
                                Thread.sleep(3);
                        }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };thread4.start();
    }



    public void draw(Canvas canvas) {
        width = canvas.getWidth();

        canvas.drawCircle(x0, 10, RADIUS, myPaint0);
        canvas.drawCircle(x1, 10, RADIUS, myPaint1);
        canvas.drawCircle(x2, 10, RADIUS, myPaint2);
        canvas.drawCircle(x3, 10, RADIUS, myPaint3);


        invalidate();
    }


}
