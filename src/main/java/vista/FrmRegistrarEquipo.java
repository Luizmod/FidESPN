package vista;

import excepciones.EquipoDuplicadoException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.SwingConstants;
import modelo.Equipo;
import servicio.ContextoAplicacion;

public class FrmRegistrarEquipo extends JFrame {

    private static final String[] GRUPOS_MUNDIAL = {
        "A", "B", "C", "D", "E", "F", "G", "H"
    };

    private final ContextoAplicacion contexto;

    private JTextField txtPais;
    private JTextField txtEscudo;
    private JComboBox<String> cmbGrupo;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public FrmRegistrarEquipo(ContextoAplicacion contexto) {
        this.contexto = contexto;
        inicializarVentana();
        construirInterfaz();
    }

    private void inicializarVentana() {
        setTitle("FidESPN - Registrar equipo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(560, 520));
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(PaletaColores.FONDO_VENTANA);

        principal.add(construirEncabezado(), BorderLayout.NORTH);
        principal.add(construirFormulario(), BorderLayout.CENTER);

        setContentPane(principal);
        pack();
    }

    private JPanel construirEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(PaletaColores.VERDE_OSCURO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel lblTitulo = new JLabel("Registrar equipo");
        lblTitulo.setForeground(PaletaColores.BLANCO);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel lblSubtitulo = new JLabel("Catálogo de selecciones del torneo");
        lblSubtitulo.setForeground(new Color(0xD4, 0xE8, 0xD0));
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel textos = new JPanel(new java.awt.GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(lblTitulo);
        textos.add(lblSubtitulo);

        encabezado.add(textos, BorderLayout.WEST);
        return encabezado;
    }

    private JPanel construirFormulario() {
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
        tarjeta.add(crearEtiqueta("Selección nacional (país)"), gbc);

        gbc.gridy = fila++;
        txtPais = crearCampoTexto();
        tarjeta.add(txtPais, gbc);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Grupo del mundial"), gbc);

        gbc.gridy = fila++;
        cmbGrupo = new JComboBox<>(GRUPOS_MUNDIAL);
        cmbGrupo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbGrupo.setPreferredSize(new Dimension(320, 38));
        tarjeta.add(cmbGrupo, gbc);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("URL del escudo (opcional)"), gbc);

        gbc.gridy = fila++;
        txtEscudo = crearCampoTexto();
        tarjeta.add(txtEscudo, gbc);

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
        JPanel panel = new JPanel(new java.awt.GridLayout(1, 2, 12, 0));
        panel.setOpaque(false);

        btnGuardar = new JButton("Guardar equipo");
        btnGuardar.setBackground(PaletaColores.VERDE_CESPED);
        btnGuardar.setForeground(PaletaColores.BLANCO);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setPreferredSize(new Dimension(150, 42));
        btnGuardar.addActionListener(e -> guardarEquipo());

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

    private void guardarEquipo() {
        String pais = txtPais.getText().trim();
        String escudo = txtEscudo.getText().trim();
        String grupo = (String) cmbGrupo.getSelectedItem();

        if (pais.isEmpty()) {
            mostrarError("La selección nacional es obligatoria.");
            return;
        }

        try {
            int nuevoId = generarSiguienteId();
            // En un mundial el nombre del equipo y el país son el mismo dato.
            Equipo equipo = new Equipo(nuevoId, pais, pais, escudo, grupo);
            contexto.getGestorEquipos().registrarEquipo(equipo);

            JOptionPane.showMessageDialog(
                    this,
                    "La selección \"" + pais + "\" se registró correctamente.",
                    "Equipo registrado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

        } catch (EquipoDuplicadoException ex) {
            mostrarError(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private int generarSiguienteId() {
        List<Equipo> equipos = contexto.getGestorEquipos().listarEquipos();
        return equipos.stream()
                .mapToInt(Equipo::getIdEquipo)
                .max()
                .orElse(0) + 1;
    }

    private void limpiarFormulario() {
        txtPais.setText("");
        txtEscudo.setText("");
        cmbGrupo.setSelectedIndex(0);
        txtPais.requestFocus();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "No se pudo registrar el equipo",
                JOptionPane.ERROR_MESSAGE
        );
    }
}