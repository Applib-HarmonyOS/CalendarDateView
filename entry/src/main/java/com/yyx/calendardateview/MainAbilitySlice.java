package com.yyx.calendardateview;

import com.yyx.library.CalendarDateView;
import com.yyx.library.WeekDayView;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;

public class MainAbilitySlice extends AbilitySlice {

    private CalendarDateView mCdv;
    private Text mTvDate;


    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initView();
        otherLogic();
        setListener();
    }

    private void initView() {
        mCdv = (CalendarDateView) findComponentById(ResourceTable.Id_main_cdv);
        mTvDate = (Text) findComponentById(ResourceTable.Id_main_date);

    }

    private void otherLogic() {
        mCdv.setDateTextView(mTvDate);
    }

    private void setListener() {
        mCdv.setOnItemClickListener((year, month, day) -> {
            String date = year + "-" + (month + 1) + "-" + day;
            new ToastDialog(MainAbilitySlice.this)
                    .setText(date)
                    .show();
        });
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
