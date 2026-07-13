package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Alineacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idAlineacion;
    private Partido partido;
    private Equipo equipo;
    private final List<Jugador> jugadores;
    private final List<Jugador> suplentes;
    private String formacion;

    public Alineacion(int idAlineacion, Partido partido, Equipo equipo, String formacion) {
        this.idAlineacion = idAlineacion;
        this.partido = Objects.requireNonNull(partido, "El partido es obligatorio.");
        this.equipo = Objects.requireNonNull(equipo, "El equipo es obligatorio.");
        this.formacion = formacion == null ? "" : formacion.trim();
        this.jugadores = new ArrayList<>();
        this.suplentes = new ArrayList<>();
    }

    public boolean agregarJugador(Jugador jugador, boolean esTitular) {
        if (jugador == null || !equipo.equals(jugador.getEquipo())
                || jugadores.contains(jugador) || suplentes.contains(jugador)) {
            return false;
        }

        if (esTitular) {
            if (jugadores.size() >= 11) {
                return false;
            }
            return jugadores.add(jugador);
        }

        if (suplentes.size() >= 12) {
            return false;
        }
        return suplentes.add(jugador);
    }

    public boolean estaCompleta() {
        return jugadores.size() == 11;
    }

    public List<Jugador> obtenerTitulares() {
        return Collections.unmodifiableList(jugadores);
    }

    public List<Jugador> obtenerSuplentes() {
        return Collections.unmodifiableList(suplentes);
    }

    public int getIdAlineacion() {
        return idAlineacion;
    }

    public Partido getPartido() {
        return partido;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public String getFormacion() {
        return formacion;
    }

    public void setFormacion(String formacion) {
        this.formacion = formacion == null ? "" : formacion.trim();
    }

    @Override
    public String toString() {
        return equipo.getNombre() + " - " + formacion;
    }
}
