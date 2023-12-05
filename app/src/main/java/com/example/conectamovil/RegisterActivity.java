package com.example.conectamovil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.conectamovil.Entidades.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;

    Button btnRegistro;
    TextView btnInicio;
    EditText txtNombre, txtApellido, txtUsuarioR, txtEmailR, txtContrasena, txtContraAgain;
    ImageView pfp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtUsuarioR = findViewById(R.id.txtUsuarioR);
        txtEmailR = findViewById(R.id.txtEmailR);
        txtContrasena = findViewById(R.id.txtContra);
        txtContraAgain = findViewById(R.id.txtContraAgain);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnInicio = findViewById(R.id.txtInicioSesion);
        pfp = findViewById(R.id.pfp);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("ProfilePic");

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = txtNombre.getText().toString();
                String apellido = txtApellido.getText().toString();
                String usuario = txtUsuarioR.getText().toString();
                String email = txtEmailR.getText().toString();
                String contrasena = txtContrasena.getText().toString();
                String confirmar = txtContraAgain.getText().toString();


                if (usuario.equals("") || email.equals("") || contrasena.equals("") || confirmar.equals("") || nombre.equals("") || apellido.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Todos los campos deben ser rellenados", Toast.LENGTH_SHORT).show();
                } else if (contrasena.length() >= 6) {
                    if (contrasena.equals(confirmar)) {
                        auth.createUserWithEmailAndPassword(email, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String uid = auth.getCurrentUser().getUid();
                                Usuarios usuarios = new Usuarios(nombre, apellido, usuario, email, contrasena);

                                db.collection("usuarios").document(uid).set(usuarios)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RegisterActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this, "Error al guardar datos en Firestore", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "La contraseña no puede tener menos de 6 caracteres", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

    }

    private void subirImagenALmacenamiento(String uid, Uri imagenUri) {
        StorageReference imagenRef = storageReference.child(uid + ".jpg");

        imagenRef.putFile(imagenUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String imagenUrl = imagenUri.toString();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imagenUri = data.getData();
            pfp.setImageURI(imagenUri);
            subirImagenALmacenamiento(auth.getCurrentUser().getUid(), imagenUri);
        }
    }
}