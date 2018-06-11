package com.example.usuario6i.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by usuario6i on 07/11/2017.
 */

public class Conexion{



    private HttpURLConnection conexion = null;
    private InputStream is = null;


    public Conexion(String source){
        URL url = null;
        try {
            url = new URL(source);
            conexion = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(conexion.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpURLConnection getConexion() {
        return conexion;
    }

    public void setConexion(HttpURLConnection conexion) {
        this.conexion = conexion;
    }

    public InputStream getIs() {
        return is;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

}
