/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service;

import Entity.User;

import java.io.File;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import java.util.HashSet;
import java.util.Set;
import Repository.UserRepository;
/**
 *
 * @author Ramiro Monroy Ramos
 */
public class UserService implements UserRepository{
    
    

    public UserService(){
        
        if(!Files.exists(archivo)){
            try{
                Files.createFile(archivo);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    private static final String REGISTRO_ELIMINADO_TEXT = "||||||||||";
    private static final String NOMBRE_ARCHIVO="user";
    private static final int LONGITUD_REGISTRO=169;
    private static final int LONGITUD_ID=10;
    private static final int LONGITUD_NOMBRE=32;
    private static final int LONGITUD_APELLIDO=32;
    private static final int LONGITUD_ROL=13;
    private static final int LONGITUD_CONTRASENA=8;
    private static final int LONGITUD_EMAIL=32;
    private static final int LONGITUD_DIRECCION=32;
    private static final int LONGITUD_TELEFONO=10;
    private static final String ENCODING_WINDOWS = "ASCII";
    private static final Path archivo=Paths.get(NOMBRE_ARCHIVO);

    @Override
    public boolean crearUsuario(User usuario) {

        String registro=parseString(usuario);
        byte data[]= registro.getBytes();
        ByteBuffer buffer=ByteBuffer.wrap(data);

        try(FileChannel fc = (FileChannel.open(archivo, APPEND))){
            fc.write(buffer);
            return true;
        }catch (IOException ioe){
            System.out.println("I/O Exception: "+ ioe);
        }
        return false;
    }
    //borrarArchivo(archivo.toString());
    public static void borrarArchivo(){
        File file=new File(archivo.toString());
        if(file.exists()){
            if(file.delete()){
                System.out.println("Eliminado");
            }else{
                System.out.println("No existe");
            }
        }
    }
    public User ubicar() {
        try(SeekableByteChannel sbc = Files.newByteChannel(archivo)){
            ByteBuffer buffer = ByteBuffer.allocate(LONGITUD_REGISTRO);
            while (sbc.read(buffer)>0){
                //devuelve el apuntador del buffer al principio
                buffer.rewind();
                CharBuffer registro = Charset.forName(ENCODING_WINDOWS).decode(buffer);
                User usuarioConvertido = parseUsuario2Objeto(registro);
                if(usuarioConvertido.getId().equals(0)){
                    return usuarioConvertido;
                }
                buffer.flip();
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return null;
    }
    @Override
    public User consultarUsuario(String id) {
        try(SeekableByteChannel sbc = Files.newByteChannel(archivo)){
            ByteBuffer buffer = ByteBuffer.allocate(LONGITUD_REGISTRO);
            while (sbc.read(buffer)>0){
                //devuelve el apuntador del buffer al principio
                buffer.rewind();
                CharBuffer registro = Charset.forName(ENCODING_WINDOWS).decode(buffer);
                User usuarioConvertido = parseUsuario2Objeto(registro);
                if(usuarioConvertido.getId().equals(id)){
                    return usuarioConvertido;
                }
                buffer.flip();
            }
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        return null;
    }

    
    @Override
    public boolean eliminarUsuario(String id) {
        Set<StandardOpenOption> options = new HashSet<StandardOpenOption>();
        options.add(READ);
        options.add(WRITE);
        long lastPosition = 0;
        try (SeekableByteChannel sbc = Files.newByteChannel(archivo, options)) {
            ByteBuffer buf = ByteBuffer.allocate(LONGITUD_REGISTRO);
            while (sbc.read(buf) > 0) {
                buf.rewind();
                CharBuffer registro = Charset.forName(ENCODING_WINDOWS).decode(buf);
                User usuarioConvertido = parseUsuario2Objeto(registro);
                
                if (usuarioConvertido.getId().equals(id)) {
//                    System.out.println("Hola");
                    sbc.position(lastPosition);
                    new String();
                    sbc.write(ByteBuffer.wrap(REGISTRO_ELIMINADO_TEXT.getBytes()));
                    return true;
                }
                buf.flip();
                lastPosition = sbc.position();
            }
        } catch (IOException ioe) {
            System.out.println("caught exception: " + ioe);
        }
        return false;
    }
   
    @Override
    public boolean editarUsuario(User usuario) {
        Set<StandardOpenOption> options = new HashSet<StandardOpenOption>();
        options.add(READ);
        options.add(WRITE);
        long lastPosition = 0;
        try (SeekableByteChannel sbc = Files.newByteChannel(archivo, options)) {
            ByteBuffer buf = ByteBuffer.allocate(LONGITUD_REGISTRO);
            String encoding = System.getProperty("file.encoding");
            while (sbc.read(buf) > 0) {
                buf.rewind();
                CharBuffer registro = Charset.forName(encoding).decode(buf);

                String idUsuario = registro.subSequence(0, LONGITUD_REGISTRO).toString().trim();
                if (idUsuario.equals(usuario.getId())) {
                    sbc.position(lastPosition);
                    sbc.write(ByteBuffer.wrap(parseString(usuario).getBytes()));
                    return true;
                }
                buf.flip();
                lastPosition = sbc.position();
            }
        } catch (IOException ioe) {
            System.out.println("caught exception: " + ioe);
        }
        return false;
    }

    @Override
    public List<User> ObtenerTodos() {
        List<User> usuario=new ArrayList<User>();
        try(SeekableByteChannel sbc=Files.newByteChannel(archivo)){
        ByteBuffer buf=ByteBuffer.allocate(LONGITUD_REGISTRO);
        String encoding=System.getProperty("file.encoding");
        while((sbc.read(buf))>0){
            buf.rewind();
            User usuari=parseUsuario2Objeto(Charset.forName(encoding).decode(buf));
            buf.flip();
            usuario.add(usuari);
        }
        }catch (IOException x){
            System.out.println("caught exception: "+x);
        }
        return usuario;
    }

    private User parseUsuario2Objeto(CharBuffer registro){
        User usuario=new User();

        String identificacion=registro.subSequence(0, LONGITUD_ID).toString().trim();
        registro.position(LONGITUD_ID);
        registro=registro.slice();
        usuario.setId(identificacion);
        
        String nombres=registro.subSequence(0, LONGITUD_NOMBRE).toString().trim();
        registro.position(LONGITUD_NOMBRE);
        registro=registro.slice();
        usuario.setNombre(nombres);
        
        String apellido = registro.subSequence(0, LONGITUD_APELLIDO).toString().trim();
        registro.position(LONGITUD_APELLIDO);
        registro = registro.slice();
        usuario.setApellido(apellido);
        
        String rol = registro.subSequence(0, LONGITUD_ROL).toString().trim();
        registro.position(LONGITUD_ROL);
        registro = registro.slice();
        usuario.setRol(rol);
        
        String contrasena = registro.subSequence(0, LONGITUD_CONTRASENA).toString().trim();
        registro.position(LONGITUD_CONTRASENA);
        registro = registro.slice();
        usuario.setContrasena(contrasena);
        
        String correo = registro.subSequence(0, LONGITUD_EMAIL).toString().trim();
        registro.position(LONGITUD_EMAIL);
        registro = registro.slice();
        usuario.setEmail(correo);
        
        String direccion = registro.subSequence(0, LONGITUD_DIRECCION).toString().trim();
        registro.position(LONGITUD_DIRECCION);
        registro = registro.slice();
        usuario.setDireccion(direccion);
        
        String telefono = registro.subSequence(0, LONGITUD_TELEFONO).toString().trim();
        registro.position(LONGITUD_TELEFONO);
        registro = registro.slice();
        usuario.setTelefono(telefono);
        
        
        
        return usuario;
    }
    
    private String parseString(User usuario) {		
		StringBuilder registro = new StringBuilder(LONGITUD_REGISTRO);
		registro.append(completarCampo(usuario.getId(),LONGITUD_ID));
		registro.append(completarCampo(usuario.getNombre(), LONGITUD_NOMBRE));
		registro.append(completarCampo(usuario.getApellido(),LONGITUD_APELLIDO));
                registro.append(completarCampo(usuario.getRol(),LONGITUD_ROL));
                registro.append(completarCampo(usuario.getContrasena(),LONGITUD_CONTRASENA));
		registro.append(completarCampo(usuario.getEmail(),LONGITUD_EMAIL));
		registro.append(completarCampo(usuario.getDireccion(),LONGITUD_DIRECCION));
		registro.append(completarCampo(usuario.getTelefono(), LONGITUD_TELEFONO));
		return registro.toString();		
	}
    private String completarCampo(String campo, int tamaño){
		if(campo.length()>tamaño){
		    campo=campo.substring(0,tamaño);
			return campo;
		}
		return String.format("%1$-" + tamaño + "s", campo);
	}
    

    
}

    

