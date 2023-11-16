package Model;

import java.util.ArrayList;

import View.Sound;

public class Confirme extends Player{

	private double cooldownFireball = 0;
	private int EVOLUTION_XP = 10000;
	
	//constructeur pour la sauvegarde
	public Confirme(String name, String manga, int[] info, Plateau p) {
		super(name,manga,info, p,"confirme");
		
	}
	
	//constructeur lors d une evolution
	public Confirme(String name, String manga,int[] stats, ArrayList<Focusable> loot,Plateau p) {
		this(name, manga,stats[0],stats[1],p);
		this.setDirection(stats[2]);
		this.setEnergy(stats[3]);
		this.setAlcool(stats[4]);
		this.setFaim(stats[5]);
		this.setSoif(stats[6]);
		this.setMission(stats[7]);
		this.setOnMission(stats[8] == 1);
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
	public Confirme(String name, String manga,int x, int y, Plateau p) {
		super(name, manga,x,y,"confirme",p);

	}
	
	//attaque du confirme
	
    public void tire() {//creation d une fireball si l energie est suffisante 
    	
    	if ((this.getGame().getTime() - cooldownFireball) > Fireball.getCooldown() && this.getRealEnergy() > 100) { //on regarde si le cooldown est depasse et l energie suffisante
	    	this.setEnergy(this.getRealEnergy()-100);
    		synchronized(Game.keyPlateau) {
	    		try {
	    			new Fireball(this, 8, this.getDamage(), this.getGame().getTimeObject()); //on cree une nouvelle fireball avec son thread
	    			cooldownFireball = this.getGame().getTime();
	    			
	    		}
	   			catch(Exception e) {	
	   			}		
    		}
    	}	
	}
    
    //evolution
    
    //le joueur doit evaluer lorsque le total temps de vie (fait tres peu augmenter) et experience gagnee au combat ou en mission est superieur au l experience totale necessaire
	public boolean isTimeToEvolve() {
		return (getXP() - this.getTimeNaissance()) > EVOLUTION_XP;
	}
	
	//creation d un nouvel hokage en recuperant les statistiques et objets a l instant de l evolution
	public Hokage evolve() {
		Hokage h = new Hokage(this.name, this.manga, getStats(), getLoot(),this.getPlateau());
		h.setTime(this.getHorloge());
		h.pureXP = -(int)this.getHorloge().getCompteur();
		Sound.playEffect("evolve");
		return h;
	}
	
	
	//experience
	
	public int getEvolutionXP() {
		return EVOLUTION_XP;
	}
	
	public void gainXP(int xp) { //gain d experience au combat ou en mission
		this.pureXP += xp;
		
	}
	
	public int getXP() { //experience totale
		return (int)this.getHorloge().getCompteur() + pureXP;
	}
		
	public int getPureXP() {
		return pureXP;
	}
}
