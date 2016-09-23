package com.myworld.designtool.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.myworld.designtool.Parser;
import com.myworld.designtool.model.ViewNode;
import com.myworld.designtool.model.ViewServerInfo;
import com.myworld.designtool.model.Window;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianglei on 16/9/10.
 */

public class ViewClient {
    private static ViewClient viewClient;
    private static final int VALUE_SERVER_VERSION = 4;

    public static ViewClient get() {
        if (viewClient == null)
            viewClient = new ViewClient();
        return viewClient;
    }

    ViewClient() {
    }

    public ViewNode dumpView(Window window) {
        try {
            Connection connection = new Connection();
            connection.sendCommand("DUMP " + window.encode());
            ViewNode viewNode = Parser.parseViewHierarchy(connection.getInputStream(), window);
            return viewNode;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<Window> loadWindows() {
        List<Window> windows = new ArrayList();
        ViewServerInfo serverInfo = getViewServerInfo();
        try {
            Connection connection = new Connection();
            connection.sendCommand("LIST");
            BufferedReader in = connection.getInputStream();
            String line;
            try {
                while (((line = in.readLine()) != null) &&
                        (!"DONE.".equalsIgnoreCase(line))) {
                    int index = line.indexOf(' ');
                    if (index != -1) {
                        String windowId = line.substring(0, index);
                        int id;
                        if (serverInfo.serverVersion > 2) {
                            id = (int) Long.parseLong(windowId, 16);
                        } else {
                            id = Integer.parseInt(windowId, 16);
                        }
                        Window w = new Window(line.substring(index + 1), id);
                        windows.add(w);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return windows;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return windows;
    }

    public Bitmap loadCapture(Window window, ViewNode node) {
        try {
            Connection connection = new Connection();
            connection.getSocket().setSoTimeout(5000);
            connection.sendCommand("CAPTURE " + window.encode() + " " + node.toString());
            return BitmapFactory.decodeStream(connection.getSocket().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ViewServerInfo getViewServerInfo() {
        try {
            Connection connection = new Connection();
            connection.sendCommand("SERVER");
            try {
                String version = connection.getInputStream().readLine();
                return new ViewServerInfo(Integer.valueOf(version), Integer.valueOf(version));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ViewServerInfo(VALUE_SERVER_VERSION, VALUE_SERVER_VERSION);
    }
}
