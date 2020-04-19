/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entity.User;
import Service.UserService;
import static Service.UserService.borrarArchivo;
import java.util.ArrayList;

import java.util.List;
import Repository.UserRepository;
/**
 *
 * @author Ramiro Monroy Ramos
 */
public class UserController {
    
    UserRepository repository;
    User nuevo;
    
    public UserController(){
        repository=new UserService();
    }

    public boolean CrearUsuario(String id, String nombre, String apellidos,String rol,String contraseña, String correo,
                                String direccion, String telefono){
        boolean r=false;
        if(!VerificarExistente(id)){
            r=false;
        }else if(!id.equals("")&&!nombre.equals("")&&!apellidos.equals("")&&!contraseña.equals("")&&!correo.equals("")
                &&!direccion.equals("")&&!telefono.equals("")){
            nuevo=new User(id,nombre,apellidos,rol,contraseña,correo,direccion,telefono);
            r= repository.crearUsuario(nuevo);
        }
        return r;
    }

    public List<User> ConsultarUsuario(){
        return repository.ObtenerTodos();
    }

    public boolean VerificarExistente(String idPersona){
        boolean d=true;

        List<User> usuario=ConsultarUsuario();

        for(User q:usuario){
            if(q.getId().trim().equals(idPersona)){
                d=false;
            }
        }
        return d;
    }
     public void EditarUsuario(User usuario){
         
        UserService r=new UserService();
        r.editarUsuario(usuario);
    }

    public String cantidadD(){
        int cont=0;
        String d;
        List<User>usuario=ConsultarUsuario();
        for(User q : usuario){
            cont++;
        }
        d=String.valueOf(cont);
        return d;
    }
  
    public User ubicar(){
       UserService r=new UserService();
       return r.ubicar();
    }
    public void Limpiar() {
        List<User> usuario = new ArrayList<User>();;
        for (User q : ConsultarUsuario()) {
            if (!q.getId().equals("||||||||||")) {
                usuario.add(q);
            }
        }
        borrarArchivo();
        UserRepository j=new UserService();
        for (User r : usuario) {
            j.crearUsuario(r);
        }
    }
    
    
    public boolean Eliminar(String dir){
        UserService r=new UserService();
        
        return r.eliminarUsuario(dir);
    }
    public User BuscarUsuario(String dir){
        UserService r=new UserService();
        
        return r.consultarUsuario(dir);
    }
       
}
