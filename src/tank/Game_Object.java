/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Vector;

/**
 *
 * @author Danny
 */
public abstract class Game_Object {
    //speed the object moves in any direction
    double speed;
    double x,y;
    Vector image;
    int imageCounter;
   
    boolean show;
    GameEvents events;
    public double getSpeed(){
        return speed;
    }
    
    public int getImageCounter(){
        return imageCounter;
    }
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
    
    public Vector getImage(){
        return image;
    }
    
    public int getImageHeight(){
        Image temp=(Image)image.elementAt(imageCounter);
        return temp.getHeight(null);
    }
    public int getImageWidth(){
        Image temp=(Image)image.elementAt(imageCounter);
        return temp.getWidth(null);
    }
    
    public boolean isShow(){
        return show;
    }
    
    public void setShow(boolean show){
        this.show=show;
    }
    
    public boolean isOutOfBound(int w, int h){
        if(x<=-200||x>=w||y<=-200||y>=h){
            return true;
        }
        return false;
    }
    public void draw(Graphics g, ImageObserver obs){
        if(show){
             g.drawImage((Image)image.elementAt(imageCounter), (int)x, (int)y, obs);
        }
    }
    public void reset(){
        
    }
    public abstract void update(int w, int h);
    
    public Rectangle boundary(){
        return new Rectangle((int)x,(int)y,this.getImageWidth(),this.getImageHeight());
    }
     
}
