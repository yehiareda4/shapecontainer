package io.github.yehiareda.shapecontainer.shapes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;

import io.github.yehiareda.shapecontainer.R;
import io.github.yehiareda.shapecontainer.ShapeContainer;
import io.github.yehiareda.shapecontainer.manager.ClipPathManager;

public class RoundRectView extends ShapeContainer {

    private final RectF rectF = new RectF();
    //region border
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF borderRectF = new RectF();
    private final Path borderPath = new Path();
    private float topStartRadius = 0f;
    private float topEndRadius = 0f;
    private float bottomEndRadius = 0f;
    private float bottomStartRadius = 0f;
    @ColorInt
    private int borderColor = Color.WHITE;
    private float borderWidthPx = 0f;
    //endregion

    public RoundRectView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public RoundRectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundRectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.RoundRectView);
            topStartRadius = attributes.getDimensionPixelSize(R.styleable.RoundRectView_shape_roundRect_topStartRadius, (int) topStartRadius);
            topEndRadius = attributes.getDimensionPixelSize(R.styleable.RoundRectView_shape_roundRect_topEndRadius, (int) topEndRadius);
            bottomStartRadius = attributes.getDimensionPixelSize(R.styleable.RoundRectView_shape_roundRect_bottomStartRadius, (int) bottomStartRadius);
            bottomEndRadius = attributes.getDimensionPixelSize(R.styleable.RoundRectView_shape_roundRect_bottomEndRadius, (int) bottomEndRadius);
            borderColor = attributes.getColor(R.styleable.RoundRectView_shape_roundRect_borderColor, borderColor);
            borderWidthPx = attributes.getDimensionPixelSize(R.styleable.RoundRectView_shape_roundRect_borderWidth, (int) borderWidthPx);
//            attributes.recycle();
        }
        borderPaint.setStyle(Paint.Style.STROKE);
        super.setClipPathCreator(new ClipPathManager.ClipPathCreator() {
            @Override
            public Path createClipPath(int width, int height) {
                rectF.set(0, 0, width, height);
                return generatePath(rectF, limitSize(topStartRadius, width, height), limitSize(topEndRadius, width, height), limitSize(bottomEndRadius, width, height), limitSize(bottomStartRadius, width, height));
            }

            @Override
            public boolean requiresBitmap() {
                return false;
            }
        });
    }

    protected float limitSize(float from, final float width, final float height) {
        return Math.min(from, Math.min(width, height));
    }

    @Override
    public void requiresShapeUpdate() {
        borderRectF.set(borderWidthPx / 2f, borderWidthPx / 2f, getWidth() - borderWidthPx / 2f, getHeight() - borderWidthPx / 2f);

        borderPath.set(generatePath(borderRectF, topStartRadius, topEndRadius, bottomEndRadius, bottomStartRadius));
        super.requiresShapeUpdate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (borderWidthPx > 0) {
            borderPaint.setStrokeWidth(borderWidthPx);
            borderPaint.setColor(borderColor);
            canvas.drawPath(borderPath, borderPaint);
        }
    }

    private Path generatePath(RectF rect, float topLeftRadius, float topRightRadius, float bottomRightRadius, float bottomLeftRadius) {
        if (Locale.getDefault().getLanguage().equals("ar")) {
            return generatePath(false, rect, topRightRadius, topLeftRadius, bottomLeftRadius, bottomRightRadius);
        } else {
            return generatePath(false, rect, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius);
        }
    }

    private Path generatePath(boolean useBezier, RectF rect, float topLeftRadius, float topRightRadius, float bottomRightRadius, float bottomLeftRadius) {
        final Path path = new Path();

        final float left = rect.left;
        final float top = rect.top;
        final float bottom = rect.bottom;
        final float right = rect.right;

        final float maxSize = Math.min(rect.width() / 2f, rect.height() / 2f);

        float topLeftRadiusAbs = Math.abs(topLeftRadius);
        float topRightRadiusAbs = Math.abs(topRightRadius);
        float bottomLeftRadiusAbs = Math.abs(bottomLeftRadius);
        float bottomRightRadiusAbs = Math.abs(bottomRightRadius);

        if (topLeftRadiusAbs > maxSize) {
            topLeftRadiusAbs = maxSize;
        }
        if (topRightRadiusAbs > maxSize) {
            topRightRadiusAbs = maxSize;
        }
        if (bottomLeftRadiusAbs > maxSize) {
            bottomLeftRadiusAbs = maxSize;
        }
        if (bottomRightRadiusAbs > maxSize) {
            bottomRightRadiusAbs = maxSize;
        }

        path.moveTo(left + topLeftRadiusAbs, top);
        path.lineTo(right - topRightRadiusAbs, top);

        //float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo
        if (useBezier) {
            path.quadTo(right, top, right, top + topRightRadiusAbs);
        } else {
            final float arc = topRightRadius > 0 ? 90 : -270;
            path.arcTo(new RectF(right - topRightRadiusAbs * 2f, top, right, top + topRightRadiusAbs * 2f), -90, arc);
        }
        path.lineTo(right, bottom - bottomRightRadiusAbs);
        if (useBezier) {
            path.quadTo(right, bottom, right - bottomRightRadiusAbs, bottom);
        } else {
            final float arc = bottomRightRadiusAbs > 0 ? 90 : -270;
            path.arcTo(new RectF(right - bottomRightRadiusAbs * 2f, bottom - bottomRightRadiusAbs * 2f, right, bottom), 0, arc);
        }
        path.lineTo(left + bottomLeftRadiusAbs, bottom);
        if (useBezier) {
            path.quadTo(left, bottom, left, bottom - bottomLeftRadiusAbs);
        } else {
            final float arc = bottomLeftRadiusAbs > 0 ? 90 : -270;
            path.arcTo(new RectF(left, bottom - bottomLeftRadiusAbs * 2f, left + bottomLeftRadiusAbs * 2f, bottom), 90, arc);
        }
        path.lineTo(left, top + topLeftRadiusAbs);
        if (useBezier) {
            path.quadTo(left, top, left + topLeftRadiusAbs, top);
        } else {
            final float arc = topLeftRadiusAbs > 0 ? 90 : -270;
            path.arcTo(new RectF(left, top, left + topLeftRadiusAbs * 2f, top + topLeftRadiusAbs * 2f), 180, arc);
        }
        path.close();

        return path;
    }

    public float getTopStartRadius() {
        return topStartRadius;
    }

    public void setTopStartRadius(float topStartRadius) {
        this.topStartRadius = topStartRadius;
        requiresShapeUpdate();
    }

    public float getTopLeftRadiusDp() {
        return pxToDp(getTopStartRadius());
    }

    public void setTopLeftRadiusDp(float topLeftRadius) {
        setTopStartRadius(dpToPx(topLeftRadius));
    }

    public float getTopEndRadius() {
        return topEndRadius;
    }

    public void setTopEndRadius(float topEndRadius) {
        this.topEndRadius = topEndRadius;
        requiresShapeUpdate();
    }

    public float getTopRightRadiusDp() {
        return pxToDp(getTopEndRadius());
    }

    public void setTopRightRadiusDp(float topRightRadius) {
        setTopEndRadius(dpToPx(topRightRadius));
    }

    public float getBottomEndRadius() {
        return bottomEndRadius;
    }

    public void setBottomEndRadius(float bottomEndRadius) {
        this.bottomEndRadius = bottomEndRadius;
        requiresShapeUpdate();
    }

    public float getBottomRightRadiusDp() {
        return pxToDp(getBottomEndRadius());
    }

    public void setBottomRightRadiusDp(float bottomRightRadius) {
        setBottomEndRadius(dpToPx(bottomRightRadius));
    }

    public float getBottomStartRadius() {
        return bottomStartRadius;
    }

    public void setBottomStartRadius(float bottomStartRadius) {
        this.bottomStartRadius = bottomStartRadius;
        requiresShapeUpdate();
    }

    public float getBottomLeftRadiusDp() {
        return pxToDp(getBottomStartRadius());
    }

    public void setBottomLeftRadiusDp(float bottomLeftRadius) {
        setBottomStartRadius(dpToPx(bottomLeftRadius));
    }

    public float getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        requiresShapeUpdate();
    }

    public float getBorderWidth() {
        return borderWidthPx;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidthPx = borderWidth;
        requiresShapeUpdate();
    }

    public float getBorderWidthDp() {
        return pxToDp(getBorderWidth());
    }

    public void setBorderWidthDp(float borderWidth) {
        setBorderWidth(dpToPx(borderWidth));
    }
}