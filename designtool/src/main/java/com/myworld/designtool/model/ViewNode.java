package com.myworld.designtool.model;

/**
 * Created by jianglei on 16/9/10.
 */

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class ViewNode {
    private static final double RED_THRESHOLD = 0.8D;
    private static final double YELLOW_THRESHOLD = 0.5D;
    public static final String MISCELLANIOUS = "miscellaneous";
    public String id;
    public String name;
    public String hashCode;

    public enum ProfileRating {
        RED, YELLOW, GREEN, NONE;

        ProfileRating() {
        }
    }

    public List<Property> properties = new ArrayList();
    public Map<String, Property> namedProperties = new HashMap();
    public ViewNode parent;
    public List<ViewNode> children = new ArrayList();
    public int left;
    public int top;
    public int width;
    public int height;
    public int scrollX;
    public int scrollY;
    public int paddingLeft;
    public int paddingRight;
    public int paddingTop;
    public int paddingBottom;
    public int marginLeft;
    public int marginRight;
    public int marginTop;
    public int marginBottom;
    public int baseline;
    public boolean willNotDraw;
    public boolean hasMargins;
    public boolean hasFocus;
    public int index;
    public double measureTime;
    public double layoutTime;
    public double drawTime;
    public ProfileRating measureRating = ProfileRating.NONE;
    public ProfileRating layoutRating = ProfileRating.NONE;
    public ProfileRating drawRating = ProfileRating.NONE;
    public Set<String> categories = new TreeSet();
    public Window window;
    public Bitmap image;
    public int imageReferences = 1;
    public int viewCount;
    public boolean filtered;
    public int protocolVersion;

    public ViewNode(Window window, ViewNode parent, String data) {
        this.window = window;
        this.parent = parent;
        this.index = (this.parent == null ? 0 : this.parent.children.size());
        if (this.parent != null) {
            this.parent.children.add(this);
        }
        int delimIndex = data.indexOf('@');
        if (delimIndex < 0) {
            throw new IllegalArgumentException("Invalid format for ViewNode, missing @: " + data);
        }
        this.name = data.substring(0, delimIndex);
        data = data.substring(delimIndex + 1);
        delimIndex = data.indexOf(' ');
        this.hashCode = data.substring(0, delimIndex);
        if (data.length() > delimIndex + 1) {
            loadProperties(data.substring(delimIndex + 1).trim());
        } else {
            this.id = "unknown";
            this.width = (this.height = 10);
        }
        this.measureTime = -1.0D;
        this.layoutTime = -1.0D;
        this.drawTime = -1.0D;
    }

    public void dispose() {
        int N = this.children.size();
        for (int i = 0; i < N; i++) {
            this.children.get(i).dispose();
        }
        dereferenceImage();
    }

    public void referenceImage() {
        this.imageReferences += 1;
    }

    public void dereferenceImage() {
        this.imageReferences -= 1;
        if ((this.image != null) && (this.imageReferences == 0)) {
            this.image.recycle();
        }
    }

    private void loadProperties(String data) {
        int start = 0;
        boolean stop;
        do {
            int index = data.indexOf('=', start);
            Property property = new Property();
            property.name = data.substring(start, index);

            int index2 = data.indexOf(',', index + 1);
            int length = Integer.parseInt(data.substring(index + 1, index2));
            start = index2 + 1 + length;
            property.value = data.substring(index2 + 1, index2 + 1 + length);

            this.properties.add(property);
            this.namedProperties.put(property.name, property);

            stop = start >= data.length();
            if (!stop) {
                start++;
            }
        } while (!stop);
        Collections.sort(this.properties, new Comparator<ViewNode.Property>() {
            public int compare(ViewNode.Property source, ViewNode.Property destination) {
                return source.name.compareTo(destination.name);
            }
        });
        this.id = this.namedProperties.get("mID").value;

        this.left = (this.namedProperties.containsKey("mLeft") ? getInt("mLeft", 0) : getInt("layout:mLeft", 0));

        this.top = (this.namedProperties.containsKey("mTop") ? getInt("mTop", 0) : getInt("layout:mTop", 0));
        this.width = (this.namedProperties.containsKey("getWidth()") ? getInt("getWidth()", 0) : getInt("layout:getWidth()", 0));

        this.height = (this.namedProperties.containsKey("getHeight()") ? getInt("getHeight()", 0) : getInt("layout:getHeight()", 0));

        this.scrollX = (this.namedProperties.containsKey("mScrollX") ? getInt("mScrollX", 0) : getInt("scrolling:mScrollX", 0));

        this.scrollY = (this.namedProperties.containsKey("mScrollY") ? getInt("mScrollY", 0) : getInt("scrolling:mScrollY", 0));

        this.paddingLeft = (this.namedProperties.containsKey("mPaddingLeft") ? getInt("mPaddingLeft", 0) : getInt("padding:mPaddingLeft", 0));

        this.paddingRight = (this.namedProperties.containsKey("mPaddingRight") ? getInt("mPaddingRight", 0) : getInt("padding:mPaddingRight", 0));

        this.paddingTop = (this.namedProperties.containsKey("mPaddingTop") ? getInt("mPaddingTop", 0) : getInt("padding:mPaddingTop", 0));

        this.paddingBottom = (this.namedProperties.containsKey("mPaddingBottom") ? getInt("mPaddingBottom", 0) : getInt("padding:mPaddingBottom", 0));

        this.marginLeft = (this.namedProperties.containsKey("layout_leftMargin") ? getInt("layout_leftMargin", Integer.MIN_VALUE) : getInt("layout:layout_leftMargin", Integer.MIN_VALUE));

        this.marginRight = (this.namedProperties.containsKey("layout_rightMargin") ? getInt("layout_rightMargin", Integer.MIN_VALUE) : getInt("layout:layout_rightMargin", Integer.MIN_VALUE));

        this.marginTop = (this.namedProperties.containsKey("layout_topMargin") ? getInt("layout_topMargin", Integer.MIN_VALUE) : getInt("layout:layout_topMargin", Integer.MIN_VALUE));

        this.marginBottom = (this.namedProperties.containsKey("layout_bottomMargin") ? getInt("layout_bottomMargin", Integer.MIN_VALUE) : getInt("layout:layout_bottomMargin", Integer.MIN_VALUE));

        this.baseline = (this.namedProperties.containsKey("getBaseline()") ? getInt("getBaseline()", 0) : getInt("layout:getBaseline()", 0));

        this.willNotDraw = (this.namedProperties.containsKey("willNotDraw()") ? getBoolean("willNotDraw()", false) : getBoolean("drawing:willNotDraw()", false));

        this.hasFocus = (this.namedProperties.containsKey("hasFocus()") ? getBoolean("hasFocus()", false) : getBoolean("focus:hasFocus()", false));

        this.hasMargins = ((this.marginLeft != Integer.MIN_VALUE) && (this.marginRight != Integer.MIN_VALUE) && (this.marginTop != Integer.MIN_VALUE) && (this.marginBottom != Integer.MIN_VALUE));
        for (String name : this.namedProperties.keySet()) {
            int index = name.indexOf(':');
            if (index != -1) {
                this.categories.add(name.substring(0, index));
            }
        }
        if (this.categories.size() != 0) {
            this.categories.add("miscellaneous");
        }
    }

    public void setProfileRatings() {
        int N = this.children.size();
        if (N > 1) {
            double totalMeasure = 0.0D;
            double totalLayout = 0.0D;
            double totalDraw = 0.0D;
            for (int i = 0; i < N; i++) {
                ViewNode child = this.children.get(i);
                totalMeasure += child.measureTime;
                totalLayout += child.layoutTime;
                totalDraw += child.drawTime;
            }
            for (int i = 0; i < N; i++) {
                ViewNode child = this.children.get(i);
                if (child.measureTime / totalMeasure >= 0.8D) {
                    child.measureRating = ProfileRating.RED;
                } else if (child.measureTime / totalMeasure >= 0.5D) {
                    child.measureRating = ProfileRating.YELLOW;
                } else {
                    child.measureRating = ProfileRating.GREEN;
                }
                if (child.layoutTime / totalLayout >= 0.8D) {
                    child.layoutRating = ProfileRating.RED;
                } else if (child.layoutTime / totalLayout >= 0.5D) {
                    child.layoutRating = ProfileRating.YELLOW;
                } else {
                    child.layoutRating = ProfileRating.GREEN;
                }
                if (child.drawTime / totalDraw >= 0.8D) {
                    child.drawRating = ProfileRating.RED;
                } else if (child.drawTime / totalDraw >= 0.5D) {
                    child.drawRating = ProfileRating.YELLOW;
                } else {
                    child.drawRating = ProfileRating.GREEN;
                }
            }
        }
        for (int i = 0; i < N; i++) {
            this.children.get(i).setProfileRatings();
        }
    }

    public void setViewCount() {
        this.viewCount = 1;
        int N = this.children.size();
        for (int i = 0; i < N; i++) {
            ViewNode child = this.children.get(i);
            child.setViewCount();
            this.viewCount += child.viewCount;
        }
    }

    public void filter(String text) {
        int dotIndex = this.name.lastIndexOf('.');
        String shortName = dotIndex == -1 ? this.name : this.name.substring(dotIndex + 1);
        this.filtered = ((!text.equals("")) && ((shortName.toLowerCase().contains(text.toLowerCase())) || ((!this.id.equals("NO_ID")) && (this.id.toLowerCase().contains(text.toLowerCase())))));

        int N = this.children.size();
        for (int i = 0; i < N; i++) {
            this.children.get(i).filter(text);
        }
    }

    private boolean getBoolean(String name, boolean defaultValue) {
        Property p = this.namedProperties.get(name);
        if (p != null) {
            try {
                return Boolean.parseBoolean(p.value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private int getInt(String name, int defaultValue) {
        Property p = this.namedProperties.get(name);
        if (p != null) {
            try {
                return Integer.parseInt(p.value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public String toString() {
        return this.name + "@" + this.hashCode;
    }

    public static class Property {
        public String name;
        public String value;

        public String toString() {
            return this.name + '=' + this.value;
        }
    }
}

