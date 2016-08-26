package com.jkmcllc.aupair01.server;

import org.eclipse.jetty.server.Server;

public class RequestServer {

    private Thread serverThread;
    private Server server;
    private static RequestServer requestServer;
    
    public static RequestServer getInstance() {
        if (requestServer == null) {
            synchronized (RequestServer.class) {
                if (requestServer == null) {
                    requestServer  = new RequestServer();
                }
            }
        }
        return requestServer;
    }
    
    
    public static void main(String[] args) {
        try {
            RequestServer requestServer  = getInstance();
            requestServer.startServer();
            requestServer.serverThread = Thread.currentThread();
            requestServer.server.join();
        } catch (InterruptedException e) {
            System.out.println("Exiting RequestServer");
        } catch (Exception e) {
            System.out.println("Exiting RequestServer due to exception: " + e);
            e.printStackTrace();
        }
    }
    
    public RequestServer startServer() {
        RequestServer requestServer = null;
        try {
            this.server = new Server(8080);
            RequestHandler r = new RequestHandler();
            this.server.setHandler(r);
            this.server.start();
            // server.dumpStdErr();
        } catch (Exception e) {
            System.out.println("Could not start RequestServer: " + e);
            e.printStackTrace();
        }
        return requestServer;
    }
    
    public void stopServer() {
        stopServerInternal();
    }
    
    private void stopServerInternal() {
        if (this.serverThread != null) {
            this.serverThread.interrupt();
        }
    }


}
