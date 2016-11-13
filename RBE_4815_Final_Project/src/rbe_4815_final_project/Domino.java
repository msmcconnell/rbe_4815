/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbe_4815_final_project;

import java.awt.Point;

/**
 * Class representing a domino
 * @author motmo
 */
public class Domino {
    private Point position;
    private double angle;
    
    public Domino(Point position, double angle){
        this.position = position;
        this.angle = angle;
    }
    
    public Domino(int x, int y, double angle) {
        this.position = new Point(x, y);
        this.angle = angle;
    }
    
    public Point getPosition() {
        return position;
    }
    
    public double getOrientation() {
        return angle;
    }
    
    public void setPosition(Point position) {
        this.position = position;
    }
    
    public void setPosition(int x, int y) {
        this.position = new Point(x, y);
    }
    
    public void setOrientation(double angle) {
        this.angle = angle;
    }
}
