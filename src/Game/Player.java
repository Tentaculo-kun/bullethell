package Game;

import Engine.GameContainer;
import Engine.Renderer;
import Engine.gfx.ImageTile;
import Engine.gfx.Light;
import Engine.sfx.SoundClip;
import Game.Bullets.PlayerBullet;
import Game.Enemies.Enemy;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends GameObject{
    private final int rateFire = 20;
    private final int inmunityTime= 60;

    private float speed = 150;
    private int fireCD=0;
    private int inmunityCD=0;
    private int hp;
    private int tileX=0,tileY=0;
    private Light light;
    private SoundClip soundClip= new SoundClip("/audio/hurt.wav");

    private boolean inmunity=false;
    private ImageTile image,inmunitySprite= new ImageTile("/player/inmunity.png",16,31);

    public Player(int posX,int posY){
        this.tag="player";
        hp=3;
        this.posX=posX;
        this.posY=posY;
        this.width=16;
        this.height=31;
        this.image = new ImageTile("/player/default.png",16,31);
        this.light = new Light(200,0xffffff);
        soundClip.setVolume(-30);
    }

    @Override
    public void update(GameContainer gc,GameManager gm, float dt) {
        //Movement Start
        if(gc.getInput().isKey(KeyEvent.VK_W) && isIn(0,-dt*speed,gc)){
            if(gc.getInput().isKey(KeyEvent.VK_D))
                tileX=5;
            else
                tileX=3;
            posY-=dt*speed;
        }
        if(gc.getInput().isKey(KeyEvent.VK_S)&& isIn(0,dt*speed,gc)){
            if(gc.getInput().isKey(KeyEvent.VK_A))
                tileX=7;
            else
                tileX=0;
            posY+=dt*speed;
        }
        if(gc.getInput().isKey(KeyEvent.VK_A)&& isIn(-dt*speed,0,gc)){
            if(gc.getInput().isKey(KeyEvent.VK_W))
                tileX=4;
            else
                if(!gc.getInput().isKey(KeyEvent.VK_S))
                    tileX=2;
            posX-=dt*speed;
        }
        if(gc.getInput().isKey(KeyEvent.VK_D)&& isIn(dt*speed,0,gc)){
            if(gc.getInput().isKey(KeyEvent.VK_S))
                tileX=6;
            else
                if(!gc.getInput().isKey(KeyEvent.VK_W))
                    tileX=1;
            posX+=dt*speed;
        }
        //Movement Finish

        //Shoot Start
        if(fireCD>0) {
            this.fireCD--;
        }
        if (gc.getInput().isKey(KeyEvent.VK_UP) && !gc.getInput().isKey(KeyEvent.VK_LEFT)) {
            if(fireCD==0) {
                if(gc.getInput().isKey(KeyEvent.VK_RIGHT))
                    gm.addObject(new PlayerBullet(8, posX + width, posY));
                else
                    gm.addObject(new PlayerBullet(2, posX + 3, posY - height / 2));
                fireCD=rateFire;
                tileY=1;
            }
            if(gc.getInput().isKey(KeyEvent.VK_RIGHT))
                tileX=5;
            else
                tileX=3;
            }
            else
                if (gc.getInput().isKey(KeyEvent.VK_RIGHT)){
                        if (fireCD == 0) {
                            if (gc.getInput().isKey(KeyEvent.VK_DOWN))
                                gm.addObject(new PlayerBullet(5, posX + width, posY + height));
                            else
                                gm.addObject(new PlayerBullet(3, posX + width, posY + height / 2));
                            fireCD = rateFire;
                            tileY = 1;
                        }
                        if(gc.getInput().isKey(KeyEvent.VK_DOWN))
                            tileX=6;
                        else
                            tileX=1;
                }
                else
                    if (gc.getInput().isKey(KeyEvent.VK_DOWN)) {
                            if (fireCD == 0) {
                                if (gc.getInput().isKey(KeyEvent.VK_LEFT))
                                    gm.addObject(new PlayerBullet(6, posX, posY + height));
                                else
                                    gm.addObject(new PlayerBullet(4, posX + width / 5, posY + height / 2));
                                fireCD = rateFire;
                                tileY = 1;
                            }
                            if(gc.getInput().isKey(KeyEvent.VK_LEFT))
                                tileX=7;
                            else
                                tileX=0;
                    }
                    else
                        if (gc.getInput().isKey(KeyEvent.VK_LEFT)) {
                            if (fireCD == 0) {
                                if (gc.getInput().isKey(KeyEvent.VK_UP))
                                    gm.addObject(new PlayerBullet(7, posX, posY));
                                else
                                    gm.addObject(new PlayerBullet(1, posX - width / 2, posY + height / 2));
                                fireCD = rateFire;
                                tileY = 1;
                            }
                            if(gc.getInput().isKey(KeyEvent.VK_UP))
                                tileX=4;
                            else
                                tileX=2;
                        }
        //Shoot finish

        //enemy collision start
        if(!inmunity) {
            ArrayList<GameObject> enemies = gm.getEnemies();
            for (GameObject e : enemies) {
                if (this.checkCollision(e)) {
                    this.damage();
                    setInmunity();
                    ((Enemy) e).damage(1);
                    break;
                }
            }
        }else{
            if(--inmunityCD==0) {
                this.inmunity = false;
                light = new Light(200,0xffffff);
            }
        }
        //enemy collision end

    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        if(inmunity)
            r.drawImageTile(this.inmunitySprite, (int) this.posX, (int) this.posY, tileX, tileY);
        else
            r.drawImageTile(this.image,(int)this.posX,(int)this.posY,tileX,tileY);
        r.drawLight(light,(int)posX+width/2,(int)posY+height/2);
        if(tileY==1 && fireCD==rateFire-5){
            tileY--;
        }
    }

    public int getHP(){
        return this.hp;
    }

    public void damage(){
        soundClip.play();
        this.hp--;
        if(hp==0)
            this.dead=true;
    }

    public boolean isInmunity() {
        return inmunity;
    }

    public void setInmunity(){
        light = new Light(100,0xff0000);
        this.inmunityCD=inmunityTime;
        this.inmunity=true;
    }
}
