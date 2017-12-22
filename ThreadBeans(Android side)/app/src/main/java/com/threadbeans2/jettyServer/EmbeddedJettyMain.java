package com.threadbeans2.jettyServer;

import android.util.Log;

import com.threadbeans2.jettyServlet.MainServlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class EmbeddedJettyMain {
    public static Server server;
    public static void main(String[] args) throws Exception {
        server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler(server, "/servlet");
        handler.addServlet(MainServlet.class, "/");
        server.start();
        Log.d("456", "main: ");
    }
}
