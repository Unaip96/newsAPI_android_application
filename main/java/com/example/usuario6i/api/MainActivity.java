package com.example.usuario6i.api;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NumberPicker numberPicker=findViewById(R.id.numberPicker);
        numberPicker.setDisplayedValues(new String[]{getString(R.string.articles_loading)});
        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.tbBuscar);
        descarga();
    }
    private void descarga() {
        try {
            new Descarga(this, "").execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
