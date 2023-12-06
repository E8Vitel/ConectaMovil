
package com.example.conectamovil;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class VerActivity extends AppCompatActivity {

    EditText txtNumero, txtEmail, txtNombre;
    Button btnModificar, btnVolver;
    FloatingActionButton btnEditar, btnEliminar;
    String nombre, numero, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        txtNombre = findViewById(R.id.txtNombreV);
        txtNumero = findViewById(R.id.txtNumeroV);
        txtEmail = findViewById(R.id.txtEmailV);
        btnModificar = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);
        btnEditar = findViewById(R.id.editar);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            txtNombre.setText(bundle.getString("nombre"));
            txtNumero.setText(bundle.getString("numero"));
            txtEmail.setText(bundle.getString("email"));
            nombre = bundle.getString("nombre");
            numero = bundle.getString("numero");
            email = bundle.getString("email");
        }

        btnModificar.setVisibility(View.INVISIBLE);

        txtNombre.setInputType(InputType.TYPE_NULL);
        txtEmail.setInputType(InputType.TYPE_NULL);
        txtNumero.setInputType(InputType.TYPE_NULL);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicio();
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerActivity.this, EditarActivity.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("numero", numero);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerActivity.this);
                builder.setMessage("¿Desea eliminar esta tarea?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarTarea();
                                inicio();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }


    public void inicio(){
        Intent intent = new Intent(VerActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void eliminarTarea() {


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        DatabaseReference tareaRef = database.getReference("Contactos")
                .child(uid)
                .child(nombre);
        tareaRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(VerActivity.this, "Tarea eliminada exitosamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerActivity.this, "Error al eliminar la tarea", Toast.LENGTH_SHORT).show();
            }
        });
    }
}