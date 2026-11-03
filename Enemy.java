import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class Enemy extends Entity
{
    GamePanel gp;
    public boolean alive = true;
    int hp = 10;
    int shootCooldown = 0;
    int shootInterval = 60;
    
    public Enemy(GamePanel gp, int x, int y)
    {
        this.gp = gp;
        this.x = x;
        this.y = y;
        setDefaultValues();
        getEnemyImage();
    }
    
    public void getEnemyImage()
    {
        try
        {
            
            center = ImageIO.read(getClass().getResourceAsStream("golem.png"));
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void setDefaultValues()
    {
        speed = 2;
        direction = "down";
        hitbox = new Rectangle(x, y, gp.tileSize * 3, gp.tileSize * 3);
    }
    
    public void update()
    {
        if(direction.equals("down"))
        {
            y += speed;
            if(y >= (gp.screenHeight - gp.tileSize * 3) + 50)
            {
                direction = "up";
            }
        }
        else if (direction.equals("up"))
        {
            y -= speed;
            if(y <= 0)
            {
                direction = "down";
            }
        }
        
        hitbox.y = y;
        hitbox.x = x;
        
        if(shootCooldown <= 0)
        {
            Bullet b = new Bullet(gp);
            b.x = x + (gp.tileSize * 3 / 2); // center of the enemy
            b.y = y;
            b.active = true;
            b.isEnemyBullet = true; 
            gp.bullets.add(b);
            shootCooldown = shootInterval;
        }
        else
        {
            shootCooldown--;
        }
    }
    
    public void draw(Graphics2D g2)
    {
        BufferedImage image = null;
        if(alive)
        {
            switch(direction)
            {
                case "down":
                case "up":
                    image = center;
                    break;
            }
            g2.drawImage(image, x , y, gp.tileSize * 3, gp.tileSize * 3, null);
        }
    }
}