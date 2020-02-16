package com.kalamin.moviedatabase.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.model.entity.ActorDetails;
import com.kalamin.moviedatabase.utils.Extra;
import com.kalamin.moviedatabase.viewmodels.ActorDetailsViewModel;
import com.kalamin.moviedatabase.views.activities.adapters.ActorDetailsPagerAdapter;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class ActorDetailsActivity extends BaseActivity {
    private ActorDetailsViewModel actorDetailsViewModel;
    private ImageView poster;
    private TextView actorNameAndAge;
    private TextView actorBirthplace;
    private TextView actorBirthday;

    private ViewPager viewPager;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_details);

        setToolbarWithBackButton();

        internetConnectionReceiver().setInternetConnectionListener(isConnected -> {
            if (!isConnected) {
                startActivity(new Intent(ActorDetailsActivity.this, NoInternetActivity.class));
            }
        });

        Intent intent = getIntent();
        String actorId = intent.getStringExtra(Extra.ACTOR_ID);
        if (actorId == null)
            actorId = intent.getStringExtra(Extra.SEARCH_ITEM_ID);

        viewPager = findViewById(R.id.pager);
        tabs = findViewById(R.id.tabs);
        actorBirthplace = findViewById(R.id.actor_birthplace);
        actorNameAndAge = findViewById(R.id.actor_name_age);
        actorBirthday = findViewById(R.id.actor_birthday);
        poster = findViewById(R.id.actor_poster);

        actorDetailsViewModel = new ViewModelProvider(this).get(ActorDetailsViewModel.class);
        actorDetailsViewModel.askActorDetails(actorId);
        actorDetailsViewModel.getActorDetails().observeForever(actorDetailsObserver);
    }

    private Observer<ActorDetails> actorDetailsObserver = new Observer<ActorDetails>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onChanged(@NotNull ActorDetails actorDetails) {
            Picasso.get().load(actorDetails.getImagePath()).fit().centerCrop().into(poster);
            actorNameAndAge.setText(actorDetails.getName() + " (" + actorDetails.getAge() + ")");
            actorBirthplace.setText(actorDetails.getPlaceOfBirth());
            String date = (actorDetails.getDeathday() == null) ? actorDetails.getBirthday() : actorDetails.getBirthday() + " - " + actorDetails.getDeathday();

            actorBirthday.setText(date);
            actorDetailsViewModel.getActorDetails().removeObserver(actorDetailsObserver);
            ActorDetailsPagerAdapter actorDetailsPagerAdapter = new ActorDetailsPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, actorDetails.getBio(), actorDetails.getImages());
            viewPager.setAdapter(actorDetailsPagerAdapter);
            tabs.setupWithViewPager(viewPager);

            poster.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            tabs.setVisibility(View.VISIBLE);
            findViewById(R.id.content).setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    };


}
