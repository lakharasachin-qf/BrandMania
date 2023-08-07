package com.make.mybrand.views;

import static android.view.View.LAYER_TYPE_SOFTWARE;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.content.ContextCompat;

import com.make.mybrand.R;


class ViewUtils {
    public static Drawable generateBackgroundWithShadow(View view, @ColorRes int backgroundColor,
                                                        @DimenRes int cornerRadius,
                                                        @ColorRes int shadowColor,
                                                        @DimenRes int elevation,
                                                        int shadowGravity) {
        float cornerRadiusValue = view.getContext().getResources().getDimension(cornerRadius);
        int elevationValue = (int) view.getContext().getResources().getDimension(elevation);
        int shadowColorValue = ContextCompat.getColor(view.getContext(), shadowColor);
        int backgroundColorValue = ContextCompat.getColor(view.getContext(), backgroundColor);

        float cornerRadiusValueTopLeft = view.getContext().getResources().getDimension(R.dimen.radius_corner_top_left);
        float cornerRadiusValueTopRight = view.getContext().getResources().getDimension(R.dimen.radius_corner_top_right);
        float cornerRadiusValueBottomLeft = view.getContext().getResources().getDimension(R.dimen.radius_corner_bottom_left);
        float cornerRadiusValueBottomRight = view.getContext().getResources().getDimension(R.dimen.radius_corner_bottom_right);

        float[] outerRadius = {
                cornerRadiusValueTopLeft, cornerRadiusValueTopLeft,
                cornerRadiusValueTopRight, cornerRadiusValueTopRight,
                cornerRadiusValueBottomRight, cornerRadiusValueBottomRight,
                cornerRadiusValueBottomLeft, cornerRadiusValueBottomLeft
        };

        Paint backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setShadowLayer(0, 0, 0, 0);

        Rect shapeDrawablePadding = new Rect();

        int DY;
        switch (shadowGravity) {
            case Gravity.CENTER:
                shapeDrawablePadding.top = elevationValue;
                shapeDrawablePadding.bottom = elevationValue;
                DY = 0;
                break;
            case Gravity.TOP:
                shapeDrawablePadding.top = elevationValue * 2;
                shapeDrawablePadding.bottom = elevationValue;
                DY = -1 * elevationValue / 3;
                break;
            default:
            case Gravity.BOTTOM:
                shapeDrawablePadding.top = elevationValue;
                shapeDrawablePadding.bottom = elevationValue * 2;
                DY = elevationValue / 3;
                break;
        }

        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setPadding(shapeDrawablePadding);

        shapeDrawable.getPaint().setColor(backgroundColorValue);
        shapeDrawable.getPaint().setShadowLayer(cornerRadiusValue / 3, 0, DY, shadowColorValue);

        view.setLayerType(LAYER_TYPE_SOFTWARE, shapeDrawable.getPaint());

        shapeDrawable.setShape(new RectShape());

        LayerDrawable drawable = new LayerDrawable(new Drawable[]{shapeDrawable});
        drawable.setLayerInset(0, elevationValue, elevationValue * 2, elevationValue, elevationValue * 2);

        return drawable;

    }
}