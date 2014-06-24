/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tank;

import java.awt.Image;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Danny
 */
public class PowerUp_Tank extends PowerUp{
    int timer,relocateTime,heightBound,widthBound;
    Random relocate;
    Image icon;
    PowerUp_Tank(Vector image,Image icon, double x, double y, double speed, int typeOfPowerUp, int amount, GameEvents events,int relocateTime,int heightBound, int widthBound) {
        super(image,x,y,speed,typeOfPowerUp,amount,events);
        this.relocateTime=relocateTime;
        this.timer=0;
        this.widthBound=widthBound;
        this.heightBound=heightBound;
        relocate=new Random();
        this.icon=icon;
    }
    
    public void reset(){
        timer=relocateTime;
        x=relocate.nextInt(widthBound);
        y=relocate.nextInt(heightBound);
    }
    
    public Image getIcon(){
        return icon;
    }
    public void update(int w, int h) {
        timer--;
        if(timer<=0){
            this.reset();
        }
        imageCounter = (imageCounter + 1) % image.size();
    }
    
}
