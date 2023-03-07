package tulsi.periodpal.Activity;

import static tulsi.periodpal.Helper.CalculateUtil.fertileCalendarList;
import static tulsi.periodpal.Helper.CalculateUtil.futurePeriodCalendarList;
import static tulsi.periodpal.Helper.CalculateUtil.highCalendarList;
import static tulsi.periodpal.Helper.CalculateUtil.lowCalendarList;
import static tulsi.periodpal.Helper.CalculateUtil.mediumCalendarList;
import static tulsi.periodpal.Helper.CalculateUtil.ovulationCalendarList;
import static tulsi.periodpal.Helper.CalculateUtil.periodCalendarList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.applandeo.materialcalendarview.CalendarView;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import tulsi.periodpal.BuildConfig;
import tulsi.periodpal.Helper.CalculateUtil;
import tulsi.periodpal.R;


public class Calendar extends AppCompatActivity {


//    VIEWS
    private CalendarView calendarView;
    private TextView dateTV, windowTV, statusTV, desTV;
    private ImageView stateIMG;


//    ARRAY
    private String[] helloArray;
    private String[] periodDayArray;
    private String[] futurePeriodDayArray;
    private String[] highDayArray;
    private String[] midDayArray;
    private String[] lowDayArray;



//    SHARED-PREFERENCES
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;




    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initViews();
        initCard(java.util.Calendar.getInstance());
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView) findViewById(R.id.rootLayoutCalendar));

        calendarView.setPreviousButtonImage(ContextCompat.getDrawable(this, R.drawable.ic_back));
        calendarView.setForwardButtonImage(ContextCompat.getDrawable(this, R.drawable.ic_next));
        calendarView.setCalendarDayLayout(R.layout.layout_calendar);
        calendarView.setEvents(CalculateUtil.eventList);

//        ON-CLICK
        calendarView.setOnDayClickListener(eventDay -> {
            java.util.Calendar clickedDayCalendar = eventDay.getCalendar();
            initCard(clickedDayCalendar);
        });



    }



    private void toggle(View view, boolean show) {
        if (show)
            viewVisibleAnimator(view);
        else viewGoneAnimator(view);
    }

    private void viewGoneAnimator(final View view) {

        view.animate()
                .alpha(0f)
                .setDuration(750)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                });

    }

    private void viewVisibleAnimator(final View view) {

        view.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });

    }



    private void initCard(java.util.Calendar clickedDayCalendar) {

        initGlide(0);
        dateTV.setText(new SimpleDateFormat(getString(R.string.date_display_format), Locale.getDefault()).format(clickedDayCalendar.getTime()));

/*
            0 = Period Day
            1 = Future Period Day
            2 = Ovulation Day
            3 = Fertile Window
*/
/*
            0 = LOW
            1 = MEDIUM
            2 = HIGH
*/

        for (java.util.Calendar cal : lowCalendarList) {
            if (DateUtils.isSameDay(clickedDayCalendar, cal)) {
                initGlide(5);
                statusTV.setText(getResources().getStringArray(R.array.statusTV)[0]);
//                statusTV.setVisibility(View.VISIBLE);
//                desTV.setVisibility(View.VISIBLE);
//                windowTV.setVisibility(View.GONE);

                toggle(statusTV, true);
                toggle(desTV, true);
                toggle(windowTV, false);
            }
        }

        for (java.util.Calendar cal : mediumCalendarList) {
            if (DateUtils.isSameDay(clickedDayCalendar, cal)) {
                initGlide(4);
                statusTV.setText(getResources().getStringArray(R.array.statusTV)[1]);
//                statusTV.setVisibility(View.VISIBLE);
//                desTV.setVisibility(View.VISIBLE);
//                windowTV.setVisibility(View.GONE);

                toggle(statusTV, true);
                toggle(desTV, true);
                toggle(windowTV, false);
            }
        }

        for (java.util.Calendar cal : highCalendarList) {
            if (DateUtils.isSameDay(clickedDayCalendar, cal)) {
                initGlide(3);
                statusTV.setText(getResources().getStringArray(R.array.statusTV)[2]);
//                statusTV.setVisibility(View.VISIBLE);
//                desTV.setVisibility(View.VISIBLE);
//                windowTV.setVisibility(View.GONE);

                toggle(statusTV, true);
                toggle(desTV, true);
                toggle(windowTV, false);
            }
        }

        for (java.util.Calendar cal : periodCalendarList) {
            if (DateUtils.isSameDay(clickedDayCalendar, cal)) {
                initGlide(1);
                windowTV.setText(getResources().getStringArray(R.array.windowTV)[0]);
                windowTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_blood, 0);
//                statusTV.setVisibility(View.GONE);
//                desTV.setVisibility(View.GONE);
//                windowTV.setVisibility(View.VISIBLE);

                toggle(statusTV, false);
                toggle(desTV, false);
                toggle(windowTV, true);
            }
        }

        for (java.util.Calendar cal : futurePeriodCalendarList) {
            if (DateUtils.isSameDay(clickedDayCalendar, cal)) {
                initGlide(2);
                windowTV.setText(getResources().getStringArray(R.array.windowTV)[1]);
                windowTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_future_blood, 0);
//                statusTV.setVisibility(View.GONE);
//                desTV.setVisibility(View.GONE);
//                windowTV.setVisibility(View.VISIBLE);

                toggle(statusTV, false);
                toggle(desTV, false);
                toggle(windowTV, true);
            }
        }

        for (java.util.Calendar cal : fertileCalendarList) {
            if (DateUtils.isSameDay(clickedDayCalendar, cal)) {
                windowTV.setText(getResources().getStringArray(R.array.windowTV)[3]);
                windowTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_fertile, 0);
//                statusTV.setVisibility(View.VISIBLE);
//                desTV.setVisibility(View.VISIBLE);
//                windowTV.setVisibility(View.VISIBLE);

                toggle(statusTV, true);
                toggle(desTV, true);
                toggle(windowTV, true);
            }
        }

        for (java.util.Calendar cal : ovulationCalendarList) {
            if (DateUtils.isSameDay(clickedDayCalendar, cal)) {
                windowTV.setText(getResources().getStringArray(R.array.windowTV)[2]);
                windowTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_egg, 0);
//                statusTV.setVisibility(View.VISIBLE);
//                desTV.setVisibility(View.VISIBLE);
//                windowTV.setVisibility(View.VISIBLE);

                toggle(statusTV, true);
                toggle(desTV, true);
                toggle(windowTV, true);
            }
        }

    } // initCard()



    private void initGlide(int whichArray) {

        switch (whichArray) {

            case 0:
                Glide.with(this)
                        .load(helloArray[new Random().nextInt(helloArray.length)])
                        .into(stateIMG);
                break;

            case 1:
                    Glide.with(this)
                            .load(periodDayArray[new Random().nextInt(periodDayArray.length)])
                            .into(stateIMG);
                    break;

            case 2:
                Glide.with(this)
                        .load(futurePeriodDayArray[new Random().nextInt(futurePeriodDayArray.length)])
                        .into(stateIMG);
                break;

            case 3:
                Glide.with(this)
                        .load(highDayArray[new Random().nextInt(highDayArray.length)])
                        .into(stateIMG);
                break;

            case 4:
                Glide.with(this)
                        .load(midDayArray[new Random().nextInt(midDayArray.length)])
                        .into(stateIMG);
                break;

            case 5:
                Glide.with(this)
                        .load(lowDayArray[new Random().nextInt(lowDayArray.length)])
                        .into(stateIMG);
                break;

        } // switch()

    } // initGlide()




    private void initViews() {

//        VIEWS
        calendarView = findViewById(R.id.calendarView);
        dateTV = findViewById(R.id.dateTV);
        windowTV = findViewById(R.id.windowTV);
        statusTV = findViewById(R.id.statusTV);
        desTV = findViewById(R.id.desTV);
        stateIMG = findViewById(R.id.stateIMG);

//        SHARED-PREFERENCES
        sharedPref = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        editor = sharedPref.edit();


//        INIT
        helloArray = getResources().getStringArray(R.array.hello_IMGS);
        periodDayArray = getResources().getStringArray(R.array.periodDay_IMGS);
        futurePeriodDayArray = getResources().getStringArray(R.array.futurePeriodDay_IMGS);
        highDayArray = getResources().getStringArray(R.array.highDay_IMGS);
        midDayArray = getResources().getStringArray(R.array.midDay_IMGS);
        lowDayArray = getResources().getStringArray(R.array.lowDay_IMGS);



    } // initViews()



} // class