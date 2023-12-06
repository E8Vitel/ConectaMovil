
package com.example.conectamovil;



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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.conectamovil.Entidades.Contactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EditarActivity extends AppCompatActivity {

    EditText txtNumero, txtEmail, txtNombre;
    Button btnModificar, btnVolver;
    FloatingActionButton btnEditar, btnEliminar;
    DatabaseReference databaseReference;
    TextView txtTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        txtTitulo = findViewById(R.id.txtTitulo);
        txtNombre = findViewById(R.id.txtNombreV);
        txtNumero = findViewById(R.id.txtNumeroV);
        txtEmail = findViewById(R.id.txtEmailV);
        btnModificar = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);
        btnEditar = findViewById(R.id.editar);

        btnModificar.setVisibility(View.VISIBLE);
        btnEliminar.setVisibility(View.INVISIBLE);

        txtTitulo.setText("Editar Contacto");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();



        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            txtNombre.setText(bundle.getString("nombre"));
            txtNumero.setText(bundle.getString("numero"));
            txtEmail.setText(bundle.getString("email"));
            String nombre1 = bundle.getString("nombre");
            databaseReference = FirebaseDatabase.getInstance().getReference("Contactos").child(uid).child(nombre1);
        }


        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombre = txtNombre.getText().toString();
                String nuevoNumero = txtNumero.getText().toString();
                String nuevoEmail = txtEmail.getText().toString();

                actualizarTarea(nuevoNombre, nuevoNumero, nuevoEmail);

                finish();
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void actualizarTarea(String nuevoNombre, String nuevoNumero, String nuevoEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Bundle bundle = getIntent().getExtras();
        String key = bundle.getString("nombre");

        Contactos contactosActualizados = new Contactos(nuevoNombre, nuevoNumero, nuevoEmail);
        contactosActualizados.setKey(key);

        databaseReference.setValue(contactosActualizados);

        Toast.makeText(EditarActivity.this, "Contacto actualizado", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(EditarActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}