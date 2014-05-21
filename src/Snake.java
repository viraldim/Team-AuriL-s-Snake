import java.awt.*;

import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
 
public class Snake extends JFrame implements KeyListener {
	
    //
    private int windowWidth = 800;
    private int windowHeight = 600;
    private ArrayList<Point> snake;
    private ArrayList<Point> obstacles;
    private Point food;
    private int dx;
    private int dy;
    private boolean snakeIsAlive;
    private int points;
    private int level;
    private Random randomGenerator = new Random();
    private Point startSnakePosition;												
    private Point startFoodPosition;
   
    public static void main(String[] args) {    	
    	new Snake();
    } 
    
    //handles the main window of the game and sets the start
    public Snake() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(windowWidth, windowHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Snake Game By Team Auril");
        this.setVisible(true);
        this.createBufferStrategy(2);   
        this.addKeyListener(this);
        snakeIsAlive = true;
        
        startSnakePosition = new Point (20 + randomGenerator.nextInt(windowWidth / 10 - 20),
				   						20 + randomGenerator.nextInt(windowHeight / 10 - 20));
        startFoodPosition = new Point (15 + randomGenerator.nextInt(windowWidth / 10 - 15),
        							   15 + randomGenerator.nextInt(windowHeight / 10 - 15));
        
        initGame();
        
        while(true) {
            long start = System.currentTimeMillis();
            gameLoop();
            while (System.currentTimeMillis() - start < 75 - level * 5) {
                 //waiting
            	 //the higher the level, the faster the snake
            }
        }
    }   
    private void initGame() {
        snake = new ArrayList<Point>();
        snake.add(startSnakePosition);
        growSnake(2);
       
        obstacles = new ArrayList<Point>();
        
        food = startFoodPosition;
        
        dx = 0;
        dy = 0;
        points = 0;
        level = 1;
        }
    
    //main loop in thegame handles movment and new object placement
    private void gameLoop() {       
        // move the snake
        moveSnake(dx, dy);
       
        // checks conditions
        if(snake.get(0).equals(food)) {
        	generateFood();
        	generateObstacles(2);
            growSnake(2);
            points++;
            if (points % 5 == 0) {
            	level++;
            }
        }
        
        // obstacle check
        for (Point p : obstacles) {
			if(snake.get(0).equals(p)) {
				snakeIsAlive = false;
				initGame();
			}
		}
        
        // check if the snake has hit itself
        for(int n = 3; n < snake.size(); n++) {
            if(snake.get(0).equals(snake.get(n))) {
            	snakeIsAlive = false;
                initGame();
            }
        }       
        
        // our snake can move through walls
        if (snake.get(0).x < 0) {
        	snake.get(0).x = windowWidth / 10;
        }
        else if (snake.get(0).x >= windowWidth / 10) {
        	snake.get(0).x = 0;
        }
        else if (snake.get(0).y < 2) {
        	snake.get(0).y = windowHeight / 10;
        }
        else if (snake.get(0).y >= windowHeight / 10) {
        	snake.get(0).y = 2;
        }     
                
        drawFrame();
    }    
   
    private void drawFrame() {
        BufferStrategy buffer = this.getBufferStrategy();
        Graphics g = null;
       
        try {
            g = buffer.getDrawGraphics();
           
            // cleaning the buffer
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, windowWidth, windowHeight);
            
            // drawing
            if (snakeIsAlive) {
            	drawObstacles(g);
            	drawFood(g);
                drawSnake(g);                    
                drawScore(g);
            }            
            else {
            	gameOver(g);
            }
            
        } finally {
            g.dispose();
        }
       
        // Showing the contents of the buffer
        buffer.show();        
        Toolkit.getDefaultToolkit().sync();
    }
   
    private void drawSnake (Graphics g) {
		try {
			Image skin = ImageIO.read(getClass().getResource("/SnakeSkin.jpg"));
			Image head = ImageIO.read(getClass().getResource("/SnakeHead.png"));
			g.drawImage(head, snake.get(0).x*10, snake.get(0).y*10, 10, 10, null);
	        for (int i = 1; i < snake.size(); i++) {            	        	
	            g.drawImage(skin, snake.get(i).x*10, snake.get(i).y*10, 10, 10, null);       
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}	
    	/*g.setColor(Color.GREEN);
        for (Point p : snake) {
           g.fillRect(p.x*10, p.y*10, 10, 10);
        }*/
    }
   
    private void moveSnake(int dx, int dy) {
        for (int n = snake.size() - 1; n > 0; n--) {
            snake.get(n).setLocation(snake.get(n-1));
        }
        snake.get(0).x += dx;
        snake.get(0).y += dy;
    }
   
    private void growSnake (int count) {
        for (int i = 0; i < count; i++) {
        	snake.add(new Point(snake.get(snake.size() - 1)));
        }
    }
   
    private void generateFood() {
    	food = new Point(8 + randomGenerator.nextInt(windowWidth / 10 - 8),
						8 + randomGenerator.nextInt(windowHeight / 10 - 8));
    }
   
    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(food.x*10, food.y*10, 10, 10);
    }
    private void generateObstacles(int count) {
    	for (int i = 0; i < count; i++) {
    		obstacles.add(new Point(4 + randomGenerator.nextInt(windowWidth / 10 - 4),
    								4 + randomGenerator.nextInt(windowHeight / 10 - 4)));
    	}
    }
    private void drawObstacles(Graphics g) {
    	g.setColor(Color.WHITE);
    	for (Point p : obstacles) {       
    		g.fillRect(p.x*10, p.y*10, 10, 10);
    	}
    }
    
    private void drawScore (Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Total score: " + points, 718, 40);
        g.drawString("Level: " + level, 720, 52);
    }
    
    private void gameOver(Graphics g) {
        String gmOver = "GAME OVER!";
        String score = "Score: " + points;
        String next = "Press any key to continue";
        int gmOverFontSize = 70;
        int scoreFontSize = 40;
        g.setColor(Color.GREEN);
        g.setFont(new Font("Tahoma", Font.PLAIN, gmOverFontSize)); 
        g.drawString(gmOver, centralTxt(gmOver, gmOverFontSize, g), 250);
        g.setFont(new Font("Tahoma", Font.PLAIN, scoreFontSize)); 
        g.drawString(score, centralTxt(score, scoreFontSize, g), 300);
        g.drawString(next, centralTxt(next, scoreFontSize, g), 400);  	
    }   
    
    private int centralTxt (String txt, int txtSize, Graphics g) {
    	FontMetrics fm = g.getFontMetrics();
    	int strWidth = fm.stringWidth(txt);
    	int middle = (windowWidth / 2) - (strWidth / 2);
    	return middle;
    }
    
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key != 0){
        	snakeIsAlive = true;
        }
        if (key == 37) {
	         dy = 0;
	         if (dx != 1) dx = -1;
        } else if (key == 38) {
	         dx = 0;
	         if (dy != 1) dy = -1;
        } else if (key == 39) {
	         dy = 0;
	         if (dx != -1) dx = 1;
        } else if (key == 40) {
	         dx = 0;
	         if (dy != -1) dy = 1;
        }       
    }   
    @Override
    public void keyReleased(KeyEvent e) {}   
    @Override
    public void keyTyped(KeyEvent e) {}
    
    
}