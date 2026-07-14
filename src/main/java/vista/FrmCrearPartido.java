package vista;

import excepciones.PartidoInvalidoException;
import java.awt.*;
import java.time.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import servicio.ContextoAplicacion;

public class FrmCrearPartido extends JFrame {
    private final ContextoAplicacion contexto;
    private final JTextField txtId=new JTextField(8),txtFecha=new JTextField(12),txtHora=new JTextField(8);
    private final JComboBox<Equipo> cmbLocal=new JComboBox<>(),cmbVisitante=new JComboBox<>();private final JComboBox<Jornada> cmbJornada=new JComboBox<>();
    private final DefaultTableModel tablaModelo=new DefaultTableModel(new Object[]{"ID","Partido","Fecha","Hora","Jornada","Estado"},0){public boolean isCellEditable(int r,int c){return false;}};
    public FrmCrearPartido(ContextoAplicacion contexto){this.contexto=contexto;setTitle("Crear partido");setDefaultCloseOperation(DISPOSE_ON_CLOSE);setSize(1000,580);setLocationRelativeTo(null);construir();cargarCombos();cargarTabla();}
    private void construir(){JPanel raiz=new JPanel(new BorderLayout(15,15));raiz.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));JLabel titulo=new JLabel("Creación de partidos");titulo.setFont(new Font("SansSerif",Font.BOLD,24));JPanel form=new JPanel(new GridLayout(0,2,8,8));form.add(new JLabel("ID:"));form.add(txtId);form.add(new JLabel("Local:"));form.add(cmbLocal);form.add(new JLabel("Visitante:"));form.add(cmbVisitante);form.add(new JLabel("Fecha (AAAA-MM-DD):"));form.add(txtFecha);form.add(new JLabel("Hora (HH:MM):"));form.add(txtHora);form.add(new JLabel("Jornada:"));form.add(cmbJornada);JButton crear=new JButton("Crear partido");crear.addActionListener(e->crear());JButton actualizar=new JButton("Actualizar listas");actualizar.addActionListener(e->cargarCombos());form.add(crear);form.add(actualizar);raiz.add(titulo,BorderLayout.NORTH);raiz.add(form,BorderLayout.WEST);raiz.add(new JScrollPane(new JTable(tablaModelo)),BorderLayout.CENTER);setContentPane(raiz);}
    private void cargarCombos(){cmbLocal.removeAllItems();cmbVisitante.removeAllItems();cmbJornada.removeAllItems();for(Equipo e:contexto.getGestorEquipos().listarEquipos()){cmbLocal.addItem(e);cmbVisitante.addItem(e);}for(Jornada j:contexto.getGestorJornadas().listarOrdenadasPorFecha())cmbJornada.addItem(j);}
    private void crear(){try{Equipo l=(Equipo)cmbLocal.getSelectedItem(),v=(Equipo)cmbVisitante.getSelectedItem();Jornada j=(Jornada)cmbJornada.getSelectedItem();if(l==null||v==null)throw new IllegalArgumentException("Debe haber al menos dos equipos.");if(j==null)throw new IllegalArgumentException("Debe registrar una jornada.");Partido p=new Partido(Integer.parseInt(txtId.getText().trim()),l,v,LocalDate.parse(txtFecha.getText().trim()),LocalTime.parse(txtHora.getText().trim()),j);contexto.getGestorPartidos().registrarPartido(p);JOptionPane.showMessageDialog(this,"Partido creado correctamente.");limpiar();cargarTabla();}catch(NumberFormatException ex){error("El ID debe ser entero.");}catch(java.time.format.DateTimeParseException ex){error("Use fecha AAAA-MM-DD y hora HH:MM.");}catch(PartidoInvalidoException|IllegalArgumentException ex){error(ex.getMessage());}}
    private void cargarTabla(){tablaModelo.setRowCount(0);for(Partido p:contexto.getGestorPartidos().listarPartidos())tablaModelo.addRow(new Object[]{p.getIdPartido(),p.getEquipoLocal().getNombre()+" vs "+p.getEquipoVisitante().getNombre(),p.getFecha(),p.getHora(),p.getJornada()==null?"":p.getJornada().getNombre(),p.getEstado()});}
    private void limpiar(){txtId.setText("");txtFecha.setText("");txtHora.setText("");}
    private void error(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
}
