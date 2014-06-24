/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tank;

import java.awt.Image;
import java.util.Vector;

/**
 *
 * @author Danny
 */
public class Bullet extends Game_Object{
    int damage;
    double dx,dy;
    Bullet(Vector image, double x, double y,double dx, double dy, int speed, int damage, GameEvents event){
        this.image=image;
        this.x=x;
        this.y=y;
        this.dx=dx;
        this.dy=dy;
        this.speed=speed;
        this.damage=damage;
        this.show=true;
        this.events=event;
        this.imageCounter=0;
    }
    public int getDamage(){
        return damage;
    }
    @Override
    public void update(int w, int h) {
        if(isOutOfBound(w,h)){
            show=false;
        }
        
        if(show){
            y+=(speed*dy);
            x+=(speed*dx);
        }
        
        imageCounter=(imageCounter+1)%image.size();
    }

    
}
