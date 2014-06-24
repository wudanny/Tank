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
public class Tank_Bullet extends Bullet {

    int timer;
    Tank_Bullet(Vector image, double x, double y, double dx, double dy, int speed, int damage, int imageCounter, GameEvents event, int timer) {
        super(image, x, y, dx, dy, speed, damage, event);
        this.imageCounter = imageCounter;
        this.timer=timer;
        this.show = true;
    }

    @Override
    public void update(int w, int h) {
        if (isOutOfBound(w, h)||timer <=0) {
            show = false;
        }
        if (show) {
            y += (speed * dy);
            x += (speed * dx);
        }
        timer--;

    }
    
    public void reset(){
        show=false;
    }
}
