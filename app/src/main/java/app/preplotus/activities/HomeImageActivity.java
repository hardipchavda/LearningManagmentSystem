package app.preplotus.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.preplotus.R;

import java.util.ArrayList;


public class HomeImageActivity  extends AppCompatActivity {

    Spinner spinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_image_activity);
        spinner = findViewById(R.id.spinner);


        ArrayList<String> splist = new ArrayList<>();
        splist.add("1 months");
        splist.add("3 months");
        splist.add("6 months");
        splist.add("1 Year");

        ArrayAdapter arrayAdapter = new ArrayAdapter(HomeImageActivity.this,android.R.layout.simple_dropdown_item_1line,splist);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
