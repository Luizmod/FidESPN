package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Administrador extends Usuario {

    private static final long serialVersionUID = 1L;

    private final List<String> permisos;

    public Administrador(int idUsuario, String nombre, String apellido, String correo,
                         String contrasena, boolean activo) {
        super(idUsuario, nombre, apellido, correo, contrasena,
              TipoUsuario.ADMINISTRADOR, activo);
        this.permisos = new ArrayList<>();
    }

    public void agregarPermiso(String permiso) {
        if (permiso != null && !permiso.isBlank() && !permisos.contains(permiso.trim())) {
            permisos.add(permiso.trim());
        }
    }

    public boolean quitarPermiso(String permiso) {
        return permisos.remove(permiso);
    }

    public List<String> getPermisos() {
        return Collections.unmodifiableList(permisos);
    }

    @Override
    public String obtenerPanelPrincipal() {
        return "Panel de administración";
    }
}
