/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Observable;

/**
 *
 * @author Danny
 */
public abstract class Player_Tank extends Vehicle {

    Image weaponIcon;
    double originalX, originalY;
    int secondaryWeapon, numberOfSecondary, maxSpeed;
    double deltaX, deltaY, angle, deltaAngle, acceleration;
    boolean alive;
    boolean action[] = new boolean[6];
    int left = 0, right = 1, up = 2, down = 3, fire = 4, secondary = 5;

    public void reset() {
        x = originalX;
        y = originalY;
        speed = 0;
        angle = 0;
        imageCounter = 0;
        for (int i = 0; i < 6; i++) {
            action[i] = false;
        }
        this.deltaX = 1;
        this.deltaY = 0;
        this.damageTaken = 0;
        this.secondaryWeapon = 0;
        this.numberOfSecondary = 0;
        this.waitForNextFire = 0;
        this.alive = true;
    }

    public int getIconHeight() {
        return weaponIcon.getHeight(null);
    }

    public int getIconWidth() {
        return weaponIcon.getWidth(null);
    }

    public boolean getAlive() {
        return alive;
    }

    public int getSecondaryWeapon() {
        return secondaryWeapon;
    }

    public int getNumberSecondaryWeapon() {
        return numberOfSecondary;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getAngle() {
        return angle;
    }

    public double getDeltaAngle() {
        return deltaAngle;
    }

    public int getmaxSpeed() {
        return maxSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void bounceBackwards() {
        speed = -speed;

    }

    public void setSecondary(int weaponType, int amount, Image icon) {
        secondaryWeapon = weaponType;
        numberOfSecondary = amount;
        weaponIcon = icon;
    }

    void moveLeft() {
        imageCounter = (image.size() + imageCounter + 1) % image.size();
        angle += deltaAngle;
        deltaX = Math.cos(angle);
        deltaY = -Math.sin(angle);
    }

    void moveRight() {
        imageCounter = (image.size() + imageCounter - 1) % image.size();
        angle -= deltaAngle;
        deltaX = Math.cos(angle);
        deltaY = -Math.sin(angle);
    }

    void moveUp() {
        x += (speed * deltaX);
        y += (speed * deltaY);
        if (speed <= maxSpeed) {
            speed += acceleration;
        }
    }

    void moveDown() {
        x += (speed * deltaX);
        y += (speed * deltaY);
        if (speed >= (-maxSpeed)) {
            speed -= acceleration;
        }
    }

    void fire() {

        if (waitForNextFire == 0) {
            waitForNextFire = gapBetweenFire;
            events.setCreateBullet(this);
        }
    }

    void fireSecondary() {

        if ((waitForNextFire == 0) && (numberOfSecondary > 0)) {
            waitForNextFire = gapBetweenFire;
            numberOfSecondary -= 1;
            events.setCreateSecondaryBullet(this);

        }
    }

    public void increaseDamageTaken(int damage) {
        damageTaken += damage;
    }

    public void update(int w, int h) {
        if (alive) {
            if (action[left]) {
                this.moveLeft();
            }
            if (action[right]) {
                this.moveRight();
            }
            if (action[up]) {
                this.moveUp();
            }
            if (action[down]) {
                this.moveDown();
            }

            if (!action[up] && !action[down]) {
                if (speed > 0) {
                    speed -= acceleration;
                    x += (speed * deltaX);
                    y += (speed * deltaY);
                } else if (speed < 0) {
                    speed += acceleration;
                    x += (speed * deltaX);
                    y += (speed * deltaY);
                }
            }
            if (action[fire]) {
                fire();
            }
            if (action[secondary]) {

                fireSecondary();
            }

            if (waitForNextFire > 0) {
                waitForNextFire--;
            }

            if (numberOfSecondary <= 0) {
                setSecondary(0, 0, null);
            }
            if (damageTaken >= health) {
                alive = false;
                events.setCreateExplosion(this);

            }
        }
    }
}
