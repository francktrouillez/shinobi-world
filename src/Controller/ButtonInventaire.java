package Controller;

import javax.swing.JButton;
import javax.swing.JFrame;

import Model.Alcool;
import Model.Apprenti;
import Model.Coffre;
import Model.Confirme;
import Model.Drink;
import Model.Focusable;
import Model.Food;
import Model.Hokage;
import Model.Player;
import Model.Potion;
import Model.Water;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

//classe pour les boutons de l'armurerie ou des coffres
public class ButtonInventaire extends JButton implements MouseListener {

	private Player p;
	private Coffre c;

	private int type;

	//les 3 premiers type sont des ameliorations de statistique limites au niveau du joueur
	private double[] addToStat = {10,2,1};
	private int[][] limitToLevel = {{150,200,250},{30,40,50},{5,7,9}};

	//les 6 premiers types sont des achetables
	private int[] priceList = {150,200,300,200,100,3000,200};
	private int price;

	private int lvl;

	private String s;
	private String t;

	public ButtonInventaire(String s,String t,int i,Player p, Coffre c,JFrame jf) {

		this.type = i;

		this.s = s;
		this.p = p;

		if(t == null) {
			this.setText(s);
		}
		else{
			this.setText(s+" "+t);
		}

		this.c = c;

		//seuls les 7 premiers types de boutons concernent un achat
		if(i<=6) {
			this.price = priceList[i];
		}


		this.addMouseListener(this);

		if(p instanceof Apprenti) {
			this.lvl = 0;
		}
		else if(p instanceof Confirme) {
			this.lvl = 1;
		}
		else if(p instanceof Hokage) {
			this.lvl = 2;
		}
	}


	public void mouseClicked(MouseEvent event) {

		//les boutons pour l achat dans l armurerie englobent les 6 premiers types
		if(type <= 6) {
			if(this.p.getGold() > this.price) {

				if(type == 0) {
					changeLife();
				}

				else if(type == 1) {
					changeDamage();
				}

				else if(type == 2) {
					changeInventaire();
				}

				else if(type == 3 || type == 4 || type == 5 || type == 6) {
					if(p.getLoot().size()<p.getinventaireMax()) {

						if(type == 3) {
							addFood();
						}

						else if(type == 4) {
							addWater();
						}

						else if(type == 5) {
							addPotion();
						}

						else if(type == 6) {
							addAlcool();
						}

						this.p.setGold(this.p.getGold()-this.price);

					}

					else {
						p.getGame().print("L'inventaire est rempli, achetez une place supplmentaire");
					}
				}
			}
			else {
				this.p.getGame().print("Vous n'avez pas assez d'argent");
			}
		}

		//boutons pour le depot ou retrait dans les coffres
		else {
			if(type == 7) {
				dropFood();
			}

			if(type == 8) {
				lootFood();
			}

			if(type == 9) {
				dropDrink();
			}

			if(type == 10) {
				lootDrink();
			}
		}
		this.setTxt();

	}


	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mousePressed(MouseEvent event) {
	}

	public void mouseReleased(MouseEvent event) {
	}

	//methodes de depot ou retrait dans les coffres
	private void dropDrink() {

		ArrayList<Focusable> playerLoot = this.p.getLoot();
		Drink d = null;

		for(Focusable f:playerLoot) {

			if(f instanceof Drink) { //on depose le premier alcool trouve
				d = (Drink)f;
				break;
			}
		}

		if(d != null) {
			this.p.getLoot().remove(d);
			this.c.addToDrinkList(d); //on l ajoute a la liste d alcool du coffre
		}

		else {
			this.p.getGame().print("Vous n'avez pas de boisson  dposer");
		}
	}


	private void lootDrink() {

		if(this.p.getLoot().size()< this.p.getinventaireMax()) { //on regarde d abords s il a de la place dans l inventaire

			if(this.c.getDrinkList().size()!=0) {
				p.addToLoot(this.c.receiveDrink()); //ajoute l alcool a l inventaire et l enleve du coffre
			}

			else {
				this.p.getGame().print("Il n'y a plus de boisson dans l'inventaire");
			}
		}

		else {
			this.p.getGame().print("L'inventaire est plein");
		}
	}


	private void dropFood() {
		ArrayList<Focusable> playerLoot = this.p.getLoot();
		Food fo = null;

		for(Focusable f:playerLoot) {

			if(f instanceof Food) {
				fo = (Food)f;
				break;
			}
		}

		if(fo != null) {
			this.p.getLoot().remove(fo);
			this.c.addToFoodList(fo);
		}

		else {
			this.p.getGame().print("Vous n'avez pas de nourriture  dposer");
		}
	}


	private void lootFood() {

		if(this.p.getLoot().size()< this.p.getinventaireMax()) {

			if(this.c.getFoodList().size()!=0){
				p.addToLoot(this.c.receiveFood());
			}

			else {
				this.p.getGame().print("Il n'y a plus de nourriture dans l'inventaire");
			}
		}

		else {
			this.p.getGame().print("L'inventaire est plein");
		}
	}

	//methodes pour les amelioration dans l armurerie
	public void changeLife() {

		double nxtLife = this.p.getMaxLife() + addToStat[this.type];

		if(nxtLife <= limitToLevel[type][lvl]) { //on verifie qu il n a pas atteint la limite de vie maximale propre a sa categorie

			this.p.setMaxLife((int)nxtLife);
			this.p.setGold(this.p.getGold()-this.price);
		}

		else {
			this.p.getGame().print("Vous avez atteint la limite de vie pour votre niveau: "+ limitToLevel[type][lvl]);
		}
	}

	public void changeDamage() {

		int nxtDamage = this.p.getDamage() + (int)addToStat[this.type];

		if(nxtDamage <= limitToLevel[type][lvl]) { //on verifie qu il n a pas atteint la limite de degat maximale propre a sa categorie

			this.p.setDamage(nxtDamage);
			this.p.setGold(this.p.getGold()-this.price);
		}

		else {
			this.p.getGame().print("Vous avez atteint la limite de dgat pour votre niveau: "+limitToLevel[type][lvl]);
		}
	}

	public void changeInventaire() {

		int nxtinventaire = this.p.getinventaireMax() + (int)addToStat[this.type];

		if(nxtinventaire <= limitToLevel[type][lvl]) { //on verifie qu il n a pas atteint la limite de taille d inventaire maximale propre a sa categorie

			this.p.setinventaireMax(nxtinventaire);
			this.p.setGold(this.p.getGold()-this.price);
		}

		else {
			this.p.getGame().print("Vous avez atteint la limite de taille pour l'inventaire: "+limitToLevel[type][lvl]);
		}
	}

	//methode pour l achat de consommables dans l armurerie

	public void addFood() {
		p.addToLoot(new Food(0,0,1500,p.getPlateau()));
	}

	public void addAlcool() {
		p.addToLoot(new Alcool(0,0,2000,2000,p.getPlateau()));
	}

	public void addWater() {
		p.addToLoot(new Water(0,0,2500,p.getPlateau()));
	}

	public void addPotion() {
		p.addToLoot(new Potion(0,0,p.getPlateau(),10));
	}

	//methode pour que le texte des boutons change lorsqu on clique dessus
	public void setTxt() {

		if(this.type == 7) {
			this.t = String.valueOf(this.p.compteurloot()[0]);
		}

		else if(this.type == 8){
			this.t = String.valueOf(this.c.getFoodList().size());
		}

		else if(this.type == 9) {
			this.t = String.valueOf(this.p.compteurloot()[1]);
		}

		else if(this.type == 10) {
			this.t = String.valueOf(this.c.getDrinkList().size());
		}

		if(t!=null) { //seuls les boutons pour le coffre ont besoin de rendre compte de ce qu il reste dans l inventaire du joueur ou dans le coffre
			this.setText(this.s+" "+this.t);
		}

		else {
			this.setText(this.s);
		}

	}
}
