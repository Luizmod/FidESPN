package modelo;

import java.io.Serializable;
import java.util.Objects;

public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idJugador;
    private String nombre;
    private Posicion posicion;
    private int numeroCamiseta;
    private Equipo equipo;
    private boolean activo;

    public Jugador(int idJugador, String nombre, Posicion posicion,
                   int numeroCamiseta, Equipo equipo, boolean activo) {
        this.idJugador = idJugador;
        setNombre(nombre);
        setPosicion(posicion);
        setNumeroCamiseta(numeroCamiseta);
        this.equipo = equipo;
        this.activo = activo;
    }

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del jugador es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = Objects.requireNonNull(posicion, "La posición es obligatoria.");
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(int numeroCamiseta) {
        if (numeroCamiseta < 1 || numeroCamiseta > 99) {
            throw new IllegalArgumentException("El número de camiseta debe estar entre 1 y 99.");
        }
        this.numeroCamiseta = numeroCamiseta;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return numeroCamiseta + " - " + nombre + " (" + posicion + ")";
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof Jugador)) return false;
        Jugador jugador = (Jugador) objeto;
        return idJugador == jugador.idJugador;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJugador);
    }
}
