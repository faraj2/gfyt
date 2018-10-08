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

public class BlueView extends View {
    Paint MyPaint = new Paint();
    Shader linearGradientShader;

    int shaderColor0 = Color.parseColor("#8AE5F8");
   // int shaderColor2 = Color.parseColor("#8AE5F8");
    int shaderColor1 = Color.parseColor("#111111");
    float w,h;
    public BlueView(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        w = getWidth();
        h = getHeight();




        MyPaint.setStyle(Paint.Style.FILL);


        MyPaint.setAntiAlias(true);


        linearGradientShader = new LinearGradient(w,0,0,h,new int[]{shaderColor0,shaderColor0,shaderColor1},
                new float[]{0f,0.1f,0.75f}, Shader.TileMode.REPEAT);

        MyPaint.setShader(linearGradientShader);
        canvas.drawRect(0, 0, w, h, MyPaint);

    }
}
