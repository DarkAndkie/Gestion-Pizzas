/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import com.itextpdf.kernel.pdf.PdfWriter;
import java.io.IOException;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import modelos.conexion_postgres;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import modelos.DetalleVenta;
import modelos.modelo_Cliente;
import modelos.modelo_Venta;
import modelos.modelo_vendedor;
import modelos.pizzaItem;
/**
 *
 * @author JESUS
 */
public class reportes {
    
    public void obtenerReportes(JTable tablaReportes,Date fechaDesde, Date fechaHasta, List<modelo_Venta>lista_Venta){
        conexion_postgres cn= new conexion_postgres();
        lista_Venta.clear();
        String Fhasta = String.valueOf(fechaHasta);
        String Fdesde = String.valueOf(fechaDesde);
        String query = "SELECT *FROM venta WHERE ? ::daterange @> fecha_venta";
        ResultSet rs = null;
        PreparedStatement pstm = null;
        
        
        try{
           if(fechaDesde.before(fechaHasta) || fechaDesde.equals(fechaHasta)){
               try {
                   pstm = cn.nuevaConexion().prepareStatement(query);
                   pstm.setString(1,'['+Fdesde+','+fechaHasta+']');
                   rs=pstm.executeQuery();
                   while(rs.next()){
                       int idCliente=rs.getInt("id_user");
                       Double total_venta =rs.getDouble("total_venta");
                       Date fecha_Venta=rs.getDate("fecha_venta");
                       int id_vendedor = rs.getInt("id_vendedor");
                       int iva=rs.getInt("iva");
                       int id_venta = rs.getInt("id_venta");
                       System.out.println("aparecen reporte"+id_venta);
                       modelo_Venta ventaQuery = new modelo_Venta(id_venta, fecha_Venta, idCliente, id_vendedor, iva, total_venta);
                       lista_Venta.add(ventaQuery);
                   }
                   generarTablaReportes(tablaReportes, lista_Venta);
                   if(rs.wasNull()){
                           JOptionPane.showMessageDialog(null, "No se han encontrado ventas en ese rango de fechas");       
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }finally{
                   rs.close();
                   pstm.close();;
                   cn.cerrarConexion();  
               }
            }else{
               JOptionPane.showMessageDialog(null, "solo se acepta un rango de fechas válido");
           }
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        
    }
    public String consultasNombresVenta(String column_buscada, String colum_comparar,int id_Consultar){
        // PAra buscar nombres por id asociadas a ventas, se da el nombre de la
        //columna donde queremos el dato y la columna que usaremos como condicion para consultar como id por ejemplo
        conexion_postgres cn = new conexion_postgres();
        ResultSet rs = null;
        PreparedStatement pstm = null;
        String nombre="";
        String query="SELECT "+column_buscada+" from usuario where "+colum_comparar + " = ?";
        try {
            pstm=cn.nuevaConexion().prepareStatement(query);
            pstm.setInt(1, id_Consultar);
            rs=pstm.executeQuery();
            while(rs.next()){
                nombre= rs.getString(column_buscada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            return nombre;
        }
    }
    public void generarTablaReportes(JTable tablaReportes, List<modelo_Venta>lista_Ventas){
        DefaultTableModel modelo;
        DecimalFormat fabrica = new DecimalFormat("#,###0.000");
        if(tablaReportes.getRowCount()!=0 && tablaReportes!=null){
            modelo = (DefaultTableModel)tablaReportes.getModel();
            modelo.setRowCount(0);
            
            for(modelo_Venta ventaTemp : lista_Ventas){
                int id_Venta=ventaTemp.getId_venta();
                String nombreCliente = consultasNombresVenta("nombre_user", "id_user", ventaTemp.getId_Cliente());
                Date fecha_venta = (java.sql.Date)ventaTemp.getFecha();
                double total_Venta =ventaTemp.getTotal_venta();
                modelo.addRow(new Object[]{id_Venta,nombreCliente,fecha_venta,fabrica.format(total_Venta)});
            }
            tablaReportes.setModel(modelo);
            
        }else{
            modelo = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column){
                return column==-1;
            }
            };
            modelo.addColumn("ID Venta");
            modelo.addColumn("Cliente");
            modelo.addColumn("Fecha");
            modelo.addColumn("Total Venta");
            
           for(modelo_Venta ventaTemp : lista_Ventas){
               
                int id_Venta=ventaTemp.getId_venta();
                String nombreCliente = consultasNombresVenta("nombre_user", "id_user", ventaTemp.getId_Cliente());
                Date fecha_venta = (java.sql.Date)ventaTemp.getFecha();
                double total_Venta =ventaTemp.getTotal_venta();
                
                modelo.addRow(new Object[]{id_Venta,nombreCliente,fecha_venta,fabrica.format(total_Venta)});
            }
           
            tablaReportes.setModel(modelo);
            tablaReportes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tablaReportes.setRowSelectionAllowed(true);
            tablaReportes.setColumnSelectionAllowed(false);
           
        }
}
    public void instanciarfactura(List<modelo_Venta>lista_Ventas, JTable tbl_Reportes) throws SQLException{
         conexion_postgres cn = new conexion_postgres();
         List<DetalleVenta>lista_Detalle = new ArrayList<>();
         List<pizzaItem>lista_Pizza = new ArrayList<>();
         String query="select *from join_Consultadetalle where id_venta = ?";
         PreparedStatement pstm = null;
         ResultSet rs = null;
         modelo_Cliente clienteObj= null;
         modelo_vendedor vendedorObj = null;
         
         //Obtengamos la factura seleccionada para exportar
         //alcile me esta quedando bien bonito este código
         //Vuelve cpnmigo Nicole :c
         int idVenta = (int) tbl_Reportes.getValueAt(tbl_Reportes.getSelectedRow(),0);
         try{
             pstm=cn.nuevaConexion().prepareStatement(query);
             pstm.setInt(1, idVenta);
             clienteObj= instanciarCliente(idVenta);
             vendedorObj=instanciarVendedor(idVenta);
             rs = pstm.executeQuery();
             if(!rs.next())
                 System.out.println("No se han encontrado registros");
             
             while(rs.next()){
                 lista_Detalle.add(new DetalleVenta(rs.getInt("id_pizza"), rs.getString("nombrepizza"), rs.getInt("cant_pizza"),
                         rs.getDouble("precio_venta")));
             }

         }catch(SQLException e){
             e.printStackTrace();
         }finally{
             pstm.close();
             rs.close();
             cn.cerrarConexion();
         }
         
    }
    
    public modelo_Cliente instanciarCliente(int id_venta) throws SQLException{
        conexion_postgres cn = new conexion_postgres();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        modelo_Cliente cliente = null;
        String query = "SELECT *FROM join_cliente where id_venta=?";
        try{
            pstm = cn.nuevaConexion().prepareStatement(query);
            pstm.setInt(1, id_venta);
            rs = pstm.executeQuery();
            if(rs.next()){
                cliente = new modelo_Cliente(rs.getString("direccion_user"),rs.getString("telefono_user"),
                        rs.getInt("id_user"), rs.getString("nombre_user"), rs.getString("apellidos_user"));
            }else{
                System.out.println("error al instancia cliente");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pstm.close();
            rs.close();
            cn.cerrarConexion();
            return cliente;
        }       
    }
        public modelo_vendedor instanciarVendedor(int id_venta) throws SQLException{
        conexion_postgres cn = new conexion_postgres();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        modelo_vendedor vendedor = null;
        String query = "SELECT *FROM join_cliente where id_venta=?";
        try{
            pstm = cn.nuevaConexion().prepareStatement(query);
            pstm.setInt(1, id_venta);
            rs = pstm.executeQuery();
            if(rs.next()){
                vendedor = new modelo_vendedor(rs.getInt("id_user"), rs.getString("nombre_user"), rs.getString("apellidos_user"));
            }else{
                System.out.println("Error al instanciar vendedor");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            pstm.close();
            rs.close();
            cn.cerrarConexion();
            return vendedor;
        }       
    }
    public void crearPdf(List<modelo_Venta>lista_Ventas, List<DetalleVenta>lista_Detalle,List<pizzaItem>lista_Pizza) throws IOException{
        PdfWriter writer = new PdfWriter("Factua n°");
    }
}
