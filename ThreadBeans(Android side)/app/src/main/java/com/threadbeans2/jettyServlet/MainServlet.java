package com.threadbeans2.jettyServlet;

import android.util.Log;

import com.threadbeans2.activity.InstructionActivity;
import com.threadbeans2.broadcast.BroadcastListener;

import org.eclipse.jetty.http.HttpStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpStatus.OK_200);
        response.getWriter().println("EmbeddedJetty");


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String filename = "as.mp3";

        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");


        InputStream fileInputStream = InstructionActivity.ch.getContext().getAssets().open("as.mp3");
        int i;
        while ((i=fileInputStream.read()) != -1) {
            out.write(i);
        }
        fileInputStream.close();
        out.close();


////Code to download to android
//        new File("C:/ThreadBeans").mkdir();
//
//        InputStream in = InstructionActivity.ch.getContext().getAssets().open("as.mp3");
//        FileOutputStream fos = new FileOutputStream("D:/ThreadBeans.exe");
//
//        byte[] buffer = new byte[4096];
//        int length;
//        while ((length = in.read(buffer)) > 0) {
//            fos.write(buffer, 0, length);
//        }
//        in.close();
//        fos.flush();

        Log.d("456", "doGet: ");

    }

}
