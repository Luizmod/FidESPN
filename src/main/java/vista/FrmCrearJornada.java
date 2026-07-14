package vista;

import excepciones.DatosInvalidosException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import modelo.Jornada;
import servicio.ContextoAplicacion;

/**
 * Formulario para crear una jornada del torneo.
 *
 * En lugar de casillas de texto libres para día/mes/año, las fechas se
 * capturan con JSpinner + SpinnerDateModel (estilo "date picker"), lo que
 * evita que el usuario digite un día inexistente: cada spinner solo permite
 * moverse dentro de los días reales del mes y año seleccionados (28, 29, 30
 * o 31 días), incluyendo el 29 de febrero en años bisiestos.
 */
public class FrmCrearJornada extends JDialog {

    private final ContextoAplicacion contexto;

    private JTextField txtNombre;
    private JSpinner spnFechaInicio;
    private JSpinner spnFechaFin;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public FrmCrearJornada(JFrame propietario, ContextoAplicacion contexto) {
        super(propietario, "FidESPN - Crear jornada", true);
        this.contexto = contexto;
        inicializarVentana();
        construirInterfaz();
    }

    private void inicializarVentana() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(460, 380));
        setLocationRelativeTo(getOwner());
        setResizable(false);
    }

    private void construirInterfaz() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(new Color(245, 247, 250));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(new Color(15, 39, 71));
        encabezado.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        JLabel lblTitulo = new JLabel("Crear jornada");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        encabezado.add(lblTitulo, BorderLayout.WEST);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        formulario.setBorder(BorderFactory.createEmptyBorder(24, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(6, 6, 6, 6);

        JLabel lblNombre = new JLabel("Nombre de la jornada");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtNombre.setPreferredSize(new Dimension(320, 36));

        JLabel lblInicio = new JLabel("Fecha de inicio");
        lblInicio.setFont(new Font("SansSerif", Font.BOLD, 13));

        spnFechaInicio = crearSpinnerFecha();

        JLabel lblFin = new JLabel("Fecha de fin");
        lblFin.setFont(new Font("SansSerif", Font.BOLD, 13));

        spnFechaFin = crearSpinnerFecha();
        // Por defecto, la jornada dura 7 días a partir de hoy.
        Calendar calendarioFin = Calendar.getInstance();
        calendarioFin.add(Calendar.DAY_OF_MONTH, 7);
        spnFechaFin.setValue(calendarioFin.getTime());

        JLabel lblAyuda = new JLabel(
                "<html><i>Use las flechas del selector para cambiar día, "
                + "mes o año. El calendario solo permite fechas "
                + "válidas (respeta meses de 28, 29, 30 y 31 días).</i></html>"
        );
        lblAyuda.setForeground(new Color(95, 105, 120));
        lblAyuda.setFont(new Font("SansSerif", Font.PLAIN, 11));

        gbc.gridy = 0;
        formulario.add(lblNombre, gbc);
        gbc.gridy = 1;
        formulario.add(txtNombre, gbc);
        gbc.gridy = 2;
        formulario.add(lblInicio, gbc);
        gbc.gridy = 3;
        formulario.add(spnFechaInicio, gbc);
        gbc.gridy = 4;
        formulario.add(lblFin, gbc);
        gbc.gridy = 5;
        formulario.add(spnFechaFin, gbc);
        gbc.gridy = 6;
        gbc.insets = new Insets(4, 6, 6, 6);
        formulario.add(lblAyuda, gbc);

        JPanel botones = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));
        botones.setOpaque(false);
        botones.setBorder(BorderFactory.createEmptyBorder(0, 20, 18, 20));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnCancelar.addActionListener(e -> dispose());

        btnGuardar = new JButton("Guardar jornada");
        btnGuardar.setBackground(new Color(26, 99, 178));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnGuardar.setPreferredSize(new Dimension(160, 38));
        btnGuardar.addActionListener(e -> guardarJornada());

        botones.add(btnCancelar);
        botones.add(btnGuardar);

        principal.add(encabezado, BorderLayout.NORTH);
        principal.add(formulario, BorderLayout.CENTER);
        principal.add(botones, BorderLayout.SOUTH);

        setContentPane(principal);
        pack();
    }

    /**
     * Crea un JSpinner configurado como selector de fecha (día/mes/año).
     * Al usar SpinnerDateModel + JSpinner.DateEditor, el propio componente
     * calcula cuántos días tiene el mes visible (28/29/30/31) y evita que
     * el usuario pueda seleccionar un día que no existe.
     */
    private JSpinner crearSpinnerFecha() {
        SpinnerDateModel modelo = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(modelo);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        spinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        spinner.setPreferredSize(new Dimension(320, 36));
        editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
        return spinner;
    }

    private void guardarJornada() {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El nombre de la jornada es obligatorio.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            txtNombre.requestFocus();
            return;
        }

        LocalDate fechaInicio = convertirALocalDate((Date) spnFechaInicio.getValue());
        LocalDate fechaFin = convertirALocalDate((Date) spnFechaFin.getValue());

        try {
            int nuevoId = contexto.getGestorJornadas().obtenerCantidadJornadas() + 1;
            Jornada jornada = new Jornada(nuevoId, nombre, fechaInicio, fechaFin);

            contexto.getGestorJornadas().registrarJornada(jornada);

            JOptionPane.showMessageDialog(
                    this,
                    "Jornada «" + nombre + "» creada correctamente.",
                    "Jornada creada",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();

        } catch (IllegalArgumentException | DatosInvalidosException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "No se pudo crear la jornada",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private LocalDate convertirALocalDate(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}