/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbe_4815_final_project;
import java.awt.Color;
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
       //Trying to figure out how to draw
       JPanel canvasJPanel = main_ui.getCanvasPaintJPanel();
//       canvasJPanel.getGraphicsConfiguration();
//       
//       Graphics canvasGraphic = canvasJPanel.getGraphics();
//       canvasGraphic.setPaintMode();
//       canvasGraphic.setColor(Color.blue);
//       canvasGraphic.drawLine(40, 40, 100, 100);
//       canvasGraphic.fillRect(0, 0, 1000, 1000);
//      // canvasGraphic.drawRect(0, 0, 1000, 1000);
//       System.out.println(canvasGraphic.getClipBounds());
//       canvasJPanel.paintComponents(canvasGraphic);
//       canvasJPanel.paint(canvasGraphic);
      
    }
    
}
