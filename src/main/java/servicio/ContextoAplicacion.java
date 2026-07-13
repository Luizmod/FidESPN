package servicio;

public final class ContextoAplicacion {

    private static final ContextoAplicacion INSTANCIA = new ContextoAplicacion();

    private final GestorUsuarios gestorUsuarios;
    private final GestorEquipos gestorEquipos;
    private final GestorJornadas gestorJornadas;
    private final GestorPartidos gestorPartidos;

    private ContextoAplicacion() {
        gestorUsuarios = new GestorUsuarios();
        gestorEquipos = new GestorEquipos();
        gestorJornadas = new GestorJornadas();
        gestorPartidos = new GestorPartidos();
    }

    public static ContextoAplicacion getInstancia() {
        return INSTANCIA;
    }

    public GestorUsuarios getGestorUsuarios() {
        return gestorUsuarios;
    }

    public GestorEquipos getGestorEquipos() {
        return gestorEquipos;
    }

    public GestorJornadas getGestorJornadas() {
        return gestorJornadas;
    }

    public GestorPartidos getGestorPartidos() {
        return gestorPartidos;
    }
}
