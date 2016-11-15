/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbe_4815_final_project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Michael McConnell
 * The contents of this class were based heavily on the SimplePaint Applet developed at
 * http://math.hws.edu/eck/cs124/javanotes6/source/SimplePaint.java
 */
public class PaintJPanel extends javax.swing.JPanel
    implements MouseListener, MouseMotionListener {

    /**
     * Creates new form PaintJPanel
     */
    public PaintJPanel() {
        initComponents();
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables



    /* The following variables are used when the user is sketching a
     curve while dragging a mouse. */

    private int prevX, prevY, prevDomX, prevDomY;     // The previous location of the mouse.

    private boolean dragging;      // This is set to true while the user is drawing.

    private Graphics graphicsForDrawing;  // A graphics context for the panel
                                // that is used to draw the user's curve.
    private static final int DOMINOE_DISTANCE = 10;
    
    private static final int AVERAGING_SIZE = 10;
    
    private int movingSumX = 0;
    private int movingSumY = 0;
    
    private LinkedList<Domino> dominoQueue = new LinkedList<Domino>();
    
    private LinkedList<Point> prevPointQueue = new LinkedList<Point>(); 

    /**
     * Draw the contents of the panel.  Since no information is
     * saved about what the user has drawn, the user's drawing
     * is erased whenever this routine is called.
     */
    @Override
    public void paintComponent(Graphics g) {

       super.paintComponent(g);  // Fill with background color (white).
    }

    /**
      * This routine is called in mousePressed when the user clicks on the drawing area.
        * It sets up the graphics context, graphicsForDrawing, to be used to draw the user's 
        * sketch in the current color.
      */
    private void setUpDrawingGraphics() {
       graphicsForDrawing = getGraphics();
       graphicsForDrawing.setColor(Color.BLUE);
    }

    /**
     * This is called when the user presses the mouse anywhere in the applet.  
     * There are three possible responses, depending on where the user clicked:  
     * Change the current color, clear the drawing, or start drawing a curve.  
     * (Or do nothing if user clicks on the border.)
     */
    @Override
    public void mousePressed(MouseEvent evt) {

       int x = evt.getX();   // x-coordinate where the user clicked.
       int y = evt.getY();   // y-coordinate where the user clicked.

       int width = getWidth();    // Width of the panel.
       int height = getHeight();  // Height of the panel.

       if (dragging == true)  // Ignore mouse presses that occur
          return;             //    when user is already drawing a curve.
                              //    (This can happen if the user presses
                              //    two mouse buttons at the same time.)
       else {
             // The user has clicked on the white drawing area.
             // Start drawing a curve from the point (x,y).
            prevX = x;
            prevY = y;
            prevDomX = x;
            prevDomY = y;
            dragging = true;
            movingSumX = x;
            movingSumY = y;
            prevPointQueue.clear();
            prevPointQueue.add(new Point(x,y));
            setUpDrawingGraphics();
       }
    }


    /**
     * Called whenever the user releases the mouse button. If the user was drawing 
     * a curve, the curve is done, so we should set drawing to false and get rid of
     * the graphics context that we created to use during the drawing.
     */
    @Override
    public void mouseReleased(MouseEvent evt) {
       if (dragging == false)
          return;  // Nothing to do because the user isn't drawing.
       dragging = false;
       graphicsForDrawing.dispose();
       graphicsForDrawing = null;
    }


    /**
     * Called whenever the user moves the mouse while a mouse button is held down.  
     * If the user is drawing, draw a line segment from the previous mouse location 
     * to the current mouse location, and set up prevX and prevY for the next call.  
     * Note that in case the user drags outside of the drawing area, the values of
     * x and y are "clamped" to lie within this area.  This avoids drawing on the color 
     * palette or clear button.
     */
    @Override
    public void mouseDragged(MouseEvent evt) {

       if (dragging == false)
          return;  // Nothing to do because the user isn't drawing.

       int x = evt.getX();   // x-coordinate of mouse.
       int y = evt.getY();   // y-coordinate of mouse.
       
       if (prevPointQueue.size() >= AVERAGING_SIZE) {
           Point oldPoint = prevPointQueue.pollFirst();
           movingSumX -= oldPoint.x;
           movingSumY -= oldPoint.y;
           
           movingSumX += x;
           movingSumY += y;
           Point averagedPoint = new Point(movingSumX / AVERAGING_SIZE, movingSumY / AVERAGING_SIZE);//computeAverage(prevPointQueue);
           x = averagedPoint.x;
           y = averagedPoint.y;
       }
       else {
           movingSumX += x;
           movingSumY += y;
       }
       
       graphicsForDrawing.setColor(Color.BLUE);
       graphicsForDrawing.drawLine(prevX, prevY, x, y);  // Draw the line.
       
       int dX = x - prevDomX;//prevX;
       int dY = y - prevDomY;//prevY;
       
       if (Math.sqrt((dX*dX) + (dY*dY)) >= DOMINOE_DISTANCE) {
           dominoQueue.add(new Domino(x, y, 0));
           graphicsForDrawing.drawOval(x, y, 4, 4);
           
           double slope = 999999;
           if (dX != 0) {
               slope = (double)dY / (double)dX;
           }
           System.out.println(slope);
           int b = prevDomY - (int)(slope*prevDomX);
           graphicsForDrawing.drawLine( prevDomX - 10, (int)(slope*(prevDomX-10)) + b, prevDomX + 10, (int)(slope*(prevDomX+10)) + b);
           prevDomX = x;
           prevDomY = y;
       }
       prevX = x;  // Get ready for the next line segment in the curve.
       prevY = y;
    }

    private Point computeAverageSlope(List<Point> points) {
        int sumX = 0;
        int sumY = 0;
        for (int i = 0; i < points.size(); i++) {
            sumX += points.get(i).x;
            sumY += points.get(i).y;
        }
        return new Point(sumX / points.size(), sumY / points.size());
    }
    
    @Override
    public void mouseEntered(MouseEvent evt) { }   // Some empty routines.
    @Override
    public void mouseExited(MouseEvent evt) { }    //    (Required by the MouseListener
    @Override
    public void mouseClicked(MouseEvent evt) { }   //    and MouseMotionListener
    @Override
    public void mouseMoved(MouseEvent evt) { }     //    interfaces).
      
}  // End class SimplePaintPanel
