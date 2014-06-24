/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tank;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 *
 * @author Danny
 */
public class GameEvents extends Observable {

    int type;
    Object player;
    Object event;

    public void setGamePlay() {
        type = 0; // let's assume this mean key input. Should use CONSTANT value for this
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setCreateExplosion(Game_Object e) {
        type = 4;
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setCreateBullet(Game_Object e) {
        type = 5; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }
    
    public void setCreateSecondaryBullet(Game_Object e) {
        type = 7; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setMovementForPlayer(KeyEvent e) {
        type = 1; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public void setStopMovementForPlayer(KeyEvent e) {
        type = 2; // let's assume this mean key input. Should use CONSTANT value for this
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

       public void setApplyPowerUp(Game_Object e, Game_Object player) {
        type = 6; // let's assume this mean key input. Should use CONSTANT value for this
        this.player=player;
        event = e;
        setChanged();
        // trigger notification
        notifyObservers(this);
    }

    public int getType() {
        return type;
    }

    public Object getEvent() {
        return event;
    }
    public Object getplayer() {
        return player;
    }
}
