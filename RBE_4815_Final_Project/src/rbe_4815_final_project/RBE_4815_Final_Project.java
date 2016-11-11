/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbe_4815_final_project;
import java.awt.Graphics;
import javax.swing.JPanel;
/**
 *
 * @author motmo
 */
public class RBE_4815_Final_Project {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       MainUI main_ui = new MainUI();
       main_ui.setVisible(true);
       JPanel canvasJPanel = main_ui.getCanvasJPanel();
       Graphics canvasGraphic = canvasJPanel.getGraphics();
       canvasGraphic.drawLine(40, 40, 100, 100);
       canvasJPanel.paintAll(canvasGraphic);
       
    }
    
}
