/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.gestioncontactos;

import com.gestioncontactos.vista.vistaContactos;
import controlador.logica_ventana;

/**
 *
 * @author Willian
 */
public class GestionContactos {

    public static void main(String[] args) {
        
        vistaContactos vista = new vistaContactos();
        logica_ventana controlador = new logica_ventana(vista);
        controlador.iniciaControl();
        
    }
}
