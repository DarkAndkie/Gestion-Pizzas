/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author yisus
 */
public class DetalleVenta {
    private int id_pizza;
    private String nombrepizza;
    private int cantidad_pizza;
    private double precio_venta;

    public int getId_pizza() {
        return id_pizza;
    }

    public String getNombrepizza() {
        return nombrepizza;
    }

    public int getCantidad_pizza() {
        return cantidad_pizza;
    }

    public double getPrecio_venta() {
        return precio_venta;
    }

    public DetalleVenta(int id_pizza, String nombrepizza, int cantidad_pizza, double precio_venta) {
        this.id_pizza = id_pizza;
        this.nombrepizza = nombrepizza;
        this.cantidad_pizza = cantidad_pizza;
        this.precio_venta = precio_venta;
    }


}
