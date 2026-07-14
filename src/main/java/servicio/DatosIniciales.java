package servicio;

import excepciones.CorresponsalNoDisponibleException;
import excepciones.DatosInvalidosException;
import excepciones.EquipoDuplicadoException;
import excepciones.NumeroCamisetaDuplicadoException;
import excepciones.PartidoInvalidoException;
import excepciones.UsuarioDuplicadoException;
import java.time.LocalDate;
import java.time.LocalTime;
import modelo.Administrador;
import modelo.Corresponsal;
import modelo.Equipo;
import modelo.Jornada;
import modelo.Jugador;
import modelo.Partido;
import modelo.Posicion;

public final class DatosIniciales {

    private DatosIniciales() {
    }

    public static void cargar(ContextoAplicacion contexto)
            throws UsuarioDuplicadoException,
                   EquipoDuplicadoException,
                   NumeroCamisetaDuplicadoException,
                   DatosInvalidosException,
                   PartidoInvalidoException,
                   CorresponsalNoDisponibleException {

        GestorUsuarios usuarios = contexto.getGestorUsuarios();
        GestorEquipos equipos = contexto.getGestorEquipos();
        GestorJornadas jornadas = contexto.getGestorJornadas();
        GestorPartidos partidos = contexto.getGestorPartidos();

        Administrador administrador = new Administrador(
                1,
                "Luis",
                "Castro",
                "admin",
                "admin",
                true
        );

        Corresponsal corresponsal = new Corresponsal(
                2,
                "Carlos",
                "Ramirez",
                "corresponsal",
                "corresponsal",
                true,
                "Cobertura deportiva"
        );

        usuarios.registrarUsuario(administrador);
        usuarios.registrarUsuario(corresponsal);

        Equipo argentina =
                new Equipo(1, "Argentina", "Argentina", "C");

        Equipo mexico =
                new Equipo(2, "Mexico", "Mexico", "C");

        equipos.registrarEquipo(argentina);
        equipos.registrarEquipo(mexico);

        Jugador jugador = new Jugador(
                1,
                "Lionel Messi",
                Posicion.DELANTERO,
                10,
                argentina,
                true
        );

        equipos.registrarJugador(
                argentina.getIdEquipo(),
                jugador
        );

        Jornada jornada = new Jornada(
                1,
                "Fase de grupos",
                LocalDate.now(),
                LocalDate.now().plusDays(7)
        );

        jornadas.registrarJornada(jornada);

        Partido partido = new Partido(
                1,
                argentina,
                mexico,
                LocalDate.now().plusDays(1),
                LocalTime.of(18, 0),
                jornada
        );

        partidos.registrarPartido(partido);

        partidos.asignarCorresponsal(
                partido.getIdPartido(),
                corresponsal
        );
    }
}
