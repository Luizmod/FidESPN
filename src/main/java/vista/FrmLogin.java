package vista;

import excepciones.CredencialesInvalidasException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import modelo.Administrador;
import modelo.Corresponsal;
import modelo.Fanatico;
import modelo.Usuario;
import servicio.ContextoAplicacion;

public class FrmLogin extends JFrame {

    private final ContextoAplicacion contexto;

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;

    public FrmLogin(ContextoAplicacion contexto) {
        this.contexto = contexto;
        inicializarVentana();
        construirInterfaz();
    }

    private void inicializarVentana() {
        setTitle("FidESPN - Inicio de sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(PaletaColores.FONDO_VENTANA);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(PaletaColores.VERDE_OSCURO);
        encabezado.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        );

        JLabel lblMarca = new JLabel("FidESPN");
        lblMarca.setForeground(PaletaColores.BLANCO);
        lblMarca.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel lblSubtitulo = new JLabel(
                "Seguimiento del mundial en tiempo real"
        );
        lblSubtitulo.setForeground(new Color(0xD4, 0xE8, 0xD0));
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        encabezado.add(lblMarca, BorderLayout.WEST);
        encabezado.add(lblSubtitulo, BorderLayout.EAST);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);

        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(PaletaColores.BLANCO);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        PaletaColores.BORDE_TARJETA
                ),
                BorderFactory.createEmptyBorder(35, 45, 35, 45)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel lblTitulo = new JLabel(
                "Iniciar sesión",
                SwingConstants.CENTER
        );
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(PaletaColores.VERDE_OSCURO);

        JLabel lblCorreo = new JLabel("Correo electrónico");
        lblCorreo.setFont(new Font("SansSerif", Font.BOLD, 13));

        txtCorreo = new JTextField(24);
        txtCorreo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtCorreo.setPreferredSize(new Dimension(320, 38));

        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setFont(new Font("SansSerif", Font.BOLD, 13));

        txtContrasena = new JPasswordField(24);
        txtContrasena.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );
        txtContrasena.setPreferredSize(new Dimension(320, 38));

        btnIniciarSesion = new JButton("Iniciar sesión");
        btnIniciarSesion.setBackground(PaletaColores.VERDE_CESPED);
        btnIniciarSesion.setForeground(PaletaColores.BLANCO);
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setFont(
                new Font("SansSerif", Font.BOLD, 14)
        );
        btnIniciarSesion.setPreferredSize(new Dimension(320, 42));
        btnIniciarSesion.addActionListener(e -> iniciarSesion());

        JLabel lblAyuda = new JLabel(
                "<html><center>"
                + "Administrador: admin / admin"
                + "<br>"
                + "Corresponsal: corresponsal "
                + "/ corresponsal"
                + "</center></html>",
                SwingConstants.CENTER
        );
        lblAyuda.setForeground(PaletaColores.TEXTO_SECUNDARIO);
        lblAyuda.setFont(new Font("SansSerif", Font.PLAIN, 12));

        gbc.gridy = 0;
        tarjeta.add(lblTitulo, gbc);

        gbc.gridy = 1;
        tarjeta.add(lblCorreo, gbc);

        gbc.gridy = 2;
        tarjeta.add(txtCorreo, gbc);

        gbc.gridy = 3;
        tarjeta.add(lblContrasena, gbc);

        gbc.gridy = 4;
        tarjeta.add(txtContrasena, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(18, 8, 8, 8);
        tarjeta.add(btnIniciarSesion, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(10, 8, 8, 8);
        tarjeta.add(lblAyuda, gbc);

        contenedor.add(tarjeta);

        panelPrincipal.add(encabezado, BorderLayout.NORTH);
        panelPrincipal.add(contenedor, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        pack();
    }

    private void iniciarSesion() {
        String correo = txtCorreo.getText().trim();
        String contrasena =
                new String(txtContrasena.getPassword());

        try {
            Usuario usuario = contexto
                    .getGestorUsuarios()
                    .iniciarSesion(correo, contrasena);

            abrirVentanaSegunRol(usuario);

        } catch (CredencialesInvalidasException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Credenciales inválidas",
                    JOptionPane.ERROR_MESSAGE
            );

            txtContrasena.setText("");
            txtContrasena.requestFocus();
        }
    }

    private void abrirVentanaSegunRol(Usuario usuario) {
        if (usuario instanceof Administrador administrador) {
            new FrmDashboardAdministrador(
                    contexto,
                    administrador
            ).setVisible(true);

            dispose();
            return;
        }

        if (usuario instanceof Corresponsal corresponsal) {
            new FrmPartidosCorresponsal(
                    contexto,
                    corresponsal
            ).setVisible(true);

            dispose();
            return;
        }

        if (usuario instanceof Fanatico) {
            JOptionPane.showMessageDialog(
                    this,
                    "El módulo del fanático se implementará "
                    + "posteriormente.",
                    "Acceso correcto",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
