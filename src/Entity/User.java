/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.Date;

/**
 *
 * @author Ramiro Monroy Ramos
 */
public class User {

    private String id;
    private String nombre;
    private String apellido;
    private String rol;
    private String contrasena;
    private String email;
    private String direccion;
    private String telefono;

    public User() {

    }

    public User(String id, String nombre, String apellido,String rol, String contrasena, String email, String direccion, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol=rol;
        this.contrasena = contrasena;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Estudiante{"
                + "id='" + id + '\''
                + ", nombre='" + nombre + '\''
                + ", apellidos='" + apellido + '\''
                + ", fechaNaci=" + contrasena
                + ", correo=" + email
                + ", direccion=" + direccion
                + ", telefono=" + telefono
                + '}';
    }

}
