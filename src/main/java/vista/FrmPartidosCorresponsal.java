package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import modelo.Corresponsal;
import modelo.EstadoPartido;
import modelo.Partido;
import servicio.ContextoAplicacion;

public class FrmPartidosCorresponsal extends JFrame {

    private final ContextoAplicacion contexto;
    private final Corresponsal corresponsal;

    private JTable tablaPartidos;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    private JLabel lblEnJuego;

    public FrmPartidosCorresponsal(
            ContextoAplicacion contexto,
            Corresponsal corresponsal
    ) {
        this.contexto = contexto;
        this.corresponsal = corresponsal;

        setTitle("FidESPN - Mis partidos asignados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);

        construirInterfaz();
        cargarPartidos();
    }

    private void construirInterfaz() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(PaletaColores.FONDO_VENTANA);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(PaletaColores.VERDE_OSCURO);
        encabezado.setBorder(
                BorderFactory.createEmptyBorder(18, 25, 18, 25)
        );

        JLabel lblMarca = new JLabel("FidESPN");
        lblMarca.setForeground(PaletaColores.BLANCO);
        lblMarca.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel lblUsuario = new JLabel(
                "Corresponsal: "
                + corresponsal.getNombre()
                + " "
                + corresponsal.getApellido()
        );
        lblUsuario.setForeground(PaletaColores.BLANCO);
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));

        encabezado.add(lblMarca, BorderLayout.WEST);
        encabezado.add(lblUsuario, BorderLayout.EAST);

        JPanel contenido = new JPanel(new BorderLayout(15, 15));
        contenido.setOpaque(false);
        contenido.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 30, 30)
        );

        JLabel lblTitulo = new JLabel("Mis partidos asignados");
        lblTitulo.setForeground(PaletaColores.VERDE_OSCURO);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel indicadores = new JPanel(new GridLayout(1, 2, 15, 15));
        indicadores.setOpaque(false);

        JPanel tarjetaTotal = crearTarjetaIndicador("Partidos asignados");
        lblTotal = (JLabel) tarjetaTotal.getClientProperty("valor");

        JPanel tarjetaEnJuego = crearTarjetaIndicador("Partidos en juego");
        lblEnJuego = (JLabel) tarjetaEnJuego.getClientProperty("valor");

        indicadores.add(tarjetaTotal);
        indicadores.add(tarjetaEnJuego);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID", "Partido", "Fecha", "Hora",
                    "Estado", "Marcador"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPartidos = new JTable(modeloTabla);
        tablaPartidos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
        tablaPartidos.setRowHeight(28);
        tablaPartidos.setBackground(PaletaColores.BLANCO);
        tablaPartidos.setForeground(PaletaColores.NEGRO);
        tablaPartidos.setGridColor(PaletaColores.BORDE_TARJETA);
        tablaPartidos.getTableHeader()
                .setBackground(PaletaColores.VERDE_OSCURO);
        tablaPartidos.getTableHeader()
                .setForeground(PaletaColores.BLANCO);
        tablaPartidos.getTableHeader()
                .setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tablaPartidos);
        scroll.setBorder(
                BorderFactory.createLineBorder(PaletaColores.BORDE_TARJETA)
        );

        JPanel acciones = new JPanel(new GridLayout(1, 4, 12, 12));
        acciones.setOpaque(false);

        JButton btnActualizar = crearBoton("Actualizar");
        btnActualizar.addActionListener(e -> cargarPartidos());

        JButton btnIniciar = crearBoton("Iniciar partido");
        btnIniciar.addActionListener(e -> iniciarPartido());

        JButton btnReportar = crearBoton("Reportar evento");
        btnReportar.addActionListener(e -> abrirReporte());

        JButton btnFinalizar = crearBoton("Finalizar partido");
        btnFinalizar.addActionListener(e -> finalizarPartido());

        acciones.add(btnActualizar);
        acciones.add(btnIniciar);
        acciones.add(btnReportar);
        acciones.add(btnFinalizar);

        JPanel superior = new JPanel(new BorderLayout(15, 15));
        superior.setOpaque(false);
        superior.add(lblTitulo, BorderLayout.NORTH);
        superior.add(indicadores, BorderLayout.CENTER);

        contenido.add(superior, BorderLayout.NORTH);
        contenido.add(scroll, BorderLayout.CENTER);
        contenido.add(acciones, BorderLayout.SOUTH);

        principal.add(encabezado, BorderLayout.NORTH);
        principal.add(contenido, BorderLayout.CENTER);

        setContentPane(principal);
        pack();
    }

    private JPanel crearTarjetaIndicador(String titulo) {
        JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
        tarjeta.setBackground(PaletaColores.BLANCO);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        PaletaColores.BORDE_TARJETA
                ),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(PaletaColores.VERDE_OSCURO);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel lblValor = new JLabel("0", SwingConstants.LEFT);
        lblValor.setForeground(PaletaColores.VERDE_CESPED);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 30));

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        tarjeta.putClientProperty("valor", lblValor);

        return tarjeta;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(PaletaColores.VERDE_CESPED);
        boton.setForeground(PaletaColores.BLANCO);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        return boton;
    }

    private void cargarPartidos() {
        modeloTabla.setRowCount(0);

        List<Partido> partidos = contexto
                .getGestorPartidos()
                .listarPorCorresponsal(corresponsal);

        int cantidadEnJuego = 0;

        for (Partido partido : partidos) {
            if (partido.getEstado() == EstadoPartido.EN_JUEGO) {
                cantidadEnJuego++;
            }

            modeloTabla.addRow(new Object[]{
                partido.getIdPartido(),
                partido.getEquipoLocal().getNombre()
                        + " vs "
                        + partido.getEquipoVisitante().getNombre(),
                partido.getFecha(),
                partido.getHora(),
                partido.getEstado(),
                partido.obtenerMarcador()
            });
        }

        lblTotal.setText(String.valueOf(partidos.size()));
        lblEnJuego.setText(String.valueOf(cantidadEnJuego));
    }

    private Partido obtenerPartidoSeleccionado() {
        int fila = tablaPartidos.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un partido de la tabla.",
                    "Partido requerido",
                    JOptionPane.WARNING_MESSAGE
            );
            return null;
        }

        int idPartido = (int) modeloTabla.getValueAt(fila, 0);

        return contexto.getGestorPartidos()
                .buscarPorId(idPartido)
                .orElse(null);
    }

    private void iniciarPartido() {
        Partido partido = obtenerPartidoSeleccionado();

        if (partido == null) {
            return;
        }

        try {
            partido.iniciarPartido();
            cargarPartidos();

            JOptionPane.showMessageDialog(
                    this,
                    "El partido fue iniciado correctamente."
            );

        } catch (IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void finalizarPartido() {
        Partido partido = obtenerPartidoSeleccionado();

        if (partido == null) {
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea finalizar el partido seleccionado?",
                "Confirmar finalización",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            partido.finalizarPartido();
            cargarPartidos();

            JOptionPane.showMessageDialog(
                    this,
                    "El partido fue finalizado."
            );

        } catch (IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void abrirReporte() {
        Partido partido = obtenerPartidoSeleccionado();

        if (partido == null) {
            return;
        }

        if (partido.getEstado() != EstadoPartido.EN_JUEGO) {
            mostrarError(
                    "El partido debe estar EN_JUEGO para reportar eventos."
            );
            return;
        }

        FrmReportarEvento ventana = new FrmReportarEvento(
                contexto,
                corresponsal,
                partido
        );

        ventana.addWindowListener(
                new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(
                            java.awt.event.WindowEvent e
                    ) {
                        cargarPartidos();
                    }
                }
        );

        ventana.setVisible(true);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
