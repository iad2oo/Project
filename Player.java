import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class Player extends Entity
{
    public int x;
    public int y, hp;
    
    GamePanel gp;
    KeyHandling keyH;
    
    public Player(GamePanel gp, KeyHandling keyH)
    {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }
    
    public void setDefaultValues()
    {
        x = 100;
        y = 288;
        hp = 100;
        speed = 4;
        direction = "down";
        hitbox = new Rectangle(x, y, gp.tileSize, gp.tileSize);
    }
    
    public void getPlayerImage()
    {
        try
        {
            
            center = ImageIO.read(getClass().getResourceAsStream("Snake.png"));
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void update()
    {
        hitbox.x = x;
        hitbox.y = y;
        if(hp == 0)
        {
            
        }
        
        if(keyH.upPressed == true)
        {
            if(y > 0)
            {
                y -= speed;
            }
        }
        else if(keyH.downPressed == true)
        {
            if(y < gp.screenHeight - gp.tileSize)
            {
                y += speed;
            }
        }
        else if(keyH.leftPressed == true)
        {
            if(x > 0)
            {
                x -= speed;
            }
        }
        else if(keyH.rightPressed == true)
        {
            if(x < gp.screenWidth - gp.tileSize)
            {
                x += speed;
            }
        }
        if(keyH.rightPressed == true && keyH.upPressed == true)
        {
            if(x < gp.screenWidth - gp.tileSize && y > 0)
            {
                x += speed;
                y -= speed;
            }
        }
        
        if(keyH.rightPressed == true && keyH.downPressed == true)
        {
            if(x < gp.screenWidth - gp.tileSize && y < gp.screenHeight - gp.tileSize)
            {
                x += speed;
                y += speed;
            }
        }
        
        if(keyH.leftPressed == true && keyH.upPressed == true)
        {
            if(x > 0 && y > 0)
            {
                x -= speed;
                y -= speed;
            }
        }
        
        if(keyH.leftPressed == true && keyH.downPressed == true)
        {
            if(y < gp.screenHeight - gp.tileSize && x > 0)
            {
                x -= speed;
                y += speed;
            }
        }
    }
    
    public void draw(Graphics2D g2)
    {
        // g2.setColor(Color.WHITE);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);
        
        BufferedImage image = null;
        switch(direction)
        {
            case "down":
                image = center;
                break;
        }
        
        g2.drawImage(image, x , y, gp.tileSize, gp.tileSize, null);
    }
}