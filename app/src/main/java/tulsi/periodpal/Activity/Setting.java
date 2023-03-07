package tulsi.periodpal.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import tulsi.periodpal.Helper.CalculateUtil;
import tulsi.periodpal.R;

public class Setting extends AppCompatActivity {


    private LinearLayout periodLengthBTN, cycleLengthBTN;
    private TextView periodLengthValueSTV, cycleLengthValueSTV;
    private ImageView backSBTN;
    private Button clearBTN;


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
        setContentView(R.layout.activity_setting);

        initViews();
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView) findViewById(R.id.rootLayoutSetting));

        periodLengthValueSTV.setText(String.valueOf(CalculateUtil.bleeding_D + " Days"));
        cycleLengthValueSTV.setText(String.valueOf(CalculateUtil.cycle + " Days"));

        periodLengthBTN.setOnClickListener(v -> startActivity(new Intent(Setting.this, PeriodLength.class)));
        cycleLengthBTN.setOnClickListener(v -> startActivity(new Intent(Setting.this, CycleLength.class)));
        backSBTN.setOnClickListener(v -> finish());
        clearBTN.setOnClickListener(v -> {
            CalculateUtil.setPreferences(-1, -1,
                    -1, -1, -1);
            startActivity(new Intent(Setting.this, Splash.class));
            finishAffinity();
        });

    }

    private void initViews() {

        clearBTN = findViewById(R.id.clearBTN);
        backSBTN = findViewById(R.id.backSBTN);
        periodLengthBTN = findViewById(R.id.periodLengthBTN);
        cycleLengthBTN = findViewById(R.id.cycleLengthBTN);
        periodLengthValueSTV = findViewById(R.id.periodLengthValueSTV);
        cycleLengthValueSTV = findViewById(R.id.cycleLengthValueSTV);

    } // initViews()


}