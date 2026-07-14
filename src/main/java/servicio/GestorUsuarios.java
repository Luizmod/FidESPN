package servicio;

import excepciones.CredencialesInvalidasException;
import excepciones.UsuarioDuplicadoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import modelo.Usuario;

public class GestorUsuarios {

    private final List<Usuario> usuarios;

    public GestorUsuarios() {
        this.usuarios = new ArrayList<>();
    }

    public boolean registrarUsuario(Usuario usuario)
            throws UsuarioDuplicadoException {

        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }

        if (buscarPorCorreo(usuario.getCorreo()).isPresent()) {
            throw new UsuarioDuplicadoException(
                    "Ya existe un usuario registrado con el correo "
                    + usuario.getCorreo() + "."
            );
        }

        return usuarios.add(usuario);
    }

    public Usuario iniciarSesion(String correo, String contrasena)
            throws CredencialesInvalidasException {

        if (correo == null || correo.isBlank()
                || contrasena == null || contrasena.isBlank()) {
            throw new CredencialesInvalidasException(
                    "Debe ingresar correo y contraseña."
            );
        }

        return usuarios.stream()
                .filter(usuario -> usuario.autenticar(correo.trim(), contrasena))
                .findFirst()
                .orElseThrow(() ->
                        new CredencialesInvalidasException(
                                "Correo o contraseña incorrectos."
                        )
                );
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return Optional.empty();
        }

        return usuarios.stream()
                .filter(usuario ->
                        usuario.getCorreo().equalsIgnoreCase(correo.trim()))
                .findFirst();
    }

    public Optional<Usuario> buscarPorId(int idUsuario) {
        return usuarios.stream()
                .filter(usuario -> usuario.getIdUsuario() == idUsuario)
                .findFirst();
    }

    public boolean cambiarEstadoUsuario(int idUsuario, boolean activo) {
        Usuario usuario = buscarPorId(idUsuario)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No se encontró el usuario indicado."
                        )
                );

        usuario.setActivo(activo);
        return true;
    }

    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public int obtenerCantidadUsuarios() {
        return usuarios.size();
    }
}
