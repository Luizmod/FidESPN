package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import modelo.EstadoPartido;
import modelo.Partido;
import servicio.ContextoAplicacion;

public class FrmListaPartidos extends JFrame {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ContextoAplicacion contexto;

    public FrmListaPartidos(ContextoAplicacion contexto) {
        this.contexto = contexto;
        inicializarVentana();
        construirInterfaz();
    }

    private void inicializarVentana() {
        setTitle("FidESPN - Partidos programados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(820, 520));
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

        JLabel lblTitulo = new JLabel("Partidos programados");
        lblTitulo.setForeground(PaletaColores.BLANCO);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        List<Partido> partidos = contexto.getGestorPartidos().listarPartidos();
        JLabel lblSubtitulo = new JLabel(partidos.size() + " partido(s) en el sistema");
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
        String[] columnas = {
            "Local", "Visitante", "Marcador", "Fecha", "Hora", "Corresponsal", "Estado"
        };

        List<Partido> partidos = contexto.getGestorPartidos().listarPartidos();
        Object[][] datos = new Object[partidos.size()][columnas.length];

        for (int i = 0; i < partidos.size(); i++) {
            Partido partido = partidos.get(i);
            datos[i][0] = partido.getEquipoLocal().getNombre();
            datos[i][1] = partido.getEquipoVisitante().getNombre();
            datos[i][2] = partido.obtenerMarcador();
            datos[i][3] = partido.getFecha().format(FORMATO_FECHA);
            datos[i][4] = partido.getHora().toString();
            datos[i][5] = partido.getCorresponsal() != null
                    ? partido.getCorresponsal().getNombre() + " " + partido.getCorresponsal().getApellido()
                    : "Sin asignar";
            datos[i][6] = partido.getEstado();
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
        tabla.getColumnModel().getColumn(6).setCellRenderer(new RenderizadorEstado());

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(PaletaColores.BLANCO);

        return scroll;
    }

    /** Colorea la celda de estado según si el partido está en juego, programado o finalizado. */
    private static class RenderizadorEstado extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable tabla, Object valor, boolean seleccionado,
                boolean conFoco, int fila, int columna
        ) {
            Component celda = super.getTableCellRendererComponent(
                    tabla, valor, seleccionado, conFoco, fila, columna
            );
            setHorizontalAlignment(SwingConstants.CENTER);

            if (valor == EstadoPartido.EN_JUEGO) {
                celda.setForeground(PaletaColores.VERDE_CESPED);
                setFont(getFont().deriveFont(Font.BOLD));
            } else if (valor == EstadoPartido.PROGRAMADO) {
                celda.setForeground(PaletaColores.NEGRO);
                setFont(getFont().deriveFont(Font.PLAIN));
            } else if (valor == EstadoPartido.FINALIZADO) {
                celda.setForeground(PaletaColores.TEXTO_SECUNDARIO);
                setFont(getFont().deriveFont(Font.PLAIN));
            } else {
                celda.setForeground(PaletaColores.NEGRO);
                setFont(getFont().deriveFont(Font.PLAIN));
            }

            return celda;
        }
    }
}
