/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.util.Date;

/**
 *
 * @author JESUS
 */
public class modelo_Venta {
    private int id_venta;
    private Date fecha;
    private int id_Cliente;
    private int id_Vendedor;
    private int iva;
    private double total_venta;

    public modelo_Venta(int id_venta,Date fecha, int id_Cliente, double total_venta) {
        this.id_venta = id_venta;
        this.fecha=fecha;
        this.id_Cliente = id_Cliente;
        this.total_venta = total_venta;
    }

    public int getId_venta() {
        return id_venta;
    }

    public Date getFecha() {
        return fecha;
    }

    public int getId_Cliente() {
        return id_Cliente;
    }

    public int getId_Vendedor() {
        return id_Vendedor;
    }

    public int getIva() {
        return iva;
    }

    public double getTotal_venta() {
        return total_venta;
    }

    public modelo_Venta(int id_venta, Date fecha, int id_Cliente, int id_Vendedor, int iva, double total_venta) {
        this.id_venta = id_venta;
        this.fecha = fecha;
        this.id_Cliente = id_Cliente;
        this.id_Vendedor = id_Vendedor;
        this.iva = iva;
        this.total_venta = total_venta;
    }
}
