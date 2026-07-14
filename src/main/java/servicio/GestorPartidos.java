package servicio;

import excepciones.CorresponsalNoDisponibleException;
import excepciones.PartidoInvalidoException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import modelo.Corresponsal;
import modelo.Equipo;
import modelo.EstadoPartido;
import modelo.EventoPartido;
import modelo.Jornada;
import modelo.Partido;

public class GestorPartidos {

    private final List<Partido> partidos;

    public GestorPartidos() {
        this.partidos = new ArrayList<>();
    }

    public boolean registrarPartido(Partido partido)
            throws PartidoInvalidoException {

        if (partido == null) {
            throw new PartidoInvalidoException(
                    "El partido no puede ser nulo."
            );
        }

        if (partido.getEquipoLocal()
                .equals(partido.getEquipoVisitante())) {
            throw new PartidoInvalidoException(
                    "El equipo local y el visitante deben ser diferentes."
            );
        }

        boolean idDuplicado = partidos.stream()
                .anyMatch(p ->
                        p.getIdPartido() == partido.getIdPartido()
                );

        if (idDuplicado) {
            throw new PartidoInvalidoException(
                    "Ya existe un partido con el mismo identificador."
            );
        }

        boolean choqueHorario = partidos.stream().anyMatch(p ->
                p.getFecha().equals(partido.getFecha())
                && p.getHora().equals(partido.getHora())
                && comparteEquipo(p, partido)
        );

        if (choqueHorario) {
            throw new PartidoInvalidoException(
                    "Uno de los equipos ya tiene un partido "
                    + "programado en ese horario."
            );
        }

        partidos.add(partido);

        Jornada jornada = partido.getJornada();
        if (jornada != null
                && !jornada.obtenerPartidos().contains(partido)) {
            jornada.agregarPartido(partido);
        }

        return true;
    }

    private boolean comparteEquipo(Partido primero, Partido segundo) {
        Equipo local1 = primero.getEquipoLocal();
        Equipo visitante1 = primero.getEquipoVisitante();
        Equipo local2 = segundo.getEquipoLocal();
        Equipo visitante2 = segundo.getEquipoVisitante();

        return local1.equals(local2)
                || local1.equals(visitante2)
                || visitante1.equals(local2)
                || visitante1.equals(visitante2);
    }

    public boolean asignarCorresponsal(
            int idPartido,
            Corresponsal corresponsal
    ) throws CorresponsalNoDisponibleException {

        if (corresponsal == null) {
            throw new CorresponsalNoDisponibleException(
                    "El corresponsal no puede ser nulo."
            );
        }

        if (!corresponsal.isActivo()) {
            throw new CorresponsalNoDisponibleException(
                    "El corresponsal debe estar activo."
            );
        }

        Partido partido = buscarPorId(idPartido)
                .orElseThrow(() ->
                        new CorresponsalNoDisponibleException(
                                "No se encontró el partido indicado."
                        )
                );

        if (partido.getEstado() != EstadoPartido.PROGRAMADO) {
            throw new CorresponsalNoDisponibleException(
                    "Solo se puede asignar o cambiar el corresponsal "
                    + "de un partido programado."
            );
        }

        return corresponsal.asignarPartido(partido);
    }

    public boolean registrarEvento(
            int idPartido,
            EventoPartido evento
    ) throws PartidoInvalidoException {

        Partido partido = buscarPorId(idPartido)
                .orElseThrow(() ->
                        new PartidoInvalidoException(
                                "No se encontró el partido indicado."
                        )
                );

        if (evento == null) {
            throw new PartidoInvalidoException(
                    "El evento no puede ser nulo."
            );
        }

        if (!evento.getPartido().equals(partido)) {
            throw new PartidoInvalidoException(
                    "El evento no pertenece al partido seleccionado."
            );
        }

        evento.registrar();
        return true;
    }

    public Optional<Partido> buscarPorId(int idPartido) {
        return partidos.stream()
                .filter(partido ->
                        partido.getIdPartido() == idPartido)
                .findFirst();
    }

    public List<Partido> listarPorCorresponsal(
            Corresponsal corresponsal
    ) {
        if (corresponsal == null) {
            return List.of();
        }

        return partidos.stream()
                .filter(partido ->
                        corresponsal.equals(
                                partido.getCorresponsal()))
                .sorted(
                        Comparator.comparing(Partido::getFecha)
                                .thenComparing(Partido::getHora)
                )
                .toList();
    }

    public List<Partido> listarPorEstado(
            EstadoPartido estado
    ) {
        if (estado == null) {
            return List.of();
        }

        return partidos.stream()
                .filter(partido ->
                        partido.getEstado() == estado)
                .toList();
    }

    public List<Partido> listarPartidos() {
        return Collections.unmodifiableList(partidos);
    }

    public boolean eliminarPartido(int idPartido) {
        return buscarPorId(idPartido)
                .map(partidos::remove)
                .orElse(false);
    }

    public int obtenerCantidadPartidos() {
        return partidos.size();
    }
}
