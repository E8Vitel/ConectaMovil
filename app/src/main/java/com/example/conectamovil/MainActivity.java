package com.example.conectamovil;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.conectamovil.Adaptador.AdaptadorContactos;
import com.example.conectamovil.Entidades.Contactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Button btnPerfil;
    FloatingActionButton fab;

    RecyclerView contactos;
    List<Contactos> listaContactos;
    AdaptadorContactos adaptadorContactos;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        btnPerfil = findViewById(R.id.btnPerfil);
        contactos = findViewById(R.id.contactos);
        listaContactos = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        contactos.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        adaptadorContactos = new AdaptadorContactos(MainActivity.this, listaContactos);
        contactos.setAdapter(adaptadorContactos);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Contactos").child(uid);
            dialog.show();

            eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listaContactos.clear();
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        Contactos contactos1 = itemSnapshot.getValue(Contactos.class);
                        listaContactos.add(contactos1);
                    }
                    adaptadorContactos.notifyDataSetChanged();
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    dialog.dismiss();
                }
            });

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CrearActivity.class));
            }
        });
        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
            }
        });


    }

}
