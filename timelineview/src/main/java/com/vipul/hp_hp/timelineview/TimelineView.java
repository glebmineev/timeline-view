package com.vipul.hp_hp.timelineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimelineView extends View {

    private TextDrawable mMarker;
    private Drawable mMarkerRing;
    private Drawable mStartLine;
    private Drawable mEndLine;
    private int mMarkerSize;
    private int mLineSize;
    private int mLineOrientation;
    private boolean mMarkerInCenter;

    private Rect mBounds;
    private Context mContext;


    public TimelineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.timeline_style);
        //mMarker = typedArray.getDrawable(R.styleable.timeline_style_marker);
        mStartLine = typedArray.getDrawable(R.styleable.timeline_style_line);
        mEndLine = typedArray.getDrawable(R.styleable.timeline_style_line);
        mMarkerSize = typedArray.getDimensionPixelSize(R.styleable.timeline_style_marker_size, 25);
        mLineSize = typedArray.getDimensionPixelSize(R.styleable.timeline_style_line_size, 2);
        mLineOrientation = typedArray.getInt(R.styleable.timeline_style_line_orientation, 1);
        mMarkerInCenter = typedArray.getBoolean(R.styleable.timeline_style_markerInCenter, true);
        typedArray.recycle();

        if(mMarker == null) {
            TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder()
                    .rect();
            mMarker = mDrawableBuilder.build("12.10", Color.WHITE);
            mMarkerRing = mContext.getResources().getDrawable(R.drawable.marker);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Width measurements of the width and height and the inside view of child controls
        int w = mMarkerSize + getPaddingLeft() + getPaddingRight();
        int h = mMarkerSize + getPaddingTop() + getPaddingBottom();

        // Width and height to determine the final view through a systematic approach to decision-making
        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);

        setMeasuredDimension(widthSize, heightSize);
        initDrawable();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // When the view is displayed when the callback
        // Positioning Drawable coordinates, then draw
        initDrawable();
    }

    private void initDrawable() {
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int width = getWidth();// Width of current custom view
        int height = getHeight();

        int cWidth = width - pLeft - pRight;// Circle width
        int cHeight = height - pTop - pBottom;

        int markSize = Math.min(mMarkerSize, Math.min(cWidth, cHeight));


        if(mMarkerInCenter) { //Marker in center is true
            float rightPaddingDps = 16;
            Float rightPaddingPxs = rightPaddingDps * getResources().getDisplayMetrics().density;
            float leftPaddingDps = 15;
            Float leftPaddingPxs = leftPaddingDps * getResources().getDisplayMetrics().density;
            if(mMarkerRing != null) {
                mMarkerRing.setBounds(((width/2) - (markSize/2) + leftPaddingPxs.intValue()),(height/2) - (markSize/2), (width/2) + (markSize/2) + rightPaddingPxs.intValue(),(height/2) + (markSize/2));
                mBounds = mMarkerRing.getBounds();
            }

        } else { //Marker in center us false

            if(mMarkerRing != null) {
                mMarkerRing.setBounds(pLeft,pTop,pLeft + markSize,pTop + markSize);
                mBounds = mMarkerRing.getBounds();
            }
        }

        int centerX = mBounds.centerX();
        int lineLeft = centerX - (mLineSize >> 1);

        if(mLineOrientation==0) {

            //Horizontal Line

            if(mStartLine != null) {
                mStartLine.setBounds(0, pTop + (mBounds.height()/2), mBounds.left, (mBounds.height()/2) + pTop + mLineSize);
            }

            if(mEndLine != null) {
                mEndLine.setBounds(mBounds.right, pTop + (mBounds.height()/2), width, (mBounds.height()/2) + pTop + mLineSize);
            }

        } else {

            //Vertical Line

            if(mStartLine != null) {
                mStartLine.setBounds(lineLeft, 0, mLineSize + lineLeft, mBounds.top);
            }

            if(mEndLine != null) {
                mEndLine.setBounds(lineLeft, mBounds.bottom, mLineSize + lineLeft, height);
            }

            if (mMarker != null) {
                float rightPaddingDps = 16;
                Float rightPaddingPxs = rightPaddingDps * getResources().getDisplayMetrics().density;
                mMarker.setBounds(rightPaddingPxs.intValue(), mBounds.top - 10, convertDPSToPxs(45F).intValue(), mBounds.bottom + 10);
            }


        }

    }

    public Float convertDPSToPxs(float dps){
        return dps * getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mMarkerRing != null) {
            mMarkerRing.draw(canvas);
        }

        if(mMarker != null) {
            mMarker.draw(canvas);
        }

        if(mStartLine != null) {
            mStartLine.draw(canvas);
        }

        if(mEndLine != null) {
            mEndLine.draw(canvas);
        }
    }

    public void setMarker(TextDrawable marker) {
        mMarker = marker;
        initDrawable();
    }

    public void setStartLine(Drawable startline) {
        mStartLine = startline;
        initDrawable();
    }

    public void setEndLine(Drawable endLine) {
        mEndLine = endLine;
        initDrawable();
    }

    public void setMarkerSize(int markerSize) {
        mMarkerSize = markerSize;
        initDrawable();
    }

    public void setLineSize(int lineSize) {
        mLineSize = lineSize;
        initDrawable();
    }

    public void initLine(int viewType) {

        if(viewType == LineType.BEGIN) {
            setStartLine(null);
        } else if(viewType == LineType.END) {
            setEndLine(null);
        } else if(viewType == LineType.ONLYONE) {
            setStartLine(null);
            setEndLine(null);
        }

        initDrawable();
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
