package principal;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import servicio.ContextoAplicacion;
import servicio.DatosIniciales;
import vista.FrmLogin;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ContextoAplicacion contexto = ContextoAplicacion.getInstancia();

            try {
                DatosIniciales.cargar(contexto);
                new FrmLogin(contexto).setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "No fue posible iniciar FidESPN:\n" + ex.getMessage(),
                        "Error de inicio",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
