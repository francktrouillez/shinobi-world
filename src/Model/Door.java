package Model;


public class Door extends GameObject implements Focusable {
	
	//classe qui permet de definir les differentes conditions pour l entree du joueur ainsi que les actions apres qu il soit sorti des mission
	private Unlock u = new Unlock();
	
	//differents types de portes
	private String[] unlockList = {"unlockapprenti", "unlockconfirme", "unlockhokage", "unlockmission","backToAcceuil","maison"};
	
	//permet de connaitre le type de la porte
	private int elementlist;
	
	
	
	//constructeur de porte de changement de plateau
	public Door(int x, int y, Plateau p, int unlock, String color) { 
		super(x,y,color,p);
		this.elementlist = unlock;
	}
	
	//constructeur pour faire apparaitre une porte seulement apres la reussite d une mission
	public Door(int x, int y, Plateau p) {
		this(x,y,p,0,"door");
	}
	
	
	//methode qui permet de verifier que la condition propre au type de porte est respectee
	public void activate(Player p) { 
		
		if(u.getCondition(unlockList[elementlist], p)) {
			p.readyToTeleport(true);
		}
	}
	
	
	//Accesseurs
	
	
	public int getUnlock() {
		return this.elementlist;
	}

	public boolean isObstacle() {
		return true;
	}



}
