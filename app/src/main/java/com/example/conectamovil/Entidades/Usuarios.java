package com.example.conectamovil.Entidades;

public class Usuarios {

    private String nombre;
    private String apellido;
    private String usuario;
    private String email;
    private String contrasena;
    private String urlPfp;

    public Usuarios(String nombre, String apellido, String usuario, String email, String contrasena, String urlPfp) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuario = usuario;
        this.email = email;
        this.contrasena = contrasena;
        this.urlPfp = urlPfp;
    }

    public Usuarios() {

    }

    public String getUrlPfp() { return urlPfp; }

    public void setUrlPfp(String urlPfp) {
        this.urlPfp = urlPfp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
