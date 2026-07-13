package modelo;

import java.util.Objects;

public class Gol extends EventoPartido {

    private static final long serialVersionUID = 1L;

    private Jugador jugador;
    private Jugador jugadorAsistente;
    private Equipo equipo;
    private boolean autogol;
    private boolean registrado;

    public Gol(int idEvento, Partido partido, int minuto, Corresponsal corresponsal,
               Jugador jugador, Jugador jugadorAsistente, Equipo equipo, boolean autogol) {
        super(idEvento, partido, minuto, corresponsal, TipoEvento.GOL);
        this.jugador = Objects.requireNonNull(jugador, "El jugador es obligatorio.");
        this.jugadorAsistente = jugadorAsistente;
        this.equipo = Objects.requireNonNull(equipo, "El equipo es obligatorio.");
        this.autogol = autogol;
    }

    @Override
    public void registrar() {
        if (registrado) {
            throw new IllegalStateException("El gol ya fue registrado.");
        }

        Equipo equipoBeneficiado = equipo;
        if (autogol) {
            equipoBeneficiado = equipo.equals(partido.getEquipoLocal())
                    ? partido.getEquipoVisitante()
                    : partido.getEquipoLocal();
        }

        partido.aumentarMarcador(equipoBeneficiado);
        partido.agregarEvento(this);
        registrado = true;
    }

    @Override
    public String obtenerDescripcion() {
        String texto = "Gol de " + jugador.getNombre() + " al minuto " + minuto;
        if (autogol) {
            texto += " (autogol)";
        } else if (jugadorAsistente != null) {
            texto += ", asistencia de " + jugadorAsistente.getNombre();
        }
        return texto + " - " + equipo.getNombre();
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Jugador getJugadorAsistente() {
        return jugadorAsistente;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public boolean isAutogol() {
        return autogol;
    }
}
