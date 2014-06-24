/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tank;

import java.util.Vector;

/**
 *
 * @author Danny
 */
public class PowerUp extends Game_Object {

    int typeOfPowerUp, amount;

    PowerUp(Vector image, double x, double y, double speed, int typeOfPowerUp, int amount, GameEvents events) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.typeOfPowerUp = typeOfPowerUp;
        this.amount = amount;
        this.events = events;
        this.imageCounter = 0;
        this.show=true;

    }

    public int getTypeOfPower() {
        return typeOfPowerUp;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public void update(int w, int h) {

        if (isOutOfBound(w, h)) {
            show = false;
        }

        if (show) {
            y += speed;
        }
        imageCounter = (imageCounter + 1) % image.size();
    }
}
