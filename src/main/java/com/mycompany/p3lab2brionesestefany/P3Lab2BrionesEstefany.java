

package com.mycompany.p3lab2brionesestefany;

import Vista.MenuPrincipal;
import java.awt.EventQueue;
public class P3Lab2BrionesEstefany {

   public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MenuPrincipal frame = new MenuPrincipal();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }}
