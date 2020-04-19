/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Repository;

import java.util.List;
import Entity.User;

/**
 *
 * @author Ramiro Monroy Ramos
 */
public interface UserRepository {
    
    boolean crearUsuario(User usuario);
    User consultarUsuario(String id);
    boolean editarUsuario(User usuario);
    boolean eliminarUsuario(String id);
    List<User> ObtenerTodos();
}
