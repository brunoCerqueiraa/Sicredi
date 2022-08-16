package com.sicredi.test.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sicredi.test.R;
import com.sicredi.test.model.TbEvent;

import java.util.HashMap;
import java.util.Map;

public class EventDetail extends AppCompatActivity {
    Intent intent;
    EditText etName, etEmail;
    Button btnRegistrar;
    TextView tvTitle, tvPrice, tvDescription, tvLocation;

    TbEvent tbEvent;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        getElementosForm();
        getPreviousInformationIntent();
        autoFill();

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + tbEvent.getLatitude() + "," + tbEvent.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formCheck()){
                    try {
                        String url = "https://5f5a8f24d44d640016169133.mockapi.io/api/checkin";
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                url,
                                response -> Toast.makeText(EventDetail.this, "Sucesso!", Toast.LENGTH_LONG).show(),
                                error -> Toast.makeText(EventDetail.this, "Erro!", Toast.LENGTH_LONG).show()){

                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError{
                                Map<String, String> params = new HashMap<>();
                                params.put("eventId", String.valueOf(tbEvent.getId()));
                                params.put("name", etName.getText().toString());
                                params.put("email", etEmail.getText().toString());
                                return params;
                            }
                        };

                        requestQueue = Volley.newRequestQueue(EventDetail.this);
                        requestQueue.add(stringRequest);
                    }
                    catch (Exception e){
                        System.out.println(e.toString());
                        showMessage("Validação", "Algo deu errado!");
                    }
                }
            }
        });
    }

    public void getElementosForm(){
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvLocation = findViewById(R.id.tvLocation);
    }


    public boolean formCheck(){
        boolean retorno = true;

        if (etName.getText().toString().length() < 1){
            retorno = false;
            showMessage("Validação", "Você precisa informar seu nome!");
        }
        if (etEmail.getText().toString().length() < 1){
            retorno = false;
            showMessage("Validação", "Você precisa informar seu e-mail!");
        }

        return retorno;
    }

    public void getPreviousInformationIntent(){
        intent = getIntent();
        tbEvent = (TbEvent) intent.getSerializableExtra("tbEvent");
    }

    public void autoFill(){
        tvTitle.setText("Título: " + tbEvent.getTitle());
        tvPrice.setText("Preço: " + tbEvent.getPrice());
        tvDescription.setText("Descrição: " + tbEvent.getDescription());
    }

    public void showMessage(String titulo, String texto){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(EventDetail.this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);
        dialogo.setNeutralButton("OK", null);
        dialogo.show();
    }

    private void redirectActivity(Intent intent) {
        startActivity(intent);
        EventDetail.this.finish();
    }


    @Override
    public void onBackPressed() {
        redirectActivity(new Intent(EventDetail.this, MainDrawer.class));
    }
}