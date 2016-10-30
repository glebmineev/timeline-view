package ru.spb.sigma.timelineview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

/**
 * Created by gleb on 30.10.16.
 */
public class SimpleTextDrawable extends ShapeDrawable {

    private final Paint textPaint;
    private final Paint borderPaint;
    private static final float SHADE_FACTOR = 0.9f;
    private final String text;
    private final int color;
    private final int height;
    private final int width;
    private final int fontSize;
    private final int borderThickness;

    private SimpleTextDrawable(Builder builder) {
        super(builder.shape);

        // shape properties
        height = builder.height;
        width = builder.width;

        // text and color
        text = builder.toUpperCase ? builder.text.toUpperCase() : builder.text;
        color = builder.color;

        // text paint settings
        fontSize = builder.fontSize;
        textPaint = new Paint();
        textPaint.setColor(builder.textColor);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(builder.isBold);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(builder.font);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(builder.borderThickness);

        // border paint settings
        borderThickness = builder.borderThickness;
        borderPaint = new Paint();
        borderPaint.setColor(getDarkerShade(color));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderThickness);

        // drawable paint color
        Paint paint = getPaint();
        paint.setColor(color);

    }

    private int getDarkerShade(int color) {
        return Color.rgb((int)(SHADE_FACTOR * Color.red(color)),
                (int)(SHADE_FACTOR * Color.green(color)),
                (int)(SHADE_FACTOR * Color.blue(color)));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect r = getBounds();

        // draw border
        if (borderThickness > 0) {
            drawBorder(canvas);
        }

        int count = canvas.save();
        canvas.translate(r.left, r.top);

        // draw text
        int width = this.width < 0 ? r.width() : this.width;
        int height = this.height < 0 ? r.height() : this.height;
        int fontSize = this.fontSize < 0 ? (Math.min(width, height) / 2) : this.fontSize;
        textPaint.setTextSize(fontSize);
        canvas.drawText(text, width / 2, height / 2 - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);

        canvas.restoreToCount(count);

    }

    private void drawBorder(Canvas canvas) {
        RectF rect = new RectF(getBounds());
        rect.inset(borderThickness / 2, borderThickness / 2);
        canvas.drawRect(rect, borderPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    public static IBuilder builder() {
        return new Builder();
    }

    public static class Builder implements IBuilder {

        private String text;
        private int color;
        private int borderThickness;
        private int width;
        private int height;
        private Typeface font;
        private RectShape shape;
        public int textColor;
        private int fontSize;
        private boolean isBold;
        private boolean toUpperCase;

        private Builder() {
            text = "";
            color = Color.GRAY;
            textColor = Color.BLACK;
            borderThickness = 0;
            width = -1;
            height = -1;
            shape = new RectShape();
            font = Typeface.create("sans-serif-light", Typeface.NORMAL);
            fontSize = -1;
            isBold = false;
            toUpperCase = false;
        }

        @Override
        public IBuilder width(int width) {
            this.width = width;
            return this;
        }

        @Override
        public IBuilder height(int height) {
            this.height = height;
            return this;
        }

        @Override
        public IBuilder textColor(int color) {
            this.textColor = color;
            return this;
        }

        @Override
        public IBuilder withBorder(int thickness) {
            this.borderThickness = thickness;
            return this;
        }

        @Override
        public IBuilder useFont(Typeface font) {
            this.font = font;
            return this;
        }

        @Override
        public IBuilder fontSize(int size) {
            this.fontSize = size;
            return this;
        }

        @Override
        public IBuilder bold() {
            this.isBold = true;
            return this;
        }

        @Override
        public IBuilder toUpperCase() {
            this.toUpperCase = true;
            return this;
        }

        @Override
        public SimpleTextDrawable build(String text, int color) {
            this.color = color;
            this.text = text;
            return new SimpleTextDrawable(this);
        }

    }

    public interface IBuilder {

        IBuilder width(int width);

        IBuilder height(int height);

        IBuilder textColor(int color);

        IBuilder withBorder(int thickness);

        IBuilder useFont(Typeface font);

        IBuilder fontSize(int size);

        IBuilder bold();

        IBuilder toUpperCase();

        SimpleTextDrawable build(String text, int color);

    }

}
