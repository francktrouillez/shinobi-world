package Model;
import java.util.Random;



public class LootFactory {
	
	//methode qui permet de creer un nouvel objet (alcool, eau, alcool, gold) et de le placer a la meme position qu un autre objet deja present sur le plateau
	//elle est utilisee lors de la destruction totale de bloc ou lorsqu un ennemi est tue
	
	public GameObject getInstance(String loot,GameObject o){
		
		GameObject res = null;
		Random rand = new Random();
		
		int x = o.getPosX();
		int y = o.getPosY();
		
		Plateau p = o.getPlateau();

		int valeurLoot = (5+5*rand.nextInt(5));
		
		
		//permet de creer un objet avec une valeur (nutritive,taux d alcool,...) aleatoire
		switch(loot) {
			case "alcool": res = new Alcool(x,y,valeurLoot*100, valeurLoot*100,p);
			break;
			case "food" : res = new Food(x,y,3*valeurLoot*100,p);
			break;
			case "gold" : res = new Gold(x,y,valeurLoot*100,p);
			break;
			case "water" : res = new Water(x,y,valeurLoot*100,p);
		}
		
		return res;
	}
}