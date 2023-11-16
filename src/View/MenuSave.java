package View;

import javax.swing.JPanel;

import Controller.ButtonMenu;

import java.util.*;
import java.awt.Graphics;
import java.awt.GridLayout;

public class MenuSave extends JPanel {

	private ArrayList<String> buttonNames = new ArrayList<String>();
	private Window w;

	private Images img = new Images(1);
	private JPanel main = new JPanel(new GridLayout(3,1));
    public MenuSave(Window w) {
    	this.w = w;
 
    	//layout manager
        this.setLayout(new GridLayout(1,3));

        this.add(new JPanel());
        this.add(main);
        this.add(new JPanel());
        
        
        createButton("Save 1");
        createButton("Save 2");
        createButton("Save 3");
        
 
    }
    public void paint(Graphics g) {
    	int width = this.getWidth();
    	int height = this.getHeight();
    	g.drawImage(img.getImage("menu.png"), 0, 0, width, height,this);
    	main.repaint();
    }
    //methode qui permet de creer les boutons
    public void createButton(String name) {
    	ButtonMenu res = new ButtonMenu(name, w);
    	main.add(res);
    	buttonNames.add(name);
    }
    
    

}