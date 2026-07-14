package vista;

import excepciones.PartidoInvalidoException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import modelo.Equipo;
import modelo.Jornada;
import modelo.Partido;
import servicio.ContextoAplicacion;

public class FrmCrearPartido extends JFrame {

    private final ContextoAplicacion contexto;

    private JComboBox<Equipo> cmbEquipoLocal;
    private JComboBox<Equipo> cmbEquipoVisitante;
    private JComboBox<Jornada> cmbJornada;
    private JSpinner spnFecha;
    private JSpinner spnHora;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public FrmCrearPartido(ContextoAplicacion contexto) {
        this.contexto = contexto;
        inicializarVentana();
        construirInterfaz();
    }

    private void inicializarVentana() {
        setTitle("FidESPN - Crear partido");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(580, 620));
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(PaletaColores.FONDO_VENTANA);

        principal.add(construirEncabezado(), BorderLayout.NORTH);

        List<Equipo> equipos = contexto.getGestorEquipos().listarEquipos();
        List<Jornada> jornadas = contexto.getGestorJornadas().listarJornadas();

        if (equipos.size() < 2 || jornadas.isEmpty()) {
            principal.add(construirMensajeDatosFaltantes(equipos.size(), jornadas.size()), BorderLayout.CENTER);
        } else {
            principal.add(construirFormulario(equipos, jornadas), BorderLayout.CENTER);
        }

        setContentPane(principal);
        pack();
    }

    private JPanel construirEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(PaletaColores.VERDE_OSCURO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel lblTitulo = new JLabel("Crear partido");
        lblTitulo.setForeground(PaletaColores.BLANCO);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel lblSubtitulo = new JLabel("Programación de encuentros del mundial");
        lblSubtitulo.setForeground(new Color(0xD4, 0xE8, 0xD0));
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(lblTitulo);
        textos.add(lblSubtitulo);

        encabezado.add(textos, BorderLayout.WEST);
        return encabezado;
    }

    private JPanel construirMensajeDatosFaltantes(int cantidadEquipos, int cantidadJornadas) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        StringBuilder mensaje = new StringBuilder("<html><center>Para crear un partido necesitas:<br>");
        if (cantidadEquipos < 2) {
            mensaje.append("• Al menos 2 selecciones registradas<br>");
        }
        if (cantidadJornadas == 0) {
            mensaje.append("• Al menos 1 jornada creada<br>");
        }
        mensaje.append("</center></html>");

        JLabel lbl = new JLabel(mensaje.toString());
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lbl.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        lbl.setHorizontalAlignment(JLabel.CENTER);

        panel.add(lbl);
        return panel;
    }

    private JPanel construirFormulario(List<Equipo> equipos, List<Jornada> jornadas) {
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

        Equipo[] arregloEquipos = equipos.toArray(new Equipo[0]);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Equipo local"), gbc);

        gbc.gridy = fila++;
        cmbEquipoLocal = new JComboBox<>(arregloEquipos);
        estilizarCombo(cmbEquipoLocal);
        tarjeta.add(cmbEquipoLocal, gbc);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Equipo visitante"), gbc);

        gbc.gridy = fila++;
        cmbEquipoVisitante = new JComboBox<>(arregloEquipos);
        estilizarCombo(cmbEquipoVisitante);
        if (arregloEquipos.length > 1) {
            cmbEquipoVisitante.setSelectedIndex(1);
        }
        tarjeta.add(cmbEquipoVisitante, gbc);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Jornada"), gbc);

        gbc.gridy = fila++;
        cmbJornada = new JComboBox<>(jornadas.toArray(new Jornada[0]));
        estilizarCombo(cmbJornada);
        tarjeta.add(cmbJornada, gbc);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Fecha del partido"), gbc);

        gbc.gridy = fila++;
        spnFecha = crearSpinnerFecha();
        tarjeta.add(spnFecha, gbc);

        gbc.gridy = fila++;
        tarjeta.add(crearEtiqueta("Hora del partido"), gbc);

        gbc.gridy = fila++;
        spnHora = crearSpinnerHora();
        tarjeta.add(spnHora, gbc);

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

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(320, 38));
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

    private JSpinner crearSpinnerHora() {
        Calendar horaInicial = Calendar.getInstance();
        horaInicial.set(Calendar.HOUR_OF_DAY, 18);
        horaInicial.set(Calendar.MINUTE, 0);

        SpinnerDateModel modelo = new SpinnerDateModel(
                horaInicial.getTime(), null, null, Calendar.MINUTE
        );
        JSpinner spinner = new JSpinner(modelo);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
        spinner.setFont(new Font("SansSerif", Font.PLAIN, 14));
        spinner.setPreferredSize(new Dimension(320, 38));
        return spinner;
    }

    private JPanel construirPanelBotones() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 0));
        panel.setOpaque(false);

        btnGuardar = new JButton("Guardar partido");
        btnGuardar.setBackground(PaletaColores.VERDE_CESPED);
        btnGuardar.setForeground(PaletaColores.BLANCO);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setPreferredSize(new Dimension(150, 42));
        btnGuardar.addActionListener(e -> guardarPartido());

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

    private void guardarPartido() {
        Equipo equipoLocal = (Equipo) cmbEquipoLocal.getSelectedItem();
        Equipo equipoVisitante = (Equipo) cmbEquipoVisitante.getSelectedItem();
        Jornada jornada = (Jornada) cmbJornada.getSelectedItem();

        if (equipoLocal == null || equipoVisitante == null || jornada == null) {
            mostrarError("Debes seleccionar ambos equipos y una jornada.");
            return;
        }

        if (equipoLocal.equals(equipoVisitante)) {
            mostrarError("El equipo local y el visitante deben ser diferentes.");
            return;
        }

        LocalDate fecha = convertirALocalDate((Date) spnFecha.getValue());
        LocalTime hora = convertirALocalTime((Date) spnHora.getValue());

        try {
            int nuevoId = generarSiguienteId();
            Partido partido = new Partido(
                    nuevoId, equipoLocal, equipoVisitante, fecha, hora, jornada
            );

            contexto.getGestorPartidos().registrarPartido(partido);

            JOptionPane.showMessageDialog(
                    this,
                    equipoLocal.getNombre() + " vs " + equipoVisitante.getNombre()
                    + " se programó correctamente.",
                    "Partido creado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (PartidoInvalidoException ex) {
            mostrarError(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private LocalDate convertirALocalDate(Date fecha) {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalTime convertirALocalTime(Date hora) {
        return hora.toInstant().atZone(ZoneId.systemDefault()).toLocalTime()
                .withSecond(0).withNano(0);
    }

    private int generarSiguienteId() {
        List<Partido> partidos = contexto.getGestorPartidos().listarPartidos();
        return partidos.stream()
                .mapToInt(Partido::getIdPartido)
                .max()
                .orElse(0) + 1;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "No se pudo crear el partido",
                JOptionPane.ERROR_MESSAGE
        );
    }
}