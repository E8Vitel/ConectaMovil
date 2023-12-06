package com.example.conectamovil.Entidades;

public class Contactos {

    private String nombreContacto;
    private String numero;
    private String email;
    private String key;

    public String getKey() {
        return key;
    }

    public Contactos() {

    }
    public void setKey(String key) {
        this.key = key;
    }

    public Contactos(String nombreContacto, String numero, String email) {
        this.nombreContacto = nombreContacto;
        this.numero = numero;
        this.email = email;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
