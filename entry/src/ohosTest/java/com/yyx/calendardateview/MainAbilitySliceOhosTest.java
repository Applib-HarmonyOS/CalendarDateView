package com.yyx.calendardateview;

import com.yyx.library.CalendarDateView;
import com.yyx.library.MonthDateComponent;
import com.yyx.library.ResourceTable;
import com.yyx.library.utils.ResUtil;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.components.Text;
import ohos.app.Context;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainAbilitySliceOhosTest {
    Context mContext;
    CalendarDateView mCalendarDateView;
    private int mSelYear;
    private int mSelMonth;
    private int mSelDay;

    @Before
    public void setUp() {
        mContext = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
        mCalendarDateView = new CalendarDateView(mContext);
        Calendar calendar = Calendar.getInstance();
        mSelYear = calendar.get(Calendar.YEAR);
        mSelMonth = calendar.get(Calendar.MONTH);
        mSelDay = calendar.get(Calendar.DATE);
    }

    @Test
    public void setCalendarDateView() {
        CalendarDateView calendarDateView;
        calendarDateView = new CalendarDateView(mContext);
        assertNotNull(calendarDateView);
    }

    //Should get whole selected date
    @Test
    public void shouldSetSelectedDateTv() {
        Text date = new Text(mContext);
        mCalendarDateView.setDateTextView(date);
        String sb = mSelYear +
                "-" +
                (mSelMonth + 1) +
                "-" +
                mSelDay;
        assertEquals(sb, date.getText());
    }

    @Test
    public void shouldSetOnItemClickListener() {
        CalendarDateView.OnItemClickListener onItemClickListener  = (year, month, day) -> {
            /*Do nothing*/
        };

        mCalendarDateView.setOnItemClickListener(onItemClickListener);
        assertEquals(onItemClickListener, mCalendarDateView.getOnItemClickListener());
    }


    //Should set selected year
    @Test
    public void shouldSetSelectedYear() {
        mCalendarDateView.setSelYear(2022);
        assertEquals(2022, mCalendarDateView.getSelYear());
    }

    //Should set selected Month
    @Test
    public void shouldSetSelectedMonth() {
        mCalendarDateView.setSelMonth(1);
        assertEquals(1, mCalendarDateView.getSelMonth());
    }

    //Should set selected Month
    @Test
    public void shouldSetSelectedDay() {
        mCalendarDateView.setSelDay(25);
        assertEquals(25, mCalendarDateView.getSelDay());
    }

    //Should set current day text color
    @Test
    public void shouldSetCurrentDayColor() {
        mCalendarDateView.setCurrentColor(ResUtil.getColor(mContext, ResourceTable.Color_black));
        assertEquals(ResUtil.getColor(mContext, ResourceTable.Color_black), mCalendarDateView.getCurrentColor());
    }

    //Should set general day's text color
    @Test
    public void shouldSetDayColor() {
        mCalendarDateView.setDayColor(ResUtil.getColor(mContext, ResourceTable.Color_black));
        assertEquals(ResUtil.getColor(mContext, ResourceTable.Color_black), mCalendarDateView.getDayColor());
    }

    //Should set selected day's text color
    @Test
    public void shouldSetSelectDayColor() {
        mCalendarDateView.setSelectDayColor(ResUtil.getColor(mContext, ResourceTable.Color_black));
        assertEquals(ResUtil.getColor(mContext, ResourceTable.Color_black), mCalendarDateView.getSelectDayColor());
    }

    //Should set selected day's background color
    @Test
    public void shouldSetSelectBGColor() {
        mCalendarDateView.setSelectBGColor(ResUtil.getColor(mContext, ResourceTable.Color_black));
        assertEquals(ResUtil.getColor(mContext, ResourceTable.Color_black), mCalendarDateView.getSelectBGColor());
    }

    //Should set tip color
    @Test
    public void shouldSetTipColor() {
        mCalendarDateView.setTipColor(ResUtil.getColor(mContext, ResourceTable.Color_black));
        assertEquals(ResUtil.getColor(mContext, ResourceTable.Color_black), mCalendarDateView.getTipColor());
    }

    //Should set selected day's background in circle shape
    @Test
    public void shouldSetSelectBGCircleShape() {
        mCalendarDateView.setBgShape(MonthDateComponent.ShapeType.CIRCLE);
        assertEquals(MonthDateComponent.ShapeType.CIRCLE, mCalendarDateView.getBgShape());
    }

    //Should set selected day's background in rectangle shape
    @Test
    public void shouldSetSelectBGRectShape() {
        mCalendarDateView.setBgShape(MonthDateComponent.ShapeType.RECT);
        assertEquals(MonthDateComponent.ShapeType.RECT, mCalendarDateView.getBgShape());
    }


}