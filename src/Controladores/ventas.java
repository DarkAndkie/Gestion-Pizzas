/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.beans.Statement;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import modelos.DetalleVenta;
import modelos.conexion_postgres;
import modelos.pizzaItem;

/**
 *
 * @author yisus
 */
public class ventas {
    
public void caja(JButton Btnestado,JLabel fecha,int idusuario, JLabel id_caja) throws SQLException{
    boolean estadoCaja = false;
    conexion_postgres cn = new conexion_postgres();
    PreparedStatement pstm = null, pstm2=null;
    CallableStatement clsm = null;
    ResultSet rs = null,rs2=null;
    int idCajaTemp=0;
    int respuestacerrarCaja;
    String query = "INSERT INTO caja (total_ventadiaria,fecha_cuadre,fk_usuario,estado_caja)values(?,?,?,?) returning id_cuadre";
    String query2 = "SELECT caja.estado_caja,caja.id_cuadre FROM caja where fk_usuario = ? and estado_caja=1";
    String query3="UPDATE caja set total_ventadiaria=?,estado_caja=? where fk_usuario = ? and estado_caja = ? and fecha_cuadre =? ";
    java.sql.Date fechaSQL = java.sql.Date.valueOf(fecha.getText());
    
    try {
        pstm2 = cn.nuevaConexion().prepareStatement(query2);
        pstm2.setInt(1, idusuario);
        rs=pstm2.executeQuery();
            if(rs.next()){
                switch (rs.getInt("estado_caja")) {
                    case 0:
                        estadoCaja = false;
                        break;
                    case 1:
                        respuestacerrarCaja=JOptionPane.showConfirmDialog(null, "Desea cerrar la caja?");
                        idCajaTemp = rs.getInt("id_cuadre");
                        System.out.println("id_caja si se encuenta"+idCajaTemp);
                        switch (respuestacerrarCaja) {
                            case JOptionPane.YES_OPTION:
                                estadoCaja = true;
                                rs.close();
                                break;
                            case JOptionPane.NO_OPTION:
                                estadoCaja=false;
                                rs.close();
                                break;
                            default:
                                throw new AssertionError();
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "hubo un errpr en la búsqueda. Revise que la caja esté abierta");
                }

            }else if(!rs.next()){
                clsm = cn.nuevaConexion().prepareCall(query);
                clsm.setDouble(1, 0);
                clsm.setDate(2, fechaSQL);
                clsm.setInt(3, idusuario);
                clsm.setInt(4, 1);
                rs2=clsm.executeQuery();
                if(rs2.next()){
                    idCajaTemp=rs2.getInt("id_cuadre");
                    System.out.println("id:cuadre en la ceacion"+id_caja);
                }
                Btnestado.setText("Cerrar Caja");
                rs.close();
            }
            
            if(estadoCaja==true){
                pstm = cn.nuevaConexion().prepareStatement(query3);
                pstm.setDouble(1, consultarTotalVentaDiaria(idusuario));
                pstm.setInt(2, 0);
                pstm.setInt(3, idusuario);
                pstm.setInt(4, 1);
                pstm.setDate(5, fechaSQL);
                pstm.executeLargeUpdate();
                Btnestado.setText("Abrir Caja");
                idCajaTemp=0;
                System.out.println("pasa aqui el id=?" +idCajaTemp);
                 pstm.close();
            }

    } catch (SQLException e) {
        e.printStackTrace();
                   System.out.println("Este es el metodo que crea lo ciera la caja");
    }finally{
        pstm2.close();
        cn.cerrarConexion();
         id_caja.setText(String.valueOf(idCajaTemp));
    }
    
    
}
    public Double consultarTotalVentaDiaria(int id_usuario) throws SQLException, SQLException{
    conexion_postgres cn = new conexion_postgres();
    PreparedStatement pstm1 = null,pstm2=null;
    ResultSet rs1 = null,rs2=null;
    double total_VentaDiaria=0;
    int id_cuadre=0;
    String query2="SELECT id_cuadre from caja where fk_usuario=? and estado_caja=1;";
        try {
            pstm2 =cn.nuevaConexion().prepareStatement(query2);
            pstm2.setInt(1, id_usuario);
            rs2 = pstm2.executeQuery();
            if(rs2.next()){
                id_cuadre=rs2.getInt("id_cuadre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Este es el metodo que revisa de total cuadre");
        }finally{
            pstm2.close();
            rs2.close();
            cn.cerrarConexion();
        }
    
    String query1 = "SELECT SUM(v.total_venta) AS total from venta AS v "
            + "JOIN caja AS c on c.id_cuadre = v.id_cuadre "
            + "WHERE v.id_cuadre =?";
         try {
             pstm1=cn.nuevaConexion().prepareStatement(query1);
             pstm1.setInt(1, id_cuadre);
             System.out.println("Id del cuadre en calcular tota"+id_cuadre);
             rs1=pstm1.executeQuery();
             if(rs1.next()){
                 total_VentaDiaria=rs1.getDouble("total");
             }else{
                 JOptionPane.showMessageDialog(null, "No se ha encontrado un cuadre abierto para el id del usuario");
             }
            
        } catch (SQLException e) {
                       System.out.println("Este es el metodo que revisa de total cuadre");
            e.printStackTrace();
        }finally{
             pstm1.close();
             rs1.close();
             cn.cerrarConexion();
             return total_VentaDiaria;
         }
  
        
    }
public void SeleccionarTablaResumen (JTable tablaProducto, JTextField idProducto, JTextField nombre
            ,JTextField tipo, JTextField precioP, JTextField stock){
        
    int fila = tablaProducto.getSelectedRow();
    try {
         DecimalFormat conversorDecimal = new DecimalFormat("#,###");
        if (fila >= 0){
            idProducto.setText(tablaProducto.getValueAt(fila, 0).toString());
            nombre.setText(tablaProducto.getValueAt(fila, 1).toString());
            tipo.setText(tablaProducto.getValueAt(fila, 2).toString());
            precioP.setText(conversorDecimal.format(tablaProducto.getValueAt(fila, 3)));
            stock.setText(tablaProducto.getValueAt(fila, 4).toString());   
            
        }
        
    }catch (Exception e){
        JOptionPane.showMessageDialog(null, "ERROR");
    }
}
public void SeleccionarTablaCliente(JTable tablaProducto, JTextField idCliente, JTextField nombre){
        
    int fila = tablaProducto.getSelectedRow();
    try {
        if (fila >= 0){
            idCliente.setText(tablaProducto.getValueAt(fila, 0).toString());
            nombre.setText(tablaProducto.getValueAt(fila, 1).toString());     
        }
        
    }catch (Exception e){
        JOptionPane.showMessageDialog(null, "ERROR");
    }
}

public void eliminarTablaVenta(JTable tablaVenta, List<pizzaItem>detalle_venta){
    int itemSelect = tablaVenta.getSelectedRow();
    int id = (int) tablaVenta.getValueAt(itemSelect, 0), index=0;
    DefaultTableModel modeloTabla = new DefaultTableModel();
    modeloTabla = (DefaultTableModel)tablaVenta.getModel();
    if(itemSelect == -1){
        JOptionPane.showMessageDialog(null, "Selecciona una fila por favor");
    }
    try{
    if(itemSelect!=-1){
    for(pizzaItem itemTemp : detalle_venta){
        if(itemTemp.getId_pizza() == id){
          break;
        }
        index++;
    }

    detalle_venta.remove(index);
    modeloTabla.removeRow(itemSelect);
    tablaVenta.setModel(modeloTabla);
    }
    }catch(Exception e){
        System.out.println("Error al eliminar item de la tabla" + e);
    }
}
public void mostrarProducto(JTable tablaproductos, String busqueda,List<pizzaItem>Detalle_Lista){
        conexion_postgres cn = new conexion_postgres();
        pizzaItem cs = new pizzaItem();
        List<pizzaItem> lista_cant = new ArrayList<>(Detalle_Lista);
        DefaultTableModel modelo = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return column==-1;
            }
        };
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");
  
        String sql = "SELECT id_pizza,nombrepizza,"
                + "tipopizza,cant,valor from pizzaitem where nombrepizza ILIKE ?";
                        System.out.println("Valor de búsqueda: '" + busqueda + "'");
                        System.out.println("SQL: " + sql);
        try {
            PreparedStatement pstm = cn.nuevaConexion().prepareStatement(sql);
            pstm.setString(1, "%"+busqueda+"%");
            ResultSet rs = pstm.executeQuery();
            
            while(rs.next()){
                cs = new pizzaItem();
                //Vamos a encargarnos de descontar las cantidades que hay en el sistema ya.
                if(lista_cant.isEmpty()){
                cs.setCantidad(Integer.parseInt(rs.getString("cant")));  
                    
                }else{
                    for(pizzaItem temporal : lista_cant){
                        if(temporal.getId_pizza() == rs.getInt("id_pizza")){
                       cs.setCantidad(rs.getInt("cant") - temporal.getCantidad());
                       break;
                        }else{
                cs.setCantidad(Integer.parseInt(rs.getString("cant")));                              
                        }
                     }
                }
                cs.setId_pizza(rs.getInt("id_pizza"));
                cs.setNombre(rs.getString("nombrepizza"));//nombre de la columna con nombres en la db
                cs.setValor((float) Double.parseDouble(rs.getString("valor")));
                cs.setTipo_Pizz(rs.getString("tipopizza"));
                
                modelo.addRow(new Object[]{cs.getId_pizza(),cs.getNombre(), cs.getTipo_Pizz(),cs.getValor(),
                cs.getCantidad()});   
            }
            tablaproductos.setModel(modelo);
            tablaproductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tablaproductos.setRowSelectionAllowed(true);
            tablaproductos.setCellSelectionEnabled(false);
        } catch (SQLException e) {
            System.out.println("Error al mostrar fun.mostratC: " + e);
        }finally{
            cn.cerrarConexion();
        }
    }
public void agregarAlaventa(JTable tablaFinal, JTextField id_producto, JTextField nombre,
        JTextField precio, JTextField cantidad, List<pizzaItem> detalle_venta) {
    
    boolean existeEnLaLista = false;  
    DefaultTableModel modelo;

    // Configurar el modelo de tabla
    if (tablaFinal.getModel() == null || tablaFinal.getModel().getRowCount() == 0) {
        modelo = new DefaultTableModel(){
                    @Override
        public boolean isCellEditable(int row, int column){
            return column==-1;
        }
        }; 
        modelo.addColumn("ID PRODUCTO");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("PRECIO");
        modelo.addColumn("CANTIDAD");
        modelo.addColumn("SUBTOTAL");
        tablaFinal.setModel(modelo);
        tablaFinal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaFinal.setRowSelectionAllowed(true);
        tablaFinal.setColumnSelectionAllowed(false);


    } else {
        modelo = (DefaultTableModel) tablaFinal.getModel();
    }
    
    // Obtener los datos del nuevo item
    double precioReal = Double.parseDouble(precio.getText().replace(".", "").replace(",", "."));
    int idProducto = Integer.parseInt(id_producto.getText());
    String nombreProducto = nombre.getText();
    int cantidadProducto = Integer.parseInt(cantidad.getText());
    

    
    //inicializamos el item
    if(cantidadEnBusqueda(cantidadProducto, idProducto,detalle_venta)==true){
        
    pizzaItem detalle = new pizzaItem(nombreProducto, cantidadProducto, idProducto, precioReal);
    
    //revisamos si ya está este item en la tabla
    if (detalle_venta.isEmpty()) {
        detalle_venta.add(detalle);
        JOptionPane.showMessageDialog(null, "Producto agregado: " + detalle.getNombre());
    } else {

        for (pizzaItem detalle_ExisteEnVenta : detalle_venta) {
            if (detalle.getId_pizza() == detalle_ExisteEnVenta.getId_pizza()) {
                detalle_ExisteEnVenta.setCantidad(detalle_ExisteEnVenta.getCantidad() + detalle.getCantidad());
                existeEnLaLista = true;
          
                break;
            }
        }

        if (!existeEnLaLista) {
            detalle_venta.add(detalle);
            JOptionPane.showMessageDialog(null, "Producto agregado: " + detalle.getNombre());
        }
    }

    modelo.setRowCount(0);
    for (pizzaItem agregarATabla : detalle_venta) {
        DecimalFormat Decimal = new DecimalFormat("#,###");
        modelo.addRow(new Object[]{
            agregarATabla.getId_pizza(),
            agregarATabla.getNombre(),
            Decimal.format(agregarATabla.getValor()),
            agregarATabla.getCantidad(),
           Decimal.format(agregarATabla.getCantidad() * agregarATabla.getValor())     
        }); 
         tablaFinal.setModel(modelo);
    }
    
   
}
}
public boolean cantidadEnBusqueda(int cantidad, int id,List<pizzaItem> detalle_venta){
    boolean validarCant = true;
    conexion_postgres cn = new conexion_postgres();
    PreparedStatement pst;
    ResultSet rs;
    String query = "SELECT cant FROM pizzaitem where id_pizza = ?";
    int cantVenta = cantidad, stockBd=0;
   
    
    try {
        pst = cn.nuevaConexion().prepareStatement(query);
        pst.setInt(1, id);
        rs = pst.executeQuery();
        
        if(rs.next()){
         stockBd = rs.getInt("cant");
        }
        for(pizzaItem temporal : detalle_venta){
            if(temporal.getId_pizza()==id){
                stockBd = stockBd - temporal.getCantidad();
                break;
            }else{
         stockBd = rs.getInt("cant");                
           }
        }
        if(cantVenta>stockBd && stockBd>-1){
            validarCant = false;
            JOptionPane.showMessageDialog(null, "No hay suficiente stock para agregar la venta");
        }
        if(cantVenta<1){
             JOptionPane.showMessageDialog(null, "No hay suficiente stock para agregar la venta");
             validarCant = false;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return validarCant;
    
}

public void reducir_StockVenta(List<pizzaItem> lista_Detalle,JTable tablaVenta){
    int cantRestable=0,rowIndex = tablaVenta.getSelectedRow();
    int item;
    DefaultTableModel modelo = new DefaultTableModel();
    modelo = (DefaultTableModel)tablaVenta.getModel();
    
    item = (int) modelo.getValueAt(rowIndex,0);


     if(rowIndex!=-1){
        for(int i = 0; i<lista_Detalle.size();i++){
            if(item==lista_Detalle.get(i).getId_pizza()){
                
                if(lista_Detalle.get(i).getCantidad()>1){
                    
                cantRestable =lista_Detalle.get(i).getCantidad()-1;
                lista_Detalle.get(i).setCantidad(cantRestable);
                    modelo.setRowCount(0);
                    
                for (pizzaItem agregarATabla : lista_Detalle) {
                    DecimalFormat Decimal = new DecimalFormat("#,###");
                    modelo.addRow(new Object[]{
                        agregarATabla.getId_pizza(),
                        agregarATabla.getNombre(),
                        Decimal.format(agregarATabla.getValor()),
                        agregarATabla.getCantidad(),
                       Decimal.format(agregarATabla.getCantidad() * agregarATabla.getValor())     
                    });
                            }
                tablaVenta.setModel(modelo);
                break;
                }else{
                  JOptionPane.showMessageDialog(null, "La cantidad a vender no puede ser inferior a 1");
                
                }

            }
        }
     }else{
         JOptionPane.showMessageDialog(null, "Por favor seleccione una columna");
     }

    }
public void aumentar_StockVenta(List<pizzaItem> lista_Detalle,JTable tablaVenta){
    int cantSumable=0,rowIndex = tablaVenta.getSelectedRow();
    int item;

    DefaultTableModel modelo = new DefaultTableModel();
    modelo = (DefaultTableModel)tablaVenta.getModel();
    item = (int) modelo.getValueAt(rowIndex,0);


     if(rowIndex!=-1){
        for(int i = 0; i<lista_Detalle.size();i++){
            int cantidad = lista_Detalle.get(i).getCantidad();
            int id=lista_Detalle.get(i).getId_pizza();
            if(item==id){
    
                if(cantidadEnBusqueda(1, id, lista_Detalle)){
              cantSumable =lista_Detalle.get(i).getCantidad()+1;
                lista_Detalle.get(i).setCantidad(cantSumable);
                    modelo.setRowCount(0);
                    
                for (pizzaItem agregarATabla : lista_Detalle) {
                    DecimalFormat Decimal = new DecimalFormat("#,###");
                    modelo.addRow(new Object[]{
                        agregarATabla.getId_pizza(),
                        agregarATabla.getNombre(),
                        Decimal.format(agregarATabla.getValor()),
                        agregarATabla.getCantidad(),
                       Decimal.format(agregarATabla.getCantidad() * agregarATabla.getValor())     
                    });
                            }
                    tablaVenta.setModel(modelo); 
                }
                break;
               

            }
            
        }
     }else{
         JOptionPane.showMessageDialog(null, "Por favor seleccione una columna");
     }

    }

    public void totalVenta(JLabel total,JComboBox impuesto,JLabel iva, List<pizzaItem>lista_detale){
        DecimalFormat decimal = new DecimalFormat("#,###");
        double precioTotal = 0;
        float impuestoAgregado = Float.parseFloat((String) impuesto.getSelectedItem());
        System.out.println("iva :"+(String) impuesto.getSelectedItem());
        for(pizzaItem itemTemp : lista_detale){
            precioTotal+=(itemTemp.getValor()*itemTemp.getCantidad());
            System.out.println("Este es el valor: "+ itemTemp.getValor());
            System.out.println("Esta la cantidad: "+itemTemp.getCantidad());
        }
        impuestoAgregado=impuestoAgregado/100;
        impuestoAgregado*=precioTotal;
        System.out.println("El total es"+decimal.format(precioTotal));
        total.setText(decimal.format(precioTotal+impuestoAgregado));
        iva.setText(decimal.format(impuestoAgregado));
    }
             public void fnt_fechaActual(JLabel txt_fecha){
        try {
            Date fecha = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");  
            txt_fecha.setText(formatoFecha.format(fecha));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al establecer la fecha: " + e.getMessage());
        }
        
    } 
    public void usuarioConsultar(JTable tablaClientes, JTextField cliente) throws SQLException{
        conexion_postgres cn = new conexion_postgres();
        PreparedStatement pst = null;
        ResultSet rs = null;
        DefaultTableModel modelo = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                    return column==-1;
                    }
        }; 
        int i=1;
        
        if(tablaClientes.getModel()==null || tablaClientes.getRowCount()==0){
            modelo.addColumn("Id");
            modelo.addColumn("Nombre");
        }else{
            modelo = (DefaultTableModel) tablaClientes.getModel();
            modelo.setRowCount(0);
        }
        String query = "SELECT u.id_user, u.nombre_user FROM usuario as u where nombre_user ILIKE ? and tipo_user = 3";
        try {
            pst = cn.nuevaConexion().prepareStatement(query);
            pst.setString(1, "%"+cliente.getText()+"%" );
            rs = pst.executeQuery();
            
            while(rs.next()){
                modelo.addRow(new Object[]{rs.getInt("id_user"),rs.getString("nombre_user")});
          
            }
            tablaClientes.setModel(modelo);
            tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tablaClientes.setRowSelectionAllowed(true);
            tablaClientes.setColumnSelectionAllowed(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            pst.close();
            rs.close();
            cn.cerrarConexion();
            
        }
    }

       public void cobrar(List<pizzaItem>Lista,JLabel total,int id_vendedor, JTextField cliente, JLabel fecha,JComboBox iva,int id_cuadre){
           conexion_postgres cn = new conexion_postgres();
           CallableStatement cs =null;
           String query = "INSERT INTO venta (id_user,total_venta,fecha_venta,id_vendedor,iva,id_cuadre) VALUES (?,?,?,?,?,?) returning id_venta";
           ResultSet rs = null;
           DecimalFormat formatoDecimal = new DecimalFormat("#,###");
           
           String fechaTexto = fecha.getText();
           System.out.println("el total es:"+total);
            java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaTexto);
           try{
               cs = cn.nuevaConexion().prepareCall(query);
               cs.setInt(1, Integer.parseInt(cliente.getText()));
               cs.setDouble(2, Double.parseDouble((total.getText())));
               cs.setDate(3,fechaSQL);
               cs.setInt(4, id_vendedor);
               cs.setDouble(5,Integer.parseInt(iva.getSelectedItem().toString()));
               cs.setInt(6, id_cuadre);
               rs = cs.executeQuery();
               
               if(rs.next()){
                   int idfactura = rs.getInt("id_venta");
                   detalle_venta(Lista, idfactura);
               }

           }catch(SQLException e){
               System.out.println("Error cobrar "+e);
           }finally{
            JOptionPane.showMessageDialog(null, "Venta Completa");
               cn.cerrarConexion();
           }

           
       }
           public void detalle_venta(List<pizzaItem> listaDetalle,int idFactura){
           conexion_postgres cn = new conexion_postgres();
           CallableStatement cs =null;
           CallableStatement csupdate = null;
           String query = "INSERT INTO detalle_venta (id_pizza,"
                   + "cant_pizza,id_venta,precio_venta) VALUES (?,?,?,?)";
           String queryStockuptade = "UPDATE pizzaitem SET cant = cant - ? where id_pizza = ?";
     
           for(pizzaItem cd : listaDetalle){
            try{
               cs = cn.nuevaConexion().prepareCall(query);
               cs.setInt(1, cd.getId_pizza());
               cs.setInt(2, cd.getCantidad());
               cs.setInt(3, idFactura);
               cs.setDouble(4, cd.getValor());
               cs.executeUpdate();
               
               csupdate = cn.nuevaConexion().prepareCall(queryStockuptade);
               csupdate.setInt(1, cd.getCantidad());
               csupdate.setInt(2, cd.getId_pizza());
               csupdate.executeUpdate();
               JOptionPane.showConfirmDialog(null, "Venta realizada con éxtio");

           }catch(SQLException e){
               System.out.println("Error clase "+e);
           }finally{
                cn.cerrarConexion();
            }
               }
 
           }
           
           public void limpiarItem(JTable item,JTextField id,JTextField nombre,JTextField tipo,JTextField cantidad, JTextField valor){
               id.setText(null);
               nombre.setText(null);
               cantidad.setText("");
               tipo.setText(null);
               valor.setText("");
               item.removeAll();
           }
          public void limpiarTodo(List<pizzaItem> listaDetalle,JTextField idItem,JTextField nombreItem,JTextField tipoItem,JTextField cantidad, JTextField valor,
          JTextField clienteB, JTextField idCliente, JTextField nombreCliente, JLabel iva, JLabel valortotal, JTable tbl_venta,
          JTable tbl_item, JTable tbl_cliente){
               idItem.setText(null);
               nombreItem.setText(null);
               cantidad.setText(null);
               tipoItem.setText(null);
               valor.setText("----");
               clienteB.setText(null);
               idCliente.setText(null);
               nombreCliente.setText(null);
               iva.setText(null);
               valortotal.setText(null);
               listaDetalle.clear();
               
               //Limpiamos las tablas
               DefaultTableModel modeloVenta = (DefaultTableModel) tbl_venta.getModel();
               modeloVenta.setRowCount(0);
               tbl_venta.setModel(modeloVenta);
               
               DefaultTableModel modeloItem = (DefaultTableModel) tbl_item.getModel();
               modeloItem.setRowCount(0);
               tbl_item.setModel(modeloItem);
               
               DefaultTableModel modeloCliente = (DefaultTableModel) tbl_cliente.getModel();
               modeloCliente.setRowCount(0);
               tbl_cliente.setModel(modeloCliente);
           }
          
          public boolean validacionVenta(List<pizzaItem>listaDetalles, JTable venta, JTextField id_Cliente,
                  JTextField nombre_cliente,int id_Trabajador, JLabel fecha){
              //a pucha la wea vamos a ver primero si la caja está abierta
              conexion_postgres cn = new conexion_postgres();
              PreparedStatement pstm = null;
              ResultSet rs = null;
              int idCliente = Integer.parseInt(id_Cliente.getText());
              String nombreCliente = nombre_cliente.getText();
              String query = "SELECT estado_caja FROM caja where id_empleado=? AND"
                      + "fecha_cuadre = ?";
              
              try {
                  
              } catch (Exception e) {
              }
              
              return true;
          }
          public void buscarProveedor(JTable tablaProv, JTextField proov){
        conexion_postgres cn = new conexion_postgres();
        DefaultTableModel modelo = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return column==-1;
            }
        };
        String busqueda = proov.getText();
        modelo.addColumn("ID");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("TELEFONO");
        
  
        String sql = "SELECT id_user,nombre_user,telefono_user from usuario "
                + "where nombre_user ILIKE ? AND tipo_user=4";
                        System.out.println("Valor de búsqueda: '" + busqueda + "'");
                        System.out.println("SQL: " + sql);
        try {
            PreparedStatement pstm = cn.nuevaConexion().prepareStatement(sql);
            pstm.setString(1, "%"+busqueda+"%");
            ResultSet rs = pstm.executeQuery();
            
            while(rs.next()){
                Object[] datos = new Object[3];
               datos[0]=rs.getString("id_user");
               datos[1]=rs.getString("nombre_user");
               datos[2]=rs.getString("telefono_user");
               
                modelo.addRow(new Object[]{datos[0],datos[1],datos[2]});   
            }
            tablaProv.setModel(modelo);
            tablaProv.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tablaProv.setRowSelectionAllowed(true);
            tablaProv.setColumnSelectionAllowed(false);
        } catch (SQLException e) {
            System.out.println("Error al mostrar fun.mostratC: " + e);
        }finally{
            cn.cerrarConexion();
        }
    
}     
          
          //CARGOS
          
}



//