package Model;


import View.Sound;
import View.Window;


import java.util.ArrayList;
import java.util.Random;

import Model.Thread.*;

public class Game implements DeletableObserver{

	//Statique

	public static Object keyPlateau = new Object(); //Cle permettant de ne pas parcourir de facon concurrente un meme plateau
	public final static int FPS = 60;

	//ATTRIBUTS

    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Canon> canons = new ArrayList<Canon>();
    private Univers univers;
    private Player active_player = null;
    private Window window;
    private int size;
    private Time time;
    private Plateau activePlateau;
    private boolean created = false;

    //CONSTRUCTEUR

    public Game() {
    	Sound.on();
    	Sound.playBackground("background");
    }

    /*
     * Complements au constructeur : Utilise lors de la creation d'un nouveau jeu ou lors de la
     * charge d'une sauvegarde
     */

    public void newGame() {
    	created = true;
    	univers = new Univers();
    	univers.initializeUnivers();
        activePlateau = univers.getPlateau(0, 0);
        size = activePlateau.getSize();

        initializePlayers();

        createCanons();

        active_player = players.get(0);
        activePlateau = active_player.getPlateau();
        initialiseGame(window);
    }

    public void load(Univers u, Double time, String player) {
    	created = true;
    	this.univers = u;
    	setActivePlayer(player);
    	activePlateau = active_player.getPlateau();
    	size = activePlateau.getSize();
    	try {
    		Thread.sleep(500);
    	} catch(Exception e) {}
    	initialiseGame(window, time);
    }

    public void initialiseGame(Window window, double time) {
    	this.window = window;
    	for(Player p:players) {
    	window.setPlayer(p);
    	}
    	window.setActivePlayer(active_player);
        initializeThread(time);
    }

    public void initialiseGame(Window window) {
    	this.window = window;
    	for(Player p:players) {
    	window.setPlayer(p);
    	}
    	window.setActivePlayer(active_player);
        initializeThread();
    }

    //Creation des canons de la mission 1

    public void createCanons(){
        Plateau mission1 = univers.getPlateau(1, 0);
        for(int i =0; i< 8;i++) {
            Canon c = new Canon(1+i,2,3,mission1,this);
            mission1.addObject(c);
        }
        for(int i =0; i< 8;i++) {
            Canon c = new Canon(16+i,2,3,mission1,this);
            mission1.addObject(c);
        }

        for(int i =0; i< 7;i++) {
            Canon c = new Canon(9+i,7,3,mission1,this);
            mission1.addObject(c);
        }
    }

    /*
     *  Methodes s'activant à chaque changement de plateau
     *  Permet de reinitialiser une map lorsqu'il n'y a aucun joueur dessus.
     *  Cela permet de toujours avoir des mobs à tuer pour xp et d'avoir
     *  des blocs à casser pour looter
     */

    public void removeItemsOnPlateau(Plateau p) {
    	int zone = p.getZone();
    	boolean condition = isNoPlayer(p);
    	if ((zone >= 1 && zone < 7) && condition ) {
    		boolean cond = true;
        	while (cond) {
        		synchronized(Game.keyPlateau) {
        			int index = -1;
            		for (int i = 0; i< p.getObjects().size();i++) {
            			if (p.getObjects().get(i) instanceof BlockBreakable) {
            				index = i;
            				break;
            			}
            			else if (p.getObjects().get(i) instanceof Food) {
            				index = i;
            				break;
            			}
            			else if (p.getObjects().get(i) instanceof Gold) {
            				index = i;
            				break;
            			}
            			else if (p.getObjects().get(i) instanceof Drink) {
            				index = i;
            				break;
            			}
            		}
            		if (index != -1) {
            			try {
            				synchronized(Game.keyPlateau) {
            					p.removeObject(p.getObjects().get(index));
            				}
            			}
            			catch(Exception e) {}
            		}
            		else {
            			cond = false;
            		}

        		}
        	}
    	}
    }

    public void createBlocksOnPlateau(Plateau p) {
    	int zone = p.getZone();
    	boolean cond = isNoPlayer(p);
		if ((zone >= 1 && zone < 7) && cond && !(p.getPos()[0] == 1 && p.getPos()[1] == 0)) { //pas de blocs sur la mission 1 avec des canons
			int coord[];
			synchronized(Game.keyPlateau) {
			Random rand = new Random();
			for (int i = 0; i < p.getNumberOfBreakablesBlocks(); i++) {
				coord = getRandomXY(p);
				BlockBreakable m = new BlockBreakable(coord[0],coord[1], rand.nextInt(5)+1, p);
				m.attachDeletable(p);
				p.addObject(m);

				}
			}
		}
    }


    public void createMobsOnPlateau(Plateau p) {
    	int zone = p.getZone();
    	boolean cond = isNoPlayer(p);
		if ((zone == 1 || zone == 2 || zone == 3) && cond ) {
			int difficult = -1;
			String color = null;
			switch(zone) {
			case 1 : difficult = 1;color = "rattata"; break;
			case 2 : difficult = 3;color = "goblin"; break;
			case 3 : difficult = 6;color = "skeletonWarrior"; break;
			}
			if (difficult != -1) {
				int coord[];
				synchronized(Game.keyPlateau) {
					for (int i = 0; i < 5; i++) {
						coord = getRandomXY(p);
						Mechant m = new Mechant(coord[0], coord[1], difficult, p, this, color);
						p.addObject(m);
					}
				}
			}
		}
    }


    //Initialisation des joueurs

    private void initializePlayers() {
    	Plateau p = univers.getPlateau(0, 0);
    	Player player;
    	int[] coord;

    	//Place les differents joueurs sur la map initiale

    	coord = getRandomXY(p);
    	player = new Apprenti("goku","dbz",coord[0],coord[1],p);
    	p.addObject(player);
    	players.add(player);
    	player.setGame(this);
    	Mangas.addPlayer(player,"dbz");

    	coord = getRandomXY(p);
    	player = new Apprenti("vegeta","dbz",coord[0],coord[1],p);
    	p.addObject(player);
    	players.add(player);
    	player.setGame(this);
    	Mangas.addPlayer(player,"dbz");

    	coord = getRandomXY(p);
    	player = new Apprenti("naruto","naruto",coord[0],coord[1],p);
    	p.addObject(player);
    	players.add(player);
    	player.setGame(this);
    	Mangas.addPlayer(player,"naruto");

    	coord = getRandomXY(p);
    	player = new Apprenti("sasuke","naruto",coord[0],coord[1],p);
    	p.addObject(player);
    	players.add(player);
    	player.setGame(this);
    	Mangas.addPlayer(player,"naruto");

    	coord = getRandomXY(p);
    	player = new Apprenti("dracaufeu","pokemon",coord[0],coord[1],p);
    	p.addObject(player);
    	players.add(player);
    	player.setGame(this);
    	Mangas.addPlayer(player,"pokemon");

    	coord = getRandomXY(p);
    	player = new Apprenti("pikachu","pokemon",coord[0],coord[1],p);
    	p.addObject(player);
    	players.add(player);
    	player.setGame(this);
    	Mangas.addPlayer(player,"pokemon");

	}



	//Print des informations sur la fenetre

    public void print(String s) {
    	this.window.print(s);

    }


    //Methodes associees au player

    public void addPlayer(Player p) {
		players.add(p);
		p.setGame(this);
	}
	public void gainXPPlayer() {//cheatcode
		active_player.gainXP(200);
	}
	public void gainGoldPlayer() {//cheatcode
		active_player.setGold(active_player.getGold()+500);
	}

    public boolean isNoPlayer(Plateau p) {
    	boolean res = true;
    	synchronized(Game.keyPlateau) {
    		for (GameObject go : p.getObjects()) {
        		if (go instanceof Player) {
        			res = false;
        			break;
        		}
        	}
    	}
    	return res;
    }

    public boolean isActivePlayer(Player p) {
		return (p == active_player);
	}
    public void movePlayer(int x, int y, Player p) {
    	//on doit attendre que le personnage ait fini de se deplacer pour
    	//pouvoir ensuite lui faire faire un nouveau deplacement s'il s'agit de l activePlayer

    	if (p != active_player || !active_player.isMoving()){
    		int realX = x;
        	int realY = y;
        	double pourcentageAlcoolemie = p.getAlcoolemie();
        	double F = 1/pourcentageAlcoolemie;
        	int min =0;
        	Random rand = new Random();
        	if(pourcentageAlcoolemie!=0.0) {
    	    	int chiffrealeatoire = rand.nextInt((int)F);
    	    	if(min == chiffrealeatoire) {
    	    		realX = -1 + rand.nextInt(3);
    	    		if (realX==0) {
    	    			realY = -1 + rand.nextInt(3);
    	    		}
    	    		else {
    	    			realY = 0;
    	    		}
    	    	}
        	}
            int nextX = p.getPosX() + realX;
            int nextY = p.getPosY() + realY;
            boolean obstacle = false;
            synchronized(keyPlateau) {
    	        for (GameObject object : p.getPlateau().getObjects()) {
    	            if (object.isAtPosition(nextX, nextY)) {
    	                obstacle = object.isObstacle();
    	            }
    	            if (obstacle == true) {
    	                break;
    	            }
    	        }
            }
            p.rotate(realX, realY);
            if (obstacle == false) {
                p.move(realX, realY);
            }
    	}

    }

    public void kickPlayer() {
    	active_player.kick();
    }

    public void tirePlayer() {
    	active_player.tire();
    }

    public void wavePlayer() {
    	active_player.wave();
    }

    public void playerEat() {
    	active_player.eat();
    }

    public void playerDrink() {
    	active_player.drink();
    }

    public void playerConsume() {
    	active_player.consume();
    }

    public void playerSwitchSpeed() {
    	active_player.switchSpeed();
    }

    public void playerPos() { //position en bloc du joueur
        System.out.println(active_player.getPosX() + ":" + active_player.getPosY());
    }

    public void changeActivePlayer() {
    	if (activePlateau.getZone() < 4 || activePlateau.getZone() == 7) {
    		int n = this.players.size();
        	int indice = this.players.indexOf(this.active_player);
        	if(indice < n-1) {
        		indice++;
        	}
        	else {
        		indice = 0;
        	}

        	this.changeSoundZone(this.activePlateau.getZone(), players.get(indice).getPlateau().getZone());
        	this.setActivePlateau(players.get(indice).getPlateau());
        	this.setActivePlayer(players.get(indice));
    	}
    	else {
    		this.print("Vous tes en mission, terminez-l avant de changer de joueur !");
    	}

    }
    public void action(Player p) {
    	synchronized(keyPlateau) { //execute l'action avant de passer à un autre thread
	        GameObject aimedObject = null; //pas d'objet activable de base
			for(GameObject object : p.getPlateau().getObjects()){ //pour chaque objet de la liste d'objet du jeu
				if(object.isAtPosition(p.getFrontX(),p.getFrontY())){ //si un objet se trouve dans le bloc à cote du perso et dans sa direction
				    if(object instanceof Activable || object instanceof Focusable){ //si l'objet est activable
				        aimedObject = object; //on caste cet objet vise par le perso en activable
				    }
				}
			}
			if(aimedObject != null){//s'il y a bien un objet vise
				//Methode activate differente si c est un objet lootable ou non
				if (aimedObject instanceof Focusable) {
					if(aimedObject instanceof Food || aimedObject instanceof Drink) {
						if(p.getLoot().size() < p.getinventaireMax()) {
							((Focusable)aimedObject).activate(p);
						}
						else {
							print("L'inventaire est rempli, achetez une amlioration  l'armurerie pour porter plus d'objets");

						}
					}
					else {
						((Focusable)aimedObject).activate(p);
					}
				}
				else {
					((Activable)aimedObject).activate();
				}
				if (p.isReadyToTeleport()) { //si l'objet est une porte, le joueur doit changer de map
					int mission = 0;
					boolean backToAccueil = false;
					if(aimedObject instanceof Door) {
						int typeDoor = ((Door)aimedObject).getUnlock();
						if(typeDoor ==3) {
							mission = p.getMission();
						}
						if(typeDoor == 4) {
							backToAccueil = true;
						}
						p.getPlateau().getObjects().remove(p); //on l'enleve de la liste d'objet du
						//plateau ou il se trouve si le joueur actif change de map
						if (isActivePlayer(p)) {
							if(backToAccueil) {
								goToAccueil(p);
							}
							else {
								int newDirOfMap = active_player.setNewPos(active_player.getPosX(), active_player.getPosY(), size); //on place le joueur actif à la nouvelle pos
								setActivePlateau(newDirOfMap, mission, ((Door)aimedObject).getColor()); //on change de plateau
								 //on change le plateau du joueur
								this.createMobsOnPlateau(activePlateau);
								this.removeItemsOnPlateau(activePlateau);
								this.createBlocksOnPlateau(activePlateau);
								p.setPlateau(activePlateau);

							}
						}
						else { //pareil pour les joueurs inactifs
							int newDirOfMap = p.setNewPos(p.getPosX(), p.getPosY(), size);
							Plateau newPlateau = getNewPlateau(p.getPlateau(),newDirOfMap,((Door)aimedObject).getColor());
							p.setPlateau(newPlateau);
						}
					}
					p.getPlateau().addObject(p); //on ajoute le joueur au objet de son plateau
					p.readyToTeleport(false);
				}
			}
    	}
    }


    //Changement de plateau

    public Plateau getNewPlateau(Plateau p,int dir, String name) {
    		int x = p.getPos()[0];
        	int y = p.getPos()[1];
        	if (dir == 0) {//porte nord
        		p = univers.getPlateau(x, y+1);
        	}
        	else if (dir == 1) {
        		p = univers.getPlateau(x, y-1);
        	}
        	else if (dir == 2) {//porte ouest
        		p = univers.getPlateau(x-1, y); //on cherche le plateau qui se trouve à gauche du plateau actuel
        	}
        	else if (dir == 3) {
        		p = univers.getPlateau(x+1, y);
        	}
    	return p;
    }


    public void setActivePlateau(int dir, int mission, String color) {
    	if (color.equals("home")) {
    		activePlateau = univers.getPlateau(-2,0);
    	}
    	else if (activePlateau == univers.getPlateau(-2, 0)) {
    		activePlateau = univers.getPlateau(0, 0);
    	}
    	else {
    	int x = activePlateau.getPos()[0]; //position du plateau avant teleportation
    	int y = activePlateau.getPos()[1];
    	int oldZone = activePlateau.getZone();
    	if (dir == 0) {//porte nord
    		activePlateau = univers.getPlateau(x, y+1);
    	}
    	else if (dir == 1) {
    		activePlateau = univers.getPlateau(x, y-1);
    	}
    	else if (dir == 2) {//porte ouest
    		activePlateau = univers.getPlateau(x-1-mission, y);//on va chercher le plateau qui se trouve à la case de gauche
    	}
    	else if (dir == 3) {//porte est
    		activePlateau = univers.getPlateau(x+1+mission, y);
    		if (mission == 0) {
    			createMission1();
    		}
    		else if(mission == 1) {
    			createMission2();
    		}
    		else if(mission == 2) {
    			createMission3();
    		}
    	}
    	int newZone = activePlateau.getZone();
    	changeSoundZone(oldZone,newZone);
    	}
    }
    public void goToAccueil(Player p) {
    	this.setActivePlateau(univers.getPlateau(0, 0));
    	p.posX = activePlateau.getSize()/2;
    	p.posY = activePlateau.getSize()/2 -3;
    	p.setDirection(Directable.SOUTH);
    	p.setPlateau(univers.getPlateau(0, 0));
    	Sound.playBackground("background");
    }
    public void setActivePlateau(Plateau p) {
    	this.activePlateau =p;
    }
    private void changeSoundZone(int oldZone, int newZone) {
    	if(oldZone != newZone) {
    		if(newZone == 0) {
    			Sound.playBackground("background");
    		}
    		else if(newZone == 1) {
    			Sound.playBackground("foretvierge");
    		}
    		else if(newZone == 2) {
    			Sound.playBackground("valleedesolee");
    		}
    		else if(newZone == 3) {
    			Sound.playBackground("terressombres");
    		}
    		else if(newZone == 4) {
    			Sound.playBackground("battle1");
    		}
    		else if(newZone == 5) {
    			Sound.playBackground("battle2");
    		}
    		else if(newZone == 6) {
    			Sound.playBackground("battle3");
    		}

    	}
    }

    //Creation des elements des missions

    public void createMission1() {
    	univers.getPlateau(1, 1).endMission();
    }

    public void createMission2() {
    	Plateau mission2 = univers.getPlateau(2, 0);
    	for (int i = 0; i < 3; i++) {
        	Mechant m1 = new Mechant(1+i, 1, 8, mission2,this,"wolf");
        	Mechant m2 = new Mechant(mission2.getSize()-i-2, 1, 8, mission2,this,"wolf");
        	Mechant m3 = new Mechant(1+i, mission2.getSize()-2, 8, mission2,this,"wolf");
        	Mechant m4 = new Mechant(mission2.getSize()-i-2, mission2.getSize()-2, 8, mission2,this,"wolf");
        	mission2.addObject(m1);
        	mission2.addObject(m2);
        	mission2.addObject(m3);
        	mission2.addObject(m4);
        }
    	univers.getPlateau(2, 1).endMission();
    }

    public void createMission3() {
    	Plateau mission3 = univers.getPlateau(3, 0);
    	int[] coord = getRandomXY(mission3);
    	Boss b = new Boss(coord[0],coord[1], mission3,this,"orochimaru");
    	mission3.addObject(b);
    	univers.getPlateau(3, 1).endMission();
    }

    //Initialisation des Threads pour les differentes barres et pour le temps

    private void initializeThread() {
    	Thread time = new Thread(new Time(this));

    	for (Player p:players) {
    		p.setTime(this.time); //temps depuis leur naissance
    	}
    	time.start();

    	for(Player p:players) {
        	giveThread(p);
        }
    	for(Canon c:canons) {
    		Thread ct = new Thread(new CanonThread(this,c));
    		ct.start();
    	}
    }

    private void initializeThread(Double compteur) {
    	Time object = new Time(this);
    	object.setCompteur(compteur);
    	Thread time = new Thread(object);

    	for (Player p:players) {
    		p.setTime(this.time); //temps depuis leur naissance
    	}
    	time.start();

    	for(Player p:players) {
        	giveThread(p);
        }
    	for(Canon c:canons) {
    		Thread ct = new Thread(new CanonThread(this,c));
    		ct.start();
    	}
    }



    private void giveThread(Player p) {//Creee des threads pour le joueur donne
    	Thread t = new Thread(new PlayerThread(p));
    	Thread ia = new Thread(new IAThread(p,this));
    	t.start();
    	ia.start();
    }


    //Methode car observeur

    @Override
    synchronized public void delete(Deletable ps, ArrayList<GameObject> loot, Player p) { // besoin d'une cle pour entre dans le bloc et executer
    	//les instructions qu'il contient
        activePlateau.getObjects().remove((GameObject)ps);//enleve de la liste d'objet le deletable ps
    }


    //Changement d'activePlayer

	public void setActivePlayer(String name) {
		for (Player p:players) {
			if (p.getName().equals(name)) {
				setActivePlayer(p);
				break;
			}
		}
	}

	public void setActivePlayer(Player p) {
		//p.setOnMove(false);
		Thread ia = new Thread(new IAThread(active_player,this)); // l ancien active player devient un bot
		ia.start();
		this.active_player = p;
		active_player.setOnMove(false);
		window.setActivePlayer(p);
	}


	//Methode pour update la carte et verifier si un joueur doit evolve

	public void updateAll() {
		for (int i = 0; i < players.size();i++) {
			synchronized(keyPlateau) {
				if (players.get(i).isTimeToEvolve()) {
					Player newPlayer;
					if (players.get(i) instanceof Apprenti){
						newPlayer = (Confirme)players.get(i).evolve(); //on cree un confirme apd apprenti
					}
					else {
						newPlayer = (Hokage)players.get(i).evolve();
					}
					if (isActivePlayer(players.get(i))) {
						this.setActivePlayer(newPlayer);
					}
					//on change dans la liste l'ancien joueur par le nouveau
					players.get(i).getPlateau().getObjects().set(players.get(i).getPlateau().getObjects().indexOf(players.get(i)), newPlayer);
					//on change dans la liste de joueur de la window
					window.getPlayers().set(window.getPlayers().indexOf(players.get(i)), newPlayer);

					newPlayer.setGame(players.get(i).getGame());
					players.set(players.indexOf(players.get(i)), newPlayer);
					giveThread(newPlayer); //lance tous les thread pour le nouveau joueur
					}
				if (players.get(i).getLife() <= 0) {
					players.get(i).die();
				}
			}

		}
		synchronized(keyPlateau) {
			for (GameObject go : activePlateau.getObjects()) {
				if (go instanceof Fireball) {
					if (((Fireball) go).getLifeTime() > 5) {
						((Fireball) go).disappear();
					}
				}
			}
			if (getActivePlayer().isMoving()) { //permet de faire avancer le personnage de facon smooth
				int mult = active_player.getSpeed();//augmente la vitesse de transition si on sprint
				for (int i = 0; i < getWindow().getSizeOfBlock(); i+=3*mult) {
					window.callPaint(i);
	    			window.update();
	    			try {
						Thread.sleep(30);
						time.setCompteur(time.getCompteur()+0.03);
					}catch(Exception e) {System.out.println("Erreur redraw moving");}
	    		}
	    		getActivePlayer().setMoving(false);
			}
			else {
				window.callPaint(0);
			window.update();
			}
		}
	}




	//Accesseurs

	public double getTime() {
    	return time.getCompteur();
    }

	public void setTime(Time time) {
		this.time = time;
	}

	public Univers getUnivers() {
		return this.univers;
	}

	public Plateau getActivePlateau() {
		return this.activePlateau;
	}

	public Player getActivePlayer() {
		return active_player;
	}

	public ArrayList<Player> getPlayers(){
		return players;
	}

	public Time getTimeObject() {
		return this.time;
	}

	public Window getWindow() {
		return window;
	}

	public void setWindow(Window w) {
		this.window = w;
	}


	public boolean isCreated() {
		return created;
	}


	public ArrayList<Canon> getCanons(){
		return canons;
	}

	//fonction interne qui permet de creer une pos random libre sur le plateau
	private int[] getRandomXY(Plateau p) {
    	Random rand = new Random();
    	int x = rand.nextInt(size-4)+2;
    	int y = rand.nextInt(size-4)+2;
    	while (!p.isFreePos(x, y)) {
    		x = rand.nextInt(size-4)+2;
        	y = rand.nextInt(size-4)+2;
    	}
    	int[] res = {x,y};
    	return res;
    }

}
