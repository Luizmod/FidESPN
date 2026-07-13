package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Jornada implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idJornada;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private final List<Partido> partidos;

    public Jornada(int idJornada, String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idJornada = idJornada;
        setNombre(nombre);
        setFechas(fechaInicio, fechaFin);
        this.partidos = new ArrayList<>();
    }

    public boolean agregarPartido(Partido partido) {
        if (partido == null || partidos.contains(partido)) {
            return false;
        }
        partidos.add(partido);
        if (partido.getJornada() != this) {
            partido.setJornada(this);
        }
        return true;
    }

    public boolean quitarPartido(Partido partido) {
        return partidos.remove(partido);
    }

    public List<Partido> obtenerPartidos() {
        return Collections.unmodifiableList(partidos);
    }

    public List<Partido> obtenerPartidosActivos() {
        return partidos.stream()
                .filter(p -> p.getEstado() == EstadoPartido.EN_JUEGO)
                .toList();
    }

    public boolean estaActiva(LocalDate fecha) {
        LocalDate evaluada = fecha == null ? LocalDate.now() : fecha;
        return !evaluada.isBefore(fechaInicio) && !evaluada.isAfter(fechaFin);
    }

    public int getIdJornada() {
        return idJornada;
    }

    public void setIdJornada(int idJornada) {
        this.idJornada = idJornada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la jornada es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public final void setFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = Objects.requireNonNull(fechaInicio, "La fecha de inicio es obligatoria.");
        this.fechaFin = Objects.requireNonNull(fechaFin, "La fecha final es obligatoria.");
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la inicial.");
        }
    }

    @Override
    public String toString() {
        return nombre + " [" + fechaInicio + " - " + fechaFin + "]";
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof Jornada)) return false;
        Jornada jornada = (Jornada) objeto;
        return idJornada == jornada.idJornada;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJornada);
    }
}
