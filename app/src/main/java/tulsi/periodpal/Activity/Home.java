package tulsi.periodpal.Activity;

import static tulsi.periodpal.Helper.CalculateUtil.bleeding_D;
import static tulsi.periodpal.Helper.CalculateUtil.clearAll;
import static tulsi.periodpal.Helper.CalculateUtil.cycle;
import static tulsi.periodpal.Helper.CalculateUtil.lastPeriod_D;
import static tulsi.periodpal.Helper.CalculateUtil.lastPeriod_M;
import static tulsi.periodpal.Helper.CalculateUtil.lastPeriod_Y;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import tulsi.periodpal.Helper.CalculateUtil;
import tulsi.periodpal.Helper.Roasty;
import tulsi.periodpal.R;


public class Home extends AppCompatActivity {


//    VIEWS
    private Button periodStartStopBTN;
    private TextView calendarBTN, settingBTN, daysTV, lateTV, nextPeriodTV, nextFertileTV;
    private ImageView calendarTVBG, settingTVBG, noteTVBG, petIMG;


//    ARRAY
    private String[] petArray;
    private String[] heroArray;


//    HANDLER
    private final Handler handler = new Handler();


//    CALENDAR
    private java.util.Calendar startDateCal;
    private List<java.util.Calendar> listOfCal;
    private DatePicker datePicker;
    private DatePickerBuilder builder;



    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
        refreshCalendar();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshCalendar();
    }

    private void refreshCalendar() {

        new Thread(() -> handler.post(() -> {

            clearAll();

//        LAST PERIOD
            CalculateUtil.initEvents(this, lastPeriod_Y, lastPeriod_M, lastPeriod_D, false);

//        FUTURE PERIOD
            java.util.Calendar nextDayOfCycle = CalculateUtil.getNextDayOfCycle(lastPeriod_Y, lastPeriod_M, lastPeriod_D, cycle);
            CalculateUtil.initEvents(this, nextDayOfCycle.get(java.util.Calendar.YEAR), nextDayOfCycle.get(java.util.Calendar.MONTH), nextDayOfCycle.get(java.util.Calendar.DAY_OF_MONTH), true);

            java.util.Calendar calendar = java.util.Calendar.getInstance();
            int daysLeft = CalculateUtil.daysBetweenDates(calendar.getTime(), nextDayOfCycle.getTime());


            if (daysLeft < 1) {
                daysTV.setText(String.valueOf( (Math.abs(daysLeft) + 1) ));
                lateTV.setText(String.valueOf("DAY OF PERIOD"));
            } else {
                daysTV.setText(String.valueOf( (Math.abs(daysLeft) + 1) ));
                lateTV.setText(String.valueOf("DAYS LEFT"));
            }

            nextDayOfCycle = CalculateUtil.getNextDayOfCycle(nextDayOfCycle.get(java.util.Calendar.YEAR), nextDayOfCycle.get(java.util.Calendar.MONTH), nextDayOfCycle.get(java.util.Calendar.DAY_OF_MONTH), cycle);
            nextPeriodTV.setText(String.valueOf("NEXT PERIOD - " + new SimpleDateFormat(getString(R.string.date_display_format), Locale.getDefault()).format(nextDayOfCycle.getTime())));

        })).start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CalculateUtil.getInstance(this);
        if (CalculateUtil.isEmpty())
            startActivity(new Intent(Home.this, Data.class));

        initViews();
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView) findViewById(R.id.rootLayoutHome));
        refreshCalendar();

        calendarBTN.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, Calendar.class));
        });

        settingBTN.setOnClickListener(v -> {
            startActivity(new Intent(Home.this, Setting.class));
        });

        periodStartStopBTN.setOnClickListener(v -> {
            datePicker.show();
        });

    }


    private final OnSelectDateListener listener = new OnSelectDateListener() {
        @Override
        public void onSelect(@NonNull List<java.util.Calendar> clickedDayCalendar) {


            CalculateUtil.getInstance(Home.this);
            CalculateUtil.setPreferences(cycle, clickedDayCalendar.size(),
                    clickedDayCalendar.get(0).get(java.util.Calendar.DAY_OF_MONTH),
                    clickedDayCalendar.get(0).get(java.util.Calendar.MONTH),
                    clickedDayCalendar.get(0).get(java.util.Calendar.YEAR)
            );

            Roasty.showToast(Home.this, -1,  "UPDATED!");
            startActivity(new Intent(Home.this, Home.class));
            finishAffinity();

        }
    };


    private void initViews() {

        calendarBTN = findViewById(R.id.calendarBTN);
        settingBTN = findViewById(R.id.settingBTN);
        noteTVBG = findViewById(R.id.noteTVBG);
        calendarTVBG = findViewById(R.id.calendarTVBG);
        settingTVBG = findViewById(R.id.settingTVBG);
        petIMG = findViewById(R.id.petIMG);
        periodStartStopBTN = findViewById(R.id.periodStartStopBTN);
        daysTV = findViewById(R.id.daysTV);
        lateTV = findViewById(R.id.lateTV);
        nextPeriodTV = findViewById(R.id.nextPeriodTV);
        nextFertileTV = findViewById(R.id.nextFertileTV);

//        INIT
        petArray = getResources().getStringArray(R.array.pets_IMGS);
        heroArray = getResources().getStringArray(R.array.hero_IMGS);
        startDateCal = java.util.Calendar.getInstance();
        startDateCal.set(lastPeriod_Y, lastPeriod_M, lastPeriod_D);
        builder = new DatePickerBuilder(this, listener)
                .pickerType(CalendarView.RANGE_PICKER)
                .minimumDate(startDateCal)
                .maximumDaysRange(bleeding_D)
                .typefaceSrc(R.font.medium);
        datePicker = builder.build();


        Glide.with(this)
                .load("https://media.tenor.com/MsEjHzeQXvUAAAAC/aesthetic.gif")
                .into(calendarTVBG);

        Glide.with(this)
                .load("https://media.tenor.com/u3hpJYKh0DAAAAAC/oasis-entrance.gif")
                .into(settingTVBG);

        Glide.with(this)
                .load(heroArray[new Random().nextInt(heroArray.length)])
                .into(noteTVBG);

        Glide.with(this)
                .load(petArray[new Random().nextInt(petArray.length)])
                .into(petIMG);


    } // initViews()


}