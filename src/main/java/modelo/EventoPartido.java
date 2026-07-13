package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class EventoPartido implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idEvento;
    protected Partido partido;
    protected int minuto;
    private Corresponsal corresponsal;
    private LocalDateTime timestamp;
    private TipoEvento tipoEvento;

    protected EventoPartido(int idEvento, Partido partido, int minuto,
                            Corresponsal corresponsal, TipoEvento tipoEvento) {
        this.idEvento = idEvento;
        this.partido = Objects.requireNonNull(partido, "El partido es obligatorio.");
        setMinuto(minuto);
        this.corresponsal = Objects.requireNonNull(corresponsal, "El corresponsal es obligatorio.");
        this.timestamp = LocalDateTime.now();
        this.tipoEvento = Objects.requireNonNull(tipoEvento, "El tipo de evento es obligatorio.");
    }

    public abstract void registrar();

    public abstract String obtenerDescripcion();

    public String obtenerIcono() {
        return switch (tipoEvento) {
            case GOL -> "⚽";
            case TARJETA -> "🟨";
            case SITUACION_RELEVANTE -> "📋";
        };
    }

    public int getIdEvento() {
        return idEvento;
    }

    public Partido getPartido() {
        return partido;
    }

    public int getMinuto() {
        return minuto;
    }

    public final void setMinuto(int minuto) {
        if (minuto < 0 || minuto > 130) {
            throw new IllegalArgumentException("El minuto debe estar entre 0 y 130.");
        }
        this.minuto = minuto;
    }

    public Corresponsal getCorresponsal() {
        return corresponsal;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    @Override
    public String toString() {
        return obtenerIcono() + " " + obtenerDescripcion();
    }
}
