package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import modelo.Administrador;
import servicio.ContextoAplicacion;

public class FrmDashboardAdministrador extends JFrame {

    private final ContextoAplicacion contexto;
    private final Administrador administrador;

    public FrmDashboardAdministrador(
            ContextoAplicacion contexto,
            Administrador administrador
    ) {
        this.contexto = contexto;
        this.administrador = administrador;

        inicializarVentana();
        construirInterfaz();
    }

    private void inicializarVentana() {
        setTitle("FidESPN - Panel de administración");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(PaletaColores.FONDO_VENTANA);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(PaletaColores.VERDE_OSCURO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel lblMarca = new JLabel("FidESPN");
        lblMarca.setForeground(PaletaColores.BLANCO);
        lblMarca.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel lblUsuario = new JLabel(
                "Administrador: " + administrador.getNombre()
                + " " + administrador.getApellido()
        );
        lblUsuario.setForeground(PaletaColores.BLANCO);
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));

        encabezado.add(lblMarca, BorderLayout.WEST);
        encabezado.add(lblUsuario, BorderLayout.EAST);

        JPanel contenido = new JPanel(new BorderLayout(15, 15));
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(25, 30, 30, 30));

        JLabel lblTitulo = new JLabel("Dashboard del administrador");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(PaletaColores.VERDE_OSCURO);

        JPanel tarjetas = new JPanel(new GridLayout(2, 2, 20, 20));
        tarjetas.setOpaque(false);

        tarjetas.add(crearTarjeta(
                "Usuarios",
                String.valueOf(contexto.getGestorUsuarios().obtenerCantidadUsuarios()),
                "Usuarios registrados"
        ));

        tarjetas.add(crearTarjeta(
                "Equipos",
                String.valueOf(contexto.getGestorEquipos().obtenerCantidadEquipos()),
                "Selecciones registradas"
        ));

        tarjetas.add(crearTarjeta(
                "Jornadas",
                String.valueOf(contexto.getGestorJornadas().obtenerCantidadJornadas()),
                "Jornadas disponibles"
        ));

        tarjetas.add(crearTarjeta(
                "Partidos",
                String.valueOf(contexto.getGestorPartidos().obtenerCantidadPartidos()),
                "Partidos programados"
        ));

        JPanel acciones = new JPanel(new GridLayout(1, 4, 12, 12));
        acciones.setOpaque(false);

        JButton btnRegistrarEquipo = crearBoton("Registrar equipo");
        btnRegistrarEquipo.addActionListener(e ->
                new FrmRegistrarEquipo(contexto).setVisible(true)
        );

        JButton btnRegistrarJugador = crearBoton("Registrar jugador");
        btnRegistrarJugador.addActionListener(e ->
                new FrmRegistrarJugador(contexto).setVisible(true)
        );

        acciones.add(btnRegistrarEquipo);
        acciones.add(btnRegistrarJugador);
        acciones.add(crearBotonPendiente("Crear jornada"));
        acciones.add(crearBotonPendiente("Crear partido"));

        contenido.add(lblTitulo, BorderLayout.NORTH);
        contenido.add(tarjetas, BorderLayout.CENTER);
        contenido.add(acciones, BorderLayout.SOUTH);

        principal.add(encabezado, BorderLayout.NORTH);
        principal.add(contenido, BorderLayout.CENTER);

        setContentPane(principal);
        pack();
    }

    private JPanel crearTarjeta(String titulo, String valor, String descripcion) {
        JPanel tarjeta = new JPanel(new BorderLayout(8, 8));
        tarjeta.setBackground(PaletaColores.BLANCO);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PaletaColores.BORDE_TARJETA),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(PaletaColores.VERDE_OSCURO);

        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblValor.setForeground(PaletaColores.VERDE_CESPED);

        JLabel lblDescripcion = new JLabel(descripcion, SwingConstants.CENTER);
        lblDescripcion.setForeground(PaletaColores.TEXTO_SECUNDARIO);

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        tarjeta.add(lblDescripcion, BorderLayout.SOUTH);

        return tarjeta;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(PaletaColores.VERDE_CESPED);
        boton.setForeground(PaletaColores.BLANCO);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setPreferredSize(new Dimension(150, 42));
        return boton;
    }

    private JButton crearBotonPendiente(String texto) {
        JButton boton = crearBoton(texto);
        boton.setBackground(PaletaColores.VERDE_MEDIO);
        boton.addActionListener(e ->
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Esta función se implementará en el siguiente formulario.",
                        texto,
                        javax.swing.JOptionPane.INFORMATION_MESSAGE
                )
        );
        return boton;
    }
}
