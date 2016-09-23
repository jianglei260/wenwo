package com.myworld.designtool.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.myworld.designtool.model.ViewNode;

import java.util.List;

/**
 * Created by jianglei on 16/9/12.
 */

public class BorderView extends View {
    private ViewNode viewNode;
    private Paint paint;

    public BorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BorderView(Context context) {
        this(context, null);
    }

    public BorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
    }

    public ViewNode getViewNode() {
        return viewNode;
    }

    public void setViewNode(ViewNode viewNode) {
        this.viewNode = viewNode;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (viewNode == null)
            return;
        drawViewNode(canvas, viewNode, false);
    }

    public void drawViewNode(Canvas canvas, ViewNode viewNode, boolean attachParent) {
        Path path = new Path();
        int left = !attachParent ? 0 : viewNode.parent.left + viewNode.left;
        int top = !attachParent ? 0 : viewNode.parent.top + viewNode.top;
        viewNode.left = left;
        viewNode.top = top;
        path.moveTo(left, top);
        path.lineTo(left + viewNode.width, top);
        path.lineTo(left + viewNode.width, top + viewNode.height);
        path.lineTo(left, top + viewNode.height);
        path.lineTo(left, top);
        path.close();
        canvas.drawPath(path, paint);
        if (viewNode.children != null && viewNode.children.size() > 0) {
            List<ViewNode> children = viewNode.children;
            for (ViewNode child : children) {
                drawViewNode(canvas, child, true);
            }
        }
    }
}
