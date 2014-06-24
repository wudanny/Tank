/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tank;

import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Vector;

/**
 *
 * @author Danny
 */
public class Explosion extends Game_Object{
    AudioClip sound;
    boolean firstCreated;
    
    public Explosion( Vector image,double x, double y, AudioClip sound){
        this.image=image;
        this.sound=sound;
        this.speed=0;
        this.x=x;
        this.y=y;
        this.show=true;
        this.firstCreated=true;
        this.imageCounter=0;
    }
    
    @Override
    public void update(int w, int h) {
        if(firstCreated){
            sound.play();
            firstCreated=false;
        }
        
        imageCounter++;
        if(imageCounter >= image.size()){
            show=false;
        }
    }

}
