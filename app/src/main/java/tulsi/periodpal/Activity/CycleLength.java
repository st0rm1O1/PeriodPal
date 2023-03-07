package tulsi.periodpal.Activity;

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

public class CycleLength extends AppCompatActivity {

    private TextView daysValueCTV;
    private ImageView plusBTN, minusBTN, backCBTN, saveCBTN;
    private int VALUE = CalculateUtil.cycle;


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
        setContentView(R.layout.activity_cycle_length);

        initViews();
        OverScrollDecoratorHelper.setUpOverScroll((ScrollView) findViewById(R.id.rootLayoutCycleLength));

        daysValueCTV.setText(String.valueOf(VALUE + " Days"));

        plusBTN.setOnClickListener(v -> {
            if (VALUE < 35) {
                VALUE++;
                daysValueCTV.setText(String.valueOf(VALUE + " Days"));
            }
        });

        minusBTN.setOnClickListener(v -> {
            if (VALUE > 23) {
                VALUE--;
                daysValueCTV.setText(String.valueOf(VALUE + " Days"));
            }
        });

        backCBTN.setOnClickListener(v -> {
            finish();
        });

        saveCBTN.setOnClickListener(v -> {
            CalculateUtil.setPreferences(VALUE, CalculateUtil.bleeding_D,
                    CalculateUtil.lastPeriod_D, CalculateUtil.lastPeriod_M, CalculateUtil.lastPeriod_Y);
            Toast.makeText(this, "SAVED!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CycleLength.this, Home.class));
            finishAffinity();
        });
    }

    private void initViews() {

        daysValueCTV = findViewById(R.id.daysValueCTV);
        plusBTN = findViewById(R.id.plusBTN);
        minusBTN = findViewById(R.id.minusBTN);
        saveCBTN = findViewById(R.id.saveCBTN);
        backCBTN = findViewById(R.id.backCBTN);

    } // initViews()

}