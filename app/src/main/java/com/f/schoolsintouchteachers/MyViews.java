package com.f.schoolsintouchteachers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

/**
 * Created by admin on 1/30/2018.
 */

public class MyViews extends View {
    public MyViews(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w,h;
        w = getWidth();
        h = getHeight();



        Paint MyPaint = new Paint();
        MyPaint.setStyle(Paint.Style.FILL);

        int shaderColor0 = Color.BLACK;
        int shaderColor1 = Color.WHITE;
        int shaderColor2 = Color.RED;
        int shaderColor3 = Color.WHITE;
        int shaderColor4 = Color.GREEN;
        MyPaint.setAntiAlias(true);
        Shader linearGradientShader;

        linearGradientShader = new LinearGradient(0,0,w,h,new int[]{shaderColor0,shaderColor1,shaderColor2,shaderColor3,shaderColor4},
                new float[]{0f,0.3f,0.4f,0.7f,1f}, Shader.TileMode.MIRROR);

        MyPaint.setShader(linearGradientShader);
        canvas.drawRect(0, 0, w, h, MyPaint);

    }
}
