package vista;

import excepciones.DatosInvalidosException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import modelo.Jornada;
import servicio.ContextoAplicacion;

public class FrmCrearJornada extends JFrame {

    private final ContextoAplicacion contexto;

    private JTextField txtNombre;
    private JSpinner spnFechaInicio;
    private JSpinner spnFechaFin;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public FrmCrearJornada(ContextoAplicacion contexto) {
        this.contexto = contexto;
        inicializarVentana();
        construirInterfaz();
    }

    private void inicializarVentana() {
        setTitle("FidESPN - Crear jornada");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(560, 480));
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

        JLabel lblTitulo = new JLabel("Crear jornada");
        lblTitulo.setForeground(PaletaColores.BLANCO);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel lblSubtitulo = new JLabel("Fases del torneo — grupos, octavos, cuartos...");
        lblSubtitulo.setForeground(new Color(0xD4, 0xE8, 0xD0));
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel textos = new JPanel(new GridLayout(2, 1));
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
        tarjeta.add(crearEtiqueta("Nombre de la jornada"), gbc);

        gbc.gridy = fila++;
        txtNombre = crearCampoTexto();
        txtNombre.setText("Fecha 1 - Fase de Grupos");
        tarjeta.add(txtNombre, gbc);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Fecha de inicio"), gbc);

        gbc.gridy = fila++;
        spnFechaInicio = crearSpinnerFecha();
        tarjeta.add(spnFechaInicio, gbc);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Fecha de fin"), gbc);

        gbc.gridy = fila++;
        spnFechaFin = crearSpinnerFecha();
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DAY_OF_MONTH, 7);
        spnFechaFin.setValue(calendario.getTime());
        tarjeta.add(spnFechaFin, gbc);

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

    private JSpinner crearSpinnerFecha() {
        SpinnerDateModel modelo = new SpinnerDateModel(
                new Date(), null, null, Calendar.DAY_OF_MONTH
        );
        JSpinner spinner = new JSpinner(modelo);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        spinner.setPreferredSize(new Dimension(320, 38));
        return spinner;
    }

    private JPanel construirPanelBotones() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 0));
        panel.setOpaque(false);

        btnGuardar = new JButton("Guardar jornada");
        btnGuardar.setBackground(PaletaColores.VERDE_CESPED);
        btnGuardar.setForeground(PaletaColores.BLANCO);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setPreferredSize(new Dimension(150, 42));
        btnGuardar.addActionListener(e -> guardarJornada());

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

    private void guardarJornada() {
        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            mostrarError("El nombre de la jornada es obligatorio.");
            return;
        }

        LocalDate fechaInicio = convertirALocalDate((Date) spnFechaInicio.getValue());
        LocalDate fechaFin = convertirALocalDate((Date) spnFechaFin.getValue());

        if (fechaFin.isBefore(fechaInicio)) {
            mostrarError("La fecha final no puede ser anterior a la fecha de inicio.");
            return;
        }

        try {
            int nuevoId = generarSiguienteId();
            Jornada jornada = new Jornada(nuevoId, nombre, fechaInicio, fechaFin);

            contexto.getGestorJornadas().registrarJornada(jornada);

            JOptionPane.showMessageDialog(
                    this,
                    "La jornada \"" + nombre + "\" se creó correctamente.",
                    "Jornada creada",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (DatosInvalidosException ex) {
            mostrarError(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private LocalDate convertirALocalDate(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private int generarSiguienteId() {
        List<Jornada> jornadas = contexto.getGestorJornadas().listarJornadas();
        return jornadas.stream()
                .mapToInt(Jornada::getIdJornada)
                .max()
                .orElse(0) + 1;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "No se pudo crear la jornada",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
