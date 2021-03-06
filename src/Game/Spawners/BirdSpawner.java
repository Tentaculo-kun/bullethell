package Game.Spawners;

import Engine.GameContainer;
import Engine.sfx.SoundClip;
import Game.Enemies.BirdEnemy;
import Game.GameObject;

import java.util.ArrayList;

public class BirdSpawner extends Spawner{

    private float random,y;
    private int cant=3,cantAct=cant, miniCD =20,actualCD=0;
    private SoundClip soundEffect= new SoundClip("/audio/crow-sound.wav");

    public BirdSpawner(ArrayList<GameObject> enemies, GameContainer gc) {
        super(enemies, gc);
        this.cooldown =300;
        soundEffect.setVolume(-40);
    }

    @Override
    public void spawn() {
        if(time==30)
            soundEffect.play();
        if(time==0) {
            if(cantAct==cant) {
                random = (float) (Math.random() * (gc.getWidht() - 125) + 50);
                if (Math.random() >= 0.5)
                    y = gc.getHeight() - 16;
                else
                    y = 0;
            }
            if(actualCD==0) {
                enemies.add(new BirdEnemy(random, y));
                actualCD= miniCD;
                cantAct--;
            }
            actualCD--;
            if(cantAct==0) {
                cantAct = cant;
                time= cooldown;
                actualCD=0;
            }
        }else
            time--;
    }
}
