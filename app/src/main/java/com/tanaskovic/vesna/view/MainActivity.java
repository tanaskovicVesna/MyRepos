package com.tanaskovic.vesna.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tanaskovic.vesna.R;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            //    avoid end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            ListFragment listFragment = new ListFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, listFragment)
                    .commit();

        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }


}