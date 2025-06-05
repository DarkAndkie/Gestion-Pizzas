/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author JESUS
 */
public class modelo_vendedor {
    private int id_vendedor;
    private String name_Vendedor;
    private String apelli_Vendedor;

    public modelo_vendedor(int id_vendedor, String name_Vendedor, String apelli_Vendedor) {
        this.id_vendedor = id_vendedor;
        this.name_Vendedor = name_Vendedor;
        this.apelli_Vendedor = apelli_Vendedor;
    }

    public int getId_vendedor() {
        return id_vendedor;
    }

    public String getName_Vendedor() {
        return name_Vendedor;
    }

    public String getApelli_Vendedor() {
        return apelli_Vendedor;
    }

    
}
