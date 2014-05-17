import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;
 
public class Snake extends JFrame implements KeyListener {
   
    private int windowWidth = 800;
    private int windowHeight = 600;
    private LinkedList<Point> snake;
    private int dx;
    private int dy;
    private Random generator = new Random();
    private Point food;
    private int points;
   
   
    public static void main(String[] args) {
        new Snake();
    }
   
    public Snake() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(windowWidth, windowHeight);
        this.setResizable(false);
        this.setLocation(100, 100);
        this.setVisible(true);
       
        this.createBufferStrategy(2);
       
        this.addKeyListener(this);
       
        initGame();
       
        while(true) {
            long start = System.currentTimeMillis();
            gameLoop();
            while(System.currentTimeMillis()-start < 40) {
                 //do nothing
            }
        }
    }
   
    private void initGame() {
        // all you're game variables should be initialized here
        snake = new LinkedList<Point>();
        snake.addFirst(new Point(20,20));
        growSnake(5);
       
        dx = 0;
        dy = 0;
       
        food = new Point(10,10);
       
        points = 0;
    }
   
    private void gameLoop() {
        // your game logic goes here
       
        // move the snake
        moveSnake(dx, dy);
       
        // check if our snake has eaten his food :)
        if(snake.getFirst().equals(food)) {
            moveFood();
            growSnake(3);
            points++;
        }
       
        // check if any walls have been hit
        if(snake.getFirst().x <= 0 ||
            snake.getFirst().x >= windowWidth/10 ||
            snake.getFirst().y <= 2 ||
            snake.getFirst().y >= windowHeight/10) {
            initGame();
        }
       
        // check if the snake has hit itself
        for(int n = 1; n < snake.size(); n++) {
            if(snake.getFirst().equals(snake.get(n))) {
                initGame();
            }
        }
       
        // so much for game logic now let's draw already!
        drawFrame();
    }
   
    private void drawFrame() {
        // code for the drawing goes here
        BufferStrategy bf = this.getBufferStrategy();
        Graphics g = null;
       
        try {
            g = bf.getDrawGraphics();
           
            // clear the back buffer (just draw a big black rectangle over it)
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, windowWidth, windowHeight);
           
            drawSnake(g);
            drawFood(g);
            drawPoints(g);
        } finally {
            // It is best to dispose() a Graphics object when done with it.
            g.dispose();
        }
       
        // Shows the contents of the backbuffer on the screen.
        bf.show();
     
        //Tell the System to do the Drawing now, otherwise it can take a few extra ms until
        //Drawing is done which looks very jerky
        Toolkit.getDefaultToolkit().sync();
    }
   
    private void drawSnake (Graphics g) {
        for(int n = 0; n < snake.size(); n++) {
            g.setColor(Color.RED);
            Point p = snake.get(n);
            g.fillOval(p.x*10, p.y*10, 10, 10);
        }
    }
   
    private void moveSnake(int dx, int dy) {
        for(int n = snake.size()-1; n >= 1; n--) {
            snake.get(n).setLocation(snake.get(n-1));
        }
        snake.getFirst().x += dx;
        snake.getFirst().y += dy;
    }
   
    private void growSnake (int n) {
        while(n > 0) {
            snake.add(new Point(snake.getLast()));
            n--;
        }
    }
   
    private void moveFood() {
        food.x = generator.nextInt((windowWidth/10)-4)+2;
        food.y = generator.nextInt((windowHeight/10)-5)+3;
    }
   
    private void drawFood(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(food.x*10, food.y*10, 10, 10);
    }
   
    private void drawPoints(Graphics g) {
        g.setColor(Color.GRAY);
        g.drawString("points: " + points, 10, 40);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
       
        if(key == 37) {
            dx = -1;
            dy = 0;
        } else if(key == 38) {
            dx = 0;
            dy = -1;
        } else if(key == 39) {
            dx = 1;
            dy = 0;
        } else if(key == 40) {
            dx = 0;
            dy = 1;
        }
    }
   
    @Override
    public void keyReleased(KeyEvent e) {
        // not needed
    }
   
    @Override
    public void keyTyped(KeyEvent e) {
        // not needed
    }
}