package de.udos.democardlist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class Indicator extends View {

    private static final int FULL_CIRCLE = 360;

    //private ShapeDrawable mShapeDrawable;
    private Context mContext;
    private int mDiameter;
    private int mInnerRadius;
    private float mCenterX;
    private float mCenterY;
    private int mOffsetX;
    private int mOffsetY;
    private Paint mArcPaint;
    private Paint mArcEmptyPaint;
    private Paint mInnerPaint;
    private Paint mLabelPaint;
    private int[] mAttrArcColors;
    private int mAttrArcEmptyColor;
    private boolean mAttrArcGradient;
    private int mAttrArcRotation; // TODO
    private int mAttrArcWidth; // TODO
    private int mAttrInnerRadius; // TODO
    private float mAttrInnerRatio; // TODO
    private int mAttrInnerColor;
    private float mAttrMaxArc;
    private float mAttrMaxValue;
    private boolean mAttrLabelAutoSize;
    private int mAttrLabelColor;
    private int mAttrLabelPadding;
    private String mAttrLabelText;
    private int mAttrLabelTextSize;
    private float mAttrValue;

    public Indicator(Context context) {

        super(context);
        mContext = context;
    }

    public Indicator(Context context, AttributeSet attrs) {

        super(context, attrs);
        mContext = context;

        initAttributes(attrs);
        initPaint();
    }

    public int getLabelColor() {
        return mAttrLabelColor;
    }

    public void setLabelColor(int labelColor) {

        this.mAttrLabelColor = labelColor;
        invalidate();
    }

    public String getLabelText() {
        return mAttrLabelText;
    }

    public void setLabelText(String labelText) {

        this.mAttrLabelText = labelText;
        invalidate();
    }

    public float getValue() {
        return mAttrValue;
    }

    public void setValue(float value) {

        this.mAttrValue = value;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        mDiameter = width < height ? width : height;
        mInnerRadius = mDiameter / 3;

        mOffsetX = width > mDiameter ? (width - mDiameter) / 2 : 0;
        mOffsetY = height > mDiameter ? (height - mDiameter) / 2 : 0;

        mCenterX = width / 2; //mDiameter/2 + mOffsetX;
        mCenterY = height / 2; //mDiameter/2 + mOffsetY;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float targetArc = calcTargetArc();
        RectF rectArc = getArcRect();

        updatePaint();

        canvas.save();
        canvas.rotate(-90, mCenterX, mCenterY);

        if (mAttrArcGradient) {

            canvas.drawArc(rectArc, 0, targetArc, true, mArcPaint);

        } else {

            int l = mAttrArcColors.length;
            int currArc = FULL_CIRCLE / l;

            for (int i = 0; i < l; i++) {

                int startAngle = currArc * i;
                mArcPaint.setColor(mAttrArcColors[i]);

                if (startAngle + currArc <= targetArc) {
                    canvas.drawArc(rectArc, startAngle, currArc, true, mArcPaint);
                } else {
                    canvas.drawArc(rectArc, startAngle, targetArc - startAngle, true, mArcPaint);
                }
            }
        }

        if (mAttrArcEmptyColor != 0) {
            canvas.drawArc(rectArc, targetArc, mAttrMaxArc - targetArc, true, mArcEmptyPaint);
        }

        canvas.drawCircle(mCenterX, mCenterY, mInnerRadius, mInnerPaint);
        canvas.restore();
        canvas.drawText(mAttrLabelText, mCenterX,
                calcLabelOffsetY(mAttrLabelText, mLabelPaint), mLabelPaint);
    }

    private boolean typeIsColor(int type) {
        return type >= TypedValue.TYPE_FIRST_COLOR_INT && type <= TypedValue.TYPE_LAST_COLOR_INT;
    }

    private int getBackgroundColor() {

        int color = Color.WHITE;

        if (this.getBackground() instanceof ColorDrawable) {
            color = ((ColorDrawable) this.getBackground()).getColor();

        } else {

            TypedValue val = new TypedValue();
            mContext.getTheme().resolveAttribute(android.R.attr.windowBackground, val, true);

            if (typeIsColor(val.type)) { // windowBackground is a color
                color = val.data;
            }
        }

        return color;
    }

    private RectF getArcRect() {

        RectF rect = new RectF(0, 0, mDiameter, mDiameter);
        rect.offsetTo(mOffsetX, mOffsetY);

        return rect;
    }

    private float calcTargetArc() { // calculates the corresponding arc value for rating
        return mAttrMaxArc * mAttrValue / mAttrMaxValue;
    }

    private float[] calcArcGradientColorStops(int[] colors, float maxArc) {

        int l = colors.length;
        float[] colorStops = new float[l];
        float factor1 = (float) 1 / (l - 1);
        float factor2 = maxArc / FULL_CIRCLE;

        for (int i = 0; i < l; i++) {
            colorStops[i] = i * factor1 * factor2;
        }

        return colorStops;
    }

    private float calcLabelOffsetY(String text, Paint paint) {

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        return mCenterY + (bounds.height() >> 1);
    }

    private int calcTextSize(String text, int maxTextSize, Paint paint) {

        Rect bounds = new Rect();
        int maxBound = (mInnerRadius * 2) - (mAttrLabelPadding * 2);
        int textSize = maxTextSize;

        paint.setTextSize(maxTextSize);
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.width() > maxBound || bounds.height() > maxBound) {

            textSize--;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }

        return textSize;
    }

    private void initAttributes(AttributeSet attrs) {

        TypedArray attr = mContext.getTheme().obtainStyledAttributes(
                attrs, R.styleable.Indicator, 0, 0);

        try {

            final int colorsId = attr.getResourceId(R.styleable.Indicator_arcColors, 0);
            if (colorsId != 0) {
                mAttrArcColors = getResources().getIntArray(colorsId);
            }
            mAttrArcEmptyColor = attr.getColor(R.styleable.Indicator_arcEmptyColor, 0);
            mAttrArcGradient = attr.getBoolean(R.styleable.Indicator_arcGradient, true);
            mAttrInnerColor = attr.getColor(R.styleable.Indicator_innerColor, getBackgroundColor());
            mAttrLabelAutoSize = attr.getBoolean(R.styleable.Indicator_labelAutoSize, true);
            mAttrLabelColor = attr.getColor(R.styleable.Indicator_labelColor, Color.BLACK);
            //getResources().getColor(android.R.color.primary_text_light));
            mAttrLabelPadding = attr.getDimensionPixelOffset(R.styleable.Indicator_labelPadding, 0);
            mAttrLabelText = attr.hasValue(R.styleable.Indicator_labelText) ?
                    attr.getString(R.styleable.Indicator_labelText) : "";
            mAttrLabelTextSize = attr.getDimensionPixelOffset(R.styleable.Indicator_labelTextSize, 100);
            mAttrMaxArc = attr.getFloat(R.styleable.Indicator_maxArc, FULL_CIRCLE);
            mAttrMaxValue = attr.getFloat(R.styleable.Indicator_maxValue, 1);
            mAttrValue = attr.getFloat(R.styleable.Indicator_value, 0.0f);

        } finally {
            attr.recycle();
        }
    }

    private void updatePaint() {

        mLabelPaint.setColor(mAttrLabelColor);
        mLabelPaint.setTextSize(mAttrLabelAutoSize ?
                calcTextSize(mAttrLabelText, mAttrLabelTextSize, mLabelPaint) : mAttrLabelTextSize);

        mInnerPaint.setColor(mAttrInnerColor);

        if (mAttrArcGradient) {
            mArcPaint.setShader(new SweepGradient(mCenterX, mCenterY, mAttrArcColors,
                    mAttrMaxArc == FULL_CIRCLE ? null : calcArcGradientColorStops(mAttrArcColors, mAttrMaxArc)));
        }

        if (mAttrArcEmptyColor != 0) {
            mArcEmptyPaint.setColor(mAttrArcEmptyColor);
        }
        /*
        mShapeDrawable = new ShapeDrawable(new ArcShape(0, calcTargetArc()));
        mShapeDrawable.setShaderFactory(new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                Resources res = getResources();
                return new SweepGradient(mCenterX, mCenterY,
                        new int[]{res.getColor(R.color.red),
                                res.getColor(R.color.yellow),
                                res.getColor(R.color.green)}, null);
            }
        });
        mShapeDrawable.setBounds(0, 0, mDiameter, mDiameter);
        */
    }

    private void initPaint() {

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.FILL);

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStyle(Paint.Style.FILL);

        mLabelPaint = new Paint();
        mLabelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setTextAlign(Paint.Align.CENTER);

        if (mAttrArcEmptyColor != 0) {

            mArcEmptyPaint = new Paint();
            mArcEmptyPaint.setAntiAlias(true);
            mArcEmptyPaint.setStyle(Paint.Style.FILL);
        }

    }

}
