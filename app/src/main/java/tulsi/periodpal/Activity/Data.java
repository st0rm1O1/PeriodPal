package tulsi.periodpal.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import tulsi.periodpal.Helper.CalculateUtil;
import tulsi.periodpal.R;

public class Data extends AppCompatActivity {

//    VIEWS
    private ImageView activityBackground;
    private TextView startDateDataTV;
    private EditText bleedETV, cycleETV;
    private Button continueBTN;


//    STRING ARRAY
    private String[] dataICONS;


//    CALENDAR
    private Calendar startDateCal;
    private DatePicker datePicker;
    private DatePickerBuilder builder;




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
        setContentView(R.layout.activity_data);

        CalculateUtil.getInstance(this);
        initViews();
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView) findViewById(R.id.rootLayoutData));
        startDateDataTV.setText(new SimpleDateFormat(getString(R.string.date_display_format), Locale.getDefault()).format(startDateCal.getTime()));

        startDateDataTV.setOnClickListener(v -> {
            datePicker.show();
        });

        continueBTN.setOnClickListener(v -> {

            int CYCLE = Integer.parseInt(cycleETV.getText().toString());
            int BLEED = Integer.parseInt(bleedETV.getText().toString());

            if (CYCLE < 35 && CYCLE > 23 && BLEED < 11 && BLEED > 3) {

                CalculateUtil.setPreferences(CYCLE, BLEED,
                        startDateCal.get(Calendar.DAY_OF_MONTH), startDateCal.get(Calendar.MONTH), startDateCal.get(Calendar.YEAR));
                startActivity(new Intent(Data.this, Home.class));
                finishAffinity();

            } else
                Toast.makeText(this, "Make sure you've filled all the details correctly!", Toast.LENGTH_SHORT).show();


        });

    }


    private final OnSelectDateListener listener = new OnSelectDateListener() {
        @Override
        public void onSelect(List<Calendar> clickedDayCalendar) {

            startDateDataTV.setText(new SimpleDateFormat(getString(R.string.date_display_format), Locale.getDefault()).format(clickedDayCalendar.get(0).getTime()));
            startDateCal.set(clickedDayCalendar.get(0).get(Calendar.YEAR),
                    clickedDayCalendar.get(0).get(Calendar.MONTH),
                    clickedDayCalendar.get(0).get(Calendar.DAY_OF_MONTH));

        }
    };


    private void initViews() {

        activityBackground = findViewById(R.id.activityBackground);
        startDateDataTV = findViewById(R.id.startDateDataTV);
        bleedETV = findViewById(R.id.bleedETV);
        cycleETV = findViewById(R.id.cycleETV);
        continueBTN = findViewById(R.id.continueBTN);
        dataICONS = getResources().getStringArray(R.array.data_BG);

        startDateCal = Calendar.getInstance();
        builder = new DatePickerBuilder(this, listener)
                .pickerType(CalendarView.ONE_DAY_PICKER)
                .typefaceSrc(R.font.medium);
        datePicker = builder.build();

        Glide.with(this)
                .load(dataICONS[new Random().nextInt(dataICONS.length)])
                .into(activityBackground);

    }

}