package Model;

import View.Armurerie;

public class ArmurerieDoor extends GameObject implements Focusable {
	
	//prix
	private int priceOfFood = 200;
	private int priceOfDrink = 200;
	
	
	public ArmurerieDoor (int x,int y, Plateau p) {
		super(x,y,"null",p);
		
	}


	public void activate(Player p) {
		openArmurerie(p);
	}
	
	//methode qui permet d acceder a l armurerie
	public void openArmurerie(Player p) {
		if(p.getGame().isActivePlayer(p)) { //si c est le joueur actif qui l active, alors on ouvre la fenetre et il a le choix d acheter ce qu il veut
			new Armurerie(p);
		}
		else { //si c est un bot qui active la porte, alors un des ses besoins (faim ou soif) l a fait venir jusqu a l armurerie
			if(p.getNeedToEat()) { //s il a faim, alors il achete une nouvelle nourriture
				p.addToLoot(new Food(0,0,2500));
				p.setGold(p.getGold()-priceOfFood);
			}
			if(p.getNeedToDrink()) { //s il a soif, alors il achete une nouvelle eau
				p.addToLoot(new Water(0,0,2500));
				p.setGold(p.getGold()-priceOfDrink);
			}
		}
		
	}
	
	public boolean isObstacle() {
		return false;
	}
	
}
