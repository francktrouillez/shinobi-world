package Model;

import java.util.ArrayList;

public class Hokage extends Player{
	
	private double cooldownFireball = 0;
	
	//constructeur pour la sauvegarde
	public Hokage(String name, String manga, int[] info, Plateau p) {
		super(name,manga,info, p,"hokage");
		
	}
	
	
	//constructeur pour l evolution
	public Hokage(String name, String manga,int[] stats, ArrayList<Focusable> loot,Plateau p) {
		super(name, manga, stats[0],stats[1],"hokage",p);
		this.setDirection(stats[2]);
		this.setEnergy(stats[3]);
		this.setAlcool(stats[4]);
		this.setFaim(stats[5]);
		this.setSoif(stats[6]);
		this.setMission(stats[7]);
		this.setOnMission(stats[8] ==1);
		this.setGold(stats[9]);
		this.setVessie(stats[10]);
		this.setLife(stats[11]);
		this.setMaxLife(stats[12]);
		this.setDamage(stats[13]);
		this.setMissionEnd(stats[14] ==1);
		this.setinventaireMax(stats[15]);
		this.setSpeed(stats[16]);
		this.setLoot(loot);
	}
	
	//constructeur pour un nouveau jeu
	public Hokage(String name, String manga,int x, int y, Plateau p) {
		super(name, manga,x,y,"hokage",p);
		
	}
	
	//evolution 
	
	public Player evolve() {
		return this;
	}
	
	public boolean isTimeToEvolve(){ //pas de stade superieur a l hokage
		return false;
	}
	

	//attaques de l hokage
	
	//creation d une fireball si l energie est suffisante
    public void tire() {
    	
    	if ((this.getGame().getTime() - cooldownFireball) > Fireball.getCooldown() && this.getRealEnergy() > 100) { //on regarde si le cooldown est depasse et l energie suffisante
    		this.setEnergy(this.getRealEnergy()-100);
    		synchronized(Game.keyPlateau) {
	    		try {
	    			new Fireball(this, 8, this.getDamage(), this.getGame().getTimeObject()); 
	    			cooldownFireball = this.getGame().getTime();
	    			
	    		}
	   			catch(Exception e) {	
	   			}		
    		}
    	}	
	}
    
    //creation d une wave si l energie est suffisante 
    public void wave() {
    	synchronized(Game.keyPlateau) {
    		
    		if (this.getRealEnergy() > 500) { 
    			
    			this.setEnergy((int)this.getRealEnergy()-500);
    			new Wave(this, this.getDamage()*4);
    		}
    	}
    }
    
    //experience
    
	public int getEvolutionXP() {
		return getXP();
	}
	
	public void gainXP(int xp) {
		this.pureXP += xp;
	}
	public int getPureXP() { //experience au combat ou en mission
		return pureXP;
	}
	public int getXP() { //experience totale
		return (int)this.getHorloge().getCompteur() + pureXP;
	}
	

	
}
