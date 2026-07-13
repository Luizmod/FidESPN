package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idPartido;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoPartido estado;
    private int marcadorLocal;
    private int marcadorVisitante;
    private Corresponsal corresponsal;
    private Alineacion alineacionLocal;
    private Alineacion alineacionVisitante;
    private Jornada jornada;
    private final List<EventoPartido> eventos;
    private final List<MensajeChat> mensajesChat;

    public Partido(int idPartido, Equipo equipoLocal, Equipo equipoVisitante,
                   LocalDate fecha, LocalTime hora, Jornada jornada) {
        this.idPartido = idPartido;
        setEquipos(equipoLocal, equipoVisitante);
        this.fecha = Objects.requireNonNull(fecha, "La fecha es obligatoria.");
        this.hora = Objects.requireNonNull(hora, "La hora es obligatoria.");
        this.jornada = jornada;
        this.estado = EstadoPartido.PROGRAMADO;
        this.marcadorLocal = 0;
        this.marcadorVisitante = 0;
        this.eventos = new ArrayList<>();
        this.mensajesChat = new ArrayList<>();
    }

    public void iniciarPartido() {
        if (estado != EstadoPartido.PROGRAMADO) {
            throw new IllegalStateException("Solo se puede iniciar un partido programado.");
        }
        estado = EstadoPartido.EN_JUEGO;
    }

    public void pasarMedioTiempo() {
        if (estado != EstadoPartido.EN_JUEGO) {
            throw new IllegalStateException("El partido debe estar en juego.");
        }
        estado = EstadoPartido.MEDIO_TIEMPO;
    }

    public void reanudarPartido() {
        if (estado != EstadoPartido.MEDIO_TIEMPO) {
            throw new IllegalStateException("El partido no está en medio tiempo.");
        }
        estado = EstadoPartido.EN_JUEGO;
    }

    public void finalizarPartido() {
        if (estado == EstadoPartido.FINALIZADO) {
            throw new IllegalStateException("El partido ya finalizó.");
        }
        estado = EstadoPartido.FINALIZADO;
    }

    public void agregarEvento(EventoPartido evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo.");
        }
        eventos.add(evento);
    }

    public void agregarMensaje(MensajeChat mensaje) {
        if (mensaje == null) {
            throw new IllegalArgumentException("El mensaje no puede ser nulo.");
        }
        mensajesChat.add(mensaje);
    }

    public void aumentarMarcador(Equipo equipo) {
        if (equipoLocal.equals(equipo)) {
            marcadorLocal++;
        } else if (equipoVisitante.equals(equipo)) {
            marcadorVisitante++;
        } else {
            throw new IllegalArgumentException("El equipo no participa en este partido.");
        }
    }

    public String obtenerMarcador() {
        return marcadorLocal + " - " + marcadorVisitante;
    }

    public List<EventoPartido> obtenerEventos() {
        return Collections.unmodifiableList(eventos);
    }

    public List<MensajeChat> obtenerMensajesChat() {
        return Collections.unmodifiableList(mensajesChat);
    }

    public int getIdPartido() {
        return idPartido;
    }

    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    public Equipo getEquipoVisitante() {
        return equipoVisitante;
    }

    public final void setEquipos(Equipo equipoLocal, Equipo equipoVisitante) {
        this.equipoLocal = Objects.requireNonNull(equipoLocal, "El equipo local es obligatorio.");
        this.equipoVisitante = Objects.requireNonNull(equipoVisitante, "El equipo visitante es obligatorio.");
        if (equipoLocal.equals(equipoVisitante)) {
            throw new IllegalArgumentException("Los equipos deben ser diferentes.");
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartido estado) {
        this.estado = Objects.requireNonNull(estado);
    }

    public int getMarcadorLocal() {
        return marcadorLocal;
    }

    public int getMarcadorVisitante() {
        return marcadorVisitante;
    }

    public Corresponsal getCorresponsal() {
        return corresponsal;
    }

    public void setCorresponsal(Corresponsal corresponsal) {
        this.corresponsal = corresponsal;
    }

    public Alineacion getAlineacionLocal() {
        return alineacionLocal;
    }

    public void setAlineacionLocal(Alineacion alineacionLocal) {
        this.alineacionLocal = alineacionLocal;
    }

    public Alineacion getAlineacionVisitante() {
        return alineacionVisitante;
    }

    public void setAlineacionVisitante(Alineacion alineacionVisitante) {
        this.alineacionVisitante = alineacionVisitante;
    }

    public Jornada getJornada() {
        return jornada;
    }

    public void setJornada(Jornada jornada) {
        this.jornada = jornada;
    }

    @Override
    public String toString() {
        return equipoLocal.getNombre() + " vs " + equipoVisitante.getNombre()
                + " | " + fecha + " " + hora + " | " + obtenerMarcador();
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (!(objeto instanceof Partido)) return false;
        Partido partido = (Partido) objeto;
        return idPartido == partido.idPartido;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPartido);
    }
}
