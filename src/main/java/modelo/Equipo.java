package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idEquipo;
    private String nombre;
    private String pais;
    private String escudo;
    private String grupoMundial;
    private final List<Jugador> jugadores;

    public Equipo(int idEquipo, String nombre, String pais, String escudo, String grupoMundial) {
        this.idEquipo = idEquipo;
        this.nombre = validarTexto(nombre, "nombre");
        this.pais = validarTexto(pais, "país");
        this.escudo = escudo == null ? "" : escudo.trim();
        this.grupoMundial = validarTexto(grupoMundial, "grupo");
        this.jugadores = new ArrayList<>();
    }

    public Equipo(int idEquipo, String nombre, String pais, String grupoMundial) {
        this(idEquipo, nombre, pais, "", grupoMundial);
    }

    public boolean agregarJugador(Jugador jugador) {
        if (jugador == null || jugadores.contains(jugador)) {
            return false;
        }
        boolean numeroRepetido = jugadores.stream()
                .anyMatch(j -> j.getNumeroCamiseta() == jugador.getNumeroCamiseta());
        if (numeroRepetido) {
            throw new IllegalArgumentException("El número de camiseta ya está asignado en el equipo.");
        }
        jugadores.add(jugador);
        if (jugador.getEquipo() != this) {
            jugador.setEquipo(this);
        }
        return true;
    }

    public boolean quitarJugador(Jugador jugador) {
        return jugadores.remove(jugador);
    }

    public List<Jugador> obtenerJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "nombre");
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = validarTexto(pais, "país");
    }

    public String getEscudo() {
        return escudo;
    }

    public void setEscudo(String escudo) {
        this.escudo = escudo == null ? "" : escudo.trim();
    }

    public String getGrupoMundial() {
        return grupoMundial;
    }

    public void setGrupoMundial(String grupoMundial) {
        this.grupoMundial = validarTexto(grupoMundial, "grupo");
    }

    @Override
    public String toString() {
        return nombre + " (" + pais + ") - Grupo " + grupoMundial;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof Equipo)) return false;
        Equipo equipo = (Equipo) objeto;
        return idEquipo == equipo.idEquipo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEquipo);
    }
}
