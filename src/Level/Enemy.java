package Level;

import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.SpriteSheet;
import java.util.HashMap;

// This class is a base class for all enemies in the game -- all enemies should extend from it
public class Enemy extends MapEntity {
    private static final int DAMAGE_FLASH_DURATION = 8;
    private int damageFlashTimer;

    public Enemy(float x, float y, int layer,  int gameplayLayerIN, SpriteSheet spriteSheet, String startingAnimation) {
        super(x, y, layer, gameplayLayerIN, spriteSheet, startingAnimation);
    }
    public Enemy(float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(x, y, spriteSheet, startingAnimation);
    }
    public Enemy(float x, float y, int layer, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
        numLayer = layer;
    }
    /*public Enemy(float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
        numLayer = layer;
    }*/
    public Enemy(float x, float y, Frame[] frames) {
        super(x, y, frames);
    }

    public Enemy(float x, float y, Frame frame) {
        super(x, y, frame);
    }

    public Enemy(float x, float y) {
        super(x, y);
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void update(Player player) {
        super.update();

        if (damageFlashTimer > 0) {
            damageFlashTimer--;
        }
        
        numLayer = map.getCurrentGameplayLayer();
        if (intersects(player)) {
            touchedPlayer(player);
        }
    }

    protected void startDamageFlash() {
        damageFlashTimer = DAMAGE_FLASH_DURATION;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        if (damageFlashTimer <= 0) {
            super.draw(graphicsHandler);
            return;
        }

        graphicsHandler.setAlpha(0.5f);
        try {
            super.draw(graphicsHandler);
        } finally {
            graphicsHandler.setAlpha(1f);
        }
    }

    // A subclass can override this method to specify what it does when it touches the player
    public void touchedPlayer(Player player) {
        player.hurtPlayer(this);
    }
}
