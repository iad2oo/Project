import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class GamePanel extends JPanel implements Runnable
{
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    boolean gameOver = false;
    boolean gameWon = false;
    public final int tileSize = originalTileSize * scale; // 48 x 48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * tileSize; // 768 pixels
    public final int screenHeight = maxScreenRow * tileSize; // 576 pixels
    
    int shootCooldown = 20;
    int enemiesDefeated = 0;
    int currentWave = 1;
    int maxWaves = 5;
    boolean waveClear = false;
    int waveCooldown = 0;
    //FPS Var
    int FPS = 59;
    
    Thread gameThread;
    KeyHandling keyH = new KeyHandling();
    Player player = new Player(this, keyH);
    
    public void setupGame()
    {
        spawnWave(currentWave);
    }
    
    public void spawnWave(int wave)
    {
        enemies.clear();
        int enemyCount = wave * 2;
        
        for(int i = 0; i < enemyCount; i++)
        {
            int x = (int)(Math.random() * (screenWidth - tileSize * 3) + 75);
            int y = (int)(Math.random() * (screenHeight - tileSize * 3));
            Enemy e = new Enemy(this, x, y);
            e.speed = wave;
            enemies.add(e);
        }
    }
    
    //CopyOnWriteArrayList of Enemies and Bullets
    CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
    
    //Set player default position and speed --->>> This was moved to player and enemy classes
    // int playerX = 100;
    // int playerY = 100;
    // int playerSpeed = 4;
    
    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH); //Recognizes key strokes and adds them to the panel.
        this.setFocusable(true); //The panel can be focused, in a way, to recieve key input.
        setupGame();
    }
    
    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    public void run()
    {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        
        while(gameThread != null)
        {
            //System.out.println("The game thread is running..."); //it was just a test
            update();
            repaint();
            
            try
            {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;
                
                if(remainingTime < 0)
                {
                    remainingTime = 0;
                }
                
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void update()
    {
        if(gameOver || gameWon) return;
        
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
        
        player.update();
        
        if(player.hp <= 0)
        {
            gameOver = true;
        }
        
        if(enemies.isEmpty())
        {
            waveCooldown++;
            if(waveCooldown >= 180)
            {
                currentWave++;
                waveCooldown = 0;
                if(currentWave <= maxWaves)
                {
                    spawnWave(currentWave);
                }
                else
                {
                    gameWon = true;
                }
            }
        }
        
        if(keyH.shootPressed && shootCooldown == 0)
        {
            Bullet b = new Bullet(this);
            b.x = player.x + this.tileSize /2;
            b.y = player.y;
            b.active = true;
            bullets.add(b);
            shootCooldown = 20;
        }
        if(shootCooldown > 0)
        {
            shootCooldown--;
        }
        
        for(Enemy e: enemies)
        {
            e.update();
        }
        
        for(Bullet b: bullets)
        {
            b.update();
            if(b.isEnemyBullet)
            {
                if(b.hitbox.intersects(player.hitbox))
                {
                    player.hp -= 10;
                    bulletsToRemove.add(b);
                }
            }
            else
            {
                for(Enemy e : enemies)
                {
                    if(b.hitbox.intersects(e.hitbox))
                    {
                        e.hp -= 10;
                        bulletsToRemove.add(b);
                        if(e.hp <= 0)
                        {
                            enemiesToRemove.add(e);
                            enemiesDefeated++;
                        }
                        break;
                    }
                }
            }
        }
        
        enemies.removeAll(enemiesToRemove);
        bullets.removeAll(bulletsToRemove);
    }
    
    //Graphics
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        player.draw(g2);
        for(Enemy e: enemies)
        {
            e.draw(g2);
        }
        for(Bullet b: bullets)
        {
            b.draw(g2);
        }
        
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.WHITE);
        g2.drawString("HP: " + player.hp, 10, 30);
        g2.drawString("Enemies Defeated: " + enemiesDefeated, 10, 60);
        g2.drawString("Wave: " + currentWave + "/" + maxWaves, 10, 90);
        
        if(enemies.isEmpty() && currentWave <= maxWaves)
        {
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.setColor(Color.YELLOW);
            g2.drawString("WAVE CLEARED!!!", screenWidth/2 - 100, screenHeight/2);
        }
        
        if(gameOver)
        {
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.setColor(Color.RED);
            g2.drawString("Game Over!", screenWidth/2 - 100, screenHeight/2);
        }
        if(gameWon)
        {
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.setColor(Color.YELLOW);
            g2.drawString("YOU WIN!..PRIZE: $1", screenWidth/2 - 80, screenHeight/2);
            g2.drawString("COMPLETE!!!", screenWidth/2 - 100, screenHeight/2);
            currentWave--;
        }
        
        g2.dispose();
    }
}