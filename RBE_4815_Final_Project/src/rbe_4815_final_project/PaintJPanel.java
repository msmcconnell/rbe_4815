/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbe_4815_final_project;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

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

    private int prevX, prevY;     // The previous location of the mouse.

    private boolean dragging;      // This is set to true while the user is drawing.
    
    public BufferedImage canvasImage;
    
    public Graphics2D graphicsForDrawing;  // A graphics context for the panel
                                // that is used to draw the user's curve.
    //Units in mm
    private final double DOMINO_THICKNESS = 6.35;
    private final double DOMINO_HEIGHT = 44.45;
    private final double DOMINO_WIDTH = 20.63;
    public double PIXELS_PER_MM = 0.5;
    private final double DOMINO_PIXEL_HEIGHT = mmToPixel(DOMINO_HEIGHT);
    private final double DOMINO_PIXEL_THICKNESS = mmToPixel(DOMINO_THICKNESS);
    private final double DOMINO_PIXEL_WIDTH = mmToPixel(DOMINO_WIDTH);
    private final int DOMINO_DISTANCE = mmToPixel((0.54*DOMINO_HEIGHT + 0.92*DOMINO_THICKNESS)); //d=0.54*h + 0.92*t ; h = 
    private final int DOMINO_DISTANCE_MM = (int)(0.54*DOMINO_HEIGHT + 0.92*DOMINO_THICKNESS);
    
    private final int AVERAGING_SIZE = 10;
    
    private final int MAX_ANGLE = 40;
    
    private final int DEFAULT_DOMINO_COUNT = 120;
    
    private boolean isValidPath = true;
    
    private int remainingDominoes = DEFAULT_DOMINO_COUNT;
    
    private int movingSumX = 0;
    private int movingSumY = 0;
    
    private LinkedList<Domino> dominoQueue = new LinkedList<Domino>();
    
    private LinkedList<Point> prevPointQueue = new LinkedList<Point>(); 

    private PropertyChangeSupport myPropertyChangeSupport = new PropertyChangeSupport(this);
    
    private boolean pathLocked = false;
    
    private Polygon workSpace;
    
    
    /**
     * Draw the contents of the panel.  Since no information is
     * saved about what the user has drawn, the user's drawing
     * is erased whenever this routine is called.
     */
    @Override
    public void paintComponent(Graphics g) {
       super.paintComponent(g);  // Fill with background color (white).
       //g.drawImage(canvasImage, 0, 0, canvasImage.getWidth(), canvasImage.getHeight(), null);
       drawWorkSpace((Graphics2D) g);
    }
    
    /**
      * This routine is called in mousePressed when the user clicks on the drawing area.
        * It sets up the graphics context, graphicsForDrawing, to be used to draw the user's 
        * sketch in the current color.
      */
    public void setUpDrawingGraphics() {
       graphicsForDrawing = (Graphics2D) getGraphics();
       graphicsForDrawing.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

       if (dragging == true || pathLocked)  // Ignore mouse presses that occur
          return;             //    when user is already drawing a curve.
                              //    (This can happen if the user presses
                              //    two mouse buttons at the same time.)
       else if (prevPointQueue.isEmpty() && workSpace.contains(x, y)) {
             // The user has clicked on the white drawing area.
             // Start drawing a curve from the point (x,y).
            prevX = x;
            prevY = y;
            dragging = true;
            movingSumX = x;
            movingSumY = y;
            prevPointQueue.clear();
            prevPointQueue.add(new Point(x,y));
            dominoQueue.add(new Domino((int) pixelToMM(x), (int) pixelToMM(y), 0));
            setUpDrawingGraphics();
            BasicStroke stroke = new BasicStroke(3);
            graphicsForDrawing.setStroke(stroke);
            decrementRemainingDominoes();
       }
    }
    
    public void drawWorkSpace(Graphics2D g) {
        int width = getWidth();
        int height = getHeight();
        Point p1 = new Point(width - mmToPixel(2024), mmToPixel(111));
        Point p2 = new Point(width - mmToPixel(1960), mmToPixel(0));
        Point p3 = new Point(width - mmToPixel(448),  mmToPixel(0));
        Point p4 = new Point(width - mmToPixel(448),  mmToPixel(444));
        Point p5 = new Point(width - mmToPixel(224),  mmToPixel(448));
        Point p6 = new Point(width - mmToPixel(224),  mmToPixel(958));
        Point p7 = new Point(width - mmToPixel(1362), mmToPixel(958));
        g.setColor(Color.GREEN);
        BasicStroke stroke = new BasicStroke(10);
        g.setStroke(stroke);
        int[] xPoints = {p1.x, p2.x, p3.x, p4.x, p5.x, p6.x, p7.x};
        int[] yPoints = {p1.y, p2.y, p3.y, p4.y, p5.y, p6.y, p7.y};
        workSpace = new Polygon(xPoints, yPoints, 7);
        g.drawPolygon(workSpace);
        
    }

    public void resetPath() {
        movingSumX = 0;
        movingSumY = 0;
        dominoQueue.clear();
        prevPointQueue.clear();
        canvasImage = null;
        if (graphicsForDrawing != null) {
            graphicsForDrawing.setColor(Color.WHITE);
            graphicsForDrawing.fillRect(0, 0, getWidth(), getHeight());
            graphicsForDrawing.dispose();
            graphicsForDrawing = null;
        }
        pathLocked = false;
        setIsValidPath(true);
        setUpDrawingGraphics();
        drawWorkSpace(graphicsForDrawing);
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

       int x = evt.getX();   // x-coordinate of mouse.
       int y = evt.getY();   // y-coordinate of mouse.
       int xMM = (int)pixelToMM(x);
       int yMM = (int)pixelToMM(y);
       if (dragging == false || !workSpace.contains(x,y))
          return;  // Nothing to do because the user isn't drawing.
       
       if (prevPointQueue.size() >= AVERAGING_SIZE) {
           Point oldPoint = prevPointQueue.pollFirst();
           movingSumX -= oldPoint.x;
           movingSumY -= oldPoint.y;
           
           movingSumX += x;
           movingSumY += y;
           Point averagedPoint = new Point(movingSumX / AVERAGING_SIZE, movingSumY / AVERAGING_SIZE);
           x = averagedPoint.x;
           y = averagedPoint.y;
       }
       else {
           movingSumX += x;
           movingSumY += y;
       }
       
       graphicsForDrawing.setColor(Color.DARK_GRAY);
       graphicsForDrawing.drawLine(prevX, prevY, x, y);  // Draw the line.
       graphicsForDrawing.setColor(Color.BLUE);

       Domino prevDomino = dominoQueue.peekLast();
       int dX = xMM - prevDomino.getPosition().x;
       int dY = yMM - prevDomino.getPosition().y;
       
       if (Math.sqrt((dX*dX) + (dY*dY)) >= DOMINO_DISTANCE_MM) {
           graphicsForDrawing.drawOval(x - 2, y - 2, 4, 4);
           decrementRemainingDominoes();
           if (getRemainingDominoes() <= 0) {
               dragging = false;
           }
           
           double angle = getAngle(prevDomino.getPosition(), new Point(xMM, yMM)); //For some reason this needs to be negative
           //need a get angle function here
           prevDomino.setOrientation(angle);
           if (dominoQueue.size() >= 2) {
               double prevPrevAngle = dominoQueue.get(dominoQueue.size() - 2).getOrientation();
               double dA = getSmallestAngleDifference(angle, prevPrevAngle);
               if (Math.abs(dA) > MAX_ANGLE) {
                   setIsValidPath(false);
                   dragging = false;
               }
           }
           Domino newDomino = new Domino(xMM, yMM, angle);
           drawDomino(prevDomino, graphicsForDrawing);
           dominoQueue.add(newDomino);
       }
       prevX = x;  // Get ready for the next line segment in the curve.
       prevY = y;
    }
    
    private double getSmallestAngleDifference(double angle1, double angle2){
        double a1 = Math.toRadians(angle1);
        double a2 = Math.toRadians(angle2);
        double dA = Math.atan2(Math.sin(a2-a1), Math.cos(a2-a1));
        return Math.toDegrees(dA);
    }
    
    private double getAngle(Point p1, Point p2) {
        int dX = p2.x - p1.x;
        int dY = p2.y - p1.y;
        double polarTheta = Math.atan2(dX, dY);
        return Math.toDegrees(polarTheta);
    }
    
    private double getSlope(Point p1, Point p2) {
        int dX = p2.x - p1.x;
        int dY = p2.y - p1.y;
        double slope = 999999;
           if (dX != 0) {
               slope = (double)dY / (double)dX;
           }
        return slope;
    }
    
    private void setIsValidPath(boolean valid) {
        this.firePropertyChange("isValidPath", this.isValidPath, valid);
        this.isValidPath = valid;
        //myPropertyChangeSupport.
    }
    
    public boolean getIsValidPath() {
        return isValidPath;
    }
    
    private void decrementRemainingDominoes(){
        setRemainingDominoes(getRemainingDominoes() - 1, true);
    }
    
    public int getRemainingDominoes() {
        return remainingDominoes;
    }
    
    public void setRemainingDominoes(int dominoCount, boolean fireChange) {
        if (fireChange) {
            this.firePropertyChange("remainingDominoes", remainingDominoes, dominoCount);
        }
        remainingDominoes = dominoCount;
    }
    
    public LinkedList<Domino> getDominoes() {
        return dominoQueue;
    }
    
    public void setDominoes(LinkedList<Domino> dominoes){
        dominoQueue = dominoes;
    }
    
    public void drawDomino(Domino d, Graphics2D g) {
        Point pos = mmPointToPXPoint(d.getPosition());
        AffineTransform transform = graphicsForDrawing.getTransform();
        graphicsForDrawing.rotate(Math.toRadians(-d.getOrientation()), pos.x, pos.y);
        
        int thickness_from_center = (int) (DOMINO_PIXEL_THICKNESS / 2);
        int width_from_center = (int) (DOMINO_PIXEL_WIDTH / 2);
        graphicsForDrawing.drawRect(pos.x - width_from_center, 
                                    pos.y - thickness_from_center, 
                                    (int)DOMINO_PIXEL_WIDTH,
                                    (int)DOMINO_PIXEL_THICKNESS);
        graphicsForDrawing.setTransform(transform);
    }
    
    public void drawPath() {
        setUpDrawingGraphics();
        BasicStroke stroke = new BasicStroke(3);
        graphicsForDrawing.setStroke(stroke);
        Point prevPos = mmPointToPXPoint(dominoQueue.peekFirst().getPosition());
        
        for (Domino d : dominoQueue){
            Point pos = mmPointToPXPoint(d.getPosition());
            graphicsForDrawing.drawOval(pos.x - 2, pos.y - 2, 4, 4);
            graphicsForDrawing.setColor(Color.DARK_GRAY);
            graphicsForDrawing.drawLine(prevPos.x, prevPos.y, pos.x, pos.y);  // Draw the line.
            graphicsForDrawing.setColor(Color.BLUE);
            drawDomino(d, graphicsForDrawing);
            prevPos = pos;
        }
        
        pathLocked = true;
    }
    
    public byte[] getDataForABB() {
        byte[] dataBytes = new byte[6 + dominoQueue.size()];
        String dataString = getWidth() + "\n" + "0\n" + "0\n" + "90\n" + "0\n" + "180\n";
        for(Domino d : dominoQueue) {
            dataString = dataString.concat("" + d.getPosition().x + ", " 
                                           + d.getPosition().y + ", " 
                                           + (int)d.getOrientation() + "\n");
        }
        return dataString.getBytes();
    }
    
    private double pixelToMM(int px){
        return (double) px / PIXELS_PER_MM;
    }
    private int mmToPixel(double mm){
        return (int) (mm * PIXELS_PER_MM);
    }
    
    private Point mmPointToPXPoint(Point p) {
        return new Point(mmToPixel(p.x), mmToPixel(p.y));
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
