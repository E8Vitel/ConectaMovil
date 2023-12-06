package com.example.conectamovil;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase baseDatos;
    DatabaseReference referencia;
    Button btnRegistro;
    TextView btnInicio;
    EditText txtNombre, txtApellido, txtUsuarioR, txtEmailR, txtContrasena, txtContraAgain;
    ImageView pfp;
    Uri imagenUri;
    String imageUrl;

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


        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            imagenUri = data.getData();
                            pfp.setImageURI(imagenUri);
                        } else {
                            Toast.makeText(RegisterActivity.this, "No se ha seleccionado ninguna imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    uploadData();
            }
        });

        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent();
                photoPicker.setType("image/*");
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(photoPicker);
            }
        });

    }

    public void uploadData() {
        String nombre = txtNombre.getText().toString();
        String apellido = txtApellido.getText().toString();
        String usuario = txtUsuarioR.getText().toString();
        String email = txtEmailR.getText().toString();
        String contrasena = txtContrasena.getText().toString();
        String confirmar = txtContraAgain.getText().toString();

        Usuarios usuarios = new Usuarios(nombre, apellido, usuario, email, contrasena, imageUrl);
        if (usuario.equals("") || email.equals("") || contrasena.equals("") || confirmar.equals("") || nombre.equals("") || apellido.equals("")) {
            Toast.makeText(RegisterActivity.this, "Todos los campos deben ser rellenados", Toast.LENGTH_SHORT).show();
        } else if (contrasena.length() >= 6) {
            if (contrasena.equals(confirmar)) {
                auth.createUserWithEmailAndPassword(email, contrasena).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            guardarDatos(uid, usuarios);

                        } else {
                            Log.d("RegisterActivity", "El usuario es nulo: " + user);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Algo ha salido mal...", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "La contraseña no puede tener menos de 6 caracteres", Toast.LENGTH_SHORT).show();
        }
    }
    public void subirDatosFireBase(String uid, Usuarios usuarios) {
        baseDatos = FirebaseDatabase.getInstance();
        referencia = baseDatos.getReference("usuarios");
        referencia.child(uid).setValue(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(RegisterActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("RegisterActivity", "Error: " + e );
                Toast.makeText(RegisterActivity.this, "Error al guardar datos en Firestore", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void guardarDatos(String uid, Usuarios usuarios) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        if (imagenUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(uid).child("Profile Picture")
                    .child(imagenUri.getLastPathSegment());
            Log.d("RegisterActivity", "Ruta de almacenamiento: " + storageReference.getPath());

            storageReference.putFile(imagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                imageUrl = downloadUri.toString();
                                Log.d("RegisterActivity", "Url: " + imageUrl.toString());
                                usuarios.setUrlPfp(imageUrl);
                                subirDatosFireBase(uid, usuarios);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error al obtener la URL de descarga", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

        } else {
            subirDatosFireBase(uid, usuarios);
            dialog.dismiss();
        }
    }
}