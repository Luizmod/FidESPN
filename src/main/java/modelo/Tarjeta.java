package modelo;

import java.util.Objects;

public class Tarjeta extends EventoPartido {

    private static final long serialVersionUID = 1L;

    private Jugador jugador;
    private Equipo equipo;
    private TipoTarjeta tipoTarjeta;
    private String motivo;
    private boolean registrada;

    public Tarjeta(int idEvento, Partido partido, int minuto, Corresponsal corresponsal,
                   Jugador jugador, Equipo equipo, TipoTarjeta tipoTarjeta, String motivo) {
        super(idEvento, partido, minuto, corresponsal, TipoEvento.TARJETA);
        this.jugador = Objects.requireNonNull(jugador, "El jugador es obligatorio.");
        this.equipo = Objects.requireNonNull(equipo, "El equipo es obligatorio.");
        this.tipoTarjeta = Objects.requireNonNull(tipoTarjeta, "El tipo de tarjeta es obligatorio.");
        this.motivo = motivo == null ? "" : motivo.trim();
    }

    @Override
    public void registrar() {
        if (registrada) {
            throw new IllegalStateException("La tarjeta ya fue registrada.");
        }
        partido.agregarEvento(this);
        registrada = true;
    }

    @Override
    public String obtenerDescripcion() {
        String texto = "Tarjeta " + tipoTarjeta + " para " + jugador.getNombre()
                + " al minuto " + minuto;
        if (!motivo.isBlank()) {
            texto += ". Motivo: " + motivo;
        }
        return texto;
    }

    @Override
    public String obtenerIcono() {
        return tipoTarjeta == TipoTarjeta.ROJA ? "🟥" : "🟨";
    }

    public boolean esTarjetaRoja() {
        return tipoTarjeta == TipoTarjeta.ROJA;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public TipoTarjeta getTipoTarjeta() {
        return tipoTarjeta;
    }

    public String getMotivo() {
        return motivo;
    }
}
