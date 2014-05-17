package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.*;

import org.omg.CORBA.INITIALIZE;

import GameState.GameStateManager;

public class GamePanel extends JPanel implements Runnable,KeyListener{

	//window dimensions
	public static int width = 800;
	public static int hight = 600;
	
	//game thread
	private Thread thread;
	private boolean running;
	private int fps = 60;
	private long targetTime = 1000/fps;
	
	//image
	private BufferedImage image;
	private Graphics2D gameGraphics;
	
	//game state manager	
	private GameStateManager gsm;
	
	public GamePanel(){
		super();
		setPreferredSize(new Dimension(width,hight));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify(){
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();			
		}
	}
	
	private void init(){
		
		image = new BufferedImage(width, hight,
				BufferedImage.TYPE_INT_RGB);
		gameGraphics = (Graphics2D) image.getGraphics();
		
		running = true;
		
		gsm = new GameStateManager();
	}
	
	public void run(){
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		//game loop
		while(running){
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - (elapsed / 1000000);//converts nano to mili secnds
			
			if (wait ==0) {
				wait = 5;
			}
			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();  
			}
		}
	}
	
	private void draw() {
		gsm.draw(gameGraphics);
		
	}
	
	private void update() {
		gsm.update();
		
	}
	
	private void drawToScreen() {
		Graphics gameGr = getGraphics();
		gameGr.drawImage(image, 0, 0,width,hight, null);
		gameGr.dispose();
		
	}



	public void keyTyped(KeyEvent key){}
	public void keyPressed(KeyEvent key){
		gsm.keyPressed(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key){
		gsm.keyReleased(key.getKeyCode());
	}
}
