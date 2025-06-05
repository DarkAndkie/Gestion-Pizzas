/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.pdfa.PdfADocument;
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
    public void instanciarfactura(List<modelo_Venta>lista_Ventas, JTable tbl_Reportes) throws SQLException, IOException{
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
         int idVenta = (int) tbl_Reportes.getValueAt((int)tbl_Reportes.getSelectedRow(),0);
         try{
             pstm=cn.nuevaConexion().prepareStatement(query);
             pstm.setInt(1, idVenta);
             clienteObj= instanciarCliente(idVenta);
             vendedorObj=instanciarVendedor(idVenta);
             rs = pstm.executeQuery();
             
             while(rs.next()){
                 lista_Detalle.add(new DetalleVenta(rs.getInt("id_pizza"), rs.getString("nombrepizza"), rs.getInt("cant_pizza"),
                         rs.getDouble("precio_venta")));
                 System.out.println("items encontrados"+lista_Detalle.getLast().getNombrepizza());
             }
             for(modelo_Venta ventaObj : lista_Ventas){
                 if(ventaObj.getId_venta()==idVenta){
                  crearPdf(ventaObj, lista_Detalle, lista_Pizza,clienteObj,vendedorObj);  
                  break;
                 }
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
 public void crearPdf(modelo_Venta ventasObj, List<DetalleVenta> lista_Detalle, 
                      List<pizzaItem> lista_Pizza, modelo_Cliente clienteObj,
                      modelo_vendedor vendedorObj) throws IOException {
    
    PdfWriter writer = new PdfWriter("Factura n° " + ventasObj.getId_venta() + ".pdf");
    PdfDocument pdfBase = new PdfDocument(writer);
    Document doc = new Document(pdfBase, PageSize.A4);
    

    doc.setMargins(50, 50, 50, 50);
    
    try {

        Table encabezado = new Table(new float[]{1, 2, 1}); 
        encabezado.setWidth(UnitValue.createPercentValue(100));
        encabezado.setMarginBottom(20);
        

        Cell celdaLogo = new Cell();
        try {
            ImageData logoPizz = ImageDataFactory.create(getClass().getResource("/iconos/Fazbear_entertainment.png.png"));
            Image logo = new Image(logoPizz).scaleToFit(60, 60);
            celdaLogo.add(logo);
        } catch (Exception e) {
            celdaLogo.add(new Paragraph("LOGO"));
        }
        celdaLogo.setBorder(Border.NO_BORDER);
        celdaLogo.setVerticalAlignment(VerticalAlignment.MIDDLE);
        
  
        Cell celdaTitulo = new Cell();
        Paragraph titulo = new Paragraph("Factura de Venta")
            .setFontSize(24)
            .setTextAlignment(TextAlignment.CENTER);
        celdaTitulo.add(titulo);
        celdaTitulo.setBorder(Border.NO_BORDER);
        celdaTitulo.setVerticalAlignment(VerticalAlignment.MIDDLE);
        
        // ESPACIO (celda derecha)
        Cell celdaEspacio = new Cell();
        celdaEspacio.setBorder(Border.NO_BORDER);
        
 
        encabezado.addCell(celdaLogo);
        encabezado.addCell(celdaTitulo);
        encabezado.addCell(celdaEspacio);
        
        doc.add(encabezado);
        
  
        Table infoFactura = new Table(new float[]{1, 1});
        infoFactura.setWidth(UnitValue.createPercentValue(100));
        infoFactura.setMarginBottom(20);

        Cell celdaEmpresa = new Cell();
        celdaEmpresa.add(new Paragraph("FAZBEAR ENTERTAINMENT").setFontSize(14));
        celdaEmpresa.add(new Paragraph("NIT: 123456789-0"));
        celdaEmpresa.add(new Paragraph("Tel: (555) 123-4567"));
        celdaEmpresa.setBorder(Border.NO_BORDER);
        celdaEmpresa.setVerticalAlignment(VerticalAlignment.TOP);
        
        // DATOS DE FACTURA (derecha)
        Cell celdaFactura = new Cell();
        celdaFactura.add(new Paragraph("FACTURA N°: " + ventasObj.getId_venta()));
        celdaFactura.add(new Paragraph("FECHA: " + ventasObj.getFecha()));
        celdaFactura.add(new Paragraph("VENDEDOR: " + vendedorObj.getName_Vendedor()));
        celdaFactura.setBorder(new SolidBorder(1));
        celdaFactura.setPadding(10);
        celdaFactura.setTextAlignment(TextAlignment.RIGHT);
        
        infoFactura.addCell(celdaEmpresa);
        infoFactura.addCell(celdaFactura);
        
        doc.add(infoFactura);

        Paragraph tituloCliente = new Paragraph("DATOS DEL CLIENTE")

            .setFontSize(12)
            .setMarginBottom(5);
        doc.add(tituloCliente);

        Paragraph datosCliente = new Paragraph()
            .add("NIT/ID: " + clienteObj.getId_vendedor()+ " | ")
            .add("Cliente: " + clienteObj.getName_Vendedor()+ "\n")
            .add("Teléfono: " + clienteObj.getTelefono() + " | ")
            .add("Dirección: " + clienteObj.getDireccion())
            .setFontSize(11)
            .setMarginBottom(20)
            .setBorder(new SolidBorder(1))
            .setPadding(10);
        doc.add(datosCliente);

        Paragraph tituloProductos = new Paragraph("DETALLE DE PRODUCTOS")

            .setFontSize(12)
            .setMarginBottom(10);
        doc.add(tituloProductos);
        

        float[] columnas = {60f, 200f, 80f, 100f, 100f}; 
        Table tablaItem = new Table(columnas);
        tablaItem.setWidth(UnitValue.createPercentValue(100));
        

        String[] encabezados = {"ID", "NOMBRE", "CANTIDAD", "PRECIO", "TOTAL"};
        for (String encabezao : encabezados) {
            Cell celdaEncabezado = new Cell()
                .add(new Paragraph(encabezao))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8)
                .setBorder(new SolidBorder(1));
            tablaItem.addHeaderCell(celdaEncabezado);
        }
        
     
        DecimalFormat formatoPrecio = new DecimalFormat("#,##0.000");

        for (DetalleVenta dtTemp : lista_Detalle) {
            System.out.println("item de venta:"+dtTemp.toString());
            Cell celdaId = new Cell()
                .add(new Paragraph(String.valueOf(dtTemp.getId_pizza())))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5)
                .setBorder(new SolidBorder(1));
            tablaItem.addCell(celdaId);
            
            Cell celdaNombre = new Cell()
                .add(new Paragraph(dtTemp.getNombrepizza()))
                .setTextAlignment(TextAlignment.LEFT)
                .setPadding(5)
                .setBorder(new SolidBorder(1));
            tablaItem.addCell(celdaNombre);

            Cell celdaCantidad = new Cell()
                .add(new Paragraph(String.valueOf(dtTemp.getCantidad_pizza())))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5)
                .setBorder(new SolidBorder(1));
            tablaItem.addCell(celdaCantidad);

            Cell celdaPrecio = new Cell()
                .add(new Paragraph("$" + formatoPrecio.format(dtTemp.getPrecio_venta())))
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5)
                .setBorder(new SolidBorder(1));
            tablaItem.addCell(celdaPrecio);

            Cell celdaTotal = new Cell()
                .add(new Paragraph("$" + formatoPrecio.format(dtTemp.getTotal_SubVenta())))
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5)
                .setBorder(new SolidBorder(1));
            tablaItem.addCell(celdaTotal);
        }
        
        doc.add(tablaItem);
        

        Table tablaTotales = new Table(new float[]{500f, 200f});
        tablaTotales.setWidth(UnitValue.createPercentValue(100));
        tablaTotales.setMarginTop(15);
        
        // Celda vacía (izquierda)
        Cell celdaVacia = new Cell()
            .setBorder(Border.NO_BORDER);
        
        // Celda con total (derecha)
        Cell celdaTotalFinal = new Cell()

            .add(new Paragraph("Valor del iva es del"+ventasObj.getIva()+"%"))
            .add(new Paragraph("TOTAL: $" + formatoPrecio.format(ventasObj.getTotal_venta()))
                .setFontSize(16))
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(new SolidBorder(2))
            .setPadding(10);
        
        tablaTotales.addCell(celdaVacia);
        tablaTotales.addCell(celdaTotalFinal);
        
        doc.add(tablaTotales);
        
        Paragraph piePagina = new Paragraph()
            .add("¡Gracias por su compra!\n")
            .add("Fecha de emisión: " + ventasObj.getFecha())
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(10)
            .setMarginTop(30);
        doc.add(piePagina);
        
    } finally {
        doc.close();
    }
}
}