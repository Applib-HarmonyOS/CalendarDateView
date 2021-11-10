package com.yyx.library;

import com.yyx.library.utils.ResUtil;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Text;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;

import java.util.Calendar;
import java.util.List;

import static com.yyx.library.utils.CanvasHelper.dpToPx;

public class MonthDateComponent extends Component implements
        Component.DrawTask, Component.TouchEventListener {

    static final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00201, "LOG_KKK2");


    //attributes
    private static final String CURRENT_DAY_COLOR = "current_day_color";
    private static final String DAY_COLOR = "day_color";
    private static final String SELECTED_DAY_COLOR = "selected_day_color";
    private static final String SELECTED_DAY_BG_COLOR = "selected_day_bg_color";
    private static final String TIP_COLOR = "tip_color";
    private static final String SHAPE_TYPE = "shape_type";


    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    public static final int RECT_SEL_BG = 0;
    public static final int CIRCLE_SEL_BG = 1;

    public enum ShapeType {CIRCLE, RECT}

    private ShapeType mBgShape = ShapeType.CIRCLE;
    private Color mDayColorMonth;
    private Color mSelectDayColorMonth;
    private Color mSelectBGColorMonth;
    private Color mCurrentColorMonth;
    private Color mTipColorMonth;

    private Paint mPaint;

    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;
    private int mColumnSize, mRowSize;
    private int mDaySize = 18;


    private Text tv_date, tv_week;

    private int weekRow;
    private int[][] daysString;
    private int mCircleRadius = 6;

    private DateClick dateClick;
    private List<Integer> daysHasThingList;

    public MonthDateComponent(Context context) {
        super(context);
        addDrawTask(this);
        setupDefaultColors();
        setTouchEventListener(this);
        init();
    }

    private void setupDefaultColors() {
        mCurrentColorMonth = ResUtil.getColor(getContext(), ResourceTable.Color_default_current_day_color);
        mDayColorMonth = ResUtil.getColor(getContext(), ResourceTable.Color_default_day_color);
        mSelectDayColorMonth = ResUtil.getColor(getContext(), ResourceTable.Color_default_selected_day_color);
        mSelectBGColorMonth = ResUtil.getColor(getContext(), ResourceTable.Color_default_selected_day_bg_color);
        mTipColorMonth = ResUtil.getColor(getContext(), ResourceTable.Color_default_tip_color);
    }

    public MonthDateComponent(Context context, AttrSet attrs) {
        super(context, attrs);
        addDrawTask(this);
        setTouchEventListener(this);

        setupDefaultColors();

        if (attrs.getAttr(CURRENT_DAY_COLOR).isPresent()) {
            mCurrentColorMonth = attrs.getAttr(CURRENT_DAY_COLOR).get().getColorValue();
        }
        if (attrs.getAttr(DAY_COLOR).isPresent()) {
            mDayColorMonth = attrs.getAttr(DAY_COLOR).get().getColorValue();
        }
        if (attrs.getAttr(SELECTED_DAY_COLOR).isPresent()) {
            mSelectDayColorMonth = attrs.getAttr(SELECTED_DAY_COLOR).get().getColorValue();
        }
        if (attrs.getAttr(SELECTED_DAY_BG_COLOR).isPresent()) {
            mSelectBGColorMonth = attrs.getAttr(SELECTED_DAY_BG_COLOR).get().getColorValue();
        }
        if (attrs.getAttr(TIP_COLOR).isPresent()) {
            mTipColorMonth = attrs.getAttr(TIP_COLOR).get().getColorValue();
        }

        if (attrs.getAttr(SHAPE_TYPE).isPresent()) {
            mBgShape = ShapeType.valueOf(attrs.getAttr(SHAPE_TYPE).get().getStringValue().toUpperCase());
        }

        init();
    }

    private void init() {
        Calendar calendar = Calendar.getInstance();
        mPaint = new Paint();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
    }

    private void checkAndSetSelectedDayColor(String dayString, Canvas canvas, int column, int row) {
        if (dayString.equals(mSelDay + "")) {
            HiLog.debug(label, "mSelDay:" + mSelDay);
            //Draw background color rectangle
            int startRecX = mColumnSize * column;
            int startRecY = mRowSize * row;
            int endRecX = startRecX + mColumnSize;
            int endRecY = startRecY + mRowSize;
            mPaint.setColor(mSelectBGColorMonth);
            if (mBgShape == ShapeType.CIRCLE) {
                HiLog.debug(label, "mBgShape:" + CIRCLE_SEL_BG);
                Rect rect = new Rect(startRecX, startRecY, endRecX, endRecY);
                int radius = rect.getWidth() > rect.getHeight() ? rect.getHeight() / 2 : rect.getWidth() / 2;
                canvas.drawCircle(rect.getCenterX(), rect.getCenterY(), radius, mPaint);

            } else if (mBgShape == ShapeType.RECT) {
                HiLog.debug(label, "mBgShape:" + RECT_SEL_BG);
                canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mPaint);
            }
            //Record the first few lines, that is, the first few weeks
            weekRow = row + 1;
        }
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        initSize();
        daysString = new int[6][7];
        mPaint.setTextSize(dpToPx(getContext(), mDaySize));
        mPaint.setAntiAlias(true);  //抗锯齿
        String dayString;
        int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
        int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);

        HiLog.debug(label, "DateView222:" + mSelMonth + "月1号周" + weekNumber);

        for (int day = 0; day < mMonthDays; day++) {
            dayString = (day + 1) + "";
            int column = (day + weekNumber - 1) % 7;
            int row = (day + weekNumber - 1) / 7;
            daysString[row][column] = day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            checkAndSetSelectedDayColor(dayString, canvas, column, row);
            //Draw a round sign for selected day
            drawCircle(row, column, day + 1, canvas);
            if (dayString.equals(mSelDay + "")) {
                mPaint.setColor(mSelectDayColorMonth);
            } else if (dayString.equals(mCurrDay + "") && mCurrDay != mSelDay && mCurrMonth == mSelMonth) {
                mPaint.setColor(mCurrentColorMonth);
            } else {
                mPaint.setColor(mDayColorMonth);
            }
            canvas.drawText(mPaint, dayString, startX, startY);
            if (tv_date != null) {
                tv_date.setText(mSelYear + "年" + (mSelMonth + 1) + "月");
            }

            if (tv_week != null) {
                tv_week.setText("第" + weekRow + "周");
            }
        }
    }


    private void drawCircle(int row, int column, int day, Canvas canvas) {
        if (daysHasThingList != null && daysHasThingList.size() > 0) {
            if (!daysHasThingList.contains(day)) return;
            mPaint.setColor(mTipColorMonth);
            float circleX = (float) (mColumnSize * column + mColumnSize * 0.8);
            float circley = (float) (mRowSize * row + mRowSize * 0.2);
            canvas.drawCircle(circleX, circley, mCircleRadius, mPaint);
        }
    }


    private int downX = 0, downY = 0;

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        HiLog.debug(label, "onTouchEvent");
        int eventCode = touchEvent.getAction();
        switch (eventCode) {
            case TouchEvent.PRIMARY_POINT_DOWN:
                HiLog.debug(label, "onTouchEvent.PRIMARY_POINT_DOWN");
                //x、y Indicates the position of the finger when pressed
                downX = (int) touchEvent.getPointerPosition(0).getX();
                downY = (int) touchEvent.getPointerPosition(0).getY();
                break;
            case TouchEvent.PRIMARY_POINT_UP:
                HiLog.debug(label, "onTouchEvent.PRIMARY_POINT_UP");
                int upX = (int) touchEvent.getPointerPosition(0).getX();
                int upY = (int) touchEvent.getPointerPosition(0).getY();
                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {//点击事件
                    HiLog.debug(label, "onTouchEvent.PMath.abs");
                    doClickAction((upX + downX) / 2, (upY + downY) / 2);
                }
                break;
            default:
                downX = (int) touchEvent.getPointerPosition(0).getX();
                downY = (int) touchEvent.getPointerPosition(0).getY();
                break;
        }
        return true;
    }

    /**
     * Initialize column width and row height
     */
    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * Set year and month
     *
     * @param year
     * @param month
     */
    public void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    public void setSelectDate(int year, int month, int day) {
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * Execute click event
     *
     * @param x
     * @param y
     */
    private void doClickAction(int x, int y) {
        HiLog.debug(label, "doClickAction");
        int row = y / mRowSize;
        int column = x / mColumnSize;
        setSelectYearMonth(mSelYear, mSelMonth, daysString[row][column]);
        invalidate();
        //执行activity发送过来的点击处理事件
        if (dateClick != null) {
            HiLog.debug(label, "DateClick: Month" + mSelMonth + " Day" + mSelDay);
            dateClick.onClickOnDate(mSelYear, mSelMonth, mSelDay);
        }
    }

    /**
     * Left click, the calendar will page backwards
     */
    public void onPreviousClick() {
        int currentYear = mSelYear;
        int currentMonth = mSelMonth;
        int currentDay = mSelDay;
        if (currentMonth == 0) {
            currentYear = mSelYear - 1;
            currentMonth = 11;
        } else {
            currentMonth--;
        }
        if (currentDay > DateUtils.getMonthDays(currentYear, currentMonth)) {
            currentDay = DateUtils.getMonthDays(currentYear, currentMonth);
        }
        setSelectYearMonth(currentYear, currentMonth, currentDay);
        invalidate();
    }

    /**
     * Right click to turn the calendar page forward
     */
    public void onNextClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 11) {//若果是12月份，则变成1月份
            year = mSelYear + 1;
            month = 0;
        } else {
            month = month + 1;
        }

        if (day > DateUtils.getMonthDays(year, month)) {
            day = DateUtils.getMonthDays(year, month);
        }

        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * Get the selected year
     *
     * @return
     */
    public int getmSelYear() {
        return mSelYear;
    }

    /**
     * Get the selected month
     *
     * @return
     */
    public int getmSelMonth() {
        return mSelMonth;
    }

    /**
     * Get the selected date
     */
    public int getmSelDay() {
        return this.mSelDay;
    }

    /**
     * The font color of ordinary date, the default is black
     *
     * @param mDayColorMonth
     */
    public void setmDayColorMonth(Color mDayColorMonth) {
        this.mDayColorMonth = mDayColorMonth;
    }

    /**
     * Select the color of the selected date, the default is white
     *
     * @param mSelectDayColorMonth
     */
    public void setmSelectDayColorMonth(Color mSelectDayColorMonth) {
        this.mSelectDayColorMonth = mSelectDayColorMonth;
    }

    /**
     * The background color of the selected date, the default is blue
     *
     * @param mSelectBGColorMonth
     */
    public void setmSelectBGColorMonth(Color mSelectBGColorMonth) {
        this.mSelectBGColorMonth = mSelectBGColorMonth;
    }

    /**
     * The background shape of the selected date (rect: rectangle, circle: circle)
     *
     * @param shape
     */
    public void setSelectedBgShape(ShapeType shape) {
        mBgShape = shape;
    }

    /**
     * Color of the current date when not selected, the default is red
     *
     * @param mCurrentColorMonth
     */
    public void setmCurrentColorMonth(Color mCurrentColorMonth) {
        this.mCurrentColorMonth = mCurrentColorMonth;
    }


    /**
     * The size of the date, the default is 18sp
     *
     * @param mDaySize
     */
    public void setmDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }

    /**
     * Set the control to display the current date
     *
     * @param tv_date Show date
     * @param tv_week Show week
     */
    public void setTextView(Text tv_date, Text tv_week) {
        this.tv_date = tv_date;
        this.tv_week = tv_week;
        invalidate();
    }

    /**
     * Set transaction days
     *
     * @param daysHasThingList
     */
    public void setDaysHasThingList(List<Integer> daysHasThingList) {
        this.daysHasThingList = daysHasThingList;
    }

    /***
     * Set the radius of the circle, the default is 6
     *
     * @param mCircleRadius
     */
    public void setmCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    /**
     * Set the radius of the circle
     *
     * @param mTipColorMonth
     */
    public void setmTipColorMonth(Color mTipColorMonth) {
        this.mTipColorMonth = mTipColorMonth;
    }


    /**
     * Set the date of the click callback event
     *
     * @author shiwei.deng
     */
    public interface DateClick {
        public void onClickOnDate(int year, int month, int day);
    }

    /**
     * Set date click event
     *
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }


    /**
     * Skip to today
     */
    public void setTodayToView() {
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
        invalidate();
    }
}
