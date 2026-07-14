package vista;

import excepciones.NumeroCamisetaDuplicadoException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import modelo.Equipo;
import modelo.Jugador;
import modelo.Posicion;
import servicio.ContextoAplicacion;

public class FrmRegistrarJugador extends JFrame {

    private final ContextoAplicacion contexto;

    private JComboBox<Equipo> cmbEquipo;
    private JTextField txtNombre;
    private JComboBox<Posicion> cmbPosicion;
    private JTextField txtNumeroCamiseta;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public FrmRegistrarJugador(ContextoAplicacion contexto) {
        this.contexto = contexto;
        inicializarVentana();
        construirInterfaz();
    }

    private void inicializarVentana() {
        setTitle("FidESPN - Registrar jugador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(560, 560));
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(PaletaColores.FONDO_VENTANA);

        principal.add(construirEncabezado(), BorderLayout.NORTH);

        List<Equipo> equipos = contexto.getGestorEquipos().listarEquipos();
        if (equipos.isEmpty()) {
            principal.add(construirMensajeSinEquipos(), BorderLayout.CENTER);
        } else {
            principal.add(construirFormulario(equipos), BorderLayout.CENTER);
        }

        setContentPane(principal);
        pack();
    }

    private JPanel construirEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(PaletaColores.VERDE_OSCURO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel lblTitulo = new JLabel("Registrar jugador");
        lblTitulo.setForeground(PaletaColores.BLANCO);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel lblSubtitulo = new JLabel("Plantilla de jugadores por equipo");
        lblSubtitulo.setForeground(new Color(0xD4, 0xE8, 0xD0));
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(lblTitulo);
        textos.add(lblSubtitulo);

        encabezado.add(textos, BorderLayout.WEST);
        return encabezado;
    }

    private JPanel construirMensajeSinEquipos() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JLabel lbl = new JLabel(
                "<html><center>Debes registrar al menos un equipo<br>"
                + "antes de poder agregar jugadores.</center></html>"
        );
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lbl.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        lbl.setHorizontalAlignment(JLabel.CENTER);

        panel.add(lbl);
        return panel;
    }

    private JPanel construirFormulario(List<Equipo> equipos) {
        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(PaletaColores.BLANCO);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PaletaColores.BORDE_TARJETA),
                BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 8, 8, 8);

        int fila = 0;

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Equipo"), gbc);

        gbc.gridy = fila++;
        cmbEquipo = new JComboBox<>(equipos.toArray(new Equipo[0]));
        cmbEquipo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbEquipo.setPreferredSize(new Dimension(320, 38));
        tarjeta.add(cmbEquipo, gbc);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Nombre completo"), gbc);

        gbc.gridy = fila++;
        txtNombre = crearCampoTexto();
        tarjeta.add(txtNombre, gbc);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Posición"), gbc);

        gbc.gridy = fila++;
        cmbPosicion = new JComboBox<>(Posicion.values());
        cmbPosicion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbPosicion.setPreferredSize(new Dimension(320, 38));
        tarjeta.add(cmbPosicion, gbc);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Número de camiseta (1-99)"), gbc);

        gbc.gridy = fila++;
        txtNumeroCamiseta = crearCampoTexto();
        tarjeta.add(txtNumeroCamiseta, gbc);

        gbc.gridy = fila++;
        gbc.insets = new Insets(22, 8, 8, 8);
        tarjeta.add(construirPanelBotones(), gbc);

        contenedor.add(tarjeta);
        return contenedor;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 13));
        etiqueta.setForeground(PaletaColores.NEGRO);
        return etiqueta;
    }

    private JTextField crearCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setPreferredSize(new Dimension(320, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PaletaColores.BORDE_TARJETA),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        return campo;
    }

    private JPanel construirPanelBotones() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 0));
        panel.setOpaque(false);

        btnGuardar = new JButton("Guardar jugador");
        btnGuardar.setBackground(PaletaColores.VERDE_CESPED);
        btnGuardar.setForeground(PaletaColores.BLANCO);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setPreferredSize(new Dimension(150, 42));
        btnGuardar.addActionListener(e -> guardarJugador());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(PaletaColores.BLANCO);
        btnCancelar.setForeground(PaletaColores.NEGRO);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCancelar.setBorder(BorderFactory.createLineBorder(PaletaColores.BORDE_TARJETA));
        btnCancelar.setPreferredSize(new Dimension(150, 42));
        btnCancelar.addActionListener(e -> dispose());

        panel.add(btnGuardar);
        panel.add(btnCancelar);
        return panel;
    }

    private void guardarJugador() {
        Equipo equipoSeleccionado = (Equipo) cmbEquipo.getSelectedItem();
        String nombre = txtNombre.getText().trim();
        Posicion posicion = (Posicion) cmbPosicion.getSelectedItem();
        String textoNumero = txtNumeroCamiseta.getText().trim();

        if (equipoSeleccionado == null) {
            mostrarError("Debes seleccionar un equipo.");
            return;
        }

        if (nombre.isEmpty()) {
            mostrarError("El nombre del jugador es obligatorio.");
            return;
        }

        int numeroCamiseta;
        try {
            numeroCamiseta = Integer.parseInt(textoNumero);
        } catch (NumberFormatException ex) {
            mostrarError("El número de camiseta debe ser un valor numérico.");
            return;
        }

        try {
            int nuevoId = generarSiguienteId();
            Jugador jugador = new Jugador(
                    nuevoId, nombre, posicion, numeroCamiseta, equipoSeleccionado, true
            );

            contexto.getGestorEquipos().registrarJugador(
                    equipoSeleccionado.getIdEquipo(), jugador
            );

            JOptionPane.showMessageDialog(
                    this,
                    "El jugador \"" + nombre + "\" se registró en "
                    + equipoSeleccionado.getNombre() + ".",
                    "Jugador registrado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

        } catch (NumeroCamisetaDuplicadoException ex) {
            mostrarError(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private int generarSiguienteId() {
        return contexto.getGestorEquipos().listarEquipos().stream()
                .flatMap(equipo -> equipo.obtenerJugadores().stream())
                .mapToInt(Jugador::getIdJugador)
                .max()
                .orElse(0) + 1;
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtNumeroCamiseta.setText("");
        cmbPosicion.setSelectedIndex(0);
        txtNombre.requestFocus();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "No se pudo registrar el jugador",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
