package Game;

import Engine.AbstractGame;
import Engine.GameContainer;
import Engine.Renderer;
import Engine.gfx.Image;
import Game.Enemies.Enemy;

import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class GameManager extends AbstractGame {
    public final static int TS=16;
    private Image background;

    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private ArrayList<GameObject> enemies = new ArrayList<>();

    public GameManager(){
    }

    @Override
    public void init(GameContainer gc) {
        gc.getRenderer().setAmbientColor(-1);
        objects.add(new Player(gc.getWidht()/2,gc.getHeight()/2));
        background = new Image("/background.png");
    }

    @Override
    public void update(GameContainer gc, float dt) {
        for(int i=0; i<objects.size();i++){
            objects.get(i).update(gc,this,dt);
            if(objects.get(i).isDead()){
                objects.remove(i--);
            }
        }
        for(int i=0; i<enemies.size();i++){
            enemies.get(i).update(gc,this,dt);
            if(enemies.get(i).isDead()){
                enemies.remove(i--);
            }
        }
        if(gc.getInput().isKeyDown(KeyEvent.VK_F1))
            enemies.add(new Enemy());
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImage(background,0,0);
        for(GameObject obj: objects){
            obj.render(gc,r);
        }
        for(GameObject obj: enemies){
            obj.render(gc,r);
        }
    }

    public void addObject(GameObject obj){
        objects.add(obj);
    }

    public void removeObject(GameObject obj){
        objects.remove(obj);
    }

    public ArrayList<GameObject> getEnemies() {
        return enemies;
    }

    public static void main(String args[]){
        GameContainer gc= new GameContainer(new GameManager());
        gc.setWidht(480);
        gc.setHeight(360);
        gc.setScale(2f);
        gc.start();
    }
}
