/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Vector;

/**
 *
 * @author Danny
 */
public class Wall extends Game_Object {

    double originalX, originalY;
    boolean destructable;
    boolean destroy;
    int respawnTime, timer;
    double relocationX, relocationY;

    Wall(Vector image, double x, double y, double relocationX, double relocationY, int respawnTime, boolean destructable, GameEvents events) {
        this.x = x;
        this.originalX = x;
        this.y = y;
        this.originalY = y;
        this.image = image;
        this.show = true;
        this.destroy = false;
        this.relocationY = relocationY;
        this.relocationX = relocationX;
        this.respawnTime = respawnTime;
        this.timer = 0;
        this.destructable = destructable;
        this.show = true;
        this.events = events;

    }

    public void reset() {
        x = originalX;
        y = originalY;
    }

    public boolean isDestructable() {
        return destructable;
    }

    public boolean isDestroy() {
        return destroy;
    }

    public double getRelocationY() {
        return relocationY;
    }

    public double getRelocationX() {
        return relocationX;
    }

    public void destroy() {
        if (destructable && !destroy) {
            events.setCreateExplosion(this);
            destroy = true;
            timer = respawnTime;
            x = relocationX;
            y = relocationY;
        }
    }

    @Override
    public void update(int w, int h) {
        if (destructable) {
            if (timer > 0) {
                timer--;
            } else {
                destroy = false;
                this.reset();
            }
        }
    }

    public void draw(Graphics g, ImageObserver obs) {
        if (destroy) {
            g.drawImage((Image) image.elementAt(imageCounter), (int) relocationX, (int) relocationY, obs);
        } else {
            g.drawImage((Image) image.elementAt(imageCounter), (int) x, (int) y, obs);
        }
    }
}
