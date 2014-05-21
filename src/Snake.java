import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
 
public class Snake extends JFrame implements KeyListener {
   
    private int windowWidth = 800;
    private int windowHeight = 600;
    private ArrayList<Point> snake;
    private ArrayList<Point> obstacles;
    private Point food;
    private int dx;
    private int dy;
    private int points;
    private int level;
    private Random randomGenerator = new Random();
    private Point startSnakePosition;												
    private Point startFoodPosition;
   
    public static void main(String[] args) {    	
    	new Snake();
    }   
    public Snake() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(windowWidth, windowHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Snake Game By Team Auril");
        this.setVisible(true);
        this.createBufferStrategy(2);   
        this.addKeyListener(this);
        
        startSnakePosition = new Point (20 + randomGenerator.nextInt(windowWidth / 10 - 20),
				   						20 + randomGenerator.nextInt(windowHeight / 10 - 20));
        startFoodPosition = new Point (15 + randomGenerator.nextInt(windowWidth / 10 - 15),
        							   15 + randomGenerator.nextInt(windowHeight / 10 - 15));
        initGame();
       
        while(true) {
            long start = System.currentTimeMillis();
            gameLoop();
            while (System.currentTimeMillis() - start < 70 - level * 5) {
                 //waiting
            	 //the more points you have, the faster the snake
            }
        }
    }   
    private void initGame() {
        snake = new ArrayList<Point>();
        snake.add(startSnakePosition);
        growSnake(7);
       
        obstacles = new ArrayList<Point>();
        
        food = startFoodPosition;
        
        dx = 0;
        dy = 0;
        points = 0;
        level = 1;
        }   
    private void gameLoop() {       
        // move the snake
        moveSnake(dx, dy);
       
        // food check
        if(snake.get(0).equals(food)) {
        	generateFood();
        	generateObstacles(2);
            growSnake(1);
            points++;
            if (points % 5 == 0) {
            	level++;
            }
        }
        
        // obstacle check
        for (Point p : obstacles) {
			if(snake.get(0).equals(p)) {
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
       
        // check if the snake has hit itself
        for(int n = 1; n < snake.size(); n++) {
            if(snake.get(0).equals(snake.get(n))) {
                initGame();
            }
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
            drawObstacles(g);
            drawFood(g);
            drawSnake(g);                    
            drawScore(g);
        } finally {
            g.dispose();
        }
       
        // Showing the contents of the buffer
        buffer.show();        
        Toolkit.getDefaultToolkit().sync();
    }
   
    private void drawSnake (Graphics g) {
    	g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x*10, p.y*10, 10, 10);
        }
    }
   
    private void moveSnake(int dx, int dy) {
        for (int n = snake.size() - 1; n >= 1; n--) {
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
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
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