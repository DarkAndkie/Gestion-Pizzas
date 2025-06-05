/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author JESUS
 */
public class modelo_Cliente extends modelo_vendedor {
    private String direccion;
    private String telefono;

    public modelo_Cliente(String direccion, String telefono, int id_vendedor, String name_Vendedor, String apelli_Vendedor) {
        super(id_vendedor, name_Vendedor, apelli_Vendedor);
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }
  
    
}
