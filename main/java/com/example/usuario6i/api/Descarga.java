package com.example.usuario6i.api;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;


public class Descarga extends AsyncTask<URL, Void, Articulo[]> {
    Activity activity;
    ArrayList<Articulo> articulos;

    String urlArticulo;

    String url;

    public Descarga(Activity activity, String filtro) throws MalformedURLException {
        this.activity = activity;
        Log.i("Filtro",filtro);
        filtro = filtro.trim().replaceAll("[^a-zA-Z0-9]", ",");
        if(filtro.equals(null) || filtro.equals(""))
            filtro="actualidad";
        url = new String("https://newsapi.org/v2/everything?language="+ Locale.getDefault().getLanguage()+
                "&q="+filtro+"&sortBy=publishedAt&apiKey=c8a6d46781854ac89fffaa4d854f847d");
    }
    @Override
    protected Articulo[] doInBackground(URL... urls) {
        Log.i("URL", url);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;


        Conexion conexion = new Conexion(url);
        ProcesadorJson pJson = new ProcesadorJson(conexion.getIs());

        articulos = pJson.getArticulos();
        return articulos.toArray(new Articulo[articulos.size()]);
    }

    @Override
    protected void onPostExecute(final Articulo[] articulos) {

        Log.i("Articulos en total", String.valueOf(articulos.length));
        if(articulos.length == 0){
            NumberPicker numberPicker = activity.findViewById(R.id.numberPicker);
            numberPicker.setDisplayedValues(null);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(0);
            numberPicker.setDisplayedValues(new String[]{activity.getString(R.string.articles_not_found)});
            cargarDatos(new Articulo("","","","","",""));
        }else {
            String[] titles = new String[articulos.length];
            ArrayList<String> autoCompleteArrayList = new ArrayList<String>();
            for (int i = 0; i < articulos.length; i++) {

                if (articulos[i].getTitle().length() <= 60)
                    titles[i] = articulos[i].getTitle();
                else
                    titles[i] = articulos[i].getTitle().substring(0, 60) + "...";
                String[] titleStrings = titles[i].split(" ");
                for (String string : titleStrings) {
                    autoCompleteArrayList.add(string.toLowerCase());
                }
            }

            final NumberPicker numberPicker = activity.findViewById(R.id.numberPicker);

            numberPicker.setDisplayedValues(null);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(titles.length - 1);
            numberPicker.setDisplayedValues(titles);
            numberPicker.setValue(0);


            //Datos iniciales
            cargarDatos(articulos[0]);
            numberPicker.setValue(1);
            //Cargar nuevos datos
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                    cargarDatos(articulos[newVal]);

                }
            });


            Button btnBuscar = activity.findViewById(R.id.btnBuscar);
            final AutoCompleteTextView tbBuscar = activity.findViewById(R.id.tbBuscar);

            String[] autoCompleteArray = autoCompleteArrayList.toArray(new String[autoCompleteArrayList.size()]);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity,
                    android.R.layout.simple_list_item_1, autoCompleteArray);

            tbBuscar.setThreshold(1);

            tbBuscar.setAdapter(arrayAdapter);

            btnBuscar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    try {
                        new Descarga(activity, tbBuscar.getText().toString()).execute();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }




    private void cargarDatos(Articulo articulo){
        final TextView tbTitle = activity.findViewById(R.id.tbTitle);
        final TextView tbDescription = activity.findViewById(R.id.tbDescription);
        final ImageView imageView = activity.findViewById(R.id.imageView);
        final TextView tbAuthor = activity.findViewById(R.id.tbAuthor);
        final TextView tbPublishedAt = activity.findViewById(R.id.tbPublishedAt);
        urlArticulo= articulo.getUrl();

        View.OnClickListener intentListener = new View.OnClickListener() {

            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.tbTitle:
                        iniciarIntent();
                        break;
                    case R.id.tbAuthor:
                        iniciarIntent();
                        break;
                    case R.id.imageView:
                        iniciarIntent();
                        break;
                    case R.id.tbDescription:
                        iniciarIntent();
                        break;
                    case R.id.tbPublishedAt:
                        iniciarIntent();
                        break;

                }
            }
        };

        tbTitle.setOnClickListener(intentListener);
        imageView.setOnClickListener(intentListener);
        tbDescription.setOnClickListener(intentListener);
        tbAuthor.setOnClickListener(intentListener);
        tbPublishedAt.setOnClickListener(intentListener);

        if(articulo.getTitle().length() <= 60)
            tbTitle.setText(articulo.getTitle());
        else
            tbTitle.setText(articulo.getTitle().substring(0,60)+"...");

        if(articulo.getDescription().length() <= 140)
            tbDescription.setText(articulo.getDescription());
        else
            tbDescription.setText(articulo.getDescription().substring(0,140)+"...");
        if(articulo.getDescription().equals(""))
            tbDescription.setBackgroundResource(0);
        else
            tbDescription.setBackgroundResource(R.drawable.tb_description_border);
        if(URLUtil.isValidUrl(articulo.getUrlToImage()) )
            Picasso.with(activity).load(articulo.getUrlToImage()).into(imageView);
        else
            imageView.setImageBitmap(null);

        if(!articulo.getAuthor().equals(""))
            tbAuthor.setText("Autor: "+articulo.getAuthor());
        else
            tbAuthor.setText("");
        if(!articulo.getPublishedAt().equals(""))
            tbPublishedAt.setText("Publicado a las: "+getTime(articulo.getPublishedAt())+" del "+getDate(articulo.getPublishedAt()));
        else
            tbPublishedAt.setText("");
    }

    private void iniciarIntent() {

        if(URLUtil.isValidUrl(urlArticulo)){
            Intent intent = new Intent(activity, NoticiaActivity.class);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, urlArticulo);
            intent.setType("text/plain");
            activity.startActivity(intent);
        }
    }


    private SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");

    private String getDate(String dateTime){
        return dateTime.substring(0,10).replaceAll("-","/");
    }
    private String getTime(String dateTime){
        return dateTime.substring(11,dateTime.length()-4);

    }
}
