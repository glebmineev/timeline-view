package ru.spb.sigma.timelineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.vipul.hp_hp.timelineview.LineType;
import com.vipul.hp_hp.timelineview.R;

/**
 * Created by gleb on 30.10.16.
 */
public class SimpleTimelineView extends View {

    //Текст слева со временем
    private SimpleTextDrawable label;
    //Маркер в виде кольца
    private Drawable marker;
    //Линия до маркера
    private Drawable startLine;
    //Линия после маркера
    private Drawable endLine;
    //Размер маркера
    private int markerSize;
    //толщина линий
    private int lineSize;

    private Rect bounds;
    private Context mContext;

    public SimpleTimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.simple_timeline_style);
        marker = typedArray.getDrawable(R.styleable.simple_timeline_style_marker_);
        startLine = typedArray.getDrawable(R.styleable.simple_timeline_style_line_);
        endLine = typedArray.getDrawable(R.styleable.simple_timeline_style_line_);
        markerSize = typedArray.getDimensionPixelSize(R.styleable.simple_timeline_style_marker_size_, 25);
        lineSize = typedArray.getDimensionPixelSize(R.styleable.simple_timeline_style_line_size_, 2);
        typedArray.recycle();

        if (marker == null) {
            marker = mContext.getResources().getDrawable(R.drawable.marker);
        }

    }

    private void initDrawable(String date) {
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int width = getWidth();// Width of current custom view
        int height = getHeight();

        int cWidth = width - pLeft - pRight;// Circle width
        int cHeight = height - pTop - pBottom;

        int markSize = Math.min(markerSize, Math.min(cWidth, cHeight));

        int stdMarginPixelSize = getResources().getDimensionPixelSize(R.dimen.std_margin);
        int stdMargin2PixelSize = getResources().getDimensionPixelSize(R.dimen.std_margin_2);
        int stdMargin45PixelSize = getResources().getDimensionPixelSize(R.dimen.std_margin_45);
        if (marker != null) {
            marker.setBounds(((width / 2) - (markSize / 2) + stdMargin2PixelSize), (height / 2) - (markSize / 2), (width / 2) + (markSize / 2) + stdMarginPixelSize, (height / 2) + (markSize / 2));
            bounds = marker.getBounds();
        }

         /*else { //Marker in center us false

            if(mMarkerRing != null) {
                mMarkerRing.setBounds(pLeft,pTop,pLeft + markSize,pTop + markSize);
                mBounds = mMarkerRing.getBounds();
            }
        }*/

        int centerX = bounds.centerX();
        int lineLeft = centerX - (lineSize >> 1);

        if (startLine != null) {
            startLine.setBounds(lineLeft, 0, lineSize + lineLeft, bounds.top);
        }

        if (endLine != null) {
            endLine.setBounds(lineLeft, bounds.bottom, lineSize + lineLeft, height);
        }

        if (label != null) {
            SimpleTextDrawable.IBuilder mDrawableBuilder = SimpleTextDrawable.builder();
            label = mDrawableBuilder.build(date, Color.WHITE);
            label.setBounds(stdMarginPixelSize, bounds.top - 10, stdMargin45PixelSize, bounds.bottom + 10);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (label != null) {
            label.draw(canvas);
        }

        if(marker != null) {
            marker.draw(canvas);
        }

        if(startLine != null) {
            startLine.draw(canvas);
        }

        if(endLine != null) {
            endLine.draw(canvas);
        }
    }

    public void setStartLine(Drawable startline) {
        this.startLine = startline;
    }

    public void setEndLine(Drawable endLine) {
        this.endLine = endLine;
    }

    public void initLine(int viewType, String date) {

        if(viewType == LineType.BEGIN) {
            setStartLine(null);
        } else if(viewType == LineType.END) {
            setEndLine(null);
        } else if(viewType == LineType.ONLYONE) {
            setStartLine(null);
            setEndLine(null);
        }
        initDrawable(date);
    }

    public static int getTimeLineViewType(int position, int total_size) {

        if(total_size == 1) {
            return LineType.ONLYONE;
        } else if(position == 0) {
            return LineType.BEGIN;
        } else if(position == total_size - 1) {
            return LineType.END;
        } else {
            return LineType.NORMAL;
        }
    }

}
