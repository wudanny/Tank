/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tank;

import java.util.Observer;

/**
 *
 * @author Danny
 */
public abstract class Vehicle extends Game_Object implements Observer {

    int health;
    int damageTaken;
    int gapBetweenFire;
    int waitForNextFire;
    int heightBound;
    int widthBound;
    int collidingDamage;

    public int getCollidingDamage() {
        return collidingDamage;
    }

    public int getHealth() {
        return health;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public void increaseDamageTaken(int damage) {
        damageTaken += damage;
    }
}
