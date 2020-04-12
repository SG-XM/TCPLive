package org.zq.live;

import java.io.IOException;
import java.net.Socket;

/**
 * @CreadBy ：SGXM
 * @date 2020/3/17
 */
public class SocketInstance {

    private static Socket socket;
    private static final String HOST = "192.168.155.209";
    private static final int PORT = 4321;

    public static Socket getSocket() {
        if (socket == null) {
            try {
                socket = new Socket(HOST, PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return socket;
        } else {
            return socket;
        }
    }
}
