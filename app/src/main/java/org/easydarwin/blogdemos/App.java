package org.easydarwin.blogdemos;

import android.app.Application;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    private static App sInstance;

    public static final String SERVER_HOST = "172.28.178.245";
    private Socket socket;
    private final int PORT = 8081;
    private Map<String, Socket> sockets = new HashMap<>();
    public Socket getSocket(String ip) {
        if (sockets.get(ip) == null) {
            try {
                sockets.put(ip, new Socket(ip, PORT));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sockets.get(ip);
        } else {
            return sockets.get(ip);
        }
    }

    public Socket getSocket() {
        if (socket == null) {
            try {
                socket = new Socket(SERVER_HOST, PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return socket;
        } else {
            return socket;
        }
    }

    public void removeSocket(String ip) {
        sockets.remove(ip);
    }

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

}
