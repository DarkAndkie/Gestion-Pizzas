/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author yisus
 */
public class categoria {
    private int id_cat;

    public categoria(int id_cat, String nombreCat, float utilidad) {
        this.id_cat = id_cat;
        this.nombreCat = nombreCat;
        this.utilidad = utilidad;
    }
    private String nombreCat;
    private float utilidad;

    public String getNombreCat() {
        return nombreCat;
    }

    public void setNombreCat(String nombreCat) {
        this.nombreCat = nombreCat;
    }

    public float getUtilidad() {
        return utilidad;
    }

    public int getId_cat() {
        return id_cat;
    }


    }




