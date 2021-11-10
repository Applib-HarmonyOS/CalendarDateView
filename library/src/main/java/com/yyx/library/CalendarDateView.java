package com.yyx.library;

import ohos.agp.components.*;

import ohos.agp.utils.Color;
import ohos.app.Context;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Calendar sliding control
 */

public class CalendarDateView extends PageSlider implements PageSlider.PageChangedListener {
    //attributes
    private static final String CURRENT_DAY_COLOR = "current_day_color";
    private static final String DAY_COLOR = "day_color";
    private static final String SELECTED_DAY_COLOR = "selected_day_color";
    private static final String SELECTED_DAY_BG_COLOR = "selected_day_bg_color";
    private static final String TIP_COLOR = "tip_color";
    private static final String SHAPE_TYPE = "shape_type";


    private MonthDateComponent.ShapeType mBgShape = MonthDateComponent.ShapeType.CIRCLE;
    private Color mDayColor;
    private Color mSelectDayColor;
    private Color mSelectBGColor;
    private Color mCurrentColor;
    private Color mTipColor;

    private MonthDateComponent.DateClick onDateClickListener;
    HashMap<Integer, MonthDateComponent> views = new HashMap<>();

    private LinkedList<MonthDateComponent> cache = new LinkedList<>();

    private OnItemClickListener onItemClickListener;

    private int mCurrentPos = Integer.MAX_VALUE / 2;
    private Text mTvDate;
    private int mSelYear, mSelMonth, mSelDay;

    public CalendarDateView(Context context) {
        super(context);
        init();
    }

    public CalendarDateView(Context context, AttrSet attrs) {
        super(context, attrs);
        if (attrs.getAttr(CURRENT_DAY_COLOR).isPresent()) {
            mCurrentColor = attrs.getAttr(CURRENT_DAY_COLOR).get().getColorValue();
        }

        if (attrs.getAttr(DAY_COLOR).isPresent()) {
            mDayColor = attrs.getAttr(DAY_COLOR).get().getColorValue();
        }

        if (attrs.getAttr(SELECTED_DAY_COLOR).isPresent()) {
            mSelectDayColor = attrs.getAttr(SELECTED_DAY_COLOR).get().getColorValue();
        }

        if (attrs.getAttr(SELECTED_DAY_BG_COLOR).isPresent()) {
            mSelectBGColor = attrs.getAttr(SELECTED_DAY_BG_COLOR).get().getColorValue();
        }

        if (attrs.getAttr(TIP_COLOR).isPresent()) {
            mTipColor = attrs.getAttr(TIP_COLOR).get().getColorValue();
        }

        if (attrs.getAttr(SHAPE_TYPE).isPresent()) {
            mBgShape = MonthDateComponent.ShapeType.valueOf(attrs.getAttr(SHAPE_TYPE).get().getStringValue().toUpperCase());
        }
        init();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setDateTextView(Text date) {
        this.mTvDate = date;
        mTvDate.setText(geSelectedDate());
    }

    private void init() {
        Calendar calendar = Calendar.getInstance();
        mSelYear = calendar.get(Calendar.YEAR);
        mSelMonth = calendar.get(Calendar.MONTH);
        mSelDay = calendar.get(Calendar.DATE);

        onDateClickListener = (year, month, day) -> {
            mSelYear = year;
            mSelMonth = month;
            mSelDay = day;
            if (mTvDate != null) {
                mTvDate.setText(geSelectedDate());
            }
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(mSelYear, mSelMonth, mSelDay);
            }
        };

        setProvider(new PageSliderProvider() {
            @Override
            public int getCount() {
                //for infinite scrolling
                return Integer.MAX_VALUE;
            }

            @Override
            public Object createPageInContainer(ComponentContainer componentContainer, int position) {
                MonthDateComponent component;
                if (!cache.isEmpty()) {
                    component = cache.removeFirst();
                } else {
                    component = new MonthDateComponent(componentContainer.getContext());
                    setMonthColors(component);
                }
                component.setDateClick(onDateClickListener);
                component.setSelectDate(mSelYear, mSelMonth, mSelDay);
                componentContainer.addComponent(component);
                views.put(position, component);
                return component;
            }


            @Override
            public void destroyPageFromContainer(ComponentContainer componentContainer, int position, Object object) {
                componentContainer.removeComponent((Component) object);
                cache.addLast((MonthDateComponent) object);
                views.remove(position);
            }

            @Override
            public boolean isPageMatchToObject(Component component, Object object) {
                return component == object;
            }
        });

        //Set the initial position at 1/2 of Integer.MAXVALUE, because the calendar can slide forward and backward
        setCurrentPage(mCurrentPos);

        addPageChangedListener(this);

    }

    private void setMonthColors(MonthDateComponent component) {
        if (mDayColor != null) component.setmDayColorMonth(mDayColor);
        if (mSelectBGColor != null) component.setmSelectBGColorMonth(mSelectBGColor);
        if (mSelectDayColor != null) component.setmSelectDayColorMonth(mSelectDayColor);
        if (mCurrentColor != null) component.setmCurrentColorMonth(mCurrentColor);
        if (mTipColor != null) component.setmTipColorMonth(mTipColor);
        if (mBgShape != null) component.setSelectedBgShape(mBgShape);
    }

    /**
     * Get the currently selected date
     *
     * @return
     */
    private String geSelectedDate() {
        StringBuilder sb = new StringBuilder();
        sb.append(mSelYear);
        sb.append("-");
        sb.append(mSelMonth + 1);
        sb.append("-");
        sb.append(mSelDay);
        return sb.toString();
    }

    /**
     * Next month
     *
     * @param view
     */
    private void nextMonth(MonthDateComponent view) {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 11) {//若果是12月份，则变成1月份
            year = mSelYear + 1;
            month = 0;
        } else {
            month++;
        }
        if (day > DateUtils.getMonthDays(year, month)) {
            day = DateUtils.getMonthDays(year, month);
        }
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
        view.setSelectDate(mSelYear, mSelMonth, mSelDay);
    }

    /**
     * Previous Month
     *
     * @param view
     */
    private void previousMonth(MonthDateComponent view) {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 0) {//若果是1月份，则变成12月份
            year = mSelYear - 1;
            month = 11;
        } else {
            month--;
        }
        if (day > DateUtils.getMonthDays(year, month)) {
            day = DateUtils.getMonthDays(year, month);
        }
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
        view.setSelectDate(mSelYear, mSelMonth, mSelDay);
    }


    @Override
    public void onPageSliding(int i, float v, int i1) {
        //Only onPageChosen needs to override
    }

    @Override
    public void onPageSlideStateChanged(int i) {
        //Only onPageChosen needs to override
    }

    @Override
    public void onPageChosen(int position) {
        MonthDateComponent view = views.get(position);
        if (position > mCurrentPos) {
            nextMonth(view);
        } else if (position < mCurrentPos) {
            previousMonth(view);
        }

        mCurrentPos = position;
        if (mTvDate != null) {
            mTvDate.setText(geSelectedDate());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int year, int month, int day);
    }

    public int getSelYear() {
        return mSelYear;
    }

    public void setSelYear(int mSelYear) {
        this.mSelYear = mSelYear;
    }

    public int getSelMonth() {
        return mSelMonth;
    }

    public void setSelMonth(int mSelMonth) {
        this.mSelMonth = mSelMonth;
    }

    public int getSelDay() {
        return mSelDay;
    }

    public void setSelDay(int mSelDay) {
        this.mSelDay = mSelDay;
    }

    public Color getSelectDayColor() {
        return mSelectDayColor;
    }

    public Color getSelectBGColor() {
        return mSelectBGColor;
    }

    public Color getCurrentColor() {
        return mCurrentColor;
    }

    /**
     * Set the selected date background shape
     *
     * @param mBgShape
     */
    public void setBgShape(MonthDateComponent.ShapeType mBgShape) {
        this.mBgShape = mBgShape;
    }

    public MonthDateComponent.ShapeType getBgShape() {
        return mBgShape;
    }

    /**
     * Set other date colors
     *
     * @param mDayColor
     */
    public void setDayColor(Color mDayColor) {
        this.mDayColor = mDayColor;
    }

    public Color getDayColor() {
        return mDayColor;
    }

    /**
     * Set the color of the selected date
     *
     * @param mSelectDayColor
     */
    public void setSelectDayColor(Color mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }

    /**
     * Set the background color of the selected date
     *
     * @param mSelectBGColor
     */
    public void setSelectBGColor(Color mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }

    /**
     * Set the color of today's date
     *
     * @param mCurrentColor
     */
    public void setCurrentColor(Color mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    /**
     * Set cue point color
     *
     * @param mTipColor
     */
    public void setTipColor(Color mTipColor) {
        this.mTipColor = mTipColor;
    }

    public Color getTipColor() {
        return mTipColor;
    }
}
