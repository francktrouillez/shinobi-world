package Controller;

import javax.swing.JButton;
import Model.Save;
import View.Images;
import View.Sound;
import View.Window;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//classe pour les boutons de l ecran d accueil ou de l ecran de pause
public class ButtonMenu extends JButton implements MouseListener {
	
	private String name;
	private Window window;
	
	public ButtonMenu(String s, Window w) {
		
		this.setText(s);
		this.name = s;
		this.window = w;
		this.addMouseListener(this);
		}
		
	public void mouseClicked(MouseEvent event) {
	
		switch(name){
		case "New Game" : newGame(); break;
		case "Reprendre" : reprendre();break;
		/*case "Sound Up" : soundUp();break;
		case "Sound Down" : soundDown();break;*/
		case "Sound On/Off" : soundOnOff();break;
		case "Save & Quit" : quitSave();break;
		case "Load Save": loadSave(); break;
		case "Quit": quit(); break;
		case "Save 1": save(1); break;
		case "Save 2": save(2); break;
		case "Save 3": save(3); break;
		}
		
	}
	
	
	//methode de creation d un nouveau jeu
	private void newGame() {
		
		window.getGame().newGame();
		
		try {
			Thread.sleep(500);
			window.changePanel("groupPanel"); //on change la carte du cardlayout afin d afficher le panel du jeu
			
		}catch(Exception e) {}
		
		
	}
	private void loadSave() {
		
		window.changePanel("MenuSave");
	}
	
	//methode qui cree une nouvelle sauvegarde du jeu
	private void save(int i) {
		
		if (window.getGame().isCreated()) {
			
			Save s = new Save(window.getGame(), i);
			s.writeSave();
			quit();
		}
		
		else {
			
			Save s = new Save(window.getGame());
			
			try {
				Thread.sleep(500);
				
			} catch(Exception e) {}
			
			s.loadSave("save"+i+".txt");
			
			try {
				Thread.sleep(500);
				
			} catch(Exception e) {}
			
			window.changePanel("groupPanel");
		}
	}
	
	//methode pour quitter le programme 
	private void quit() {
		System.exit(0);
	}
	
	//methode pour retourner sur le panel de jeu
	private void reprendre() {
		window.changePanel("groupPanel");
	}
	
	//methodes pour modifier le son
	/*private void soundUp() {
		Sound.goUp();
	}
	
	private void soundDown() {
		Sound.goDown();
	}*/
	
	private void soundOnOff() {
		Sound.switchOnOff();
	}
	
	//methode pour revenir au panel contenant les save
	private void quitSave() {

		window.changePanel("MenuSave");
		
	}

	public void mouseEntered(MouseEvent event) {
	}
	public void mouseExited(MouseEvent event) {		
	}
	public void mousePressed(MouseEvent event) {		
	}
	public void mouseReleased(MouseEvent event) {
	}
	
}