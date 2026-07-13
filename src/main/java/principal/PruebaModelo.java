package principal;

import java.time.LocalDate;
import java.time.LocalTime;
import modelo.*;

public class PruebaModelo {
    public static void main(String[] args) {
        Administrador admin = new Administrador(
                1, "Luis", "Castro", "admin@fidespn.com", "Admin123", true);

        Corresponsal corresponsal = new Corresponsal(
                2, "Carlos", "Ramirez", "corresponsal@fidespn.com",
                "Corresponsal123", true, "Cobertura deportiva");

        Equipo argentina = new Equipo(1, "Argentina", "Argentina", "C");
        Equipo mexico = new Equipo(2, "Mexico", "Mexico", "C");

        Jugador jugador = new Jugador(
                1, "Lionel Messi", Posicion.DELANTERO, 10, argentina, true);
        argentina.agregarJugador(jugador);

        Jornada jornada = new Jornada(
                1, "Fase de grupos", LocalDate.now(), LocalDate.now().plusDays(7));

        Partido partido = new Partido(
                1, argentina, mexico, LocalDate.now(), LocalTime.of(18, 0), jornada);

        jornada.agregarPartido(partido);
        corresponsal.asignarPartido(partido);
        partido.iniciarPartido();

        Gol gol = corresponsal.reportarGol(
                1, partido, 25, jugador, null, argentina, false);

        System.out.println(admin.obtenerPanelPrincipal());
        System.out.println(partido);
        System.out.println(gol.obtenerDescripcion());
    }
}
