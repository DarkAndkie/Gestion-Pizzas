/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package formularios;

import Controladores.agregacionesJdbc;
import Controladores.consultas;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import modelos.categoria;
import modelos.conexion_postgres;
import modelos.limpiarTxtfields;
import modelos.pizzaItem;



public class crearItem1 extends javax.swing.JFrame{
        
    
    DefaultTableModel itemTbl = new DefaultTableModel(){
    @Override
    public boolean isCellEditable(int row, int column){
        return column < 0;
    }
    
    };
     List<pizzaItem> listaPizza = new ArrayList<>();
    List<categoria> listCat = new ArrayList<>();
    pizzaItem pizActualizar = null;
    public crearItem1() {
        
        initComponents();
        consultas cs = new consultas();
        cs.rellenarComboCat(categoriaCmb, listCat);
        itemTbl.addColumn("id");
        itemTbl.addColumn("nombre");
        itemTbl.addColumn("tipo");
        itemTbl.addColumn("Tamaño");
        itemTbl.addColumn("categoria");
        itemTbl.addColumn("valor");
        eventoSelectFila();
        
        actualizarBtn.setVisible(false);
        eliminarBtn.setVisible(false);
    }
    pizzaItem crearPizzaItem(){

     consultas cd = new consultas();
     int id_cat=0;
     float utilidad = 0;
     String categoria = (String)categoriaCmb.getSelectedItem();
     float valor = Float.parseFloat(costoTxt.getText());
     pizzaItem pizza = new pizzaItem();
     
     //buscamos el id de la categoria seleccionada
     try{
     for(int i = 0; i<listCat.size();i++){  
         categoria ct = listCat.get(i);
         
         if(categoria.equals(ct.getNombreCat())){
             //pbtenemos el id de la categoria para su almacenamiento
             id_cat=ct.getId_cat();
             utilidad = ct.getUtilidad();
             
             //calculo aqui el valor del item correspondiente a la utilidad
             //no se multiplica por el porcentaje sino que directamente 
             //debemos tener el cuenta el valor del costo mas el costo mismo
             
             valor = valor / utilidad;
             valor = (float) ((Math.ceil(valor/1000))*1000);
             JOptionPane.showMessageDialog(null, "Valor: "+valor);
             pizza = new pizzaItem(nombre_Txt.getText(), tipoTxt.getText(),
             tamañoTxt.getText(), (int)cantidadSpn.getValue(),
             id_cat, valor, Float.parseFloat(costoTxt.getText()));
             break;
         } 
     }
     }catch(Exception e){
         JOptionPane.showMessageDialog(null, e);
     }finally{
         return pizza;
     }

    }
    
    void agregarAlaTabla(TableModel tablItem, pizzaItem pizza){

            
            Object tableRellen[] = new Object[]{pizza.getId_pizza(),
                pizza.getNombre(),pizza.getTipo_Pizz(),pizza.getTamañoPizza(),
                pizza.getId_categoría(),pizza.getValor()};
                itemTbl.addRow(tableRellen);
        
    }
    
    void eventoSelectFila(){
        jTable1.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e){
             if (jTable1.getSelectedRow() != -1) {
                try {
                    int filaSeleccionada = jTable1.getSelectedRow();
                    
                   
                    int idActualizar =(Integer.parseInt(jTable1.getValueAt(filaSeleccionada, 0).toString()));
                    
                    consultas cs = new consultas();
                    Object[] itemActualizar=null;
                    itemActualizar = cs.retornarITemporId(idActualizar, null, 
                        "SELECT * FROM pizzaitem WHERE id_pizza = ?");
                    
                    if (itemActualizar != null && itemActualizar.length > 0) {
                        pizzaItem pz = (pizzaItem) itemActualizar[0];
                        pizActualizar = pz;
                     
                        nombre_Txt.setText(pz.getNombre());
                        tipoTxt.setText(pz.getTipo_Pizz());
                        tamañoTxt.setText(pz.getTipo_Pizz());
                        cantidadSpn.setValue(pz.getCantidad());
                        costoTxt.setText(String.valueOf(pz.getCosto()));
                        cs.rellenarComboActualizar(categoriaCmb, listCat, pz.getId_categoría());
                    }
                } catch (Exception ex) {
                    // Manejo de errores
                    System.err.println("Error al seleccionar fila: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    });
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cantidadSpn = new javax.swing.JSpinner();
        categoriaCmb = new javax.swing.JComboBox<>();
        nombre_Txt = new javax.swing.JTextField();
        tamañoTxt = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        tipoTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        costoTxt = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable(itemTbl);
        busquedaTXxt = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        actualizarBtn = new javax.swing.JButton();
        eliminarBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel1.setText("Categoria:");

        jLabel2.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel2.setText("Nombre:");

        jLabel4.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel4.setText("Tamaño:");

        jLabel9.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel9.setText("Costo:");

        jLabel10.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel10.setText("Cantidad:");

        jButton3.setText("Crear");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel3.setText("tipo:");

        costoTxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        costoTxt.setText("0");
        costoTxt.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                costoTxtPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(costoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tipoTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                                    .addComponent(nombre_Txt))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addGap(170, 170, 170))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tamañoTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cantidadSpn, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(categoriaCmb, javax.swing.GroupLayout.Alignment.TRAILING, 0, 272, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(119, 119, 119)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombre_Txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tipoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(36, 36, 36)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(categoriaCmb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(tamañoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cantidadSpn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(35, 35, 35)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(costoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jButton3)
                .addContainerGap(76, Short.MAX_VALUE))
        );

        jTable1.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Busqueda");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        actualizarBtn.setText("Actualizar");
        actualizarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarBtnActionPerformed(evt);
            }
        });

        eliminarBtn.setText("Eliminar");
        eliminarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(busquedaTXxt, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 109, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(actualizarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(eliminarBtn)
                        .addGap(146, 146, 146))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(busquedaTXxt)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(actualizarBtn)
                    .addComponent(eliminarBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
     //vamos a tomar el id de la cat

    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        agregacionesJdbc ag = new agregacionesJdbc();
        consultas cs = new consultas();
        pizzaItem pizzI = new pizzaItem();
        limpiarTxtfields clean = new limpiarTxtfields();
        if(!nombre_Txt.getText().equals("") && !tipoTxt.getText().equals("")
                && !tamañoTxt.getText().equals("") &&
                (int)cantidadSpn.getValue()>0 && (int)cantidadSpn.getValue()<100
                &&!costoTxt.getText().equals("")){
            try {
                pizzI = crearPizzaItem();
                ag.insertarPizza(pizzI);   
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error "+e,"error",JOptionPane.ERROR_MESSAGE);
            }finally{
                agregarAlaTabla(itemTbl, pizzI);
                clean.limpiar_txt(jPanel6);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void costoTxtPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_costoTxtPropertyChange
        Double validador= null;
        validador= Double.valueOf(costoTxt.getText());
        if(validador==null){
            JOptionPane.showMessageDialog(null, "Error, solo se aceptan valores numericos");
            costoTxt.setText("");
        }
    }//GEN-LAST:event_costoTxtPropertyChange

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
    
    if(!busquedaTXxt.getText().equals("")){
        
        String nombreBusqueda ="%"+ busquedaTXxt.getText()+"%";//porcentajes para indicar que de inicio a fin
        //se pueda obtener por valores mas similares
        consultas cs = new consultas();
        Object[] objPizza = null;
        pizzaItem pz = new pizzaItem();
        String query ="SELECT *FROM PIZZAITEM where nombrepizza like ?";
        objPizza = cs.retornarITem(nombreBusqueda, objPizza,query);
        itemTbl.setRowCount(0);
            //enviamos cada elemento encontrado con ese nombre
                if(objPizza!=null && objPizza.length>0 ){
                for(int i = 0; i<objPizza.length;i++){
                  pz = (pizzaItem) objPizza[i];
                  agregarAlaTabla(itemTbl, pz);
                }
                  actualizarBtn.setVisible(true);
                  eliminarBtn.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(null, "No se pudo encontrar el item","No encontrado",
                               JOptionPane.INFORMATION_MESSAGE);
                }
    }else{
        JOptionPane.showMessageDialog(null, "Prueba con escribir lo que necesitas buscar!");
    }
    
    }//GEN-LAST:event_jLabel5MouseClicked

    private void actualizarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarBtnActionPerformed
        int id_temporal = 0;
        agregacionesJdbc ag = new agregacionesJdbc();
        limpiarTxtfields clean = new limpiarTxtfields();
        
        if(!nombre_Txt.getText().equals("") && !tipoTxt.getText().equals("")
                && !tamañoTxt.getText().equals("") &&
                (int)cantidadSpn.getValue()>0 && (int)cantidadSpn.getValue()<100
                &&!costoTxt.getText().equals("")){
            try {
                id_temporal = pizActualizar.getId_pizza();
                pizActualizar = crearPizzaItem();
                pizActualizar.setId_pizza(id_temporal);
                ag.actualizarPizza(pizActualizar);   
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error "+e,"error",JOptionPane.ERROR_MESSAGE);
            }finally{
                itemTbl.setRowCount(0);
                agregarAlaTabla(itemTbl, pizActualizar);
                clean.limpiar_txt(jPanel6);
                pizActualizar = null;
                
            }
        }
       
    }//GEN-LAST:event_actualizarBtnActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
      

    }//GEN-LAST:event_jTable1MouseClicked

    private void eliminarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarBtnActionPerformed
        agregacionesJdbc ag = new agregacionesJdbc();
        limpiarTxtfields clean = new limpiarTxtfields();
       int filaSeleccionada = jTable1.getSelectedRow();
       int idEliminarPizza = (int) jTable1.getValueAt(filaSeleccionada, 0);
       ag.eliminarItem(idEliminarPizza);
       itemTbl.setRowCount(0);
       clean.limpiar_txt(jPanel6);
      
    }//GEN-LAST:event_eliminarBtnActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(categoriaFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(categoriaFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(categoriaFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(categoriaFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new categoriaFrm().setVisible(true);
                
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizarBtn;
    private javax.swing.JTextField busquedaTXxt;
    private javax.swing.JSpinner cantidadSpn;
    private javax.swing.JComboBox<String> categoriaCmb;
    private javax.swing.JTextField costoTxt;
    private javax.swing.JButton eliminarBtn;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField nombre_Txt;
    private javax.swing.JTextField tamañoTxt;
    private javax.swing.JTextField tipoTxt;
    // End of variables declaration//GEN-END:variables

    private void setClosable(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
