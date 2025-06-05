/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.sql.*;
import javax.sound.midi.Soundbank;

public class conexion_postgres {
        Connection a = null;
    
    //Las variables que van a almacenar la dirección de la db
    String jdbcPostgres = "jdbc:postgresql://localhost:5432/freddy";
    String userPostgres = "postgres";
    String contraseña = "Chucho2003";
    
    public Connection nuevaConexion(){

        try{
            a = DriverManager.getConnection(jdbcPostgres,userPostgres,contraseña);
            System.out.println("Conectado a la db postgres");
        }catch(SQLException e){
            System.out.println("Error al establecer la conexión" + e);
        }
        return a;
    }
    
    public void cerrarConexion(){
        try{
        if(a!=null && !a.isClosed()){
            a.close();
            System.out.println("Conexión cerrada");
        }
        }catch(SQLException e){
            System.out.println("Error al cerrar la conexión "+e);
        }
    }
}
