/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author yisus
 */
public class limpiarTxtfields {
    
    public void limpiar_txt(JPanel panelGeneric){
        //vamos a obtener en un for los elementos dfel jpanel que mandemos
        //en este momento si validamos que puede instanmciarse a un txtfield 
        //lo convertimos y le hacemos unsetText para limpiarlo.
        
        for(int i = 0;panelGeneric.getComponentCount()>i ;i++){
            if(panelGeneric.getComponent(i) instanceof JTextField){
                ((JTextField)panelGeneric.getComponent(i)).setText("");
            }else if(panelGeneric.getComponent(i) instanceof JSpinner){
                ((JSpinner)panelGeneric.getComponent(i)).setValue(1);
            }
        }
    }
}
