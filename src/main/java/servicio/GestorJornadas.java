package servicio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import modelo.Jornada;

public class GestorJornadas {

    private final List<Jornada> jornadas;

    public GestorJornadas() {
        this.jornadas = new ArrayList<>();
    }

    public boolean registrarJornada(Jornada jornada) {
        if (jornada == null) {
            throw new IllegalArgumentException("La jornada no puede ser nula.");
        }

        boolean idDuplicado = jornadas.stream()
                .anyMatch(j -> j.getIdJornada() == jornada.getIdJornada());

        if (idDuplicado) {
            throw new IllegalArgumentException(
                    "Ya existe una jornada con el mismo identificador."
            );
        }

        boolean fechasSuperpuestas = jornadas.stream().anyMatch(j ->
                !jornada.getFechaFin().isBefore(j.getFechaInicio())
                        && !jornada.getFechaInicio().isAfter(j.getFechaFin())
        );

        if (fechasSuperpuestas) {
            throw new IllegalArgumentException(
                    "Las fechas de la jornada se superponen con otra jornada registrada."
            );
        }

        return jornadas.add(jornada);
    }

    public Optional<Jornada> buscarPorId(int idJornada) {
        return jornadas.stream()
                .filter(jornada -> jornada.getIdJornada() == idJornada)
                .findFirst();
    }

    public Optional<Jornada> buscarJornadaActiva(LocalDate fecha) {
        LocalDate fechaEvaluada = fecha == null ? LocalDate.now() : fecha;

        return jornadas.stream()
                .filter(jornada -> jornada.estaActiva(fechaEvaluada))
                .findFirst();
    }

    public List<Jornada> listarOrdenadasPorFecha() {
        return jornadas.stream()
                .sorted(Comparator.comparing(Jornada::getFechaInicio))
                .toList();
    }

    public List<Jornada> listarJornadas() {
        return Collections.unmodifiableList(jornadas);
    }

    public boolean eliminarJornada(int idJornada) {
        return buscarPorId(idJornada)
                .map(jornadas::remove)
                .orElse(false);
    }

    public int obtenerCantidadJornadas() {
        return jornadas.size();
    }
}
