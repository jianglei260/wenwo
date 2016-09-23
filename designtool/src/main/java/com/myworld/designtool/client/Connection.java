package com.myworld.designtool.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import static android.R.attr.port;

/**
 * Created by jianglei on 16/9/11.
 */

public class Connection {
    private SocketChannel mSocketChannel;
    private BufferedReader mIn;
    private BufferedWriter mOut;
    private static final int VIEW_SERVER_DEFAULT_PORT = 4939;

    public Connection()
            throws IOException {
        this.mSocketChannel = SocketChannel.open();
        this.mSocketChannel.connect(new InetSocketAddress("127.0.0.1", VIEW_SERVER_DEFAULT_PORT));
        this.mSocketChannel.socket().setSoTimeout(40000);
    }

    public BufferedReader getInputStream()
            throws IOException {
        if (this.mIn == null) {
            this.mIn = new BufferedReader(new InputStreamReader(this.mSocketChannel.socket().getInputStream(), "utf-8"), 32 * 1024);
        }
        return this.mIn;
    }

    public BufferedWriter getOutputStream()
            throws IOException {
        if (this.mOut == null) {
            this.mOut = new BufferedWriter(new OutputStreamWriter(this.mSocketChannel.socket().getOutputStream(), "utf-8"), 32 * 1024);
        }
        return this.mOut;
    }

    public Socket getSocket() {
        return this.mSocketChannel.socket();
    }

    public void sendCommand(String command)
            throws IOException {
        BufferedWriter out = getOutputStream();
        out.write(command);
        out.newLine();
        out.flush();
    }

    public void close() {
        try {
            if (this.mIn != null) {
                this.mIn.close();
            }
        } catch (IOException e) {
        }
        try {
            if (this.mOut != null) {
                this.mOut.close();
            }
        } catch (IOException e) {
        }
        try {
            this.mSocketChannel.close();
        } catch (IOException e) {
        }
    }
}
