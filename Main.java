import javax.swing.*;
import java.awt.*;
//import java.util.Scanner;

public class Main 
{
    public static void main(String[] args) 
    {
        //Creating the Scanner
        //Scanner input = new Scanner(System.in);
        
        // //creating instance of JFrame
        JFrame window = new JFrame();
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        gamePanel.startGameThread();
        
    }
}