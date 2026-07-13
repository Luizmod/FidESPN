package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Corresponsal extends Usuario {

    private static final long serialVersionUID = 1L;

    private String especialidad;
    private final List<Partido> partidosAsignados;

    public Corresponsal(int idUsuario, String nombre, String apellido, String correo,
                        String contrasena, boolean activo, String especialidad) {
        super(idUsuario, nombre, apellido, correo, contrasena,
              TipoUsuario.CORRESPONSAL, activo);
        this.especialidad = especialidad == null ? "" : especialidad.trim();
        this.partidosAsignados = new ArrayList<>();
    }

    public boolean asignarPartido(Partido partido) {
        if (partido == null || partidosAsignados.contains(partido)) {
            return false;
        }
        partidosAsignados.add(partido);
        partido.setCorresponsal(this);
        return true;
    }

    public boolean quitarPartido(Partido partido) {
        return partidosAsignados.remove(partido);
    }

    public List<Partido> obtenerPartidosAsignados() {
        return Collections.unmodifiableList(partidosAsignados);
    }

    public Gol reportarGol(int idEvento, Partido partido, int minuto, Jugador jugador,
                           Jugador asistente, Equipo equipo, boolean autogol) {
        Gol gol = new Gol(idEvento, partido, minuto, this, jugador, asistente, equipo, autogol);
        gol.registrar();
        return gol;
    }

    public Tarjeta reportarTarjeta(int idEvento, Partido partido, int minuto, Jugador jugador,
                                   Equipo equipo, TipoTarjeta tipoTarjeta, String motivo) {
        Tarjeta tarjeta = new Tarjeta(idEvento, partido, minuto, this, jugador,
                                      equipo, tipoTarjeta, motivo);
        tarjeta.registrar();
        return tarjeta;
    }

    public SituacionRelevante reportarSituacion(int idEvento, Partido partido, int minuto,
                                                String descripcion, CategoriaSituacion categoria) {
        SituacionRelevante situacion = new SituacionRelevante(
                idEvento, partido, minuto, this, descripcion, categoria);
        situacion.registrar();
        return situacion;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad == null ? "" : especialidad.trim();
    }

    @Override
    public String obtenerPanelPrincipal() {
        return "Módulo de cobertura";
    }
}
