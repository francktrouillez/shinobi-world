package View;

import javax.swing.JFrame;

import Controller.ButtonInventaire;

import java.awt.GridLayout;

import Model.Player;

public class Armurerie extends JFrame {
	
	private Player p;

	public Armurerie(Player p) {
		
		this.setTitle("Armurerie");
		this.setBounds(0, 0, 500, 500);
		this.setLocationRelativeTo(null);
		this.p = p;
		
		//creation des boutons
		ButtonInventaire b1 = new ButtonInventaire("Life: +10 pour 150 gold",null,0,this.p,null,this);
		ButtonInventaire b2 = new ButtonInventaire("Damage: +2 pour 200 gold",null,1,this.p,null,this);
		ButtonInventaire b3 = new ButtonInventaire("Inventaire: +1 pour 300 gold",null,2,this.p,null,this);
		ButtonInventaire b4 = new ButtonInventaire("Nourriture: +1 pour 200 gold",null,3,this.p,null,this);
		ButtonInventaire b5 = new ButtonInventaire("Eau: +1 pour 100 gold",null,4,this.p,null,this);
		ButtonInventaire b6 = new ButtonInventaire("Potion: +1 pour 3000 gold",null,5,this.p,null,this);
		ButtonInventaire b7 = new ButtonInventaire("Alcool: +1 pour 200 gold",null,6,this.p,null,this);
		
		//Layout manager
		GridLayout gl = new GridLayout();
		gl.setColumns(1);
		gl.setRows(7);
		gl.setVgap(5);
		this.setLayout(gl);
		
		//ajout des composants au conteneur
		this.getContentPane().add(b1);
		this.getContentPane().add(b2);
		this.getContentPane().add(b3);
		this.getContentPane().add(b4);
		this.getContentPane().add(b5);
		this.getContentPane().add(b6);
		this.getContentPane().add(b7);
		
		
		this.setVisible(true);
	}
}
