/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author yisus
 */
public class pizzaItem {
    protected int id_pizza = 0;
    protected String nombre;
    protected String tipo_Pizz;
    protected String tamañoPizza;
    protected int cantidad;
    protected int id_categoría;
    protected double valor;
    
    public int getId_pizza() {
        return id_pizza;
    }

    public void setId_pizza(int id_pizza) {
        this.id_pizza = id_pizza;
    }

    public pizzaItem(String nombre, int cantidad, int id_pizza, double valor) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.id_pizza = id_pizza;
        this.valor = valor;
    }
    protected float costo;

    public pizzaItem() {
    }

    public pizzaItem(String nombre, String tipo_Pizz, String tamañoPizza,int cantidad, int id_categoría, float valor, float costo) {
        this.nombre = nombre;
        this.tipo_Pizz = tipo_Pizz;
        this.tamañoPizza=tamañoPizza;
        this.cantidad = cantidad;
        this.id_categoría = id_categoría;
        this.valor = valor;
        this.costo = costo;
    }

    public String getTamañoPizza() {
        return tamañoPizza;
    }

    public void setTamañoPizza(String tamañoPizza) {
        this.tamañoPizza = tamañoPizza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo_Pizz() {
        return tipo_Pizz;
    }

    public void setTipo_Pizz(String tipo_Pizz) {
        this.tipo_Pizz = tipo_Pizz;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_categoría() {
        return id_categoría;
    }

    public void setId_categoría(int id_categoría) {
        this.id_categoría = id_categoría;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }
    
    
}
