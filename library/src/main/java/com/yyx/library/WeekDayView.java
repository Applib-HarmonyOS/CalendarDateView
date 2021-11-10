package com.yyx.library;

import com.yyx.library.utils.ResUtil;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.Paint.Style;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import static com.yyx.library.utils.CanvasHelper.dpToPx;


public class WeekDayView extends Component implements Component.EstimateSizeListener, Component.DrawTask, Component.LayoutRefreshedListener {
    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00201, "KKK_LOG");

    //attributes
    private static final String TOP_LINE_COLOR = "top_line_color";
    private static final String BOTTOM_LINE_COLOR = "bottom_line_color";
    private static final String WORK_DAY_COLOR = "work_day_color";
    private static final String WEEKEND_COLOR = "week_day_color";
    private static final String IS_DRAW_T_B_LINE = "is_draw_t_b_line";


    //Top line color
    private Color mTopLineColor;
    //下横线颜色
    private Color mBottomLineColor;
    //Underline color
    private Color mWorkDayColor;
    //Saturday and Sunday colors
    private Color mWeekendColor;
    private boolean isDrawTBLine;

    //线的宽度
    private int mStrokeWidth = 4;
    private int mWeekSize = 14;
    private Paint paint;
    private String[] weekString = new String[]{"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};

    public WeekDayView(Context context) {
        this(context, null);
        setEstimateSizeListener(this);
        addDrawTask(this);
    }

    public WeekDayView(Context context, AttrSet attrs) {
        super(context, attrs);
        setEstimateSizeListener(this);
        addDrawTask(this);
        setLayoutRefreshedListener(this);
        HiLog.debug(label, "WeekDayView Constructor");

        Color DEF_TOP_LINE_COLOR = ResUtil.getColor(getContext(), ResourceTable.Color_default_top_line_color);
        mTopLineColor = attrs.getAttr(TOP_LINE_COLOR).isPresent() ?
                attrs.getAttr(TOP_LINE_COLOR).get().getColorValue() :
                DEF_TOP_LINE_COLOR;

        Color DEF_BOTTOM_LINE_COLOR = ResUtil.getColor(getContext(), ResourceTable.Color_default_bottom_line_color);
        mBottomLineColor = attrs.getAttr(BOTTOM_LINE_COLOR).isPresent() ?
                attrs.getAttr(BOTTOM_LINE_COLOR).get().getColorValue() :
                DEF_BOTTOM_LINE_COLOR;

        Color DEF_WORK_DAY_COLOR = ResUtil.getColor(getContext(), ResourceTable.Color_default_work_day_color);
        mWorkDayColor = attrs.getAttr(WORK_DAY_COLOR).isPresent() ?
                attrs.getAttr(WORK_DAY_COLOR).get().getColorValue() :
                DEF_WORK_DAY_COLOR;

        Color DEF_WEEKEND_COLOR = ResUtil.getColor(getContext(), ResourceTable.Color_default_weekend_color);
        mWeekendColor = attrs.getAttr(WEEKEND_COLOR).isPresent() ?
                attrs.getAttr(WEEKEND_COLOR).get().getColorValue() :
                DEF_WEEKEND_COLOR;

        isDrawTBLine = !attrs.getAttr(IS_DRAW_T_B_LINE).isPresent() || attrs.getAttr(IS_DRAW_T_B_LINE).get().getBoolValue();

        init();
    }

    private void init() {
        HiLog.debug(label, "In init");

        paint = new Paint();
    }


    @Override
    public boolean onEstimateSize(int widthMeasureSpec, int heightMeasureSpec) {
        HiLog.debug(label, "onEstimateSize");
        int widthSize = EstimateSpec.getSize(widthMeasureSpec);
        int widthMode = EstimateSpec.getMode(widthMeasureSpec);

        int heightSize = EstimateSpec.getSize(heightMeasureSpec);
        int heightMode = EstimateSpec.getMode(heightMeasureSpec);

        if (heightMode == EstimateSpec.NOT_EXCEED) {
            heightSize = EstimateSpec.getSizeWithMode(30, EstimateSpec.PRECISE);
            HiLog.debug(label, "heightSize===" + heightSize);

        }
        if (widthMode == EstimateSpec.NOT_EXCEED) {
            widthSize = EstimateSpec.getSizeWithMode(300, EstimateSpec.PRECISE);
        }
        setEstimatedSize(widthSize, heightSize);
        return false;
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        HiLog.debug(label, "onDraw");
        paint.setAntiAlias(true);    //抗锯齿
        int width = getEstimatedWidth();
        int height = getEstimatedHeight();
        if (isDrawTBLine) {
            //进行画上下线
            paint.setStyle(Style.STROKE_STYLE);
            HiLog.debug(label, "mTopLineColor:==" + mTopLineColor);
            paint.setColor(mTopLineColor);
            paint.setStrokeWidth(mStrokeWidth);
            canvas.drawLine(0, 0, width, 0, paint);

            //画下横线
            paint.setColor(mBottomLineColor);
            canvas.drawLine(0, height, width, height, paint);
        }
        paint.setStyle(Paint.Style.FILL_STYLE);
        paint.setTextSize(dpToPx(getContext(), mWeekSize));
        int columnWidth = width / 7;
        for (int i = 0; i < weekString.length; i++) {
            HiLog.debug(label, "onDraw===" + i);
            String text = weekString[i];
            int fontWidth = (int) paint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth) / 2;
            int startY = (int) (height / 2 - (paint.ascent() + paint.descent()) / 2);
            if (text.indexOf("Sun") > -1 || text.indexOf("Sat") > -1) {
                paint.setColor(mWeekendColor);
            } else {
                paint.setColor(mWorkDayColor);
            }
            canvas.drawText(paint, text, startX, startY);
        }
        HiLog.debug(label, "onDrawComplete");

    }

    /**
     * Set the color of the top line
     *
     * @param mTopLineColor
     */
    public void setmTopLineColor(Color mTopLineColor) {
        this.mTopLineColor = mTopLineColor;
    }

    /**
     * Set the color of the bottom line
     *
     * @param mBottomLineColor
     */
    public void setmBottomLineColor(Color mBottomLineColor) {
        this.mBottomLineColor = mBottomLineColor;
    }

    /**
     * Set the color of Monday-Friday
     *
     * @return
     */
    public void setmWorkDayColor(Color mWorkDayColor) {
        this.mWorkDayColor = mWorkDayColor;
    }

    /**
     * Set the color of Saturday and Sunday
     *
     * @param mWeekendColor
     */
    public void setmWeekendColor(Color mWeekendColor) {
        this.mWeekendColor = mWeekendColor;
    }

    /**
     * Set the width of the edge
     *
     * @param mStrokeWidth
     */
    public void setmStrokeWidth(int mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }


    /**
     * Set the size of the font
     *
     * @param mWeekSize
     */
    public void setmWeekSize(int mWeekSize) {
        this.mWeekSize = mWeekSize;
    }


    /**
     * Set the form of the day of the week
     *
     * @param weekString Default value "day", "one", "two", "three", "four", "five", "six"
     */
    public void setWeekString(String[] weekString) {
        this.weekString = weekString;
    }


    @Override
    public void onRefreshed(Component component) {
        component.invalidate();
    }
}
