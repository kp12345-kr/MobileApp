package com.example.onestopwellbeing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.text.Html;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Button findoutmoreButton;
    private BottomNavigationView bottomNavigationView;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String videoId = "mxz8KyV3Ydc";

        String html = "<html><body style='margin:0px;padding:0px;'><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        webView.loadData(html, "text/html", "utf-8");

        findoutmoreButton = findViewById(R.id.Findoutmore_button);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        TextView wellBeingInfoTextView = findViewById(R.id.wellbeing_info_textview);
        wellBeingInfoTextView.setText("Well-being extends beyond momentary happiness and includes various facets such as overall life satisfaction, sense of purpose, and feelings of control. " +
                "While happiness is a component, well-being encompasses physical, emotional, and psychological dimensions of one's life. Click below to find more information on what services are available to help support wellbeing.");

        // Additional code to add the extra information
        String existingText = "Well-being extends beyond momentary happiness and includes various facets such as overall life satisfaction, sense of purpose, and feelings of control. " +
                "While happiness is a component, well-being encompasses physical, emotional, and psychological dimensions of one's life. Click below to find more information on what services are available to help support wellbeing.";

        String additionalInfoHtml = "<br><br>Click on the profile section below; where you can search the profiles for:" +
                "<ul>" +
                "<li>Deep Yoga</li>" +
                "<li>Mental Health Services</li>" +
                "<li>Psychotherapy</li>" +
                "<li>Physiotherapy</li>" +
                "</ul>";

        String combinedText = existingText + additionalInfoHtml;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            wellBeingInfoTextView.setText(Html.fromHtml(combinedText, Html.FROM_HTML_MODE_COMPACT));
        } else {
            wellBeingInfoTextView.setText(Html.fromHtml(combinedText));
        }

        final Animation zoomInAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        final Animation zoomOutAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_out);

        findoutmoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findoutmoreButton.startAnimation(zoomInAnimation);
                findoutmoreButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findoutmoreButton.startAnimation(zoomOutAnimation);
                        navigateToActivity(MapsActivity.class);
                    }
                }, 200);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.maps_item:
                        navigateToActivity(MyMapActivity.class);
                        return true;
                    case R.id.navigation_profile:
                        navigateToActivity(Search.class);
                        return true;
                    case R.id.navigation_home:
                        navigateToActivity(MainActivity.class);
                        return true;
                }
                return false;
            }
        });
    }

    private void navigateToActivity(Class<?> cls) {
        startActivity(new Intent(MainActivity.this, cls));
    }
}
