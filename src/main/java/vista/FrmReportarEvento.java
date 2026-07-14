package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import modelo.CategoriaSituacion;
import modelo.Corresponsal;
import modelo.Equipo;
import modelo.EventoPartido;
import modelo.Jugador;
import modelo.Partido;
import modelo.TipoTarjeta;
import servicio.ContextoAplicacion;

public class FrmReportarEvento extends JFrame {

    private static final AtomicInteger SECUENCIA_EVENTO =
            new AtomicInteger(1000);

    private final ContextoAplicacion contexto;
    private final Corresponsal corresponsal;
    private final Partido partido;

    private JLabel lblMarcador;
    private DefaultTableModel modeloEventos;

    private JComboBox<Equipo> cmbEquipoGol;
    private JComboBox<Jugador> cmbJugadorGol;
    private JComboBox<Jugador> cmbAsistente;
    private JSpinner spnMinutoGol;
    private JCheckBox chkAutogol;

    private JComboBox<Equipo> cmbEquipoTarjeta;
    private JComboBox<Jugador> cmbJugadorTarjeta;
    private JComboBox<TipoTarjeta> cmbTipoTarjeta;
    private JSpinner spnMinutoTarjeta;
    private JTextArea txtMotivo;

    private JComboBox<CategoriaSituacion> cmbCategoria;
    private JSpinner spnMinutoSituacion;
    private JTextArea txtDescripcion;

    public FrmReportarEvento(
            ContextoAplicacion contexto,
            Corresponsal corresponsal,
            Partido partido
    ) {
        this.contexto = contexto;
        this.corresponsal = corresponsal;
        this.partido = partido;

        setTitle("FidESPN - Reportar evento");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);

        construirInterfaz();
        cargarEquiposYJugadores();
        cargarEventos();
    }

    private void construirInterfaz() {
        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBackground(PaletaColores.FONDO_VENTANA);
        principal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(PaletaColores.VERDE_OSCURO);
        encabezado.setBorder(
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        );

        JLabel lblPartido = new JLabel(
                partido.getEquipoLocal().getNombre()
                + " vs "
                + partido.getEquipoVisitante().getNombre()
        );
        lblPartido.setForeground(PaletaColores.BLANCO);
        lblPartido.setFont(new Font("SansSerif", Font.BOLD, 22));

        lblMarcador = new JLabel(partido.obtenerMarcador());
        lblMarcador.setForeground(PaletaColores.BLANCO);
        lblMarcador.setFont(new Font("SansSerif", Font.BOLD, 28));

        encabezado.add(lblPartido, BorderLayout.WEST);
        encabezado.add(lblMarcador, BorderLayout.EAST);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setBackground(PaletaColores.BLANCO);
        pestanas.setForeground(PaletaColores.VERDE_OSCURO);
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 13));
        pestanas.addTab("Gol", crearPanelGol());
        pestanas.addTab("Tarjeta", crearPanelTarjeta());
        pestanas.addTab(
                "Situación relevante",
                crearPanelSituacion()
        );

        modeloEventos = new DefaultTableModel(
                new Object[]{"Minuto", "Tipo", "Descripción"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaEventos = new JTable(modeloEventos);
        tablaEventos.setRowHeight(26);
        tablaEventos.setBackground(PaletaColores.BLANCO);
        tablaEventos.setForeground(PaletaColores.NEGRO);
        tablaEventos.setGridColor(PaletaColores.BORDE_TARJETA);
        tablaEventos.getTableHeader()
                .setBackground(PaletaColores.VERDE_OSCURO);
        tablaEventos.getTableHeader()
                .setForeground(PaletaColores.BLANCO);
        tablaEventos.getTableHeader()
                .setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scrollEventos = new JScrollPane(tablaEventos);
        scrollEventos.setBorder(
                BorderFactory.createLineBorder(
                        PaletaColores.BORDE_TARJETA
                )
        );

        JPanel centro = new JPanel(new GridLayout(1, 2, 15, 15));
        centro.setOpaque(false);
        centro.add(pestanas);
        centro.add(scrollEventos);

        JButton btnCerrar = crearBoton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        JPanel inferior = new JPanel(new BorderLayout());
        inferior.setOpaque(false);
        inferior.add(btnCerrar, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);
        principal.add(centro, BorderLayout.CENTER);
        principal.add(inferior, BorderLayout.SOUTH);

        setContentPane(principal);
        pack();
    }

    private JPanel crearPanelGol() {
        JPanel panel = crearPanelFormulario();

        cmbEquipoGol = new JComboBox<>();
        cmbJugadorGol = new JComboBox<>();
        cmbAsistente = new JComboBox<>();
        spnMinutoGol = crearSpinnerMinuto();
        chkAutogol = new JCheckBox("Autogol");
        chkAutogol.setOpaque(false);
        chkAutogol.setForeground(PaletaColores.NEGRO);

        cmbEquipoGol.addActionListener(
                e -> cargarJugadoresGol()
        );

        GridBagConstraints gbc = crearGbc();

        agregarCampo(panel, gbc, 0, "Equipo:", cmbEquipoGol);
        agregarCampo(panel, gbc, 1, "Jugador:", cmbJugadorGol);
        agregarCampo(panel, gbc, 2, "Asistente:", cmbAsistente);
        agregarCampo(panel, gbc, 3, "Minuto:", spnMinutoGol);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(chkAutogol, gbc);

        JButton btnRegistrar = crearBoton("Registrar gol");
        btnRegistrar.addActionListener(e -> registrarGol());

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnRegistrar, gbc);

        return panel;
    }

    private JPanel crearPanelTarjeta() {
        JPanel panel = crearPanelFormulario();

        cmbEquipoTarjeta = new JComboBox<>();
        cmbJugadorTarjeta = new JComboBox<>();
        cmbTipoTarjeta = new JComboBox<>(TipoTarjeta.values());
        spnMinutoTarjeta = crearSpinnerMinuto();
        txtMotivo = crearAreaTexto(4, 20);

        cmbEquipoTarjeta.addActionListener(
                e -> cargarJugadoresTarjeta()
        );

        GridBagConstraints gbc = crearGbc();

        agregarCampo(panel, gbc, 0, "Equipo:", cmbEquipoTarjeta);
        agregarCampo(panel, gbc, 1, "Jugador:", cmbJugadorTarjeta);
        agregarCampo(panel, gbc, 2, "Tarjeta:", cmbTipoTarjeta);
        agregarCampo(panel, gbc, 3, "Minuto:", spnMinutoTarjeta);
        agregarCampo(
                panel,
                gbc,
                4,
                "Motivo:",
                new JScrollPane(txtMotivo)
        );

        JButton btnRegistrar = crearBoton("Registrar tarjeta");
        btnRegistrar.addActionListener(e -> registrarTarjeta());

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnRegistrar, gbc);

        return panel;
    }

    private JPanel crearPanelSituacion() {
        JPanel panel = crearPanelFormulario();

        cmbCategoria = new JComboBox<>(
                CategoriaSituacion.values()
        );
        spnMinutoSituacion = crearSpinnerMinuto();
        txtDescripcion = crearAreaTexto(7, 22);

        GridBagConstraints gbc = crearGbc();

        agregarCampo(panel, gbc, 0, "Categoría:", cmbCategoria);
        agregarCampo(panel, gbc, 1, "Minuto:", spnMinutoSituacion);
        agregarCampo(
                panel,
                gbc,
                2,
                "Descripción:",
                new JScrollPane(txtDescripcion)
        );

        JButton btnRegistrar = crearBoton("Registrar situación");
        btnRegistrar.addActionListener(e -> registrarSituacion());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnRegistrar, gbc);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PaletaColores.BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        PaletaColores.BORDE_TARJETA
                ),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(PaletaColores.VERDE_CESPED);
        boton.setForeground(PaletaColores.BLANCO);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        return boton;
    }

    private JTextArea crearAreaTexto(int filas, int columnas) {
        JTextArea area = new JTextArea(filas, columnas);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setForeground(PaletaColores.NEGRO);
        area.setBorder(
                BorderFactory.createLineBorder(
                        PaletaColores.BORDE_TARJETA
                )
        );
        return area;
    }

    private GridBagConstraints crearGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private JSpinner crearSpinnerMinuto() {
        return new JSpinner(
                new SpinnerNumberModel(1, 0, 130, 1)
        );
    }

    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String etiqueta,
            java.awt.Component campo
    ) {
        JLabel lblCampo = new JLabel(etiqueta);
        lblCampo.setForeground(PaletaColores.NEGRO);
        lblCampo.setFont(new Font("SansSerif", Font.BOLD, 13));

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(lblCampo, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campo, gbc);
    }

    private void cargarEquiposYJugadores() {
        cmbEquipoGol.removeAllItems();
        cmbEquipoTarjeta.removeAllItems();

        cmbEquipoGol.addItem(partido.getEquipoLocal());
        cmbEquipoGol.addItem(partido.getEquipoVisitante());

        cmbEquipoTarjeta.addItem(partido.getEquipoLocal());
        cmbEquipoTarjeta.addItem(partido.getEquipoVisitante());

        cargarJugadoresGol();
        cargarJugadoresTarjeta();
    }

    private void cargarJugadoresGol() {
        cmbJugadorGol.removeAllItems();
        cmbAsistente.removeAllItems();
        cmbAsistente.addItem(null);

        Equipo equipo = (Equipo) cmbEquipoGol.getSelectedItem();

        if (equipo == null) {
            return;
        }

        for (Jugador jugador : equipo.obtenerJugadores()) {
            if (jugador.isActivo()) {
                cmbJugadorGol.addItem(jugador);
                cmbAsistente.addItem(jugador);
            }
        }
    }

    private void cargarJugadoresTarjeta() {
        cmbJugadorTarjeta.removeAllItems();

        Equipo equipo =
                (Equipo) cmbEquipoTarjeta.getSelectedItem();

        if (equipo == null) {
            return;
        }

        for (Jugador jugador : equipo.obtenerJugadores()) {
            if (jugador.isActivo()) {
                cmbJugadorTarjeta.addItem(jugador);
            }
        }
    }

    private void registrarGol() {
        try {
            Equipo equipo =
                    (Equipo) cmbEquipoGol.getSelectedItem();
            Jugador jugador =
                    (Jugador) cmbJugadorGol.getSelectedItem();
            Jugador asistente =
                    (Jugador) cmbAsistente.getSelectedItem();

            if (equipo == null || jugador == null) {
                throw new IllegalArgumentException(
                        "Seleccione un equipo y un jugador."
                );
            }

            corresponsal.reportarGol(
                    SECUENCIA_EVENTO.incrementAndGet(),
                    partido,
                    (int) spnMinutoGol.getValue(),
                    jugador,
                    asistente,
                    equipo,
                    chkAutogol.isSelected()
            );

            actualizarVista();

            JOptionPane.showMessageDialog(
                    this,
                    "Gol registrado correctamente."
            );

        } catch (IllegalArgumentException
                | IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void registrarTarjeta() {
        try {
            Equipo equipo =
                    (Equipo) cmbEquipoTarjeta.getSelectedItem();
            Jugador jugador =
                    (Jugador) cmbJugadorTarjeta.getSelectedItem();

            if (equipo == null || jugador == null) {
                throw new IllegalArgumentException(
                        "Seleccione un equipo y un jugador."
                );
            }

            corresponsal.reportarTarjeta(
                    SECUENCIA_EVENTO.incrementAndGet(),
                    partido,
                    (int) spnMinutoTarjeta.getValue(),
                    jugador,
                    equipo,
                    (TipoTarjeta) cmbTipoTarjeta.getSelectedItem(),
                    txtMotivo.getText().trim()
            );

            txtMotivo.setText("");
            actualizarVista();

            JOptionPane.showMessageDialog(
                    this,
                    "Tarjeta registrada correctamente."
            );

        } catch (IllegalArgumentException
                | IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void registrarSituacion() {
        try {
            corresponsal.reportarSituacion(
                    SECUENCIA_EVENTO.incrementAndGet(),
                    partido,
                    (int) spnMinutoSituacion.getValue(),
                    txtDescripcion.getText().trim(),
                    (CategoriaSituacion)
                            cmbCategoria.getSelectedItem()
            );

            txtDescripcion.setText("");
            actualizarVista();

            JOptionPane.showMessageDialog(
                    this,
                    "Situación registrada correctamente."
            );

        } catch (IllegalArgumentException
                | IllegalStateException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void actualizarVista() {
        lblMarcador.setText(partido.obtenerMarcador());
        cargarEventos();
    }

    private void cargarEventos() {
        modeloEventos.setRowCount(0);

        List<EventoPartido> eventos =
                partido.obtenerEventos();

        for (EventoPartido evento : eventos) {
            modeloEventos.addRow(new Object[]{
                evento.getMinuto(),
                evento.getTipoEvento(),
                evento.obtenerDescripcion()
            });
        }
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
