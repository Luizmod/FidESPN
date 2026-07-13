package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private TipoUsuario tipoUsuario;
    private boolean activo;
    private LocalDateTime fechaRegistro;

    protected Usuario(int idUsuario, String nombre, String apellido, String correo,
                      String contrasena, TipoUsuario tipoUsuario, boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = validarTexto(nombre, "nombre");
        this.apellido = validarTexto(apellido, "apellido");
        this.correo = validarTexto(correo, "correo");
        this.contrasena = validarTexto(contrasena, "contraseña");
        this.tipoUsuario = Objects.requireNonNull(tipoUsuario, "El tipo de usuario es obligatorio.");
        this.activo = activo;
        this.fechaRegistro = LocalDateTime.now();
    }

    public boolean autenticar(String correo, String contrasena) {
        return activo
                && this.correo.equalsIgnoreCase(correo)
                && this.contrasena.equals(contrasena);
    }

    public abstract String obtenerPanelPrincipal();

    protected static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "nombre");
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = validarTexto(apellido, "apellido");
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = validarTexto(correo, "correo");
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = validarTexto(contrasena, "contraseña");
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    protected void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = Objects.requireNonNull(tipoUsuario);
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + tipoUsuario + ")";
    }
}
