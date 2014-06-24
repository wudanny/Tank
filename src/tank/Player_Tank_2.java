/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Vector;

/**
 *
 * @author Danny
 */
public class Player_Tank_2 extends Player_Tank {

    Player_Tank_2(Vector image, double x, double y, double deltaAngle, int maxSpeed, GameEvents event, int health, int gapBetweenFire, double acceleration) {
        this.image = image;
        this.x = x;
        this.originalX = x;
        this.y = y;
        this.originalY = y;
        this.speed = 0;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.events = event;
        this.health = health;
        this.gapBetweenFire = gapBetweenFire;
        this.collidingDamage = 0;
        this.show = true;
        this.damageTaken = 0;
        this.waitForNextFire = 0;
        this.imageCounter = 0;
        this.angle = 0;
        this.deltaAngle = deltaAngle;
        this.deltaX = 1;
        this.deltaY = 0;
        this.secondaryWeapon = 0;
        this.numberOfSecondary = 0;
        this.show = true;
        this.alive = true;
    }

    @Override
    public void update(Observable o, Object o1) {
        GameEvents ge = (GameEvents) o1;
        if (show) {
            if (ge.getType() == 1) {
                KeyEvent e = (KeyEvent) ge.event;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        action[up] = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        action[down] = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        action[left] = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        action[right] = true;
                        break;
                    case KeyEvent.VK_ENTER:
                        action[fire] = true;
                        break;
                    case KeyEvent.VK_SHIFT:
                        action[secondary] = true;
                        break;
                    default:
                        break;

                }
            } else if (ge.getType() == 2) {
                KeyEvent e = (KeyEvent) ge.event;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        action[left] = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        action[right] = false;
                        break;
                    case KeyEvent.VK_UP:
                        action[up] = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        action[down] = false;
                        break;
                    case KeyEvent.VK_ENTER:
                        action[fire] = false;
                        break;
                    case KeyEvent.VK_SHIFT:
                        action[secondary] = false;
                        break;
                    default:
                        break;

                }
            } else if (ge.getType() == 6) {
                if (ge.getplayer() instanceof Player_Tank_2) {
                    PowerUp_Tank power = (PowerUp_Tank) ge.getEvent();
                    setSecondary(power.getTypeOfPower(), power.getAmount(), power.getIcon());
                }
            }
        }
    }

    public void draw(Graphics g, ImageObserver obs) {
        if (alive) {
            g.drawImage((Image) image.elementAt(imageCounter), (int) x, (int) y, obs);
            int healthScale = 50 * (damageTaken) / health;
            g.setColor(Color.BLUE);
            g.drawRect((int) (x), (int) y, 50, 4);
            g.fillRect((int) (x), (int) y, 50 - healthScale, 4);
            if (weaponIcon != null) {
                g.drawImage(weaponIcon, (int) x, (int) y + this.getImageHeight(), obs);
                g.drawString(String.valueOf(numberOfSecondary), (int) x + this.getIconWidth(), (int) y + this.getImageHeight());
            }
        }
    }
}
