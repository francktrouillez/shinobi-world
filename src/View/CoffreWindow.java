package View;

import javax.swing.JFrame;

import Controller.ButtonInventaire;

import java.awt.GridLayout;

import Model.Coffre;
import Model.Player;

public class CoffreWindow extends JFrame {

	private Player p;
	private Coffre c;

	//fenetre associee a l ouverture d un coffre c par un joueur p
	public CoffreWindow(Player p, Coffre c) {
		this.setTitle("Coffre");
		this.setBounds(0, 0, 500, 500);
		this.setLocationRelativeTo(null);
		this.p = p;
		this.c = c;

		//creation des boutons
		ButtonInventaire b1 = new ButtonInventaire("dposer de la nourriture: ",""+this.p.compteurloot()[0],7,this.p,this.c,this);
		ButtonInventaire b2 = new ButtonInventaire("reprendre de la nourriture: ",""+this.c.getFoodList().size(),8,this.p,this.c,this);
		ButtonInventaire b3 = new ButtonInventaire("dposer une boisson:",""+this.p.compteurloot()[1],9,this.p,this.c,this);
		ButtonInventaire b4 = new ButtonInventaire("reprendre une boisson: ",""+this.c.getDrinkList().size(),10,this.p,this.c,this);

		//Layout manager
		GridLayout gl = new GridLayout();
		gl.setColumns(2);
		gl.setRows(2);
		gl.setVgap(5);
		gl.setHgap(5);
		this.setLayout(gl);

		//ajout des composants au conteneur de la fenetre
		this.getContentPane().add(b1);
		this.getContentPane().add(b2);
		this.getContentPane().add(b3);
		this.getContentPane().add(b4);


		this.setVisible(true);
	}
}
