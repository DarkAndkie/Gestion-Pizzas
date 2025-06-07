/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import modelos.conexion_postgres;
import modelos.pizzaItem;
import java.sql.*;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author yisus
 */
public class agregacionesJdbc {
    
    public void insertarPizza(pizzaItem a) throws SQLException{
        conexion_postgres cn = new conexion_postgres();
        PreparedStatement ps = null;
        String query = "INSERT INTO pizzaitem (nombrepizza,tipopizza,"
                + "tamañopizza,cant,id_categoria,valor,costo)values (?,?,?,?,?,?,?)";
        ResultSet rs = null;
        try {
            ps = cn.nuevaConexion().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);      
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getTipo_Pizz());
            ps.setString(3, a.getTamañoPizza());
            ps.setInt(4, a.getCantidad());
            ps.setInt(5, a.getId_categoría());
            ps.setDouble(6, a.getValor());
            ps.setFloat(7, a.getCosto());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if(rs.next()){
            a.setId_pizza(rs.getInt(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }finally{
            ps.close();
            cn.cerrarConexion();
        }
    }
     
       
    public void actualizarPizza(pizzaItem a) throws SQLException{
        conexion_postgres cn = new conexion_postgres();
        PreparedStatement ps = null;
        String query = "UPDATE pizzaitem SET nombrepizza=?,tipopizza=?,"
                + "tamañopizza=?,cant=?,id_categoria=?,valor=?,costo=? where id_pizza = ?";
        ResultSet rs = null;
        try {
            ps = cn.nuevaConexion().prepareStatement(query);      
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getTipo_Pizz());
            ps.setString(3, a.getTamañoPizza());
            ps.setInt(4, a.getCantidad());
            ps.setInt(5, a.getId_categoría());
            ps.setDouble(6, a.getValor());
            ps.setFloat(7, a.getCosto());
            ps.setInt(8, a.getId_pizza());
            ps.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }finally{
            ps.close();
            cn.cerrarConexion();
        }
    }
    
    public void eliminarItem(int id){
        conexion_postgres cn = new conexion_postgres();
        PreparedStatement ps = null;
        String query = "DELETE FROM pizzaitem where id_pizza = ?";
        try {
            ps = cn.nuevaConexion().prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Eliminado Correctamente","Succes",JOptionPane.YES_OPTION);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void insertarCat(String name, int utilidad) throws SQLException{
       conexion_postgres cn =  new conexion_postgres();
       PreparedStatement ps = null;
        System.out.println("utilidad al llegar es"+utilidad);
       String query = "INSERT INTO categoria (nombre_cat, utilidad) values (?,?)";
       float utilidadReal = (float)(100 - utilidad)/100;
        System.out.println("al convertirse es"+ utilidadReal);
        try {
            ps = cn.nuevaConexion().prepareStatement(query);
            ps.setString(1, name);
            ps.setDouble(2, utilidadReal);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "agregado correctamente" + " Utilidad:"+utilidadReal);
        } catch (Exception e) {
            System.out.println("Categoria erro al insertar:" +e);
        }finally{
            cn.cerrarConexion();
        }
    }
      public void crear_usuario(JTextField id, JTextField name, JTextField apellidos,
              JTextField direccion, JTextField telefono, JComboBox user, JTextField contraseña) throws NoSuchPaddingException, InvalidAlgorithmParameterException{
      int r = 0;
      String password = contraseña.getText();
      
      
     switch((String)user.getSelectedItem()){
         case "Administrador":
             r = 1;
             break;
         case "Trabajador":
             r = 2;
            break;
         case "Cliente":
             r = 3;
             password = null;
             break;
         case "Proveedor":
             r= 4;
             password = null;
             break;
     }
        
      
        conexion_postgres cn = new conexion_postgres();
        String query = "INSERT INTO usuario (id_user,nombre_user,apellidos_user,"
              + "direccion_user,telefono_user,tipo_user,contraseña)values(?,?,?,?,?,?,?)";
      PreparedStatement pst;
      ResultSet rs;
      try {
          pst = cn.nuevaConexion().prepareStatement(query);
          pst.setInt(1, Integer.parseInt(id.getText()));
          pst.setString(2, name.getText());
          pst.setString(3, apellidos.getText());
          pst.setString(4, direccion.getText());
          pst.setString(5, telefono.getText());
          pst.setInt(6, r);
          if(password==null){
              pst.setString(7, password);
          }else{
          pst.setString(7, encrypt(password));
          }
         pst.executeUpdate();
         JOptionPane.showMessageDialog(null, "Se ha creado exitosamente el nuevo usuario: "
         ,"CREADO", JOptionPane.OK_OPTION);
      } catch (SQLException e) {
          JOptionPane.showConfirmDialog(null, "Error al guardar usuario: " + e, 
                  "ERROR", JOptionPane.ERROR_MESSAGE);
      } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(agregacionesJdbc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(agregacionesJdbc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(agregacionesJdbc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(agregacionesJdbc.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
      cn.cerrarConexion();
      }
      }
      
public String encrypt(String pass) throws NoSuchAlgorithmException, InvalidKeyException, 
        IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException {
    

    String ALGORITHM = "AES/CBC/PKCS5Padding";
    String SECRET_KEY = "ClaveSecreta1599"; 
    
    SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    

    byte[] iv = new byte[16];
    SecureRandom random = new SecureRandom();
    random.nextBytes(iv);
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    
 
    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
 
    byte[] encrypted = cipher.doFinal(pass.getBytes());
    

    byte[] encryptedIVAndText = new byte[iv.length + encrypted.length];
    System.arraycopy(iv, 0, encryptedIVAndText, 0, iv.length);
    System.arraycopy(encrypted, 0, encryptedIVAndText, iv.length, encrypted.length);

    return Base64.getEncoder().encodeToString(encryptedIVAndText);
}
      


}
