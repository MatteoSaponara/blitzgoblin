package Level;

import Engine.Config;
import Engine.GraphicsHandler;
import GameObject.Frame;
import GameObject.PhysicsParticle;
import GameObject.SpriteSheet;

import java.awt.Color;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

// This class is a base class for all enemies in the game -- all enemies should extend from it
public class Enemy extends MapEntity {
    private static final int DAMAGE_FLASH_DURATION = 8;
    private static final int DEATH_PARTICLE_LIFETIME = 180;
    private static final Color BLOOD_COLOR = new Color(190, 24, 42);
    private int damageFlashTimer;
    private boolean deathParticlesSpawned;

    public Enemy(float x, float y, SpriteSheet spriteSheet, String startingAnimation) {
        super(x, y, spriteSheet, startingAnimation);
    }

    public Enemy(float x, float y, HashMap<String, Frame[]> animations, String startingAnimation) {
        super(x, y, animations, startingAnimation);
    }

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
        if (intersects(player)) {
            touchedPlayer(player);
        }
    }

    protected void startDamageFlash() {
        damageFlashTimer = DAMAGE_FLASH_DURATION;
        spawnBloodParticles(1, 3, 5, 60);
    }

    protected void spawnDeathParticles() {
        if (deathParticlesSpawned || map == null || !hasBlood()) {
            return;
        }
        deathParticlesSpawned = true;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        int particleCount = random.nextInt(2, 6);
        spawnBloodParticles(particleCount, 4, 7.5f, DEATH_PARTICLE_LIFETIME);
    }

    protected boolean hasBlood() {
        return true;
    }

    private void spawnBloodParticles(int particleCount, float minSize, float maxSize, int lifetimeFrames) {
        if (map == null || !hasBlood()) {
            return;
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        float centerX = getX() + getWidth() / 2f;
        float centerY = getY() + getHeight() / 2f;

        for (int i = 0; i < particleCount; i++) {
            PhysicsParticle particle = new PhysicsParticle(
                    centerX + (float) random.nextDouble(-2, 2),
                    centerY + (float) random.nextDouble(-2, 2),
                    (float) random.nextDouble(minSize, maxSize),
                    BLOOD_COLOR);
            particle.setVelocity((float) random.nextDouble(-3.2, 3.2), (float) random.nextDouble(-5.8, -2.6));
            particle.setGravity(0, 0.18f);
            particle.setLifetimeFrames(lifetimeFrames);
            map.addParticle(particle);
        }
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
