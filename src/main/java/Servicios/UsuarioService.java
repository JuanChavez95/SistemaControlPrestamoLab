/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

/**
 *
 * @author DOC
 */
public class UsuarioService{
    
}
/*
import Dao.UsuarioDAOImpl;
import Clases.Permiso;
import Clases.Rol;
import Clases.Usuario;
import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    // Registrar un nuevo usuario
    public Usuario registrarUsuario(Usuario usuario, String rolNombre) {
        if (usuario.getNombre() == null || usuario.getApellido() == null ||
            usuario.getCorreo() == null || usuario.getContrasena() == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }

        if (buscarPorCorreo(usuario.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        usuario.setContrasena(encriptarContrasena(usuario.getContrasena()));

        // Aquí debes obtener el rol desde la base de datos (pendiente de implementación real)
        Rol rol = buscarRolPorNombre(rolNombre)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        usuario.addRol(rol);

        usuarioDAO.guardar(usuario);
        return usuario;
    }

    
    // Autenticación de usuario
    public Optional<Usuario> autenticar(String correo, String contrasena) {
        return usuarioDAO.listarTodos().stream()
                .filter(u -> u.getCorreo().equals(correo) && verificarContrasena(contrasena, u.getContrasena()))
                .findFirst();
    }

    // Buscar usuario por correo
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioDAO.listarTodos().stream()
                .filter(u -> u.getCorreo().equals(correo))
                .findFirst();
    }

    // Buscar usuario por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioDAO.buscarPorId(id);
    }

    // Listar todos los usuarios
    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    // Actualizar usuario
    public Usuario actualizar(Usuario usuario) {
        if (usuario.getId() == null || !usuarioDAO.buscarPorId(usuario.getId()).isPresent()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioDAO.actualizar(usuario);
        return usuario;
    }

    // Eliminar usuario
    public void eliminar(Long id) {
        Usuario usuario = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuarioDAO.eliminar(usuario);
    }

    // Cambiar estado (activo/inactivo)
    public Usuario cambiarEstado(Long id, boolean activo) {
        Usuario usuario = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setActivo(activo);
        return actualizar(usuario);
    }

    // Asignar un rol a un usuario
    public Usuario asignarRol(Long usuarioId, Long rolId) {
        Usuario usuario = buscarPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Rol rol = buscarRolPorId(rolId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        usuario.addRol(rol);
        return actualizar(usuario);
    }

    // Verificar si un usuario tiene cierto permiso
    public boolean tienePermiso(Long usuarioId, String nombrePermiso) {
        Usuario usuario = buscarPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        for (Rol rol : usuario.getRoles()) {
            for (Permiso permiso : rol.getPermisos()) {
                if (permiso.getNombre().equalsIgnoreCase(nombrePermiso)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Simulación para buscar rol por nombre (debes implementar en RolDAO)
    public Optional<Rol> buscarRolPorNombre(String nombre) {
        return Optional.empty(); // TODO: conectar con RolDAOImpl
    }

    // Simulación para buscar rol por ID (debes implementar en RolDAO)
    public Optional<Rol> buscarRolPorId(Long id) {
        return Optional.empty(); // TODO: conectar con RolDAOImpl
    }

    // Encriptar contraseña (usar BCrypt en producción)
    private String encriptarContrasena(String contrasena) {
        return contrasena; // Simulación (no seguro)
    }

    // Verificar contraseña (usar BCrypt en producción)
    private boolean verificarContrasena(String contrasenaPlana, String contrasenaEncriptada) {
        return contrasenaPlana.equals(contrasenaEncriptada);
    }
}
*/


