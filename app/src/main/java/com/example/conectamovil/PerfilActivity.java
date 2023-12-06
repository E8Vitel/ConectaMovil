package com.example.conectamovil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PerfilActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    Button btnLogout;
    TextView txtNombre, txtUsuario, txtEmail;
    ImageView pfp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(uid);
        btnLogout = findViewById(R.id.btnSignOut);
        txtNombre = findViewById(R.id.txtNombre);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtEmail = findViewById(R.id.txtEmail);
        pfp = findViewById(R.id.pfpPerfil);
        Log.d("PerfilActivity", "Uid: " + uid);
        Log.d("PerfilActivity", "databasereference: " + databaseReference.toString());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        obtenerDatosUsuario();
    }
    private void cerrarSesion() {
        auth.signOut();
        startActivity(new Intent(PerfilActivity.this, LoginActivity.class));
        finish();
    }

    private void obtenerDatosUsuario() {
        FirebaseUser user = auth.getCurrentUser();
        Log.d("PerfilActivity", "Uid: " + user);
        if (user != null) {
            String uid = user.getUid();

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Obtener los datos del usuario
                        String nombre = snapshot.child("nombre").getValue(String.class);
                        String apellido = snapshot.child("apellido").getValue(String.class);
                        String usuario = snapshot.child("usuario").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String urlPfp = snapshot.child("urlPfp").getValue(String.class);
                        Log.d("PerfilActivity", "Uid: " + uid);
                        // Mostrar los datos en los TextView
                        txtNombre.setText(nombre +" "+ apellido);
                        txtUsuario.setText(usuario);
                        txtEmail.setText(email);

                        Glide.with(PerfilActivity.this)
                                .load(Uri.parse(urlPfp))
                                .into(pfp);

                    } else {
                        Toast.makeText(PerfilActivity.this, "No se encontraron datos de usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("CuentaFragment", "Error al obtener datos del usuario: " + error.getMessage());
                }
            });
        }
    }
}