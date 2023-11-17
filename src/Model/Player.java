package Model;
import java.util.*;

import Model.Thread.PotionThread;
import Model.Thread.Time;
import View.Sound;


public abstract class Player extends GameObject implements Directable {

	//caracteristiques propres au personnage
	protected String name;
	protected String manga;

	//statistiques initiales
    private int energy = 1000;
    private int direction = EAST;
    private int alcool = 0;
    private int faim = 0;
    private int soif = 0;
    private int vessie = 0;
    private int life = 100;
    private int speed = 1;
    private double maxLife = 100;
    private int damage = 20;
    private int inventaireMax = 3;
    private double alcoolmax = 10000.0;

    protected int pureXP = 0;

    private Time time_horloge;
    private double time_naissance;

    private boolean teleportation = false;
    private boolean invincible = false;
    private boolean moving = false;

    private Game game;

    private Random rand = new Random();

    //besoins
    private boolean needToPee = false;
    private boolean needToDrink = false;
    private boolean needToEat = false;
    private boolean needToGo = false;
    private boolean onMove = false;

    //missions
    private boolean onmission = false;
    private int activeMission = -1;
    private boolean missionEnd = false;


    private ArrayList<Focusable> loot = new ArrayList<Focusable>();


    //Constantes

    public static final double MAX_ENERGY = 1000;
    public static final double MAX_FAIM = 6000;
    public static final double MAX_SOIF = 10000;
    public static final double MAX_VESSIE = 8000;


    //constructeur du joueur pour la sauvegarde
    public Player(String name,String manga, int[] info, Plateau p, String color) {
    	super(info[0], info[1], color,p);
    	this.name = name;
    	this.manga = manga;
    	setPlateau(p);
    	setDirection(info[2]);
		setEnergy(info[3]);
		setAlcool(info[4]);
		setFaim(info[5]);
		setSoif(info[6]);
		setMission(info[7]);
		setOnMission(info[8]);
		setGold(info[9]);
		setVessie(info[10]);
		setMaxLife(info[12]);
		setLife(info[11]);
		setDamage(info[13]);
		setMissionEnd(info[14]);
		setinventaireMax(info[15]);
		Mangas.addPlayer(this, manga);


    }


    public Player(String name, String manga,int x, int y, String color, Plateau p) {
        super(x, y, color,p );
        this.alcool = 0;
        this.name = name;
        this.manga = manga;
    }

    //on associe le joueur a un jeu ce qui permet d acceder aux methodes du jeu

    public void setGame(Game g) {
    	this.game = g;
    }


    //methodes implementees de GameObject

    public boolean isObstacle() {
        return false;
    }

    public int getDirection() {
    	return direction;
    }


    //methodes de changements de position et direction

    public void move(int X, int Y) {
        this.posX = this.posX + X;
        this.posY = this.posY + Y;
        if (game.isActivePlayer(this)) {
        	this.setMoving(true);
        }
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


    //methode pour faire aller le joueur dans une position aleatoire, utilisee dans le cas des bots
    public void RandomMove() {
		int value = rand.nextInt(4);
		if (!game.isActivePlayer(this)) {
			switch(value) {
			case 0 : game.movePlayer(1, 0,this);break;
			case 1 : game.movePlayer(-1, 0,this);break;
			case 2 : game.movePlayer(0, 1,this);break;
			case 3 : game.movePlayer(0, -1,this);break;



			}

		}
    }

    //methodes pour regarder en face dans la direction du joueur

    public int getFrontX() {
        int delta = 0;
        if (direction % 2 == 0){
            delta += 1 - direction;
        }
        return this.posX + delta;
    }

    public int getFrontY() {
        int delta = 0;
        if (direction % 2 != 0){
            delta += direction - 2;
        }
        return this.posY + delta;
    }

    //methode qui permet de changer la vitesse du personnage de lent vers rapide ou inversement

	public void switchSpeed() {
		if(speed == 1) {
			this.speed = 3;
		}
		else if(speed == 3) {
			this.speed = 1;
		}
	}

    //methode pour les attaques

    public void kick() {
    	synchronized(Game.keyPlateau) {
    		if (energy > 50) {
    			energy -=50;
    			new Kick(this,damage);
    		}
    	}
    }

    public void tire() { //tire est redefini pour confirme et hokage

    }

    public void wave() { //wave est redefini pour hokage
    }


    //methodes pour l evolution redefinies selon la sous classe de joueur

    public abstract Player evolve();

    public abstract boolean isTimeToEvolve();


    //methodes pour consommer un objet de l inventaire

	public void eat() {

		for(Focusable f:loot) { //on cherche le premier objet de type Food, on fait diminuer la faim des joueurs en recuperant sa valeur puis on le supprime de son inventaire
			if(f instanceof Food) {
				this.setFaim(faim-((Food)f).getValue());
				this.loot.remove(f);
				Sound.playEffect("eat");
				break;
				}
		}
	}
	public void drink() {

		for(Focusable f:loot) { //pareil que la nourriture avec un alcool
			if(f instanceof Alcool) {
				this.setAlcool(alcool+((Alcool)f).getAlcool());
				this.setVessie(getVessie() + 50);
				this.setSoif(soif-((Alcool)f).getAlcool());
				this.loot.remove(f);

				Sound.playEffect("drink");
				break;
			}
			else if(f instanceof Water) {
				this.setVessie(getVessie()+50);
				this.setSoif(getSoif() - ((Water)f).getValue());
				this.loot.remove(f);

				Sound.playEffect("drink");
				break;
			}
		}

	}
	public void consume() {
		for(Focusable f: loot) {
			if(f instanceof Potion) {
				try {
					Thread pt = new Thread(new PotionThread((Potion)f,this)); //thread qui bloque la faim, la soif et la vessie et empeche de perdre de la vie
					pt.start();
					this.loot.remove(f);

					Sound.playEffect("potion");
					break;
				}
				catch(Exception e) {

				}

			}
		}
	}

	//methode en rapport avec l inventaire

	public void addToLoot(Focusable f) { //permet d ajouter des objets a l inventaire
		if(this.loot.size()< this.getinventaireMax()) {
			this.loot.add(f);
		}
	}


	public int[] compteurloot() {
    	int n = loot.size();
    	int compteurdrink = 0;
    	int compteurfood = 0;
    	for(int i =0; i < n; i++) {
    		Focusable f = loot.get(i);
    		if(f instanceof Drink) {
    			compteurdrink++;
    		}
    		if(f instanceof Food) {
    			compteurfood++;
    		}
    	}
    	int [] res = {compteurfood, compteurdrink};
    	return res;
	}

	//methode pour perdre de la vie

    public void loseLife(int value) {
    	try {
    	if (getPlateau() == game.getActivePlateau()) {
    		Sound.playEffect("nani");
    	}
    	setLife((int)getLife()-value);
    	}
    	catch(Exception e) {
    		loseLife(value);
    	}
    }

    //methodes lors de la mort d un joueur

	public void die() { //on active le reset des statistiques et de l inventaire et puis on place le joueur au milieu de la map d acceuil
		reset();
		this.game.print(this.name.toUpperCase().charAt(0)+this.name.substring(1) +" est mort");
		this.posX = this.getPlateau().getSize()/2;
		this.posY = this.getPlateau().getSize()/2 - 3;
		this.getPlateau().removeObject(this);
		this.setPlateau(game.getUnivers().getPlateau(0, 0));
		this.getPlateau().addObject(this);
		if (game.isActivePlayer(this)) {
			game.goToAccueil(this);
		}

	}

	public void reset() { //permet de remettre a zero les statistiques, l inventaire et fait perdre de l argent au joueur
		this.life = (int)maxLife;
		this.energy = (int)this.getMaxEnergy();
		this.faim = 0;
		this.soif = 0;
		this.vessie = 0;
		loot = new ArrayList<Focusable>();
		this.setGold(getGold()-1000);
	}


    //methode qui permet de donner la position du joueur sur le nouveau plateau en fonction de la porte activee

	public int setNewPos(int x, int y, int size) {
		int res = -1;

		if (x == 1 && y == size/2) { //il s agit d une porte ouest, le personnage qui etait tout a gauche doit donc se retrouver tout a droite
			res = 2;
			this.posX = size-2;
		}
		else if (x == size -2 && y == size/2) {
			res = 3;
			this.posX = 1;
		}
		else if (y == 1 && x == size/2) {
			res = 0; //NORD
			this.posY = size-2;
		}
		else if ( y == size -2 &&  x == size/2) {
			res = 1;
			this.posY = 1;
		}
		else if(x == 2 && y == 6) { //du plateau d acceuil vers le plateau maison
			res = 2;
			this.posX = 20-2;
			this.posY = 20/2;
		}
		if (getPlateau() == game.getUnivers().getPlateau(-2, 0)) { //du plateau maison vers le plateau d acceuil
			this.posX = 2;
			this.posY = 6;
			res = 3;
		}
		return res;

	}

	//methodes pour le changement de plateau


	public void readyToTeleport(boolean res) {
		this.teleportation = res;
	}

	public boolean isReadyToTeleport() {
		return this.teleportation;
	}

	//methode qui recupere toutes les statistiques et qui sera utile lors de l evolution

	public int[] getStats(){
		int[] res = new int[17];
		res[0] = posX;
		res[1] = posY;
		res[2] = this.getDirection();
		res[3] = this.getRealEnergy();
		res[4] = this.getAlcool();
		res[5] = this.getFaim();
		res[6] = this.getSoif();
		res[7] = this.activeMission;
		res[8] = 0;
		if(this.haveMission()) {
			res[8] = 1;

		}
		res[9] = this.getGold();
		res[10] = getVessie();
		res[11] = (int)getLife();
		res[12] = (int)getMaxLife();
		res[13] = getDamage();
		res[14] = 0;
		if(this.missionEnd) {
			res[14] = 1;
		}
		res[15] = this.inventaireMax;
		res[16] = speed;
		return res;
	}






    //Accesseurs



	//jeu associe

	public Game getGame() {
		return game;
	}


	//identite du joueur

	public String getName() {
		return name;
	}

	public String getManga() {
		return manga;
	}


	//statistiques

	public void setInvincibility(boolean b) {
		this.invincible =b;
	}

	public boolean getInvincibility() {
		return this.invincible;
	}


	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}


    public int getDamage() {
    	return this.damage;
    }
    public void setDamage(int d) {
    	this.damage = d;
    }


	public void setGold(int gold) {

		Mangas.setAccount(gold,manga);
	}

	public int getGold() {
		return Mangas.getAccount(manga);
	}


	public ArrayList<Focusable> getLoot(){
		return this.loot;
	}

	public void setLoot(ArrayList<Focusable> loot) {
		this.loot = loot;
	}

	public int getinventaireMax() {
		return this.inventaireMax;
	}

	public void setinventaireMax(int i) {
		this.inventaireMax = i;
	}


	public void setMaxLife(int i) {
		this.maxLife =i;
	}

	public double getMaxLife() {
		return this.maxLife;
	}

    public double getLife() {
    	return life;
    }

    public void setLife(int value) {
    	if(this.invincible) {
    		if(value > this.life) {
            	if (value > maxLife) {
            		value = (int)maxLife;
            	}

            	this.life = value;
    		}
    	}
    	else {
        	if (value > maxLife) {
        		value = (int)maxLife;
        	}
        	else if (value < 0){
        		value = 0;
        	}
        	this.life = value;
    	}

    }

    public double getMaxEnergy() {
    	return MAX_ENERGY;
    }

    public int getRealEnergy() {
    	return energy;
    }

    public void setEnergy(int energy) {
    	if (energy > MAX_ENERGY) {
    		this.energy = (int)MAX_ENERGY;
    	}
    	else if (energy < 0) {
    		this.energy = 0;
    	}
    	else {
    		this.energy = energy;
    	}
    }


    public int getVessie() {
    	return vessie;
    }

    public void setVessie(int value) {
    	if (value > MAX_VESSIE) {
    		value = (int)MAX_VESSIE;
    	}
    	else if (value < 0){
    		value = 0;
    	}
    	this.vessie = value;
    }


    public void setFaim(int faim) {
    	if (faim > MAX_FAIM) {
    		this.faim = (int)MAX_FAIM;
    	}
    	else if (faim < 0) {
    		this.faim = 0;
    	}
    	else {
    		this.faim = faim;
    	}
    }

	public int getFaim() {
		return faim;
	}


    public void setSoif(int soif) {
    	if (soif > MAX_SOIF) {
    		this.soif = (int)MAX_SOIF;
    	}
    	else if (soif < 0) {
    		this.soif = 0;
    	}
    	else {
    		this.soif = soif;
    	}
    }

	public int getSoif() {
		return soif;
	}


    public double getAlcoolemie() {
    	return this.alcool/alcoolmax;
    }

    public double getAlcoolemieMax() {
    	return alcoolmax;
    }

    public int getAlcool() {
    	return this.alcool;
    }

	public void setAlcool(int alcool){
		if(alcool>=alcoolmax) {
			this.alcool =(int)alcoolmax;
		}
		else if (alcool < 0){
			this.alcool = 0;
		}
		else {
			this.alcool = alcool;
		}
	}

	public abstract void gainXP(int xp);
	public abstract int getXP();
	public abstract int getEvolutionXP();

	public void setPureXP(int xp) {
		this.pureXP = xp;
	}

	public int getPureXP() {
		return pureXP;
	}


	//temps

	public double getTimeNaissance() { //temps de naissance
		return this.time_naissance;
	}

	public Time getHorloge() { //temps reel du joueur
		return this.time_horloge;
	}

	public void setTime(Time time) {
		this.time_horloge = time;
		this.time_naissance = time.getCompteur();
	}


	//direction et mouvement

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean res) {
		moving = res;
	}


	//besoins

	public void setNeedToPee() {
		boolean cond = false;

		synchronized(Game.keyPlateau){
			for(GameObject go:this.getPlateau().getObjects()) { //on regarde d abords si il y a une toilette sur le plateau actif
				if (go instanceof Toilet) {
					cond = true;
					break;
				}
			}
		}
		if(this.getVessie()> MAX_VESSIE*3/5 && cond) {
			this.needToPee = true;
		}
		else if(this.getVessie()== 0) {
			this.needToPee = false;
		}
	}

	public void setNeedToDrink() {
		if(this.getSoif()> MAX_SOIF*3/4) {
			while(this.compteurloot()[1] != 0) { //il regarde d abords s il possede une ou plusieurs boissons et les boit
				this.drink();
				this.getGame().print(this.name.toUpperCase().charAt(0)+this.name.substring(1)+ " a mang de la nourriture de son inventaire");
				if (this.getSoif() < MAX_SOIF*3/4) {
					break;
				}

			}
			if (this.getSoif() > MAX_SOIF*3/4) {
				this.needToDrink = true;
			}
			else {
				this.needToDrink = false;
			}
		}
		else {
			this.needToDrink = false;
		}
	}

	public void setNeedToEat() {
		if(this.getFaim()> MAX_FAIM*3/4) {
			while(this.compteurloot()[0] != 0) { //il regarde d abord s il possede une ou plusieurs nourritures et les boit
				this.eat();
				if (this.getFaim() < MAX_FAIM*3/4) {
					break;
				}
			}
			if (this.getFaim() > MAX_FAIM*3/4) {
				this.needToEat = true;
			}
			else {
				this.needToEat = false;
			}
		}
		else {
			this.needToEat = false;
		}
	}

	public boolean getNeedToPee() {
		return this.needToPee;
	}

	public boolean getNeedToEat() {
		return this.needToEat;
	}

	public boolean getNeedToDrink() {
		return this.needToDrink;
	}

	public void setNeedToGo() { //indique au bot s il doit arreter son comportement aleatoire et se deplacer vers un endroit
		if(needToPee || needToEat || needToDrink) {
			this.needToGo = true;
		}
		else {
			this.needToGo = false;
		}
	}

	public void setOnMove(boolean b) {
		this.onMove =b;
	}

	public boolean getOnMove() {
		return this.onMove;
	}

	public boolean getNeedToGo() {
		return this.needToGo;
	}


    //mission

    public boolean haveMission() {
    	return this.onmission;
    }

    public void setOnMission(boolean b) {
    	this.onmission = b;
    }

    public void setOnMission(int i) {
    	if (i == 0) {
    		setOnMission(false);
    	}
    	else {
    		setOnMission(true);
    	}
    }

    public void setMission(int i) {
    	this.activeMission = i;
    }
    public int getMission() {
    	return this.activeMission;
    }
    public void setMissionEnd(boolean b) {
    	this.missionEnd = b;

    }
    public void setMissionEnd(int i) {
    	if (i==0) {
    		setMissionEnd(false);
    	}
    	else {
    		setMissionEnd(true);
    	}
    }

    public boolean getMissionEnd() {
    	return this.missionEnd;
    }





}
