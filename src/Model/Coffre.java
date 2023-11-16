package Model;
import java.util.*;

import View.CoffreWindow;
public class Coffre extends GameObject implements Focusable {
	
	//liste de boisson et d alcool du coffre
	private ArrayList<Food> foodList = new ArrayList<Food>();
	private ArrayList<Drink> drinkList = new ArrayList<Drink>();

	
	public Coffre(int X, int Y, Plateau p) {
		super(X, Y, "coffre", p);
	}
	

	
	public void activate(Player p) {
		openWindow(p);
		
	}

	
	//ouvre la fenetre pour le coffre
	public void openWindow(Player p) {
		new CoffreWindow(p,this);
	}
	
	//methodes pour ajouter aux coffres
	
	public void addToFoodList(Food f) {
		this.foodList.add(f);
	}
	
	
	public void addToDrinkList(Drink d) {
		this.drinkList.add(d);
	}
	
	//methodes pour recevoir une boisson ou de la nourriture provenant du coffre
	
	public Food receiveFood() {
		Food f = null;
		int n = foodList.size();
		if(n!= 0) {
			f = this.foodList.get(n-1);
			foodList.remove(f);
		}
		else {
			System.out.println("il n'y a plus de nourriture dans l'inventaire");
		}
		return f;
	}
	
	
	public Drink receiveDrink() {
		Drink d = null;
		int n = drinkList.size();
		if(n!= 0) {
			d = this.drinkList.get(n-1);
			drinkList.remove(d);
		}
		else {
			System.out.println("il n'y a plus de boissons dans l'inventaire");
		}
		return d;
	}
	
	
	//Accesseurs
	
	
	public boolean isObstacle() {
		return true;
	}
	
	public ArrayList<Food> getFoodList(){
		return this.foodList;
	}

	public ArrayList<Drink> getDrinkList(){
		return this.drinkList;
	}
	


}
