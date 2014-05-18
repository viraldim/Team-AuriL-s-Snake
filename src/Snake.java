import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
 
public class Snake extends JFrame implements KeyListener {
   
    private int windowWidth = 800;
    private int windowHeight = 600;
    private LinkedList<Point> snake;
    private ArrayList<Point> obstacles;
    private Point food;
    private int dx;
    private int dy;
    private Random generator = new Random();    
    private int points;
   
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
        
        initGame();
       
        while(true) {
            long start = System.currentTimeMillis();
            gameLoop();
            while (System.currentTimeMillis() - start < 65 - points) {
                 //waiting
            	 //the more points you have, the faster the snake
            }
        }
    }   
    private void initGame() {
        snake = new LinkedList<Point>();
        snake.addFirst(new Point(50, 30));
        growSnake(7);
       
        obstacles = new ArrayList<Point>();
        
        food = new Point(40, 50);
        
        dx = 0;
        dy = 0;
        points = 0;        
        }   
    private void gameLoop() {       
        // move the snake
        moveSnake(dx, dy);
       
        // food check
        if(snake.getFirst().equals(food)) {
        	generateFood();
        	generateObstacles(2);
            growSnake(1);
            points++;
        }
        
        // obstacle check
        for (Point p : obstacles) {
			if(snake.getFirst().equals(p)) {
				initGame();
			}
		}
        
        // our snake can move through walls
        if (snake.getFirst().x < 0) {
        	snake.getFirst().x = windowWidth / 10;
        }
        else if (snake.getFirst().x >= windowWidth / 10) {
        	snake.getFirst().x = 0;
        }
        else if (snake.getFirst().y < 2) {
        	snake.getFirst().y = windowHeight / 10;
        }
        else if (snake.getFirst().y >= windowHeight / 10) {
        	snake.getFirst().y = 2;
        }
       
        // check if the snake has hit itself
        for(int n = 1; n < snake.size(); n++) {
            if(snake.getFirst().equals(snake.get(n))) {
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
            drawSnake(g);
            drawObstacles(g);
            drawFood(g);
            drawPoints(g);
        } finally {
            g.dispose();
        }
       
        // Showing the contents of the backbuffer
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
        snake.getFirst().x += dx;
        snake.getFirst().y += dy;
    }
   
    private void growSnake (int count) {
        for (int i = 0; i < count; i++) {
            snake.add(new Point(snake.getLast()));
        }
    }
   
    private void generateFood() {
        food.x = 8 + generator.nextInt(windowWidth / 10 - 8);
        food.y = 8 + generator.nextInt(windowHeight / 10 - 8);
    }
   
    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(food.x*10, food.y*10, 10, 10);
    }
    private void generateObstacles(int count) {
    	for (int i = 0; i < count; i++) {
    		obstacles.add(new Point(4 + generator.nextInt(windowWidth / 10 - 4), generator.nextInt(4 + windowHeight / 10 - 4)));
    	}
    }
    private void drawObstacles(Graphics g) {
    	g.setColor(Color.WHITE);
    	for (Point p : obstacles) {       
        g.fillRect(p.x*10, p.y*10, 10, 10);
    	}
    }
    private void drawPoints(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("Total score: " + points, 710, 40);
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