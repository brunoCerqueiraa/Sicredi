package com.sicredi.test.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sicredi.test.R;
import com.google.android.material.navigation.NavigationView;
import com.sicredi.test.model.TbEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainDrawer extends AppCompatActivity {

    Intent intent;
    DrawerLayout drawer_layout;
    NavigationView navMenu;
    Toolbar toolBarMenu;

    String url = "https://5f5a8f24d44d640016169133.mockapi.io/api/events";
    List<TbEvent> listEvents = new ArrayList<>();

    public void getFormElements() {
        drawer_layout = findViewById(R.id.drawer_layout);
        navMenu = findViewById(R.id.navMenu);
        toolBarMenu = findViewById(R.id.toolBarMenu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        getFormElements();
        getPreviousIntent();
        setDrawer_layout();

        navMenu.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.main:
                    recreate();
                    break;

                case R.id.events:
                    if (listEvents.size() == 0){
                        showMessage("Erro!", "Verifique sua conexão com a internet e reinicie o aplicativo");
                    }
                    else{
                        redirectActivity(new Intent(MainDrawer.this, ListEvents.class), listEvents);
                    }
                    break;
                case R.id.sair:
                    sair(this);
                    break;

            }
            drawer_layout.closeDrawer(GravityCompat.START);
            return true;
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    System.out.println("Certo: " + response.toString());
                    Gson gson = new Gson();
                    TbEvent events[] = gson.fromJson(String.valueOf(response), TbEvent[].class);
                    listEvents = Arrays.asList(events);
                    for (TbEvent tbEvent : listEvents){
                        System.out.println(tbEvent.toString());
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Erro: " + error.toString());
                }
            }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public void getPreviousIntent(){
        intent = getIntent();
    }

    public void setDrawer_layout() {
        setSupportActionBar(toolBarMenu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolBarMenu, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer_layout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void showMessage(String titulo, String texto){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainDrawer.this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }

    public void redirectActivity(Intent intent, List<TbEvent> list) {
        intent.putExtra("list", (Serializable) list);
        startActivity(intent);
        MainDrawer.this.finish();
    }

    public void sair(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Sair");
        builder.setMessage("Você tem certeza que deseja sair?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
                System.exit(0);
            }
        });
        builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START);
        }
        else{
            sair(this);
        }
    }
}