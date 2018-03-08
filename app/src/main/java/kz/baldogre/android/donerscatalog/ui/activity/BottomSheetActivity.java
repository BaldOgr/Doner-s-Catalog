package kz.baldogre.android.donerscatalog.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import kz.baldogre.android.donerscatalog.R;

/**
 * Created by lol on 08.03.2018.
 */

public class BottomSheetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);

        // настройка поведения нижнего экрана
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // настройка состояний нижнего экрана
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // настройка максимальной высоты
        bottomSheetBehavior.setPeekHeight(340);

        // настройка возможности скрыть элемент при свайпе вниз
        bottomSheetBehavior.setHideable(false);

        // настройка колбэков при изменениях
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }
}
