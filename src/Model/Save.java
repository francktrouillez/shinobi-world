package Model;

import java.util.ArrayList;

public class Save {
	/*
	 * Classe permettant de faire la sauvegarde de son jeu
	 * Grce  celle-ci, on peut faire autant de sauvegarde qu'on veut et les stocker dans
	 * des fichiers texte. Dans le cadre de ce jeu, elles ont t limite  3.
	 * Elle ne se base pas sur le principe de persistance vu au TP.
	 * On ne conserve pas les anciens objets mais on ne retient que leurs informations.
	 * A la charge de la sauvegarde, on recre des objets possdant les mmes attributs.
	 */
	private Game g;
	private int number;

	public Save(Game g) {
		this.g = g;
	}

	public Save(Game g, int number) {
		this.g = g;
		this.number = number;

	}

	public void writeSave() {
		/*
		 * Permet d'crire la sauvegarde. Si l'emplacement de sauvegarde est dj utilis,
		 * il l'crase.
		 *
		 * Format de la sauvegarde :
		 *
		 *  Compteur de l'objet Time
		 *  Sparation : #
		 *  Pour tous les plateaux :
		 *  	- Position, zone et taille
		 *  	- Pour tous les objets du plateaux :
		 *  		- Sparation : ":"
		 *  		- Toutes les informations de l'objet
		 *  		-Sparation "\n"
		 *  	- Sparation : "#"
		 *  Sparation "&"
		 *  Nom du joueur actif
		 */
		String res = "";
		String n = "\n";
		res += String.valueOf((int)g.getTimeObject().getCompteur());
		res += "#";
		synchronized(Game.keyPlateau) {
			for (Plateau p:g.getUnivers().getAllPlateaux()) {
				int[] coord = p.getPos();
				res+=String.valueOf(coord[0])+" "+String.valueOf(coord[1])+" "+String.valueOf(coord[2])+" ";
				int size = p.getSize();
				res+= size+n;
				for (GameObject go:p.getObjects()) {
					if (go instanceof Player) {
						res += "player:";
						res+=((Player) go).getName()+ " ";
						res+= go.getPosX() + " ";
						res+= go.getPosY() + " ";
						res+= go.getColor() + " ";
						int[] stats = ((Player) go).getStats();
						for (int i = 2; i < 15; i++) {
							res += stats[i]+" ";
						}
						res+= stats[15]+" ";
						res+= ((Player) go).getPureXP()+" ";
						ArrayList<Focusable> loot = ((Player) go).getLoot();
						for (int i = 0; i < loot.size();i++) {
							Focusable f = loot.get(i);
							if (i == loot.size()-1) {
								if (f instanceof Food) {
									res+="f/";
									res+=((Food) f).getValue();
								}
								else if(f instanceof Alcool) {
									res+="a/";
									res+=((Alcool) f).getAlcool();
								}
								else if(f instanceof Potion) {
									res+="p/";
									res+=((Potion) f).getDuree();
								}
								else if(f instanceof Water) {
									res+="w/";
									res+=((Water) f).getValue();
								}

							}
							else {
								if (f instanceof Food) {
									res+="f/";
									res+=((Food) f).getValue()+" ";
								}
								else if(f instanceof Alcool) {
									res+="a/";
									res+=((Alcool) f).getAlcool()+" ";
								}
								else if(f instanceof Potion) {
									res+="p/";
									res+=((Potion) f).getDuree()+" ";
								}
								else if(f instanceof Water) {
									res+="w/";
									res+=((Water) f).getValue()+" ";
								}
							}
						}
					}
					else if(go instanceof BlockBreakable) {
						res += "breakable:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor() + " ";
						res += ((BlockBreakable) go).getLifePoints();
					}
					else if (go instanceof Alcool) {
							res += "alcool:";
							res += go.getPosX() + " ";
							res += go.getPosY() + " ";
							res += go.getColor() + " ";
							res += ((Alcool) go).getAlcool();
						}
					else if (go instanceof Potion) {
						res += "potion:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor() + " ";
						res += ((Potion) go).getDuree();
					}
					else if (go instanceof ArmurerieDoor) {
						res += "armurerieDoor:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor();
					}
					else if (go instanceof BlockUnbreakable) {
						res += "unbreakable:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						String color = go.getColor();
						if (color == null) {
							color = "null";
						}
						res += color + " ";
						res += ((BlockUnbreakable) go).isOnBorder();
					}
					else if (go instanceof Canon) {
						res +="canon:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += ((Canon) go).getDirection() + " ";
						res += go.getColor();
					}
					else if (go instanceof Coffre) {
						res +="coffre:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor() + " ";
						try{
							for (int i = 0; i < ((Coffre)go).getDrinkList().size(); i++) {
								res += "a/";
								res += ((Coffre) go).getDrinkList().get(i).getValue()+" ";

							}
							for (int i = 0; i < ((Coffre)go).getFoodList().size(); i++) {
								res += "f/";
								res += ((Coffre) go).getFoodList().get(i).getValue()+" ";

								}

						} catch(Exception e) {}

					}
					else if (go instanceof Door) {
						res += "door:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor()+ " ";
						res += ((Door) go).getUnlock();
					}
					else if(go instanceof Food) {
						res += "food:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor() + " ";
						res += ((Food) go).getValue();
					}
					else if (go instanceof Gold) {
						res += "gold:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor()+ " ";
						res += ((Gold) go).getValue();
					}
					else if (go instanceof Key) {
						res += "key:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor();
					}
					else if (go instanceof Mechant) {
						res += "mechant:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor() + " ";
						res += ((Mechant) go).getDifficulty();
					} else if (go instanceof Boss) {
						res += "boss:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor();
					}
					else if (go instanceof Mission) {
						res += "mission:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor() + " ";
						res += ((Mission) go).getMission() +1;
					}
					else if (go instanceof Toilet) {
						res += "toilet:";
						res += go.getPosX() + " ";
						res += go.getPosY() + " ";
						res += go.getColor();
					}
					res+=n;
				}
				res+="#";
			}
		}
		res+="&";
		res+=g.getActivePlayer().getName();
		Fichier f = new Fichier();
		f.write("save"+number+".txt", res);

	}

	public void loadSave(String path) {
		/*
		 * On load le fichier
		 * On va lire les informations en suivant le format lors de l'criture, crer les objets
		 * et les ajouter au fur et  mesure au plateau
		 */
		Fichier f = new Fichier();
		Univers univers = new Univers();
		String content = f.read(path);
		String[] all = content.split("&");
		String[] lines = all[0].split("#");
		double time = (double)Integer.valueOf(lines[0]);
		synchronized(Game.keyPlateau) {
			for (int i = 1; i < lines.length;i++) {
				String[] info = lines[i].split("\n");
				String[] coordPlateau = info[0].split(" ");
				int plateauX = Integer.valueOf(coordPlateau[0]);
				int plateauY = Integer.valueOf(coordPlateau[1]);
				int zone = Integer.valueOf(coordPlateau[2]);
				int size = Integer.valueOf(coordPlateau[3]);
				Plateau plateau = new Plateau(plateauX,plateauY,zone,size);
				for (int j = 1; j<info.length;j++) {
					GameObject res = null;
					String[] sep = info[j].split(":");
					String object = sep[0];
					String[] stats = sep[1].split(" ");
					int posX;
					int posY;
					String color;
					if (object.equals("player")) {
						String name = stats[0];
						String manga;
						if (name.equals("goku") || name.equals("vegeta")){
							manga = "dbz";
						}
						else if(name.equals("naruto") || name.equals("sasuke")) {
							manga = "naruto";
						}
						else {
							manga = "pokemon";
						}
						posX = Integer.valueOf(stats[1]);
						posY = Integer.valueOf(stats[2]);
						String grade = stats[3];
						int direction = Integer.valueOf(stats[4]);
						int energy = Integer.valueOf(stats[5]);
						int alcool = Integer.valueOf(stats[6]);
						int faim = Integer.valueOf(stats[7]);
						int soif = Integer.valueOf(stats[8]);
						int mission = Integer.valueOf(stats[9]);
						int onMission = Integer.valueOf(stats[10]);
						int gold = Integer.valueOf(stats[11]);
						int vessie = Integer.valueOf(stats[12]);
						int life = Integer.valueOf(stats[13]);
						int maxLife = Integer.valueOf(stats[14]);
						int damage = Integer.valueOf(stats[15]);
						int missionEnd = Integer.valueOf(stats[16]);
						int inventaireMax = Integer.valueOf(stats[17]);
						int xp = Integer.valueOf(stats[18]);
						int[] information = {posX,posY,direction,energy,alcool,faim,soif,mission,onMission,gold,vessie,life,maxLife,damage,missionEnd,inventaireMax,xp};

						switch(grade) {
							case "apprenti" : res = new Apprenti(name, manga,information,plateau);break;
							case "confirme" : res = new Confirme(name,manga,information,plateau);break;
							case "hokage" : res = new Hokage(name,manga,information,plateau);break;
						}

						g.addPlayer((Player)res);

						try {

							for (int k = 19; k < stats.length; k++) {
								String[] objet = stats[k].split("/");
								String type = objet[0];
								int value = Integer.valueOf(objet[1]);
								if (type.equals("f")) {
									((Player)res).addToLoot(new Food(1,1, value, plateau));
								}
								else if (type.equals("a")) {
									((Player)res).addToLoot(new Alcool(1,1,value, value, plateau));
								}
								else if (type.equals("p")) {
									((Player)res).addToLoot(new Potion(1,1,plateau,value));
								}
								else if (type.equals("w")) {
									((Player)res).addToLoot(new Water(1,1,value,plateau));
								}
							}
						} catch(Exception e) {System.out.println("Erreur dans lecture des consommables");}
					}
					else if (object.equals("alcool")){
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						int value = Integer.valueOf(stats[3]);
						res = new Alcool(posX,posY,value,value,plateau);
					}

					else if (object.equals("water")){
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						int value = Integer.valueOf(stats[3]);
						res = new Water(posX,posY,value,plateau);
					}
					else if (object.equals("potion")){
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						int value = Integer.valueOf(stats[3]);
						res = new Potion(posX,posY,plateau,value);
					}
					else if (object.equals("armurerieDoor")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						res = new ArmurerieDoor(posX,posY,plateau);
					}
					else if (object.equals("unbreakable")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						int onBoard = Integer.valueOf(stats[3]);
						res = new BlockUnbreakable(posX,posY,plateau,onBoard == 1,color);
					}
					else if (object.equals("breakable")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						int lifepoints = Integer.valueOf(stats[3]);
						res = new BlockBreakable(posX,posY,lifepoints,plateau,color);
						((BlockBreakable)res).attachDeletable(plateau);
					}
					else if (object.equals("canon")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						int direction = Integer.valueOf(stats[2]);
						color = stats[3];
						res = new Canon(posX,posY,direction,plateau,g);
					}
					else if (object.equals("coffre")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						res = new Coffre(posX,posY,plateau);
						try {
							for (int k = 3; k < stats.length; k++) {
								String type = stats[k].split("/")[0];
								int value = Integer.valueOf(stats[k].split("/")[1]);
								if (type.equals("f")) {
									((Coffre)res).addToFoodList(new Food(1,1,value));
								}
								else if (type.equals("a")) {
									((Coffre)res).addToDrinkList(new Alcool(1,1, value,value));
								}
								else if (type.equals("w")) {
									((Coffre)res).addToDrinkList(new Water(1,1,value));
								}
							}
						} catch(Exception e) {System.out.println("Erreur dans lecture des consommables");}
					}
					else if (object.equals("door")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						int unlock = Integer.valueOf(stats[3]);
						res = new Door(posX,posY,plateau,unlock,color);
					}
					else if (object.equals("food")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						int value = Integer.valueOf(stats[3]);
						res = new Food(posX,posY,value, plateau);
					}
					else if (object.equals("gold")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						int value = Integer.valueOf(stats[3]);
						res = new Gold(posX,posY,value, plateau);
					}
					else if (object.equals("key")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						res = new Key(posX,posY,plateau);
					}
					else if (object.equals("mechant")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						int difficulty = Integer.valueOf(stats[3]);
						res = new Mechant(posX,posY,difficulty, plateau, g, color);
					} else if (object.equals("boss")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						res = new Boss(posX,posY, plateau, g, color);
					}
					else if (object.equals("mission")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						int mission = Integer.valueOf(stats[3]);
						res = new Mission(posX,posY,mission, plateau);
					}
					else if (object.equals("toilet")) {
						posX = Integer.valueOf(stats[0]);
						posY = Integer.valueOf(stats[1]);
						color = stats[2];
						res = new Toilet(posX, posY, plateau);
					}
					if (res == null) {
						System.out.println("Objet non dfini lors de la load de la save");
						/*
						 * Ce cas-ci n'est sens jamais arriv mais il permet de savoir
						 * au plus vite o se situe l'erreur pour un ventuel dbogage
						 */
					}
					else {
						plateau.addObject(res);
					}

				}
				univers.addPlateau(plateau);
			}
		}
		String activePlayer = all[1];
		g.load(univers,time, activePlayer);

	}

}
