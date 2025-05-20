/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.gestioncontactos;

import com.formdev.flatlaf.FlatDarkLaf;
import com.gestioncontactos.vista.vistaContactos;
import controlador.logica_ventana;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Willian
 */
public class GestionContactos {

    public static void main(String[] args) {
        /*try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        vistaContactos vista = new vistaContactos();
        logica_ventana controlador = new logica_ventana(vista);
        controlador.iniciaControl();
        
    }*/

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        vistaContactos vista = new vistaContactos();
        logica_ventana controlador = new logica_ventana(vista);
        controlador.iniciaControl();

    }
}
