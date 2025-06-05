/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.awt.Label;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import modelos.conexion_postgres;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelos.pizzaItem;

/**
 *
 * @author JESUS
 */
public class cargos {

    DecimalFormat valorModelo = new DecimalFormat("#,###");
    public void tablaResumen(JTable tablaResumen, JTable tablaItem, List<pizzaItem>detalle_cargo){
       ventas v = new ventas();
       
        if( tablaItem.getRowCount()!=0){
            DefaultTableModel modelo;
            boolean existeEnLista=false;
            int index = tablaItem.getSelectedRow();
            //inicializo el item a añadir
            int id= Integer.parseInt(tablaItem.getValueAt(index, 0).toString());
            String name = String.valueOf(tablaItem.getValueAt(index, 1));
            double valor= Double.parseDouble(tablaItem.getValueAt(index, 3).toString());
            int cant = 1;
            pizzaItem producto= new pizzaItem(name, cant, id, valor);
            
            for(pizzaItem det : detalle_cargo){
                if(det.getId_pizza()==producto.getId_pizza()){
                    det.setCantidad(det.getCantidad()+producto.getCantidad());
                    existeEnLista=true;
                    System.out.println("existe en lista");
                    break;
                }
            }

            if(!existeEnLista){
                detalle_cargo.add(producto);
            }
            
            if(tablaResumen.getModel()==null || tablaResumen.getRowCount()==0){
                modelo = new DefaultTableModel(){
                    @Override
                    public boolean isCellEditable(int row, int column){
                        
                        return column==2;
                    }
                };
                modelo.addColumn("ID PRODUCTO");
                modelo.addColumn("NOMBRE");
                modelo.addColumn("CANTIDAD");
                modelo.addColumn("VALOR");
                modelo.addColumn("TOTAL");
                tablaResumen.setModel(modelo);
                tablaResumen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tablaResumen.setRowSelectionAllowed(true);
                tablaResumen.setColumnSelectionAllowed(false);
            }else{
            modelo = (DefaultTableModel) tablaResumen.getModel();
            }
             modelo.setRowCount(0);
             for(pizzaItem temp: detalle_cargo){

                    modelo.addRow(new Object[]{temp.getId_pizza(),temp.getNombre(),temp.getCantidad(),
                    valorModelo.format(temp.getValor()),valorModelo.format(temp.getValor()*temp.getCantidad())});
             }
             tablaResumen.setModel(modelo);
            
        
        
    }else{
            JOptionPane.showMessageDialog(null, "Busque o seleccione un item antes de buscar agregar mijo ");
        }

   }
    public void actualizarCantidadTabla(JTable tablaResumen,List<pizzaItem>detalle){
        int index = tablaResumen.getSelectedRow(),cantidadNueva=0;
        if(index<0){
            index=0;
        }
        String cantStr=tablaResumen.getValueAt(index,2).toString();
        int idItem=Integer.parseInt(tablaResumen.getValueAt(index,0).toString());
        if(esNumero(cantStr)){
            cantidadNueva=Integer.parseInt(tablaResumen.getValueAt(index,2).toString());
         
        if(cantidadNueva>0){
            for(pizzaItem temporal : detalle){
                if(temporal.getId_pizza()==idItem){
                    temporal.setCantidad(cantidadNueva);
                }
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "No se permiten valores nulo o negativos");
               for(pizzaItem temporal : detalle){
                if(temporal.getId_pizza()==idItem){
                    temporal.setCantidad(1);
                }
            }
        }

        }else{
            JOptionPane.showMessageDialog(null, "Solo se admiten numeros");
        }
            DefaultTableModel modelo;
            modelo = (DefaultTableModel) tablaResumen.getModel();
            modelo.setRowCount(0);
             for(pizzaItem temp: detalle){

                    modelo.addRow(new Object[]{temp.getId_pizza(),temp.getNombre(),temp.getCantidad(),
                    temp.getValor(),valorModelo.format(temp.getValor()*temp.getCantidad())});
             }
             tablaResumen.setModel(modelo);
}
    public static boolean esNumero(String verificar){

        try {
            Integer.parseInt(verificar);
            return true;
        } catch (Exception e) {
            System.out.println("No es cadena");
            return false;
        }
    }
    
    //Descargos
     public void tablaResumenDescargo(JTable tablaResumen, JTable tablaItem, List<pizzaItem>detalle_cargo){
       ventas v = new ventas();
       
        if( tablaItem.getRowCount()!=0){
                DefaultTableModel modelo;
                boolean existeEnLista=false;
                int index = tablaItem.getSelectedRow();
                int id= Integer.parseInt(tablaItem.getValueAt(index, 0).toString());
                String name = String.valueOf(tablaItem.getValueAt(index, 1));
                double valor= Double.parseDouble(tablaItem.getValueAt(index, 3).toString());
                int cant = -1;
                pizzaItem producto= new pizzaItem(name, cant, id, valor);

            for(pizzaItem det : detalle_cargo){
                if(det.getId_pizza()==producto.getId_pizza()){
                    det.setCantidad(det.getCantidad()+producto.getCantidad());
                    existeEnLista=true;
                    System.out.println("existe en lista");
                    break;
                }
            }

            if(!existeEnLista){
                detalle_cargo.add(producto);
            }
            
            if(tablaResumen.getModel()==null || tablaResumen.getRowCount()==0){
                modelo = new DefaultTableModel(){
                    @Override
                    public boolean isCellEditable(int row, int column){
                        
                        return column==2;
                    }
                };
                modelo.addColumn("ID PRODUCTO");
                modelo.addColumn("NOMBRE");
                modelo.addColumn("CANTIDAD");
                modelo.addColumn("VALOR");
                modelo.addColumn("TOTAL");
                tablaResumen.setModel(modelo);
                tablaResumen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                tablaResumen.setRowSelectionAllowed(true);
                tablaResumen.setColumnSelectionAllowed(false);
            }else{
            modelo = (DefaultTableModel) tablaResumen.getModel();
            }
             modelo.setRowCount(0);
             for(pizzaItem temp: detalle_cargo){

                    modelo.addRow(new Object[]{temp.getId_pizza(),temp.getNombre(),temp.getCantidad(),
                   valorModelo.format(temp.getValor()),valorModelo.format(temp.getValor()*temp.getCantidad())});
             }
             tablaResumen.setModel(modelo);
            
        
        
    }else{
            JOptionPane.showMessageDialog(null, "Busque o seleccione un item antes de buscar agregar mijo ");
        }

   }
    public void actualizarCantidadTablaDescargo(JTable tablaResumen,List<pizzaItem>detalle){
        int index = tablaResumen.getSelectedRow(),cantidadNueva=0;
        if(index<0){
            index=0;
        }
        String cantStr=tablaResumen.getValueAt(index,2).toString();
        int idItem=Integer.parseInt(tablaResumen.getValueAt(index,0).toString());
        if(esNumero(cantStr)){
            cantidadNueva=Integer.parseInt(tablaResumen.getValueAt(index,2).toString());
         
        if(cantidadNueva<0){
            for(pizzaItem temporal : detalle){
                if(temporal.getId_pizza()==idItem){
                    temporal.setCantidad(cantidadNueva);
                }
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "No se permiten valores nulo o posotivos");
               for(pizzaItem temporal : detalle){
                if(temporal.getId_pizza()==idItem){
                    temporal.setCantidad(-1);
                }
            }
        }

        }else{
            JOptionPane.showMessageDialog(null, "Solo se admiten numeros");
        }
            DefaultTableModel modelo;
            modelo = (DefaultTableModel) tablaResumen.getModel();
            modelo.setRowCount(0);
             for(pizzaItem temp: detalle){

                    modelo.addRow(new Object[]{temp.getId_pizza(),temp.getNombre(),temp.getCantidad(),
                    temp.getValor(),valorModelo.format(temp.getValor()*temp.getCantidad())});
             }
             tablaResumen.setModel(modelo);
}

    public void limpiarTodo(JTable clientes, JTable items, JTable resumen, List<pizzaItem> detalles,JTextField txt_proovedor
    ,JTextField item,JTextField txt_idProv, JTextField txt_nombreProv, JTextArea desc, JLabel total){
        DefaultTableModel modelo= new DefaultTableModel();
        modelo.setRowCount(0);
        clientes.setModel(modelo);
        items.setModel(modelo);
        resumen.setModel(modelo);
        detalles.removeAll(detalles);
        txt_proovedor.setText("");
        item.setText("");
        txt_idProv.setText(null);
       txt_nombreProv.setText("");
       desc.setText("null");
       total.setText("-------");
    }
    //generales
    public void valorTotal(List<pizzaItem>lista_Detalle, JLabel valortotal){
        double total=0;           
        for(pizzaItem temporal : lista_Detalle){
            total+=temporal.getCantidad()*temporal.getValor();
        }
        valortotal.setText(String.valueOf(valorModelo.format(total)));
    }
    public void finalizarMovimiento(List<pizzaItem>lista_Detalle, JTextField idProov, JLabel fecha, JTextArea descripcion,
            JLabel total, boolean tipo){
        
        int idProveed = Integer.parseInt(idProov.getText()),tipoMv;
        double totalMv = Double.parseDouble(total.getText());
        Date fechaMv = java.sql.Date.valueOf(fecha.getText());
        String desc;
        desc = descripcion.getText();
        conexion_postgres cn = new conexion_postgres();
        PreparedStatement pstm;
        ResultSet rs;
        if(tipo==true){
            tipoMv=0;
        } else{
            tipoMv=1;
        }
        String query="INSERT INTO cargos (tipo_mov,totalmovimiento,fechamovimiento,id_proveedor,descripciondescargo)values("
                + "?,?,?,?,?) returning id_movimiento;";
        try {
            pstm = cn.nuevaConexion().prepareStatement(query);
            pstm.setInt(1, tipoMv);
            pstm.setDouble(2, totalMv);
            pstm.setDate(3, (java.sql.Date) fechaMv);
            pstm.setInt(4, idProveed);
            pstm.setString(5, desc);
            rs = pstm.executeQuery();
            if(rs.next()){
                int idMov = rs.getInt("id_movimiento");
                generarDetalleMv(idMov,lista_Detalle);
            }
            rs.close();
            pstm.close();
        } catch (SQLException ex) {
           ex.printStackTrace();
        }finally{
            JOptionPane.showMessageDialog(null, "Se ha ingresado con exito");
            cn.cerrarConexion();
        }
        
        
    }
    public boolean validarRestodeCantidades(List<pizzaItem> lista_detalle){
        String query = "Select cant,nombrepizza from pizzaitem where id_pizza=?";
        boolean bandera = true;

           conexion_postgres cn = new conexion_postgres();
           PreparedStatement pstm=null;
        try{
        pstm = cn.nuevaConexion().prepareStatement(query); 
        
        for(pizzaItem temporal : lista_detalle){
        ResultSet rs = null;   
        try {

            pstm.setInt(1, temporal.getId_pizza());
            rs=pstm.executeQuery();
            
            if(rs.next()){
               int cantLista = temporal.getCantidad();
                int cantQuery = cantLista+rs.getInt("cant");

                if(cantQuery<0){
                    JOptionPane.showMessageDialog(null, "No se pudo realizar el descargo, es superior al stock disponible"
                            + " para el item: "+ rs.getString("nombrepizza")+"Con id:"+temporal.getId_pizza());
                    bandera= false;
                    break;
                    
               }
                
            }
        } catch (SQLException e){
            e.printStackTrace();
            bandera=false;
        }finally{
            if(rs.next()){
                rs.close();
            }
        }
          
        }

        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(pstm!=null){
                try {
                    pstm.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cn.cerrarConexion();
        }
        return bandera;
    }
    public void generarDetalleMv(int idMov, List<pizzaItem>lista_Detalle){
        conexion_postgres cn = new conexion_postgres();

        String query = "INSERT INTO detalle_movimiento (cant_item,totaldetalle,id_item,id_movimiento)values(?,?,?,?)";
        
        for(pizzaItem temporal : lista_Detalle){
            PreparedStatement pstm;
            try {
                pstm = cn.nuevaConexion().prepareStatement(query);
                pstm.setInt(1, temporal.getCantidad());
                pstm.setDouble(2, temporal.getValor());
                pstm.setInt(3, temporal.getId_pizza());
                pstm.setInt(4, idMov);
                pstm.executeUpdate();
                System.out.println("detalleGenerado");
                actualizarInventario(temporal.getId_pizza(), temporal.getCantidad());
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                cn.cerrarConexion();;
            }
        }
        
    }
    public void actualizarInventario(int id, int cant){
        conexion_postgres cn = new conexion_postgres();
        PreparedStatement pstm;
        String quey = "UPDATE pizzaitem set cant= cant+(?) where id_pizza=?";
        try {
            pstm = cn.nuevaConexion().prepareStatement(quey);
            pstm.setInt(1, cant);
            pstm.setInt(2, id);
            pstm.executeUpdate();
            pstm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            cn.cerrarConexion();
        }
        
    }
    
    public void eliminarSeleccionItem(JTable resumen, List<pizzaItem>detalle_lista){
        //Usemos comentarios mejors xd aqui tomamos el id que vamos a eliminar
        int id = (int) resumen.getValueAt(resumen.getSelectedRow(),0);
        
        //crearemos un modelo que tomara el que ya tenemos por tabla para no meter columnas a mano
        DefaultTableModel modelo = (DefaultTableModel) resumen.getModel();
        //Eliminamos el contenido, de iugal forma lo repondremos al final
        modelo.setRowCount(0);
        
        //buscamos en la lista de los elementos a usar el que queremos borrar
        for(int i=0;i<detalle_lista.size();i++){
            if(detalle_lista.get(i).getId_pizza()==id){
               detalle_lista.remove(i);
            }
        }
        //rellenamos nuevamente la tabla
        for(pizzaItem temp : detalle_lista){
                    modelo.addRow(new Object[]{temp.getId_pizza(),temp.getNombre(),temp.getCantidad(),
                    temp.getValor(),valorModelo.format(temp.getValor()*temp.getCantidad())});
        }
        resumen.setModel(modelo);
        
    }
     public void seleccionarProveedor(JTable tablaProv, JTextField idProov, JTextField nameProv){
              int fila = tablaProv.getSelectedRow();
              idProov.setText((String) tablaProv.getValueAt(fila,0));
              nameProv.setText(tablaProv.getValueAt(fila, 1).toString());
         }
             
    public void estadoResumen(JTable resumen, List<pizzaItem>lista_resumen,String tipoEstado){
        if(tipoEstado.equals("Cargos")){
            actualizarCantidadTabla(resumen, lista_resumen);
        }
        if(tipoEstado.equals("Descargos")){
            actualizarCantidadTablaDescargo(resumen, lista_resumen);
        }
    }
}
  




