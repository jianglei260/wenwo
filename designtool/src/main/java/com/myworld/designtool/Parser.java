package com.myworld.designtool;

import android.util.Log;

import com.myworld.designtool.model.ViewNode;
import com.myworld.designtool.model.Window;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by jianglei on 16/9/10.
 */

public class Parser {
    public static ViewNode parseViewHierarchy(BufferedReader in, Window window) {
        ViewNode currentNode = null;
        int currentDepth = -1;
        try {
            String line;
            while (((line = in.readLine()) != null) &&
                    (!"DONE.".equalsIgnoreCase(line))) {
                int depth = 0;
                while (line.charAt(depth) == ' ') {
                    depth++;
                }
                while (depth <= currentDepth) {
                    if (currentNode != null) {
                        currentNode = currentNode.parent;
                    }
                    currentDepth--;
                }
                currentNode = new ViewNode(window, currentNode, line.substring(depth));
                currentDepth = depth;
            }
        } catch (IOException e) {
            Log.e("hierarchyviewer", "Error reading view hierarchy stream: " + e.getMessage());
            return null;
        }
        if (currentNode == null) {
            return null;
        }
        while (currentNode.parent != null) {
            currentNode = currentNode.parent;
        }
        return currentNode;
    }
}
