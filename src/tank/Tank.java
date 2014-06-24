/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tank;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Danny
 */
public class Tank extends JApplet implements Runnable {

    //basic data for applet
    private Thread thread;
    ImageObserver observer;
    //size of the game increase
    int increaseSize;
    int seperationlineSize;
    //score information
    int sizeOfScoreFont;
    int player1ScoreX,player1ScoreY;
    int player2ScoreX,player2ScoreY;
    //image where the actual game is drawn on
    private BufferedImage bimg;
    //the image that will be shown on the screen
    private BufferedImage screen;
    //the height and width of the game
    private int widthBound, heightBound;
    //gap between deaths before restarting the game
    int endRoundGap;
    //if it is the start, end, or need for restart
    boolean gameOver, start, restart;
    //score for player1 and player2
    int player1Score, player2Score;
    //the images for a split screen
    BufferedImage player1View, player2View;
    //image of the miniMap
    Image miniMap;
    int miniMapHeight,miniMapWidth;
    //background music
    AudioClip backgroundMusic;
    //responsible for creating all objects
    CreateInstance create;
    //game events
    GameEvents gameEvents;
    //container for all game objects
    Vector<LinkedList> gameObjects;
    LinkedList<Player_Tank> players;
    LinkedList<Bullet> players1bullets;
    LinkedList<Bullet> players2bullets;
    LinkedList<Wall> walls;
    LinkedList<Explosion> explosions;
    LinkedList<PowerUp> powerUp;
    //allow easier access to player tanks
    Player_Tank player1, player2;

    public void init() {
        setBackground(Color.white);
        increaseSize=2;
        seperationlineSize=20;
        miniMapHeight=150;
        miniMapWidth=150;
        sizeOfScoreFont=40;
        player1ScoreX=280;
        player1ScoreY=50;
        player2ScoreX=350;
        player2ScoreY=50;
        //the game just started
        gameOver = false;
        start = true;
        restart = false;
        observer = this;
        player1Score = 0;
        player2Score = 0;
        endRoundGap = 0;
        gameEvents = new GameEvents();
        create = new CreateInstance();
        gameEvents.addObserver(create);
        KeyControl key = new KeyControl();
        addKeyListener(key);
        backgroundMusic = getSound("Resources/background.wav");
        gameObjects = new Vector<LinkedList>();
        powerUp = new LinkedList<PowerUp>();
        gameObjects.add(powerUp);
        players1bullets = new LinkedList<Bullet>();
        gameObjects.add(players1bullets);
        players2bullets = new LinkedList<Bullet>();
        gameObjects.add(players2bullets);
        players = new LinkedList<Player_Tank>();
        gameObjects.add(players);
        walls = new LinkedList<Wall>();
        gameObjects.add(walls);
        explosions = new LinkedList<Explosion>();
        gameObjects.add(explosions);
    }

    class CreateInstance implements Observer {
        //helps seperate the images in the imagestrip

        BufferedImage imageStrip;
        //container for all images for objects
        Vector<Vector<Image>> players1Img, players2Img, bulletsImg, explosionsImg, environmentImg, powerUpImg, weaponIcon;
        //sound of explosion
        Vector<AudioClip> explosionSounds;
        //powerup information
        final int secondaryWeaponAmount = 5, powerUpTimer = 280, amountOfEachPowerUp = 3, numberTypeSecondary = 2;
        //wall information
        double wallRelocationX = -500, wallRelocationY = -500;
        int wallReappearTimer = 200;
        //player tank information
        double player1X = 100, player1Y = 100, player2X = 1100, player2Y = 750, deltaAngle = (Math.PI / 30), acceleration = 1;
        int maxSpeed = 6, playerHealth = 200, gapBetweenFire = 10;
        SourceReader wallDesign;
        //regular bullet information
        int speedBullet = 12, damageBullet = 10, timerBullet = 25;
        //secondary bullet information
        int secondary1Damage = 10, secondary1Speed = 30, secondary1Timer = 50, secondary2Damage = 20, secondary2Speed = 15, secondary2timer = 10;

        CreateInstance() {
            try {
                wallDesign = new SourceReader("wallplacement.txt");
            } catch (IOException e) {
                System.err.println("Cannot open file for wall design!");
            }
            players1Img = new Vector<Vector<Image>>();
            players2Img = new Vector<Vector<Image>>();
            bulletsImg = new Vector<Vector<Image>>();
            explosionsImg = new Vector<Vector<Image>>();
            environmentImg = new Vector<Vector<Image>>();
            powerUpImg = new Vector<Vector<Image>>();
            weaponIcon = new Vector<Vector<Image>>();

            explosionSounds = new Vector<AudioClip>();
            //players
            imageStrip = (BufferedImage) getSprite("Resources/Tank1_strip60.png");
            players1Img.add(new Vector<Image>());
            for (int i = 0; i < 60; i++) {
                players1Img.elementAt(0).add(imageStrip.getSubimage(i * 64, 0, 64, 64));
            }
            imageStrip = (BufferedImage) getSprite("Resources/Tank2_strip60.png");
            players1Img.add(new Vector<Image>());
            for (int i = 0; i < 60; i++) {
                players1Img.elementAt(1).add(imageStrip.getSubimage(i * 64, 0, 64, 64));
            }
            //bullets
            imageStrip = (BufferedImage) getSprite("Resources/Shell_strip60.png");
            bulletsImg.add(new Vector<Image>());
            for (int i = 0; i < 60; i++) {

                bulletsImg.elementAt(0).add(imageStrip.getSubimage(i * 24, 0, 24, 24));
            }

            imageStrip = (BufferedImage) getSprite("Resources/Rocket_strip60.png");
            bulletsImg.add(new Vector<Image>());
            for (int i = 0; i < 60; i++) {
                bulletsImg.elementAt(1).add(imageStrip.getSubimage(i * 24, 0, 24, 24));
            }

            imageStrip = (BufferedImage) getSprite("Resources/Bouncing_strip60.png");
            bulletsImg.add(new Vector<Image>());
            for (int i = 0; i < 60; i++) {
                bulletsImg.elementAt(2).add(imageStrip.getSubimage(i * 24, 0, 24, 24));
            }

            //walls
            environmentImg.add(new Vector<Image>());
            environmentImg.elementAt(0).add(getSprite("Resources/Wall1.png"));
            environmentImg.add(new Vector<Image>());
            environmentImg.elementAt(1).add(getSprite("Resources/Wall2.png"));

            //explosions
            explosionsImg.add(new Vector<Image>());
            imageStrip = (BufferedImage) getSprite("Resources/Explosion_large_strip7.png");
            for (int i = 0; i < 7; i++) {
                explosionsImg.elementAt(0).add(imageStrip.getSubimage(i * 64, 0, 64, 64));
            }


            explosionsImg.add(new Vector<Image>());
            imageStrip = (BufferedImage) getSprite("Resources/Explosion_small_strip6.png");
            for (int i = 0; i < 6; i++) {
                explosionsImg.elementAt(1).add(imageStrip.getSubimage(i * 32, 0, 32, 32));
            }
            explosionSounds.add(getSound("Resources/Explosion_small.wav"));
            explosionSounds.add(getSound("Resources/Explosion_large.wav"));

            //powerUps
            imageStrip = (BufferedImage) getSprite("Resources/Pickup_strip4.png");
            for (int i = 0; i < 2; i++) {
                powerUpImg.add(new Vector<Image>());
                powerUpImg.elementAt(i).add(imageStrip.getSubimage(i * 32, 0, 32, 32));
            }

            imageStrip = (BufferedImage) getSprite("Resources/Weapon_strip3.png");
            for (int i = 0; i < 2; i++) {
                weaponIcon.add(new Vector<Image>());
                weaponIcon.elementAt(i).add(imageStrip.getSubimage(i * 16, 0, 16, 16));
            }

        }

        @Override
        public void update(Observable o, Object o1) {
            GameEvents ge = (GameEvents) o1;

            if (ge.getType() == 0) {

                //create 3 of each power up
                for (int i = 0; i < amountOfEachPowerUp; i++) {
                    for (int j = 1; j <= numberTypeSecondary; j++) {
                        powerUp.add(new PowerUp_Tank(powerUpImg.elementAt(j - 1), weaponIcon.elementAt(j - 1).elementAt(0), 0, 0, 0, j, secondaryWeaponAmount, gameEvents, powerUpTimer, heightBound, widthBound));
                    }
                }

                //the wall bound around the screen
                int col = 0, row = 0, location;
                while (true) {
                    try {
                        location = wallDesign.read();
                    } catch (IOException e) {
                        break;
                    }
                    switch (location) {
                        case ' ':
                            col = 0;
                            row += 1;
                            break;
                        case '1':
                            walls.add(new Wall(environmentImg.elementAt(0), col * 32, row * 32, wallRelocationX, wallRelocationY, wallReappearTimer, false, gameEvents));
                            col += 1;
                            break;
                        case '2':
                            walls.add(new Wall(environmentImg.elementAt(1), col * 32, row * 32,  wallRelocationX, wallRelocationY, wallReappearTimer, true, gameEvents));
                            col += 1;
                            break;
                        default:
                            col += 1;
                            break;
                            
                    }
                }

                //players
                player1 = new Player_Tank_1(players1Img.elementAt(0), player1X, player1Y, deltaAngle, maxSpeed, gameEvents, playerHealth, gapBetweenFire, acceleration);
                players.add(player1);
                gameEvents.addObserver(player1);
                player2 = new Player_Tank_2(players1Img.elementAt(1), player2X, player2Y, deltaAngle, maxSpeed, gameEvents, playerHealth, gapBetweenFire, acceleration);
                players.add(player2);
                gameEvents.addObserver(player2);
            } else if (ge.getType() == 4) {
                //creates explosions for the tank and wall
                Game_Object explosion = (Game_Object) ge.getEvent();
                if (explosion instanceof Player_Tank) {
                    explosions.add(new Explosion(explosionsImg.elementAt(0), explosion.getX(), explosion.getY(), explosionSounds.elementAt(1)));
                } else if (explosion instanceof Wall) {
                    explosions.add(new Explosion(explosionsImg.elementAt(1), explosion.getX(), explosion.getY(), explosionSounds.elementAt(0)));
                }
            } else if (ge.getType() == 5) {
                //creates each bullet relative to each class
                Player_Tank tank = (Player_Tank) ge.getEvent();
                if (tank instanceof Player_Tank_1) {
                    players1bullets.add(new Tank_Bullet(bulletsImg.elementAt(0), tank.getX() + tank.getImageWidth() / 2, tank.getY() + tank.getImageHeight() / 2, tank.getDeltaX(), tank.getDeltaY(), speedBullet, damageBullet, tank.getImageCounter(), gameEvents, timerBullet));
                } else if (tank instanceof Player_Tank_2) {
                    players2bullets.add(new Tank_Bullet(bulletsImg.elementAt(0), tank.getX() + tank.getImageWidth() / 2, tank.getY() + tank.getImageHeight() / 2, tank.getDeltaX(), tank.getDeltaY(), speedBullet, damageBullet, tank.getImageCounter(), gameEvents, timerBullet));
                }
            } else if (ge.getType() == 7) {
                //creates each secondary bullet relative to each class
                Player_Tank tank = (Player_Tank) ge.getEvent();
                if (tank instanceof Player_Tank_1) {
                    if (tank.getSecondaryWeapon() == 1) {
                        players1bullets.add(new Tank_Bullet(bulletsImg.elementAt(1), tank.getX() + tank.getImageWidth() / 2, tank.getY() + tank.getImageHeight() / 2 , tank.getDeltaX(), tank.getDeltaY(), secondary1Damage, secondary1Speed, tank.getImageCounter(), gameEvents, secondary1Timer));
                    } else if (tank.getSecondaryWeapon() == 2) {

                        players1bullets.add(new Tank_Bullet(bulletsImg.elementAt(2), tank.getX() + tank.getImageWidth() / 2, tank.getY() + tank.getImageHeight() / 2 , tank.getDeltaX(), tank.getDeltaY(), secondary2Damage, secondary2Speed, tank.getImageCounter(), gameEvents, secondary2timer));
                    }
                } else if (tank instanceof Player_Tank_2) {
                    if (tank.getSecondaryWeapon() == 1) {
                        players2bullets.add(new Tank_Bullet(bulletsImg.elementAt(1), tank.getX() + tank.getImageWidth() / 2, tank.getY() + tank.getImageHeight() / 2 , tank.getDeltaX(), tank.getDeltaY(), secondary1Damage, secondary1Speed, tank.getImageCounter(), gameEvents, secondary1Timer));
                    } else if (tank.getSecondaryWeapon() == 2) {
                        players2bullets.add(new Tank_Bullet(bulletsImg.elementAt(2), tank.getX() + tank.getImageWidth() / 2 , tank.getY() + tank.getImageHeight() / 2 , tank.getDeltaX(), tank.getDeltaY(), secondary2Damage, secondary2Speed, tank.getImageCounter(), gameEvents, secondary2timer));
                    }
                }
            }
        }
    }

    public class KeyControl extends KeyAdapter {
        //for keys that are held down

        public void keyPressed(KeyEvent e) {
            gameEvents.setMovementForPlayer(e);
        }
        //for keys that are released

        public void keyReleased(KeyEvent e) {
            gameEvents.setStopMovementForPlayer(e);
        }
    }

    //gets image from file
    public Image getSprite(String name) {
        URL url = Tank.class
                .getResource(name);
        BufferedImage img = null;


        try {
            img = ImageIO.read(url);
        } catch (IOException ex) {
            Logger.getLogger(Tank.class.getName()).log(Level.SEVERE, null, ex);
        }


        try {
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(img, 0);
            tracker.waitForID(0);
        } catch (Exception e) {
        }
        return img;
    }

    //creates the background tiles 
    public void drawBackGroundWithTileImage(int w, int h, Graphics2D g2) {
        Image ground;
        ground = getSprite("Resources/Background.png");
        int TileWidth = ground.getWidth(this);
        int TileHeight = ground.getHeight(this);

        int NumberX = (int) (w / TileWidth);
        int NumberY = (int) (h / TileHeight);

        Image Buffer = createImage(NumberX * TileWidth, NumberY * TileHeight);
        //Graphics BufferG = Buffer.getGraphics();



        for (int i = -1; i <= NumberY; i++) {
            for (int j = 0; j <= NumberX; j++) {
                g2.drawImage(ground, j * TileWidth, i * TileHeight, TileWidth, TileHeight, this);
            }
        }
        // move += speed;
    }

    //calls all objects update 
    void updateObject(int w, int h) {
        Game_Object currentObject;
        ListIterator currentIterator;
        //goes through all the objects and call their update method
        for (int i = 0; i < gameObjects.size(); i++) {
            currentIterator = gameObjects.elementAt(i).listIterator();
            while (currentIterator.hasNext()) {
                currentObject = (Game_Object) currentIterator.next();
                currentObject.update(w, h);
            }
        }
    }

    //calls all objects draw method
    void drawObject(int w, int h, Graphics2D g2) {
        Game_Object currentObject;
        ListIterator currentIterator;
        //draw the background water images
        drawBackGroundWithTileImage(w, h, g2);

        //goes through all the objects and draw them
        for (int i = 0; i < gameObjects.size(); i++) {
            currentIterator = gameObjects.elementAt(i).listIterator();
            while (currentIterator.hasNext()) {
                currentObject = (Game_Object) currentIterator.next();
                currentObject.draw(g2, observer);
            }
        }
    }

    //delete all objects that is not showing
    void deleteObject() {
        Game_Object currentObject;
        ListIterator currentIterator;
        //goes through all the objects and check if it is showing, if not remove them from their list
        for (int i = 0; i < gameObjects.size(); i++) {
            currentIterator = gameObjects.elementAt(i).listIterator();
            while (currentIterator.hasNext()) {
                currentObject = (Game_Object) currentIterator.next();
                if (!currentObject.isShow()) {
                    currentIterator.remove();
                }
            }
        }
    }

    //handles all possible collision in the game
    void collision() {
        Player_Tank playersObject1, playersObject2;
        Bullet bulletsObject;
        Wall wallObject;
        PowerUp_Tank powerUpObject;
        //PowerUp powerUpObject;
        ListIterator currentIterator;
        ListIterator collisionIterator;
        Rectangle currentRec, collisionRec;


        currentIterator = players.listIterator();
        playersObject1 = (Player_Tank) currentIterator.next();
        playersObject2 = (Player_Tank) currentIterator.next();

        //player1 and player2
        currentRec = playersObject1.boundary();
        collisionRec = playersObject2.boundary();
        if (currentRec.intersects(collisionRec)) {
            playersObject1.bounceBackwards();
            playersObject2.bounceBackwards();
        }
        //player1 and player2 bullet
        collisionIterator = players2bullets.listIterator();
        currentRec = playersObject1.boundary();
        while (collisionIterator.hasNext()) {
            bulletsObject = (Bullet) collisionIterator.next();
            collisionRec = bulletsObject.boundary();
            if (bulletsObject.isShow() && currentRec.intersects(collisionRec)) {
                bulletsObject.setShow(false);
                playersObject1.increaseDamageTaken(bulletsObject.getDamage());
            }
        }


        //player2 and player1 bullet
        collisionIterator = players1bullets.listIterator();
        currentRec = playersObject2.boundary();
        while (collisionIterator.hasNext()) {
            bulletsObject = (Bullet) collisionIterator.next();
            collisionRec = bulletsObject.boundary();
            if (bulletsObject.isShow() && currentRec.intersects(collisionRec)) {
                bulletsObject.setShow(false);
                playersObject2.increaseDamageTaken(bulletsObject.getDamage());
            }
        }

        //players and wall
        currentIterator = players.listIterator();
        while (currentIterator.hasNext()) {
            collisionIterator = walls.listIterator();
            playersObject1 = (Player_Tank) currentIterator.next();
            currentRec = playersObject1.boundary();
            while (collisionIterator.hasNext()) {
                wallObject = (Wall) collisionIterator.next();
                collisionRec = wallObject.boundary();
                if (currentRec.intersects(collisionRec)) {
                    playersObject1.bounceBackwards();
                    //if mutilple wall are together, this allows only one wall to create collsion rather than having
                    //multiple collsion happening at once causing strange reaction
                    break;
                }
            }
        }
        //player1 bullets and wall
        currentIterator = players1bullets.listIterator();
        while (currentIterator.hasNext()) {
            collisionIterator = walls.listIterator();
            bulletsObject = (Bullet) currentIterator.next();
            currentRec = bulletsObject.boundary();
            while (collisionIterator.hasNext()) {
                wallObject = (Wall) collisionIterator.next();
                collisionRec = wallObject.boundary();
                if (bulletsObject.isShow() && currentRec.intersects(collisionRec)) {
                    if (wallObject.isDestructable()) {
                        wallObject.destroy();
                    }
                    bulletsObject.setShow(false);
                }
            }
        }
        //player 2 bullets and wall
        currentIterator = players2bullets.listIterator();
        while (currentIterator.hasNext()) {
            collisionIterator = walls.listIterator();
            bulletsObject = (Bullet) currentIterator.next();
            currentRec = bulletsObject.boundary();
            while (collisionIterator.hasNext()) {
                wallObject = (Wall) collisionIterator.next();
                collisionRec = wallObject.boundary();
                if (bulletsObject.isShow() && currentRec.intersects(collisionRec)) {
                    if (wallObject.isDestructable()) {
                        wallObject.destroy();
                    }
                    bulletsObject.setShow(false);
                }
            }
        }


        //players and powerup
        currentIterator = players.listIterator();
        while (currentIterator.hasNext()) {
            collisionIterator = powerUp.listIterator();
            playersObject1 = (Player_Tank) currentIterator.next();
            currentRec = playersObject1.boundary();
            while (collisionIterator.hasNext()) {
                powerUpObject = (PowerUp_Tank) collisionIterator.next();
                collisionRec = powerUpObject.boundary();
                if (currentRec.intersects(collisionRec)) {
                    powerUpObject.reset();
                    gameEvents.setApplyPowerUp(powerUpObject, playersObject1);
                }
            }
        }

    }

    //calls the reset method of game objects 
    public void reset() {
        ListIterator objectsList;
        Game_Object object;
        for (int i = 0; i < gameObjects.size(); i++) {
            objectsList = gameObjects.elementAt(i).listIterator();
            while (objectsList.hasNext()) {
                object = (Game_Object) objectsList.next();
                object.reset();
            }
        }
    }

    //return true if one player is destroyed, update the score of the player's tank that is not destroyed
    public boolean endRound() {
        if (!(player1.getAlive()) && !(player2.getAlive())) {
            player1Score += 1;
            player2Score += 1;
            return true;
        } else if (!(player1.getAlive())) {
            player2Score += 1;
            return true;
        } else if (!(player2.getAlive())) {
            player1Score += 1;
            return true;
        } else {
            return false;
        }
    }

    //performs that game play of the game, calls draw, update, and check collisions
    public void gamePlay(int w, int h, Graphics2D g2) {
        if (start) {
            gameEvents.setGamePlay();
            start = false;
        }
        if (endRoundGap == 15) {
            this.reset();
            endRoundGap = 0;
            restart = false;
        }
        if (!gameOver) {
            updateObject(w, h);
            drawObject(w, h, g2);
            collision();
            createScreenPic(w/increaseSize, h/increaseSize);
            if (!restart) {
                restart = endRound();
            } else {
                endRoundGap++;
            }
        }
    }

    //creates the image of the player split screen
    public BufferedImage playerView(Player_Tank player, BufferedImage image, int w, int h, int wBound, int hBound) {
        int x = (int) player.getX();
        int y = (int) player.getY();

        if (x <= w / 2 && y <= h / 2) {
            //when player is at the top right corner of screen
            return image.getSubimage(0, 0, w, h);
        } else if (x <= w / 2 && y >= hBound - h / 2) {
            //when player is at the bottom right corner of screen
            return image.getSubimage(0, hBound - h, w, h);
        } else if (x >= wBound - w / 2 && y <= h / 2) {
            //when player is at the top left corner of screen
            return image.getSubimage(wBound - w, 0, w, h);
        } else if (x >= wBound - w / 2 && y >= hBound - h / 2) {
            //when player is at the bottom right corner of screen
            return image.getSubimage(wBound - w, hBound - h, w, h);
        } else if (x <= w / 2) {
            //when player is at the left most of screen
            return image.getSubimage(0, y - h / 2, w, h);
        } else if (x >= wBound - w / 2) {
            //when player is at the right most of screen
            return image.getSubimage(wBound - w, y - h / 2, w, h);
        } else if (y <= h / 2) {
            //when player is at the top most of screen
            return image.getSubimage(x - w / 2, 0, w, h);
        } else if (y >= hBound - h / 2) {
            //when player is at the bottom most of screen
            return image.getSubimage(x - w / 2, hBound - h, w, h);
        } else {
            //when player is anywhere in the middle
            return image.getSubimage(x - w / 2, y - h / 2, w, h);
        }

    }

    public BufferedImage minimap(BufferedImage img, int newW, int newH) {
        //create new image with new deminsions
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRect(0, 0, newW, newH);
        //create the img image onto the new iamge
        g.drawImage(img, 0, 0, newW, newH, null);
        g.dispose();
        return dimg;
    }

    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
            bimg = (BufferedImage) createImage(w, h);
        }
        g2 = bimg.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        return g2;
    }

    //create the imag that will be shown on the screen
    public void createScreenPic(int w, int h) {
        //clear the buffered image
        Graphics2D g2 = null;
        if (screen == null || screen.getWidth() != w || screen.getHeight() != h) {
            screen = (BufferedImage) createImage(w, h);
        }
        g2 = screen.createGraphics();
        g2.setBackground(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.clearRect(0, 0, w, h);
        //get the two images for their respective players view
        player1View = playerView(player1, bimg, w/2-seperationlineSize/2, h, widthBound, heightBound);
        player2View = playerView(player2, bimg, w/2-seperationlineSize/2, h, widthBound, heightBound);
        //gets the image of a mini map
        miniMap = minimap(bimg, miniMapWidth, miniMapHeight);
        //draw all three images onto the screen image
        g2.drawImage(player1View, 0, 0, this);
        g2.drawImage(player2View, w/2+seperationlineSize/2, 0, this);

        //the divider that seperates the two screens
        g2.setColor(Color.black);
        g2.fillRect(w/2-seperationlineSize/2, 0, seperationlineSize, h);

        //draw mini map
        g2.drawImage(miniMap, w/2-miniMapWidth/2, h-miniMapHeight, this);
        
        //draw scores 
        Font font = new Font("Arial", Font.BOLD, sizeOfScoreFont);
        g2.setFont(font);
        g2.setColor(Color.red);
        g2.drawString(String.valueOf(player1Score), player1ScoreX, player1ScoreY);
        g2.setColor(Color.blue);
        g2.drawString(String.valueOf(player2Score), player2ScoreX, player2ScoreY);

    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        //creates a gaming field that is four times larger
        widthBound = increaseSize * d.width;
        heightBound = increaseSize * d.height;
        Graphics2D g2 = createGraphics2D(widthBound, heightBound);
        gamePlay(widthBound, heightBound, g2);
        g2.dispose();
        g.drawImage(screen, 0, 0, this);
    }

    public void start() {
        this.requestFocusInWindow();
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        backgroundMusic.loop();
        thread.start();
    }

    public void run() {
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();

            try {
                thread.sleep(20);
            } catch (InterruptedException e) {
                break;
            }

        }

    }

    public AudioClip getSound(String source) {
        URL url = getClass().getResource(source);
        return Applet.newAudioClip(url);
    }

    public static void main(String argv[]) {
        final Tank demo = new Tank();
        demo.init();
        JFrame f = new JFrame("Scrolling Shooter");
        f.addWindowListener(new WindowAdapter() {
        });
        f.getContentPane().add("Center", demo);
        f.pack();
        f.setSize(new Dimension(640, 480));
        f.setVisible(true);
        f.setResizable(false);
        demo.start();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
