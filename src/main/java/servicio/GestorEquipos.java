package servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import modelo.Equipo;
import modelo.Jugador;

public class GestorEquipos {

    private final List<Equipo> equipos;

    public GestorEquipos() {
        this.equipos = new ArrayList<>();
    }

    public boolean registrarEquipo(Equipo equipo) {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo no puede ser nulo.");
        }

        boolean duplicado = equipos.stream().anyMatch(e ->
                e.getNombre().equalsIgnoreCase(equipo.getNombre())
                        && e.getPais().equalsIgnoreCase(equipo.getPais())
        );

        if (duplicado) {
            throw new IllegalArgumentException(
                    "Ya existe un equipo con el mismo nombre y país."
            );
        }

        return equipos.add(equipo);
    }

    public boolean registrarJugador(int idEquipo, Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }

        Equipo equipo = buscarPorId(idEquipo)
                .orElseThrow(() ->
                        new IllegalArgumentException("No se encontró el equipo indicado."));

        return equipo.agregarJugador(jugador);
    }

    public Optional<Equipo> buscarPorId(int idEquipo) {
        return equipos.stream()
                .filter(equipo -> equipo.getIdEquipo() == idEquipo)
                .findFirst();
    }

    public Optional<Equipo> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return Optional.empty();
        }

        return equipos.stream()
                .filter(equipo -> equipo.getNombre().equalsIgnoreCase(nombre.trim()))
                .findFirst();
    }

    public List<Equipo> listarPorGrupo(String grupo) {
        if (grupo == null || grupo.isBlank()) {
            return List.of();
        }

        return equipos.stream()
                .filter(equipo ->
                        equipo.getGrupoMundial().equalsIgnoreCase(grupo.trim()))
                .toList();
    }

    public List<Equipo> listarEquipos() {
        return Collections.unmodifiableList(equipos);
    }

    public boolean eliminarEquipo(int idEquipo) {
        return buscarPorId(idEquipo)
                .map(equipos::remove)
                .orElse(false);
    }

    public int obtenerCantidadEquipos() {
        return equipos.size();
    }
}
