package Model;
import java.util.*;

import Model.Thread.BossThread;
import View.Sound;

public class Boss extends GameObject implements Directable, Deletable {
	
	
	//donne initiale
	private int direction = EAST;
	private int minDammage = 25;
	private int minLife = 500;
	private int minXP = 3000;

	
	int dammage;
	int life;
	int xpValue;
	
	private Game game;
	private ArrayList<DeletableObserver> observers = new ArrayList<DeletableObserver>();
	
	//difficulte actuelle du boss, augemente a chaque fois qu il est tue
	private static double difficulty = 1;
	
	public Boss(int x, int y, Plateau p, Game g, String color) {
		super(x,y,color,p);
		this.setGame(g);
		
		//fonction de la difficulte
		this.dammage = (int)(difficulty*minDammage);
		this.life = (int)(difficulty*minLife);
		this.xpValue = (int)(minXP*difficulty);
		
		//lancement du thread
		Thread t = new Thread(new BossThread(this));
		t.start();
		
	}
	
    public void setGame(Game g) {
    	this.game = g;
    }
    
    //methode pour le deplacement 
    
    public void move(int x,int y) {
    	
        this.posX = this.posX + x;
        this.posY = this.posY + y;
    }
    public void rotate(int x, int y) {
        if(x == 0 && y == -1) 
            direction = NORTH;
        else if(x == 0 && y == 1)
            direction = SOUTH;
        else if(x == 1 && y == 0)
            direction = EAST;
        else if(x == -1 && y == 0)
            direction = WEST;

    }
    
    
	public void attachDeletable(DeletableObserver po) {
		observers.add(po);
	}
	public void notifyDeletableObserver() {
		synchronized(Game.keyPlateau) {
			for (DeletableObserver o : observers) {
				
				o.delete(this, null, null);
        	}
		}
	}
	
	
	public int getDirection() {
		return this.direction;
	}
	
	

	//methodes qui permettent de regarder le bloc qui se trouve face au joueur
	
    public int getFrontX() { 
        int delta = 0;
        if (direction % 2 == 0){ 
            delta += 1 - direction;
        }
        return this.posX + delta; 
    }

    public int getFrontY() {
        int delta = 0;
        if (direction % 2 != 0) {
            delta += direction - 2; 
        }
        return this.posY + delta; 
    }
    
	
    //methode lorsque le boss attaque
	public void hit(Player p) {
		int[] coord = {p.getPosX(),p.getPosY()};
		new Hit(coord,getDirection(), getPlateau()); //on cree un nouvel objet hit qui fait des degats dans une zone de trois blocs perpendiculaire a la direction du joueur
		p.loseLife(this.dammage);
	}
	
	//methode qui permet d invoquer un nombre nb de mechant de difficulte aleatoire
	public void summon(int nb) {
		int x;
		int y;
		int difficulte;
		int apperance;
		String color = "";
		Random rand = new Random();
		for (int i = 0; i<nb;i++) {
			int[] coord = getRandomXY(getPlateau()); //coordonnees aleatoires dans un rayon autour du boss
			x = coord[0];
			y = coord[1];
			difficulte = rand.nextInt(5)+1;
			if (x != -1 && y != -1) {
				apperance = rand.nextInt(3);
				switch(apperance) {
				case 0 : color = "wolf";break;
				case 1 : color = "skeletonWarrior"; break;
				case 2 : color = "goblin";break;
				}
				Mechant m = new Mechant(x,y,difficulte,getPlateau(),game,color);
				getPlateau().addObject(m);
			}
		}
		Sound.playEffect("summon");
	}
	
	
	//methode qui permet de donner une position aleatoire a un mechant invoque autour du boss
	private int[] getRandomXY(Plateau p) {
    	Random rand = new Random();
    	int iteration = 0;

    	int x = rand.nextInt(7)-3 + this.getPosX();
    	int y = rand.nextInt(7)-3 + this.getPosY();
    	
    	while (!p.isFreePos(x, y) && iteration < 20) { //tant que la position n est pas libre, on test jusque 20 fois une nouvelle position
    		x = rand.nextInt(7)-3 + this.getPosX();
        	y = rand.nextInt(7)-3 + this.getPosY();
        	iteration ++;
    	}
    	if (iteration >= 20) {
    		x = -1;
    		y = -1;
    	}
    	int[] res = {x,y};
    	return res;
    }
    
	
	//methode pour perdre de la vie
    public void loseLife(int dammage) {
    	int nxtLife = this.life - dammage;
    	setLife(nxtLife);

    }
    
    //methode pour supprimer l objet du jeu
	public void destroy() {
		System.out.println("Destroyed");
		
		this.getPlateau().removeObject(this);
	}
	
	//augmente la difficulte du boss
	public static void increaseDifficulty() {
		difficulty += 0.2;
	}
	
    
    // Accesseurs
	
	
	public boolean isObstacle() {
		return true;
	}
	
	public static double getDifficulty() {
		return difficulty;
	}
	
	public int getXPValue() {
		return xpValue;
	}
    
    public boolean isAlive() {
    	return life>0;
    }
    
	public int getLife() {
		return this.life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public int getDammage() {
		return this.dammage;
	}
	public void setDammage(int dammage) {
		this.dammage = dammage;
	}
	
	public Game getGame() {
		return game;
	}
		
	public void setDirection(int dir) {
		direction = dir;
	}
	
	
}
