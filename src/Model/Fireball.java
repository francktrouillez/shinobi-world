package Model;

import Model.Thread.FireballThread;
import Model.Thread.Time;
import View.Sound;

public class Fireball extends GameObject implements Directable{
	
	//caracteristiques
	private int speed;
	private int damage;
	private int orientation;
	
	//lanceurs
	private Player player;
	
	//temps de la fireball
	private double debut;
	private Time time;
	
	//temps entre deux lancements consecutifs
	private static double COOLDOWN = 0.4;
	
	
	
	//constructeur pour un lanceur de type joueur
	public Fireball(Player p, int speed, int damage, Time t) {
		super(p.getPosX(),p.getPosY(), "fireball", p.getPlateau());
		//p.projectiles.add(this);
		this.debut = t.getCompteur();
		this.time = t;
		synchronized(Game.keyPlateau) {
			p.getPlateau().addObject(this);
		}
		this.orientation = p.getDirection();
		avance();
		this.speed = speed;
		this.player = p;
		this.damage = damage;
		Sound.playEffect("fireball");
		
		Thread ft = new Thread(new FireballThread(this)); //thread necessaire au deplacement de la fireball
		ft.start();
	}
	
	
	
	//constructeur pour un lanceur de type canon
	public Fireball(Canon canon, int speed, int damage, Time t) {
		super(canon.getPosX(), canon.getPosY(),"fireball",canon.getPlateau());
		this.debut = t.getCompteur();
		this.time =t;
		canon.getPlateau().getObjects().add(this);
		this.orientation = canon.getDirection();
		avance();
		this.speed = speed;
		this.damage = damage;
		
		Thread ft = new Thread(new FireballThread(this));
		ft.start();
		
	}

	
	//methode pour faire avancer la fireball dans sa direction
	public void avance() {
		if (orientation == NORTH) {
			this.posY--;
		}
		else if(orientation == SOUTH) {
			this.posY++;
		}
		else if(orientation == EAST) {
			this.posX++;
		}
		else if(orientation == WEST) {
			this.posX--;
		}
	}



	//methode qui permet de savoir si la fireball a touche un objet
	public boolean collide(GameObject go) {
		return (go.getPosX() == this.getPosX() && go.getPosY() == this.getPosY() && go != this);
	}




	//methode d interaction entre un objet et la fireball lorsqu elle touche l objet en question
	public void explode(GameObject go) {
		if (go instanceof Player) {
			((Player) go).loseLife(this.damage);
		}
		else if (go instanceof Mechant) {
			((Mechant)go).loseLife(this.damage);
		}else if (go instanceof Boss) {
			((Boss) go).loseLife(damage);
		}
		else if (go instanceof Fireball) {
			((Fireball) go).disappear();
		}
		else if (go instanceof BlockBreakable) {
			((BlockBreakable)go).activate();
		}
		this.getPlateau().getObjects().remove(this);
		new Explosion(this);
		
	}
	

	//methode pour detruire la fireball
	public void disappear() {
		this.getPlateau().getObjects().remove(this);
	}
	
	
	    
	//Accesseurs  
	    
		
	public boolean isObstacle() {
		return false;
	}    
	    
	public int getSpeed() {
		return speed;
	}
	
	public static double getCooldown() {
		return COOLDOWN;
	}
	
	
	public int getDirection() {
		return orientation;
	}
	
	public double getLifeTime() {
		return time.getCompteur() - debut;
	}
	
	public Player getPlayer() {
		return this.player;
	}
}



