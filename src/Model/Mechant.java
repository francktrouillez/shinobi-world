package Model;

import Model.Thread.MechantCacThread;

public class Mechant extends GameObject implements Directable {
	
	//caracteristiques du mechant
	private int difficulte;
	private int life;
	private int dammage;
	private int direction = EAST;
	private int minDammage = 5;
	private int minLife = 10;
	private String name;
	private int minXP = 100; //experience donnee au joueur de la mort d un mechant
	private int xpValue;

	private Game game;
	
	
	public Mechant(int x, int y, int difficulte, Plateau p, Game g, String color) {
		super(x,y,color,p);
		
		this.setGame(g);
		this.difficulte = difficulte;
		this.dammage = (difficulte*minDammage);
		this.life = (difficulte*minLife);
		this.xpValue = minXP*difficulte;
		Thread t = new Thread(new MechantCacThread(this)); //thread de deplacement du mechant vers l actif player
		t.start();
		
	}
	
	//permet de pouvoir utiliser les methodes de game dans mechant
    public void setGame(Game g) {
    	this.game = g;
    }
    
    //methodes de deplacement et rotation
    
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


	
	//methodes pour obtenir la position de la case en face du joueur dans sa direction
	
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
    
	
    //methode pour inflier des degats a un joueur
	public void hit(Player p) {
		int[] coord = {p.getPosX(),p.getPosY()};
		new Hit(coord,getDirection(), getPlateau()); //creation de l objet graphique associe au coup du mechant
		p.loseLife(this.dammage);
	}
	
	//methode pour perdre de la vie
    public void loseLife(int dammage) {
    	int nxtLife = this.life - dammage;
    	setLife(nxtLife);

    }
    
    //on supprime le mechant du plateau et on fait apparaitre de la nourriture de maniere aleatoire a la position du mechant
    public void destroy() {
    	getPlateau().removeObject(this);
    	getPlateau().spawnLoot(this);
    }
    
    
    //Accesseurs
    
    
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
	
	public void setName(String name) {
		this.name= name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDifficulty() {
		return difficulte;
	}
	
	public int getXPValue() {
		return xpValue;
	}
	
	public int getDirection() {
		return this.direction;
	}
	public boolean isObstacle() {
		return true;
	}
	
}
