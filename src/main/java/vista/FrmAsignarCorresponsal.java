package vista;

import excepciones.CorresponsalNoDisponibleException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import modelo.Corresponsal;
import modelo.Partido;
import modelo.Usuario;
import servicio.ContextoAplicacion;

public class FrmAsignarCorresponsal extends JFrame {

    private static final String[] COLUMNAS_TABLA = {
        "ID",
        "Partido",
        "Corresponsal",
        "Estado"
    };

    private final ContextoAplicacion contexto;

    private JComboBox<Partido> cmbPartido;
    private JComboBox<Corresponsal> cmbCorresponsal;
    private DefaultTableModel modeloTabla;
    private JTable tablaPartidos;

    public FrmAsignarCorresponsal(ContextoAplicacion contexto) {
        this.contexto = contexto;

        inicializarVentana();
        construirInterfaz();
        cargarDatos();
    }

    private void inicializarVentana() {
        setTitle("FidESPN - Asignar corresponsal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(950, 560));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent evento) {
                cargarDatos();
            }
        });
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = crearPanelPrincipal();
        JPanel encabezado = crearEncabezado();
        JPanel contenido = crearContenido();

        panelPrincipal.add(encabezado, BorderLayout.NORTH);
        panelPrincipal.add(contenido, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        pack();
    }

    private JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PaletaColores.FONDO_VENTANA);
        return panel;
    }

    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(PaletaColores.VERDE_OSCURO);
        encabezado.setBorder(
                BorderFactory.createEmptyBorder(18, 25, 18, 25)
        );

        JLabel lblMarca = new JLabel("FidESPN");
        lblMarca.setForeground(PaletaColores.BLANCO);
        lblMarca.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel lblModulo = new JLabel("Administración de corresponsales");
        lblModulo.setForeground(PaletaColores.BLANCO);
        lblModulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        encabezado.add(lblMarca, BorderLayout.WEST);
        encabezado.add(lblModulo, BorderLayout.EAST);

        return encabezado;
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new BorderLayout(15, 15));
        contenido.setOpaque(false);
        contenido.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 30, 30)
        );

        JLabel lblTitulo = new JLabel("Asignación de corresponsales");
        lblTitulo.setForeground(PaletaColores.VERDE_OSCURO);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel panelFormulario = crearPanelFormulario();
        JScrollPane scrollTabla = crearTabla();

        JPanel centro = new JPanel(new BorderLayout(15, 15));
        centro.setOpaque(false);
        centro.add(panelFormulario, BorderLayout.WEST);
        centro.add(scrollTabla, BorderLayout.CENTER);

        contenido.add(lblTitulo, BorderLayout.NORTH);
        contenido.add(centro, BorderLayout.CENTER);

        return contenido;
    }

    private JPanel crearPanelFormulario() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(PaletaColores.BLANCO);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        PaletaColores.BORDE_TARJETA
                ),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));

        cmbPartido = new JComboBox<>();
        cmbCorresponsal = new JComboBox<>();

        JButton btnAsignar = crearBotonPrimario(
                "Asignar corresponsal"
        );
        btnAsignar.addActionListener(
                evento -> asignarCorresponsal()
        );

        JButton btnActualizar = crearBotonSecundario(
                "Actualizar listas"
        );
        btnActualizar.addActionListener(
                evento -> cargarDatos()
        );

        GridBagConstraints gbc = crearRestricciones();

        agregarCampo(
                formulario,
                gbc,
                0,
                "Partido:",
                cmbPartido
        );

        agregarCampo(
                formulario,
                gbc,
                1,
                "Corresponsal:",
                cmbCorresponsal
        );

        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.add(btnAsignar);
        panelBotones.add(btnActualizar);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(18, 6, 6, 6);
        formulario.add(panelBotones, gbc);

        return formulario;
    }

    private JScrollPane crearTabla() {
        modeloTabla = new DefaultTableModel(
                COLUMNAS_TABLA,
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna
            ) {
                return false;
            }
        };

        tablaPartidos = new JTable(modeloTabla);
        tablaPartidos.setRowHeight(28);
        tablaPartidos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
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
                BorderFactory.createLineBorder(
                        PaletaColores.BORDE_TARJETA
                )
        );

        return scroll;
    }

    private GridBagConstraints crearRestricciones() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private void agregarCampo(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String textoEtiqueta,
            java.awt.Component campo
    ) {
        JLabel etiqueta = new JLabel(textoEtiqueta);
        etiqueta.setForeground(PaletaColores.NEGRO);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 13));

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(etiqueta, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private JButton crearBotonPrimario(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(PaletaColores.VERDE_CESPED);
        boton.setForeground(PaletaColores.BLANCO);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setPreferredSize(new Dimension(175, 38));
        return boton;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(PaletaColores.VERDE_MEDIO);
        boton.setForeground(PaletaColores.BLANCO);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setPreferredSize(new Dimension(150, 38));
        return boton;
    }

    private void cargarDatos() {
        cargarPartidos();
        cargarCorresponsales();
        cargarTabla();
    }

    private void cargarPartidos() {
        cmbPartido.removeAllItems();

        for (Partido partido
                : contexto.getGestorPartidos().listarPartidos()) {
            cmbPartido.addItem(partido);
        }
    }

    private void cargarCorresponsales() {
        cmbCorresponsal.removeAllItems();

        for (Usuario usuario
                : contexto.getGestorUsuarios().listarUsuarios()) {

            if (usuario instanceof Corresponsal corresponsal
                    && corresponsal.isActivo()) {
                cmbCorresponsal.addItem(corresponsal);
            }
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);

        for (Partido partido
                : contexto.getGestorPartidos().listarPartidos()) {

            modeloTabla.addRow(new Object[]{
                partido.getIdPartido(),
                obtenerNombrePartido(partido),
                obtenerNombreCorresponsal(partido),
                partido.getEstado()
            });
        }
    }

    private String obtenerNombrePartido(Partido partido) {
        return partido.getEquipoLocal().getNombre()
                + " vs "
                + partido.getEquipoVisitante().getNombre();
    }

    private String obtenerNombreCorresponsal(Partido partido) {
        Corresponsal corresponsal = partido.getCorresponsal();

        if (corresponsal == null) {
            return "Sin asignar";
        }

        return corresponsal.getNombre()
                + " "
                + corresponsal.getApellido();
    }

    private void asignarCorresponsal() {
        Partido partido =
                (Partido) cmbPartido.getSelectedItem();

        Corresponsal corresponsal =
                (Corresponsal) cmbCorresponsal.getSelectedItem();

        try {
            validarSeleccion(partido, corresponsal);

            contexto.getGestorPartidos().asignarCorresponsal(
                    partido.getIdPartido(),
                    corresponsal
            );

            cargarTabla();

            JOptionPane.showMessageDialog(
                    this,
                    "Corresponsal asignado correctamente.",
                    "Asignación completada",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (CorresponsalNoDisponibleException
                | IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void validarSeleccion(
            Partido partido,
            Corresponsal corresponsal
    ) {
        if (partido == null) {
            throw new IllegalArgumentException(
                    "Debe crear y seleccionar un partido."
            );
        }

        if (corresponsal == null) {
            throw new IllegalArgumentException(
                    "Debe existir y seleccionar un corresponsal activo."
            );
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
