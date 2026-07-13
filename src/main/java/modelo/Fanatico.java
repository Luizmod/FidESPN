package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fanatico extends Usuario {

    private static final long serialVersionUID = 1L;

    private final List<Equipo> equiposFavoritos;
    private String fotoPerfil;
    private String pais;

    public Fanatico(int idUsuario, String nombre, String apellido, String correo,
                    String contrasena, boolean activo, String pais) {
        super(idUsuario, nombre, apellido, correo, contrasena,
              TipoUsuario.FANATICO, activo);
        this.equiposFavoritos = new ArrayList<>();
        this.pais = pais == null ? "" : pais.trim();
        this.fotoPerfil = "";
    }

    public boolean seleccionarEquipoFavorito(Equipo equipo) {
        if (equipo == null || equiposFavoritos.contains(equipo) || equiposFavoritos.size() >= 8) {
            return false;
        }
        return equiposFavoritos.add(equipo);
    }

    public boolean quitarEquipoFavorito(Equipo equipo) {
        return equiposFavoritos.remove(equipo);
    }

    public List<Equipo> getEquiposFavoritos() {
        return Collections.unmodifiableList(equiposFavoritos);
    }

    public MensajeChat enviarMensajeChat(int idMensaje, Partido partido, String texto) {
        MensajeChat mensaje = new MensajeChat(idMensaje, texto, this, partido);
        mensaje.enviar();
        return mensaje;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil == null ? "" : fotoPerfil.trim();
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais == null ? "" : pais.trim();
    }

    @Override
    public String obtenerPanelPrincipal() {
        return "Inicio del fanático";
    }
}
