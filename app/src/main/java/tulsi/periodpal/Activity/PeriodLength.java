package tulsi.periodpal.Activity;

import static tulsi.periodpal.Helper.CalculateUtil.lastPeriod_D;
import static tulsi.periodpal.Helper.CalculateUtil.lastPeriod_M;
import static tulsi.periodpal.Helper.CalculateUtil.lastPeriod_Y;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import tulsi.periodpal.Helper.CalculateUtil;
import tulsi.periodpal.R;

public class PeriodLength extends AppCompatActivity {

    private TextView daysValuePTV;
    private ImageView plusBTN, minusBTN, backPBTN, savePBTN;
    private int VALUE = CalculateUtil.bleeding_D;


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
        setContentView(R.layout.activity_period_length);

        initViews();
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView) findViewById(R.id.rootLayoutPeriodLength));

        daysValuePTV.setText(String.valueOf( VALUE + " Days"));

        plusBTN.setOnClickListener(v -> {
            if (VALUE < 11) {
                VALUE++;
                daysValuePTV.setText(String.valueOf(VALUE + " Days"));
            }
        });

        minusBTN.setOnClickListener(v -> {
            if (VALUE > 3) {
                VALUE--;
                daysValuePTV.setText(String.valueOf(VALUE + " Days"));
            }
        });

        backPBTN.setOnClickListener(v -> {
            finish();
        });

        savePBTN.setOnClickListener(v -> {
            CalculateUtil.setPreferences(CalculateUtil.cycle, VALUE,
                    lastPeriod_D, lastPeriod_M, lastPeriod_Y);
            Toast.makeText(this, "SAVED!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PeriodLength.this, Home.class));
            finishAffinity();
        });
    }

    private void initViews() {

        daysValuePTV = findViewById(R.id.daysValuePTV);
        plusBTN = findViewById(R.id.plusBTN);
        minusBTN = findViewById(R.id.minusBTN);
        savePBTN = findViewById(R.id.savePBTN);
        backPBTN = findViewById(R.id.backPBTN);

    } // initViews()

}