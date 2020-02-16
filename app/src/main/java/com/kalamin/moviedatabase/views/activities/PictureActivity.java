package com.kalamin.moviedatabase.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.utils.Extra;
import com.squareup.picasso.Picasso;

public class PictureActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        setToolbarWithBackButton();

        this.internetConnectionReceiver().setInternetConnectionListener(isConnected -> {
            if (!isConnected) {
                startActivity(new Intent(PictureActivity.this, NoInternetActivity.class));
            }
        });

        ImageView image = findViewById(R.id.image);
        Intent intent = getIntent();
        String url = intent.getStringExtra(Extra.URL);
        Picasso.get().load(url).into(image);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }
}
