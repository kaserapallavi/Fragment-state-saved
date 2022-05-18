package com.example.imperativeassignment.customview.stepcomplitionforrecyclerview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IntDef;

import com.example.imperativeassignment.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class StepComplitionForRecyclerView extends View {

    public static final String TAG = StepComplitionForRecyclerView.class.getSimpleName();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineOrientation.HORIZONTAL, LineOrientation.VERTICAL})
    public @interface LineOrientation {
        int HORIZONTAL = 0;
        int VERTICAL = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineType.NORMAL, LineType.START, LineType.END, LineType.ONLYONE})
    private @interface LineType {
        int NORMAL = 0;
        int START = 1;
        int END = 2;
        int ONLYONE = 3;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MarkerPosition.TOP, MarkerPosition.BOTTOM, MarkerPosition.CENTER})
    public @interface MarkerPosition {
        int TOP = 0;
        int BOTTOM = 1;
        int CENTER = 2;
        int NONE = 3;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LineStyle.NORMAL, LineStyle.DASHED})
    public @interface LineStyle {
        int NORMAL = 0;
        int DASHED = 1;
    }

    private Drawable mMarker;
    private int mMarkerSize;
    private int mMarkerPaddingLeft;
    private int mMarkerPaddingTop;
    private int mMarkerPaddingRight;
    private int mMarkerPaddingBottom;
    /*private boolean mMarkerInCenter;*/
    private boolean mIsCompletedMileStone;
    private Paint mLinePaint = new Paint();
    private boolean mDrawStartLine = false;
    private boolean mDrawEndLine = false;
    private float mStartLineStartX, mStartLineStartY, mStartLineStopX, mStartLineStopY;
    private float mEndLineStartX, mEndLineStartY, mEndLineStopX, mEndLineStopY;
    private int mCompletedMileStoneColor, mInCompletedMileStoneColor;
    private boolean isStartLineCompletedMileStone, isEndLineCompletedMileStone;
    private int mLineWidth;
    private int mLineOrientation;
    private int mLineStyle;
    private int mMarkerPosition;
    private int mLineStyleDashLength;
    private int mLineStyleDashGap;
    private int mLinePadding;

    private Rect mBounds;

    public StepComplitionForRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StepComplitionView);
        mMarker = typedArray.getDrawable(R.styleable.StepComplitionView_marker);
        mMarkerSize = typedArray.getDimensionPixelSize(R.styleable.StepComplitionView_markerSize, Utils.dpToPx(19, getContext()));
        mCompletedMileStoneColor = typedArray.getColor(R.styleable.StepComplitionView_completedMileStoneLineColor, getResources().getColor(R.color.colorCompeletedMileStone));
        mInCompletedMileStoneColor = typedArray.getColor(R.styleable.StepComplitionView_inCompleteMileStoneLineColor, getResources().getColor(R.color.colorInCompeletedMileStone));
        mLineWidth = typedArray.getDimensionPixelSize(R.styleable.StepComplitionView_lineWidth, Utils.dpToPx(1, getContext()));
        mLineOrientation = typedArray.getInt(R.styleable.StepComplitionView_lineOrientation, LineOrientation.VERTICAL);
        mLinePadding = typedArray.getDimensionPixelSize(R.styleable.StepComplitionView_linePadding, 0);
        mLineStyle = typedArray.getInt(R.styleable.StepComplitionView_lineStyle, LineStyle.NORMAL);
        mMarkerPosition = typedArray.getInt(R.styleable.StepComplitionView_markerPosition, MarkerPosition.CENTER);
        mLineStyleDashLength = typedArray.getDimensionPixelSize(R.styleable.StepComplitionView_lineStyleDashLength, Utils.dpToPx(2f, getContext()));
        mLineStyleDashGap = typedArray.getDimensionPixelSize(R.styleable.StepComplitionView_lineStyleDashGap, Utils.dpToPx(2f, getContext()));
        typedArray.recycle();

        if (isInEditMode()) {
            mDrawStartLine = true;
            mDrawEndLine = true;
        }

        if (mMarker == null) {
            mMarker = getResources().getDrawable(R.drawable.ic_step_incompleted_tick);
        }

        initTimeline();
        initLinePaint();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
        initTimeline();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        initTimeline();
    }

    private void initTimeline() {

        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int width = getWidth();// Width of current custom view
        int height = getHeight();

        int cWidth = width - pLeft - pRight;// Circle width
        int cHeight = height - pTop - pBottom;

        int markSize = Math.min(mMarkerSize, Math.min(cWidth, cHeight));
        int left, top, right, bottom;
        switch (mMarkerPosition) {
            case MarkerPosition.CENTER:
                left = (width / 2) - (markSize / 2);
                right = (width / 2) + (markSize / 2);
                top = (height / 2) - (markSize / 2);
                bottom = (height / 2) + (markSize / 2);

                switch (mLineOrientation) {
                    case LineOrientation.HORIZONTAL: {
                        left += mMarkerPaddingLeft - mMarkerPaddingRight;
                        right += mMarkerPaddingLeft - mMarkerPaddingRight;
                        break;
                    }
                    case LineOrientation.VERTICAL: {
                        top += mMarkerPaddingTop - mMarkerPaddingBottom;
                        bottom += mMarkerPaddingTop - mMarkerPaddingBottom;
                        break;
                    }
                }

                if (mMarker != null) {
                    mMarker.setBounds(left, top, right, bottom);
                    mBounds = mMarker.getBounds();
                }
                break;

            case MarkerPosition.BOTTOM:
               /* left = pLeft;
                right = pLeft + markSize;*/
                left = (width / 2) - (markSize / 2);
                right = (width / 2) + (markSize / 2);
                top = height - markSize - pTop;
                bottom = height - pBottom;
                mDrawEndLine = false;

                switch (mLineOrientation) {
                    case LineOrientation.HORIZONTAL: {
                        left += mMarkerPaddingLeft - mMarkerPaddingRight;
                        right += mMarkerPaddingLeft - mMarkerPaddingRight;
                        break;
                    }
                    case LineOrientation.VERTICAL: {
                        top -= mMarkerPaddingTop - mMarkerPaddingBottom;
                        bottom -= mMarkerPaddingTop - mMarkerPaddingBottom;
                        break;
                    }
                }

                if (mMarker != null) {
                    mMarker.setBounds(left, top, right, bottom);
                    mBounds = mMarker.getBounds();
                }
                break;

            case MarkerPosition.TOP:
               /* left = pLeft;
                right = pLeft + markSize;*/
                left = (width / 2) - (markSize / 2);
                right = (width / 2) + (markSize / 2);
                top = pTop;
                bottom = pTop;
                mDrawStartLine = false;
                switch (mLineOrientation) {
                    case LineOrientation.HORIZONTAL: {
                        left += mMarkerPaddingLeft - mMarkerPaddingRight;
                        right += mMarkerPaddingLeft - mMarkerPaddingRight;
                        break;
                    }
                    case LineOrientation.VERTICAL: {
                        top += mMarkerPaddingTop - mMarkerPaddingBottom;
                        bottom += markSize + mMarkerPaddingTop - mMarkerPaddingBottom;
                        break;
                    }
                }

                if (mMarker != null) {
                    mMarker.setBounds(left, top, right, bottom);
                    mBounds = mMarker.getBounds();
                }
                break;

            case MarkerPosition.NONE:
                left = (width / 2) - (markSize / 2);
                right = (width / 2) + (markSize / 2);
                top = (height / 2) - (markSize / 2);
                bottom = (height / 2) + (markSize / 2);
                mDrawEndLine = false;

                switch (mLineOrientation) {
                    case LineOrientation.HORIZONTAL: {
                        left += mMarkerPaddingLeft - mMarkerPaddingRight;
                        right += mMarkerPaddingLeft - mMarkerPaddingRight;
                        break;
                    }
                    case LineOrientation.VERTICAL: {
                        top -= mMarkerPaddingTop - mMarkerPaddingBottom;
                        bottom -= mMarkerPaddingTop - mMarkerPaddingBottom;
                        break;
                    }
                }

                if (mMarker != null) {
                    mMarker.setBounds(left, top, right, bottom);
                    mBounds = mMarker.getBounds();
                    mMarker.setVisible(false, false);
                    mMarker = null;
                }
                break;

        }

        if (mLineOrientation == LineOrientation.HORIZONTAL) {
            if (mDrawStartLine) {
                mStartLineStartX = pLeft;
                mStartLineStartY = mBounds.centerY();
                mStartLineStopX = mBounds.left - mLinePadding;
                mStartLineStopY = mBounds.centerY();
            }

            if (mDrawEndLine) {
                if (mLineStyle == LineStyle.DASHED) {
                    mEndLineStartX = getWidth() - mLineStyleDashGap;
                    mEndLineStartY = mBounds.centerY();
                    mEndLineStopX = mBounds.right + mLinePadding;
                    mEndLineStopY = mBounds.centerY();
                } else {
                    mEndLineStartX = mBounds.right + mLinePadding;
                    mEndLineStartY = mBounds.centerY();
                    mEndLineStopX = getWidth();
                    mEndLineStopY = mBounds.centerY();
                }
            }
        } else {

            if (mDrawStartLine) {
                mStartLineStartX = mBounds.centerX();
                mStartLineStartY = pTop;
                mStartLineStopX = mBounds.centerX();
                mStartLineStopY = mBounds.top - mLinePadding;
            }

            if (mDrawEndLine) {
                if (mLineStyle == LineStyle.DASHED) {
                    mEndLineStartX = mBounds.centerX();
                    mEndLineStartY = getHeight() - mLineStyleDashGap;
                    mEndLineStopX = mBounds.centerX();
                    mEndLineStopY = mBounds.bottom + mLinePadding;
                } else {
                    mEndLineStartX = mBounds.centerX();
                    mEndLineStartY = mBounds.bottom + mLinePadding;
                    mEndLineStopX = mBounds.centerX();
                    mEndLineStopY = getHeight();
                }
            }
        }

        invalidate();
    }

    private void initLinePaint() {
        mLinePaint.setAlpha(0);
        mLinePaint.setAntiAlias(true);

        if (mIsCompletedMileStone)
            mLinePaint.setColor(mCompletedMileStoneColor);
        else
            mLinePaint.setColor(mInCompletedMileStoneColor);

        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mLineWidth);

        if (mLineStyle == LineStyle.DASHED)
            mLinePaint.setPathEffect(new DashPathEffect(new float[]{(float) mLineStyleDashLength, (float) mLineStyleDashGap}, 0.0f));
        else
            mLinePaint.setPathEffect(new PathEffect());

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMarker != null) {
            mMarker.draw(canvas);
        }
        if (mDrawStartLine) {
            if (mIsCompletedMileStone && isStartLineCompletedMileStone)
                mLinePaint.setColor(mCompletedMileStoneColor);
            else
                mLinePaint.setColor(mInCompletedMileStoneColor);
            canvas.drawLine(mStartLineStartX, mStartLineStartY, mStartLineStopX, mStartLineStopY, mLinePaint);
        }

        if (mDrawEndLine) {
            if (mIsCompletedMileStone && isEndLineCompletedMileStone)
                mLinePaint.setColor(mCompletedMileStoneColor);
            else
                mLinePaint.setColor(mInCompletedMileStoneColor);
            canvas.drawLine(mEndLineStartX, mEndLineStartY, mEndLineStopX, mEndLineStopY, mLinePaint);
        }
    }

    /**
     * Sets marker.
     *
     * @param marker will set marker drawable to timeline
     */
    public void setMarker(Drawable marker) {
        mMarker = marker;
        initTimeline();
    }

    public Drawable getMarker() {
        return mMarker;
    }

    /**
     * Sets marker.
     *
     * @param marker will set marker drawable to timeline
     * @param color  with a color
     */
    public void setMarker(Drawable marker, int color) {
        mMarker = marker;
        mMarker.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        initTimeline();
    }

    /**
     * Sets marker color.
     *
     * @param color the color
     */
    public void setMarkerColor(int color) {
        mMarker.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        initTimeline();
    }

    /**
     * Sets marker size.
     *
     * @param markerSize the marker size
     */
    public void setMarkerSize(int markerSize) {
        mMarkerSize = markerSize;
        initTimeline();
    }

    public int getMarkerSize() {
        return mMarkerSize;
    }

    /**
     * Sets marker left padding
     *
     * @param markerPaddingLeft the left padding to marker, works only in vertical orientation
     */
    public void setMarkerPaddingLeft(int markerPaddingLeft) {
        mMarkerPaddingLeft = markerPaddingLeft;
        initTimeline();
    }

    public int getMarkerPaddingLeft() {
        return mMarkerPaddingLeft;
    }

    /**
     * Sets marker top padding
     *
     * @param markerPaddingTop the top padding to marker, works only in horizontal orientation
     */
    public void setMarkerPaddingTop(int markerPaddingTop) {
        mMarkerPaddingTop = markerPaddingTop;
        initTimeline();
    }

    public int getMarkerPaddingTop() {
        return mMarkerPaddingTop;
    }

    /**
     * Sets marker right padding
     *
     * @param markerPaddingRight the right padding to marker, works only in vertical orientation
     */
    public void setMarkerPaddingRight(int markerPaddingRight) {
        mMarkerPaddingRight = markerPaddingRight;
        initTimeline();
    }

    public int getMarkerPaddingRight() {
        return mMarkerPaddingRight;
    }

    /**
     * Sets marker bottom padding
     *
     * @param markerPaddingBottom the bottom padding to marker, works only in horizontal orientation
     */
    public void setMarkerPaddingBottom(int markerPaddingBottom) {
        mMarkerPaddingBottom = markerPaddingBottom;
        initTimeline();
    }

    public int getMarkerPaddingBottom() {
        return mMarkerPaddingBottom;
    }


    public int getmMarkerPosition() {
        return mMarkerPosition;
    }

    public void setmMarkerPosition(int mMarkerPosition) {
        this.mMarkerPosition = mMarkerPosition;
    }

    /**
     * Sets line width.
     *
     * @param lineWidth the line width
     */
    public void setLineWidth(int lineWidth) {
        mLineWidth = lineWidth;
        initTimeline();
    }

    public int getLineWidth() {
        return mLineWidth;
    }

    /**
     * Sets line padding
     *
     * @param padding the line padding
     */
    public void setLinePadding(int padding) {
        mLinePadding = padding;
        initTimeline();
    }

    public int getLineOrientation() {
        return mLineOrientation;
    }

    /**
     * Sets line orientation
     *
     * @param lineOrientation the line orientation i.e horizontal or vertical
     */
    public void setLineOrientation(int lineOrientation) {
        this.mLineOrientation = lineOrientation;
    }

    public int getLineStyle() {
        return mLineStyle;
    }

    /**
     * Sets line style
     *
     * @param lineStyle the line style i.e normal or dashed
     */
    public void setLineStyle(int lineStyle) {
        this.mLineStyle = lineStyle;
        initLinePaint();
    }

    public int getLineStyleDashLength() {
        return mLineStyleDashLength;
    }

    /**
     * Sets dashed line length
     *
     * @param lineStyleDashLength the dashed line length
     */
    public void setLineStyleDashLength(int lineStyleDashLength) {
        this.mLineStyleDashLength = lineStyleDashLength;
        initLinePaint();
    }

    public int getLineStyleDashGap() {
        return mLineStyleDashGap;
    }

    /**
     * Sets dashed line gap
     *
     * @param lineStyleDashGap the dashed line gap
     */
    public void setLineStyleDashGap(int lineStyleDashGap) {
        this.mLineStyleDashGap = lineStyleDashGap;
        initLinePaint();
    }

    public boolean ismIsCompletedMileStone() {
        return mIsCompletedMileStone;
    }

    public void setmIsCompletedMileStone(boolean mIsCompletedMileStone) {
        this.mIsCompletedMileStone = mIsCompletedMileStone;
        initLinePaint();
    }

    public int getLinePadding() {
        return mLinePadding;
    }

    private void showStartLine(boolean show) {
        mDrawStartLine = show;
    }

    private void showEndLine(boolean show) {
        mDrawEndLine = show;
    }

    public boolean isStartLineCompletedMileStone() {
        return isStartLineCompletedMileStone;
    }

    public void setStartLineCompletedMileStone(boolean startLineCompletedMileStone) {
        isStartLineCompletedMileStone = startLineCompletedMileStone;
    }

    public boolean isEndLineCompletedMileStone() {
        return isEndLineCompletedMileStone;
    }

    public void setEndLineCompletedMileStone(boolean endLineCompletedMileStone) {
        isEndLineCompletedMileStone = endLineCompletedMileStone;
    }

    /**
     * Init line.
     *
     * @param viewType the view type
     */
    public void initLine(int viewType) {
        if (viewType == LineType.START) {
//            showStartLine(false);
            showStartLine(true);
            showEndLine(true);
        } else if (viewType == LineType.END) {
            showStartLine(true);
            showEndLine(false);
        } else if (viewType == LineType.ONLYONE) {
//            showStartLine(false);
            showStartLine(true);
            showEndLine(false);
        } else {
            showStartLine(true);
            showEndLine(true);
        }

        initTimeline();
    }

    /**
     * Gets timeline view type.
     *
     * @param position  the position of current item_caurosel view
     * @param totalSize the total size of the items
     * @return the timeline view type
     */
    public static int getStepComplitionViewType(int position, int totalSize) {
        if (totalSize == 1) {
            return LineType.ONLYONE;
        } else if (position == 0) {
            return LineType.START;
        } else if (position == totalSize - 1) {
            return LineType.END;
        } else {
            return LineType.NORMAL;
        }
    }
}