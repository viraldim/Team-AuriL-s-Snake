package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import TileMap.Background;

public class MenuState extends GameState {
	
	private Background bg;
	private String[] options = {"Start","Help","Quit"};
	private int currentChoice = 0;
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	public MenuState(GameStateManager gsm){
		this.gsm = gsm;
		
		try{
			bg = new Background("/Backgrounds/menubg.gif",1);
			bg.setVector(1,0);
			
			titleColor = new Color(100,0,0);
			titleFont = new Font("Times",Font.BOLD,40);
			
			font =  new Font("Arial",Font.PLAIN,32);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void init(){}
	public void update(){
		bg.update();
	}
	public void draw(java.awt.Graphics2D g){
		bg.draw(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Team Auril's Snake", 220, 60);
		
		g.setFont(font);
		//draw menu options
		for (int i = 0; i < options.length; i++) {
			
			if (i == currentChoice) {
				g.setColor(Color.BLACK);
			}
			else{
				g.setColor(Color.BLUE);
			}
			
			g.drawString(options[i], (GamePanel.width/2)-options[i].length(), (GamePanel.hight/2) + i*33);
		}
		
	}
	
	private void select(){
		if(currentChoice == 0){
			
		}
		if(currentChoice == 1){
			
		}
		if(currentChoice == 2){
			System.exit(0);
		}
			
	}
	public void keyPressed(int k){
		if(k== KeyEvent.VK_ENTER){
			select();
		}
		if(k == KeyEvent.VK_UP){
			currentChoice--;
			if (currentChoice==-1) {
				currentChoice =2;
			}
		}
		if(k == KeyEvent.VK_DOWN){
				currentChoice++;
				if (currentChoice ==3) {
					currentChoice=0;
				}
		}
	}
	public void keyReleased(int k){}
}
