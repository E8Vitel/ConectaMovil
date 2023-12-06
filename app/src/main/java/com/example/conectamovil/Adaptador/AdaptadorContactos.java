package com.example.conectamovil.Adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conectamovil.ChatActivity;
import com.example.conectamovil.Entidades.Contactos;
import com.example.conectamovil.Entidades.Usuarios;
import com.example.conectamovil.R;
import com.example.conectamovil.VerActivity;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorContactos extends RecyclerView.Adapter<VerContactos> {

    private Context context;
    private List<Contactos> listaContactos;

    public AdaptadorContactos(Context context, List<Contactos> listaContactos) {
        this.context = context;
        this.listaContactos = listaContactos;
    }

    @NonNull
    @Override
    public VerContactos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_tarea, parent, false);
        return new VerContactos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerContactos holder, int position) {
        holder.viewNombre.setText(listaContactos.get(position).getNombreContacto());
        holder.viewNumero.setText(listaContactos.get(position).getNumero());
        holder.viewEmail.setText(listaContactos.get(position).getEmail());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("nombre", listaContactos.get(holder.getAdapterPosition()).getNombreContacto());
                context.startActivity(intent);
            }
        });
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VerActivity.class);
                intent.putExtra("nombre", listaContactos.get(holder.getAdapterPosition()).getNombreContacto());
                intent.putExtra("numero", listaContactos.get(holder.getAdapterPosition()).getNumero());
                intent.putExtra("email", listaContactos.get(holder.getAdapterPosition()).getEmail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public void buscarTarea(ArrayList<Contactos> buscarLista) {
        listaContactos = buscarLista;
        notifyDataSetChanged();
    }
}

class VerContactos extends RecyclerView.ViewHolder {

    TextView viewNombre, viewNumero, viewEmail;
    RelativeLayout recCard;
    ImageButton btnUpdate;
    public VerContactos(@NonNull View itemView) {
        super(itemView);

        viewNombre = itemView.findViewById(R.id.viewNombre);
        viewNumero = itemView.findViewById(R.id.viewNumero);
        viewEmail = itemView.findViewById(R.id.viewEmail);
        recCard = itemView.findViewById(R.id.recCard);
        btnUpdate = itemView.findViewById(R.id.btnUpdate);
    }
}