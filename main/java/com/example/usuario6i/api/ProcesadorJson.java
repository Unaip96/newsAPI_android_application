package com.example.usuario6i.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by usuario6i on 14/11/2017.
 */

public class ProcesadorJson {

    public ArrayList<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(ArrayList<Articulo> articulos) {
        this.articulos = articulos;
    }

    private ArrayList<Articulo> articulos = new ArrayList<Articulo>();


    public ProcesadorJson(InputStream is){
        JSONObject object=null;
        try{

            BufferedReader br = new BufferedReader(  new InputStreamReader(is));
            String line = "";

            StringBuilder responseStrBuilder = new StringBuilder();
            while((line =  br.readLine()) != null){

                responseStrBuilder.append(line);
            }

            object= new JSONObject(responseStrBuilder.toString());
            Log.i("Object : ", responseStrBuilder.toString());
            JSONArray array = object.getJSONArray("articles");
            for(int i=0; i< array.length();i++){
                JSONObject newObject = (JSONObject) array.get(i);
                String author= newObject.getString("author");
                Log.i("Author: ", author);
                String title= newObject.getString("title");;
                String description= newObject.getString("description");;
                String url= newObject.getString("url");;
                String urlToImage= newObject.getString("urlToImage");;
                String publishedAt= newObject.getString("publishedAt");;
                Articulo articulo = new Articulo(author, title, description, url, urlToImage, publishedAt);
                articulos.add(articulo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
