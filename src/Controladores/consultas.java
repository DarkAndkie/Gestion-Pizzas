/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;


import formularios.Venta_Diaria;
import formularios.home;
import formularios.home_empleado;
import formularios.inicioSesiónFrm;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import modelos.conexion_postgres;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import modelos.categoria;
import modelos.pizzaItem;

/**
 *
 * @author yisus
 */
public class consultas {

           public void inicio_sesion(inicioSesiónFrm inicioS,JTextField user, JPasswordField passwo) throws NoSuchAlgorithmException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException{
        
        conexion_postgres cn = new conexion_postgres();
        String query = "Select u.id_user, u.contraseña, u.tipo_user "
                + "from usuario u where"
                + " u.id_user = ? ";
        PreparedStatement pst;
        ResultSet rs;
        String pass = new String(passwo.getPassword());
        try {
            pst = cn.nuevaConexion().prepareStatement(query);
            pst.setInt(1, Integer.parseInt(user.getText())); 
            rs = pst.executeQuery();
        if(rs.next()){
            String pssDesencrip = rs.getString("contraseña");
            pssDesencrip =  new String(decrypt(pssDesencrip));
            
            JOptionPane.showMessageDialog(null, "Enviada: "+pass+" y"
            + " cesencriptada: "+ pssDesencrip);
            if(pass.equals(pssDesencrip)){
            switch(rs.getInt("tipo_user")){
                case 1 -> {
                    home a = new home();
                    a.setLocationRelativeTo(null);
                    a.setVisible(true);
                    
                    inicioS.dispose();
                    }
                case 2 -> {
                    home_empleado homeE = new home_empleado();
                    homeE.setLocationRelativeTo(null);
                    homeE.setVisible(true);
                    homeE.recibirId(rs.getInt("id_user"));
                    inicioS.dispose();
                    }
                default -> {
                    JOptionPane.showMessageDialog(null, "Credenciales o nivel de acceso invalido");
                }
            }
            }else{
                JOptionPane.showMessageDialog(null,"Contraseña inválida","ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Credenciales incorrectas","Invalid",JOptionPane.CANCEL_OPTION);
        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos "+ e, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        cn.cerrarConexion();
    }
 
      public void rellenarComboActualizar(JComboBox cmb, List<categoria> lCat, int id_cat){
       conexion_postgres cn = new conexion_postgres();
       cmb.removeAllItems();
       Statement st = null;
       ResultSet rs = null;
       String query= "SELECT *FROM categoria";
       categoria temporal = null;
       try {
           st = cn.nuevaConexion().createStatement();
           rs = st.executeQuery(query);
           
           while(rs.next()){ 
                
               temporal = new categoria(rs.getInt("id_categoria"), rs.getString("nombre_cat")
               , rs.getFloat("utilidad"));
               
               lCat.add(temporal);
               cmb.addItem(rs.getString("nombre_cat"));
               
               for(int i = 0; i<lCat.size();i++){
                   if(id_cat==lCat.get(i).getId_cat()){
                       cmb.setSelectedItem(lCat.get(i).getNombreCat());
                   }
               }
           }
       } catch (SQLException e) {
           JOptionPane.showMessageDialog(null, "Error " + e, "ERROR", JOptionPane.ERROR_MESSAGE);
       }

       cn.cerrarConexion();
   }
   public void rellenarComboCat(JComboBox cmb, List<categoria> lCat){
       conexion_postgres cn = new conexion_postgres();
       cmb.removeAllItems();
       Statement st = null;
       ResultSet rs = null;
       String query= "SELECT *FROM categoria";
       
       try {
           st = cn.nuevaConexion().createStatement();
           rs = st.executeQuery(query);
           while(rs.next()){
               categoria temporal = new categoria(rs.getInt("id_categoria"), rs.getString("nombre_cat")
               , rs.getFloat("utilidad"));
           lCat.add(temporal);
           cmb.addItem(rs.getString("nombre_cat"));
           }
       } catch (SQLException e) {
           JOptionPane.showMessageDialog(null, "Error " + e, "ERROR", JOptionPane.ERROR_MESSAGE);
       }

       cn.cerrarConexion();
   }
       public boolean buscarUnicoEle(String tabla, String condicion ,String columna){
        conexion_postgres cn = new conexion_postgres();
        ResultSet rs = null;
        Statement st = null;
        boolean a = false;
        String query = "Select "+columna+" from " + tabla + " where "+condicion + ";";
        
           try {
               st = cn.nuevaConexion().createStatement();
               rs = st.executeQuery(query);
               
               if(rs.next()){
                   int valorid= rs.getInt(columna);
                   a = true;
               }
           } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "Error "+e);
           }finally{   
                 cn.cerrarConexion();
                return a;
           }
 
    }
        public void obtenerNombreCat( int id, String a){
        conexion_postgres cn = new conexion_postgres();
        ResultSet rs = null;
        Statement st = null;
        String query = "SELECT *FROM categoria WHERE id_categoria = "+ id;
        
           try {
               st = cn.nuevaConexion().createStatement();
               rs = st.executeQuery(query);
               int i = 0;
              if(rs.next()){
                  a = rs.getString("nombre_cat");
              }
               
           } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "Error "+e);
           }finally{   
                 cn.cerrarConexion();
           }
        }
    public Object[] retornarITem( String condicion, Object[] a, String query){
        conexion_postgres cn = new conexion_postgres();
        ResultSet rs = null;
        PreparedStatement pstm = null;
        List<pizzaItem>pzL = new ArrayList<>();
      
        
           try {
               pstm = cn.nuevaConexion().prepareStatement(query);
               pstm.setString(1,condicion);
               rs = pstm.executeQuery();
              
              
               while(rs.next()){
                   pizzaItem pizza = new pizzaItem();
                   pizza.setId_pizza(rs.getInt("id_pizza"));
                   pizza.setNombre(rs.getString("nombrepizza"));
                   pizza.setTipo_Pizz(rs.getString("tipopizza"));
                   pizza.setTamañoPizza(rs.getString("tamañopizza"));
                   pizza.setCantidad(rs.getInt("cant"));
                   pizza.setId_categoría(rs.getInt("id_categoria"));
                   pizza.setValor(rs.getFloat("valor"));
                   pizza.setCosto(rs.getFloat("costo"));
                   pzL.add(pizza);
               }
               
           } catch (SQLException e) {
              
               JOptionPane.showMessageDialog(null, "Error "+e);
           }finally{   
                 cn.cerrarConexion(); 
           }

           return pzL.toArray(new pizzaItem[0]);
}
      public Object[] retornarITemporId( int condicion, Object[] a, String query){
        conexion_postgres cn = new conexion_postgres();
        ResultSet rs = null;
        PreparedStatement pstm = null;
        List<pizzaItem>pzL = new ArrayList<>();
      
        
           try {
               pstm = cn.nuevaConexion().prepareStatement(query);
               pstm.setInt(1,condicion);
               rs = pstm.executeQuery();
              
              
               while(rs.next()){
                   pizzaItem pizza = new pizzaItem();
                   pizza.setId_pizza(rs.getInt("id_pizza"));
                   pizza.setNombre(rs.getString("nombrepizza"));
                   pizza.setTipo_Pizz(rs.getString("tipopizza"));
                   pizza.setTamañoPizza(rs.getString("tamañopizza"));
                   pizza.setCantidad(rs.getInt("cant"));
                   pizza.setId_categoría(rs.getInt("id_categoria"));
                   pizza.setValor(rs.getFloat("valor"));
                   pizza.setCosto(rs.getFloat("costo"));
                   pzL.add(pizza);
               }
               
           } catch (SQLException e) {
              
               JOptionPane.showMessageDialog(null, "Error "+e);
           }finally{   
                 cn.cerrarConexion(); 
           }

           return pzL.toArray(new pizzaItem[0]);
}
      
 public String decrypt(String pass) throws NoSuchAlgorithmException,
        InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException,
        IllegalBlockSizeException, BadPaddingException {
    

    String ALGORITHM = "AES/CBC/PKCS5Padding";
    String SECRET_KEY = "ClaveSecreta1599";
    
    
    byte[] encryptedIVAndText = Base64.getDecoder().decode(pass);
    
   
    byte[] iv = new byte[16];
    System.arraycopy(encryptedIVAndText, 0, iv, 0, iv.length);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    
    // Extraer el texto cifrado (los bytes restantes después del IV)
    byte[] encrypted = new byte[encryptedIVAndText.length - iv.length];
    System.arraycopy(encryptedIVAndText, iv.length, encrypted, 0, encrypted.length);
    
    SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
    

    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
    
 
    byte[] decrypted = cipher.doFinal(encrypted);
    
   
    return new String(decrypted);
}     
      
    
}
