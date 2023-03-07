package tulsi.periodpal.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.util.Random;

import tulsi.periodpal.Helper.CalculateUtil;
import tulsi.periodpal.R;


public class Splash extends AppCompatActivity {

//    VIEWS
    private ImageView splashIMG;


//    ARRAY
    private String[] splashICONS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initViews();
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.gradient_start));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.gradient_end));
        getWindow().getDecorView().setSystemUiVisibility(0);


        new Handler().postDelayed(() -> {

//                CalculateUtil.setPreferences(28, 7, 1, 1, 2023, -1, -1, -1);
            CalculateUtil.getInstance(this);
            if (CalculateUtil.isEmpty())
                startActivity(new Intent(Splash.this, Data.class));
            else
                startActivity(new Intent(Splash.this, Home.class));

            finishAffinity();

        }, 3000);

    }

    private void initViews() {

        splashIMG = findViewById(R.id.splashIMG);
        splashICONS = getResources().getStringArray(R.array.splashICONS);

        Glide.with(this)
                .load(splashICONS[new Random().nextInt(splashICONS.length)])
                .into(splashIMG);

    } // initViews()


}