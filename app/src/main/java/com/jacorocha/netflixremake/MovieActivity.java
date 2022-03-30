package com.jacorocha.netflixremake;

import static com.jacorocha.netflixremake.R.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieActivity extends AppCompatActivity {
    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_movie);

        txtTitle = findViewById(id.text_view_title);
        txtDesc = findViewById(id.text_view_desc);
        txtCast = findViewById(id.text_view_cast);

        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(drawable.ic_baseline_arrow_back_24);
            getSupportActionBar().setTitle(null);
        }
        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);
        if (drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4);
            drawable.setDrawableByLayerId(id.cover_drawable, movieCover);
            ((ImageView) findViewById(id.image_view_cover)).setImageDrawable(drawable);
        }

        txtTitle.setText("Batman begins");
        txtDesc.setText("Batman begins");
        txtCast.setText(getString(R.string.cast, "teste"));

    }
}