import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet
{
    public int x, y, speed; 
    public boolean active = false;
    Rectangle hitbox;
    GamePanel gp;
    public boolean isEnemyBullet = false;
    
    public Bullet(GamePanel gp)
    {
        this.gp = gp;
        speed = 8;
        hitbox = new Rectangle(x, y, 5, 5);
    }
    
    public void update()
    {
        if(isEnemyBullet)
        {
            x -= speed;
        }
        else
        {
            x += speed;
        }
        
        hitbox.x = x;
        hitbox.y = y;
        
        if(x < 0 || x > gp.screenWidth)
        {
            active = false;
        }
    }
    
    public void draw(Graphics2D g2)
    {
        if(active)
        {
            g2.setColor(Color.YELLOW);
            g2.fillRect(x, y, 5, 5);
        }
    }
}