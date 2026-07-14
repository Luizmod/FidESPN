package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Equipo;
import servicio.ContextoAplicacion;

public class FrmListaEquipos extends JFrame {

    private final ContextoAplicacion contexto;

    public FrmListaEquipos(ContextoAplicacion contexto) {
        this.contexto = contexto;
        inicializarVentana();
        construirInterfaz();
    }

    private void inicializarVentana() {
        setTitle("FidESPN - Equipos registrados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(PaletaColores.FONDO_VENTANA);

        principal.add(construirEncabezado(), BorderLayout.NORTH);
        principal.add(construirTabla(), BorderLayout.CENTER);

        setContentPane(principal);
        pack();
    }

    private JPanel construirEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(PaletaColores.VERDE_OSCURO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel lblTitulo = new JLabel("Selecciones registradas");
        lblTitulo.setForeground(PaletaColores.BLANCO);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        List<Equipo> equipos = contexto.getGestorEquipos().listarEquipos();
        JLabel lblSubtitulo = new JLabel(equipos.size() + " equipo(s) en el torneo");
        lblSubtitulo.setForeground(new Color(0xD4, 0xE8, 0xD0));
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(lblTitulo);
        textos.add(lblSubtitulo);

        encabezado.add(textos, BorderLayout.WEST);
        return encabezado;
    }

    private JScrollPane construirTabla() {
        String[] columnas = {"Selección", "Grupo", "Jugadores registrados"};

        List<Equipo> equipos = contexto.getGestorEquipos().listarEquipos();
        Object[][] datos = new Object[equipos.size()][columnas.length];

        for (int i = 0; i < equipos.size(); i++) {
            Equipo equipo = equipos.get(i);
            datos[i][0] = equipo.getNombre();
            datos[i][1] = "Grupo " + equipo.getGrupoMundial();
            datos[i][2] = equipo.obtenerJugadores().size();
        }

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(PaletaColores.VERDE_MEDIO);
        tabla.getTableHeader().setForeground(PaletaColores.BLANCO);
        tabla.setSelectionBackground(new Color(0xDD, 0xF0, 0xD8));
        tabla.setGridColor(PaletaColores.BORDE_TARJETA);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(PaletaColores.BLANCO);

        return scroll;
    }
}
