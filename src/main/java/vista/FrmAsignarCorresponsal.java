package vista;

import excepciones.CorresponsalNoDisponibleException;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import servicio.ContextoAplicacion;

public class FrmAsignarCorresponsal extends JFrame {
    private final ContextoAplicacion contexto;
    private final JComboBox<Partido> cmbPartido=new JComboBox<>();private final JComboBox<Corresponsal> cmbCorresponsal=new JComboBox<>();
    private final DefaultTableModel tablaModelo=new DefaultTableModel(new Object[]{"ID","Partido","Corresponsal","Estado"},0){public boolean isCellEditable(int r,int c){return false;}};
    public FrmAsignarCorresponsal(ContextoAplicacion contexto){this.contexto=contexto;setTitle("Asignar corresponsal");setDefaultCloseOperation(DISPOSE_ON_CLOSE);setSize(950,540);setLocationRelativeTo(null);construir();cargarCombos();cargarTabla();}
    private void construir(){JPanel raiz=new JPanel(new BorderLayout(15,15));raiz.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));JLabel titulo=new JLabel("Asignación de corresponsales");titulo.setFont(new Font("SansSerif",Font.BOLD,24));JPanel form=new JPanel(new GridLayout(0,2,8,8));form.add(new JLabel("Partido:"));form.add(cmbPartido);form.add(new JLabel("Corresponsal:"));form.add(cmbCorresponsal);JButton asignar=new JButton("Asignar");asignar.addActionListener(e->asignar());JButton actualizar=new JButton("Actualizar listas");actualizar.addActionListener(e->cargarCombos());form.add(asignar);form.add(actualizar);raiz.add(titulo,BorderLayout.NORTH);raiz.add(form,BorderLayout.WEST);raiz.add(new JScrollPane(new JTable(tablaModelo)),BorderLayout.CENTER);setContentPane(raiz);}
    private void cargarCombos(){cmbPartido.removeAllItems();cmbCorresponsal.removeAllItems();for(Partido p:contexto.getGestorPartidos().listarPartidos())cmbPartido.addItem(p);for(Usuario u:contexto.getGestorUsuarios().listarUsuarios())if(u instanceof Corresponsal c)cmbCorresponsal.addItem(c);}
    private void asignar(){try{Partido p=(Partido)cmbPartido.getSelectedItem();Corresponsal c=(Corresponsal)cmbCorresponsal.getSelectedItem();if(p==null)throw new IllegalArgumentException("Debe crear un partido.");if(c==null)throw new IllegalArgumentException("Debe existir un corresponsal.");contexto.getGestorPartidos().asignarCorresponsal(p.getIdPartido(),c);JOptionPane.showMessageDialog(this,"Corresponsal asignado correctamente.");cargarTabla();}catch(CorresponsalNoDisponibleException|IllegalArgumentException ex){error(ex.getMessage());}}
    private void cargarTabla(){tablaModelo.setRowCount(0);for(Partido p:contexto.getGestorPartidos().listarPartidos())tablaModelo.addRow(new Object[]{p.getIdPartido(),p.getEquipoLocal().getNombre()+" vs "+p.getEquipoVisitante().getNombre(),p.getCorresponsal()==null?"Sin asignar":p.getCorresponsal().getNombre()+" "+p.getCorresponsal().getApellido(),p.getEstado()});}
    private void error(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
}
