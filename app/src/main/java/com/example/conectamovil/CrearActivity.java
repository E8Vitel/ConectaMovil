package com.example.conectamovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.conectamovil.Entidades.Contactos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CrearActivity extends AppCompatActivity {

    EditText txtNombreContacto, txtNumeroContacto, txtEmail;
    private FirebaseAuth auth;

    Button btnGuardar, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        auth = FirebaseAuth.getInstance();
        txtNombreContacto = findViewById(R.id.createNombreC);
        txtNumeroContacto = findViewById(R.id.createNumeroC);
        txtEmail = findViewById(R.id.createEmailC);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVolver = findViewById(R.id.btnVolver);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirDatos();
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CrearActivity.this, MainActivity.class));
            }
        });
    }

    public void subirDatos() {

        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();

        String nombreContacto = txtNombreContacto.getText().toString();
        String numero = txtNumeroContacto.getText().toString();
        String email = txtEmail.getText().toString();


        Contactos contactos = new Contactos(nombreContacto, numero, email);

        FirebaseDatabase.getInstance().getReference("Contactos").child(uid).child(nombreContacto)
                .setValue(contactos).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CrearActivity.this, "Contacto creado", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CrearActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}