
package Model;

import java.util.ArrayList;
import java.util.Random;


public class Plateau implements DeletableObserver{
	
	private ArrayList<GameObject> objects = new ArrayList<GameObject>(); //liste d'objets du plateau
	
	private int numberOfBreakableBlocks = 40; //nombre de blocs cassables
	
	private int x; //la position x,y du plateau correspond a sa position dans une grille ou le plateau d accueil est le centre (0,0)
	private int y;
	private int size; //taille du plateau en blocs 
	private int[] valporte;
	
	boolean[] pDoor; //tableau de 4 booleen (1 pour chaque bord) qui indique pour chaque bord du plateau s il y a une porte ou non

	//permet l apparition d objet la ou un bloc ou un mechant est detruit
	private LootFactory lf = new LootFactory();
	private String[] listloot = {"alcool","gold", "water"};
	
	private Random rand = new Random();
	
	private int zone; //une zone est un ensemble de deux plateaux
	
	
	
	//constructeur pour la sauvegarde
	public Plateau(int x, int y, int zone, int size) {
		this.x = x;
		this.y = y;
		this.zone = zone;
		this.size = size;
	}
	
	//si une taille n est pas precisee, on construit avec une taille 25
	public Plateau(int x, int y,  boolean[] pDoor, int[] valporte, int zone) {
		this(x,y,pDoor,valporte,zone,25);
		
	}
	
	//constructeur pour un plateau de taille choisie prealablement
	public Plateau(int x, int y,  boolean[] pDoor, int[] valporte, int zone, int size) { 
		this.x = x;
		this.y = y;
		this.pDoor = pDoor;
		this.zone = zone;
		this.valporte = valporte;
		this.setSize(size);

		borderDoor();
		
		createBorder();
		
		createBreakableBlocks();
		
		plateauAccueil();
		
		addMissionPlateau();
		
		jungle();
		
		desert();
		
		home();
		
		terresSombres();
			
	}
	
	
	
	//creation des blocs incassables entournat chaque plateau
	public void createBorder() {
		for (int i = 0; i < size; i++) { 
			if (isFreePos(i,0)) { 
				objects.add(new BlockUnbreakable(i, 0,this,true,null));
			}
			if (isFreePos(0,i)) {
				objects.add(new BlockUnbreakable(0, i,this,true,null));
			}
			if (isFreePos(i,size-1)) {
				objects.add(new BlockUnbreakable(i, size - 1,this,true,null));
			}
			if (isFreePos(size-1,i)) {
				objects.add(new BlockUnbreakable(size - 1, i,this,true,null));
			}
           
        }
	}
	
	
	//creation du plateau d accueil avec ses objets et decors
	public void plateauAccueil() {
		if(zone == 0) {
			
			objects.add(new ArmurerieDoor(3,21,this));
			
			objects.add(new BlockUnbreakable(5,16,this,false,"armurerie"));
			objects.add(new BlockUnbreakable(1,16,this,false,null));
			objects.add(new BlockUnbreakable(2,16,this,false,null));
			objects.add(new BlockUnbreakable(3,16,this,false,null));
			objects.add(new BlockUnbreakable(4,16,this,false,null));
			
			for(int i = 0;i<6;i++) {
				for(int j = 0;j<3;j++) {
					objects.add(new BlockUnbreakable(i,17+j,this,false,null));
				}
			}
			objects.add(new BlockUnbreakable(1,20,this,false,null));
			objects.add(new BlockUnbreakable(2,20,this,false,null));
			objects.add(new BlockUnbreakable(4,20,this,false,null));
			objects.add(new BlockUnbreakable(5,20,this,false,null));
			
			objects.add(new Door(2,5,this,this.valporte[0], "home"));
			objects.add(new BlockUnbreakable(5,5,this,false,"maison"));
			for(int i = 1;i<=5;i++) {
				for(int j = 1;j<=5;j++) {
					objects.add(new BlockUnbreakable(i,j,this,false,null));
				}
			}
			objects.add(new Coffre(22,23,this));
			
			objects.add(new Toilet(23, 1,this));
			
			Mission m = new Mission(15,20,1,this);
			objects.add(m);
			Mission m2 = new Mission(16,20,2,this);
			objects.add(m2);
			Mission m3 = new Mission(17,20,3,this);
			objects.add(m3);
			
			objects.add(new BlockUnbreakable(12,12,this,false,"fountain"));
			objects.add(new BlockUnbreakable(11,12,this,false,null));
			objects.add(new BlockUnbreakable(13,12,this,false,null));
			objects.add(new BlockUnbreakable(12,11,this,false,null));
			objects.add(new BlockUnbreakable(12,13,this,false,null));
			
			
			for(int i = 0; i<3;i++) {
				objects.add(new BlockUnbreakable(7+i,6,this,false,"flower"));
				objects.add(new BlockUnbreakable(6+i,14,this,false,"flower"));
				objects.add(new BlockUnbreakable(6+i,10,this,false,"flower"));
				objects.add(new BlockUnbreakable(7+i,18,this,false,"flower"));
				objects.add(new BlockUnbreakable(15+i,6,this,false,"flower"));
				objects.add(new BlockUnbreakable(16+i,10,this,false,"flower"));
				objects.add(new BlockUnbreakable(16+i,14,this,false,"flower"));
				objects.add(new BlockUnbreakable(15+i,18,this,false,"flower"));
				
				objects.add(new BlockUnbreakable(6,7+i,this,false,"flower"));
				objects.add(new BlockUnbreakable(10,6+i,this,false,"flower"));
				objects.add(new BlockUnbreakable(14,6+i,this,false,"flower"));
				objects.add(new BlockUnbreakable(18,7+i,this,false,"flower"));
				objects.add(new BlockUnbreakable(6,15+i,this,false,"flower"));
				objects.add(new BlockUnbreakable(10,16+i,this,false,"flower"));
				objects.add(new BlockUnbreakable(14,16+i,this,false,"flower"));
				objects.add(new BlockUnbreakable(18,15+i,this,false,"flower"));
			}
			for(int i = 0;i<2;i++) {
				objects.add(new BlockUnbreakable(8+i,8,this,false,"flower"));
				objects.add(new BlockUnbreakable(15+i,8,this,false,"flower"));
				objects.add(new BlockUnbreakable(8+i,16,this,false,"flower"));
				objects.add(new BlockUnbreakable(15+i,16,this,false,"flower"));
				
				objects.add(new BlockUnbreakable(8+i,7,this,false,null));
				objects.add(new BlockUnbreakable(15+i,7,this,false,null));
				objects.add(new BlockUnbreakable(8+i,17,this,false,null));
				objects.add(new BlockUnbreakable(15+i,17,this,false,null));
				objects.add(new BlockUnbreakable(7,8+i,this,false,null));
				objects.add(new BlockUnbreakable(17,8+i,this,false,null));
				objects.add(new BlockUnbreakable(7,15+i,this,false,null));
				objects.add(new BlockUnbreakable(17,15+i,this,false,null));
			}

			
			objects.add(new BlockUnbreakable(7,7,this,false,"flower"));
			objects.add(new BlockUnbreakable(17,7,this,false,"flower"));
			objects.add(new BlockUnbreakable(7,17,this,false,"flower"));
			objects.add(new BlockUnbreakable(17,17,this,false,"flower"));
			objects.add(new BlockUnbreakable(8,9,this,false,"flower"));
			objects.add(new BlockUnbreakable(16,9,this,false,"flower"));
			objects.add(new BlockUnbreakable(8,15,this,false,"flower"));
			objects.add(new BlockUnbreakable(16,15,this,false,"flower"));
		}
	}

	
	//creation du plateau maison avec ses objets et decors
	public void home() {
		if(this.zone == 7) {
			for(int i = 0; i< 7;i++) {
				objects.add(new BlockUnbreakable(8,1+i,this,false,"woodWall"));
			}
			for(int i = 0 ;i<8;i++) {
				objects.add(new BlockUnbreakable(6+i,8,this,false,"woodWall"));
			}
			for(int i = 0 ;i<8;i++) {
				objects.add(new BlockUnbreakable(6+i,12,this,false,"woodWall"));
			}
			
			for(int i =0;i<7;i++) {
				objects.add(new BlockUnbreakable(11,12+i,this,false,"woodWall"));
						
			}
			objects.add(new BlockUnbreakable(1,12,this,false,"woodWall"));
			objects.add(new BlockUnbreakable(2,12,this,false,"woodWall"));
			objects.add(new BlockUnbreakable(size-2,12,this,false,"woodWall"));
			objects.add(new BlockUnbreakable(size-3,12,this,false,"woodWall"));
			objects.add(new BlockUnbreakable(1,8,this,false,"woodWall"));
			objects.add(new BlockUnbreakable(2,8,this,false,"woodWall"));
			objects.add(new BlockUnbreakable(size-2,8,this,false,"woodWall"));
			objects.add(new BlockUnbreakable(size-3,8,this,false,"woodWall"));
			
			objects.add(new BlockUnbreakable(2,size-3,this,false,"lit1"));
			objects.add(new BlockUnbreakable(2,size-2,this,false,null));
			
			objects.add(new BlockUnbreakable(4,size-3,this,false,"lit1"));
			objects.add(new BlockUnbreakable(4,size-2,this,false,null));
			
			objects.add(new BlockUnbreakable(6,size-3,this,false,"lit2"));
			objects.add(new BlockUnbreakable(6,size-2,this,false,null));
			
			objects.add(new BlockUnbreakable(8,size-3,this,false,"lit2"));
			objects.add(new BlockUnbreakable(8,size-2,this,false,null));
			
			objects.add(new BlockUnbreakable(10,15,this,false,"armoireCote"));
			objects.add(new BlockUnbreakable(10,16,this,false,null));
			
			objects.add(new BlockUnbreakable(10,17,this,false,"armoireCote"));
			objects.add(new BlockUnbreakable(10,18,this,false,null));
			
			objects.add(new BlockUnbreakable(7,3,this,false,"armoireCote"));
			objects.add(new BlockUnbreakable(7,4,this,false,null));
			
			objects.add(new BlockUnbreakable(7,5,this,false,"armoireCote"));
			objects.add(new BlockUnbreakable(7,6,this,false,null));
			
			objects.add(new BlockUnbreakable(18,3,this,false,"armoireCote"));
			objects.add(new BlockUnbreakable(18,4,this,false,null));
			
			objects.add(new BlockUnbreakable(18,5,this,false,"armoireCote"));
			objects.add(new BlockUnbreakable(18,6,this,false,null));
			
			objects.add(new BlockUnbreakable(18,15,this,false,"armoireCote"));
			objects.add(new BlockUnbreakable(18,16,this,false,null));
			
			objects.add(new BlockUnbreakable(18,17,this,false,"armoireCote"));
			objects.add(new BlockUnbreakable(18,18,this,false,null));
			
			objects.add(new BlockUnbreakable(6,13,this,false,"armoire"));
			objects.add(new BlockUnbreakable(7,13,this,false,null));
			objects.add(new BlockUnbreakable(8,13,this,false,null));
			objects.add(new BlockUnbreakable(9,13,this,false,null));
			
			objects.add(new BlockUnbreakable(15,1,this,false,"armoire"));
			objects.add(new BlockUnbreakable(16,1,this,false,null));
			objects.add(new BlockUnbreakable(17,1,this,false,null));
			objects.add(new BlockUnbreakable(18,1,this,false,null));
			
			objects.add(new BlockUnbreakable(10,13,this,false,"flowerpot"));
			objects.add(new BlockUnbreakable(10,14,this,false,"plant"));
			objects.add(new BlockUnbreakable(1,1,this,false,"flowerpot"));
			objects.add(new BlockUnbreakable(7,7,this,false,"flowerpot"));
			objects.add(new BlockUnbreakable(12,18,this,false,"flowerpot"));
			
			
			objects.add(new BlockUnbreakable(10,1,this,false,"cheminee"));
			
			objects.add(new BlockUnbreakable(9,1,this,false,"plant"));
			objects.add(new BlockUnbreakable(18,7,this,false,"plant"));
			objects.add(new BlockUnbreakable(1,18,this,false,"plant"));
			
			objects.add(new BlockUnbreakable(2,1,this,false,"bureau"));
			objects.add(new BlockUnbreakable(3,1,this,false,null));
			objects.add(new BlockUnbreakable(2,2,this,false,"chaise2"));
			
			objects.add(new BlockUnbreakable(6,1,this,false,"commode"));
			objects.add(new BlockUnbreakable(7,1,this,false,null));
			
			objects.add(new BlockUnbreakable(17,13,this,false,"commode"));
			objects.add(new BlockUnbreakable(18,13,this,false,null));
			
			objects.add(new BlockUnbreakable(12,13,this,false,"miroir"));
			
			for(int i = 0;i<3;i++) {
				for(int j = 0;j<3;j++) {
					objects.add(new BlockUnbreakable(13+i,3+j,this,false,null));
				}
			}
			objects.add(new BlockUnbreakable(13,3,this,false,"table"));
			objects.add(new BlockUnbreakable(14,2,this,false,"chaise0"));
			objects.add(new BlockUnbreakable(12,4,this,false,"chaise1"));
			objects.add(new BlockUnbreakable(14,6,this,false,"chaise2"));
			objects.add(new BlockUnbreakable(16,4,this,false,"chaise3"));
			
			objects.add(new BlockUnbreakable(2,5,this,false,"chaise0"));
			objects.add(new BlockUnbreakable(4,5,this,false,"chaise0"));
			objects.add(new BlockUnbreakable(3,5,this,false,"echec"));
			
			objects.add(new BlockUnbreakable(12,13,this,false,"miroir"));
			
			objects.add(new BlockUnbreakable(1,13,this,false,"biblio"));
			
			for(int i = 0;i<3;i++) {
				objects.add(new Coffre(14+i,18,this));
			}	
			
		}

	}
	
	
	//creation des portes aux 4 bords du plateau si le booleen correspondant dans pDoor est verifie 
	public void borderDoor() {
		if (this.pDoor[0]) {
			objects.add(new Door((size/2),0,this,valporte[0],"door")); //permet par exemple de placer une porte au milieu du bord superieur du plateau
		} if (this.pDoor[1]) {
			objects.add(new Door((size/2),size-1,this,valporte[1],"door"));
		} if (this.pDoor[2]) {
			objects.add(new Door(0,(size/2),this,valporte[2],"door")); 
		} if (this.pDoor[3]) {
			objects.add(new Door(size-1,(size/2),this,valporte[3],"door")); 
		}
	}
	
	//methode pour ajouter une porte a un endroit ou il n y en avait pas, qui est utilisee notamment pour acceder au deuxieme plateau d une zone mission une fois le defi reussi
	public void addDoor(int dir) {

		if (dir == Directable.NORTH) {
			objects.add(new Door((size/2),0,this)); //permet de placer une porte en haut au milieu
		} 
		else if (dir == Directable.SOUTH) {
			objects.add(new Door((size/2),size-1,this));
		} 
		else if (dir == Directable.WEST) {
			objects.add(new Door(0,(size/2),this)); 
		}  
		else if (dir == Directable.EAST) {
			objects.add(new Door(size-1,(size/2),this)); 
		}
	}
	
	
	//methode pour placer les recompenses de fin de mission et la cle sur les maps de fin de mission
	//les zone missions sont composee d un plateau avec le defi et d un plateau de fin
	
	public void endMission() {
		if(this.zone == 4 || this.zone == 5 || this.zone == 6) {
			if(this.getPos()[1] == 1) {
				boolean cond = true;
				synchronized(Game.keyPlateau) {
					for (GameObject go : getObjects()) {
						if (go instanceof Key) {
							cond = false;
							break;
						}
					}
				}
				
				if (cond) {
						Key k = new Key(size/2,size/2,this);
						k.attachDeletable(this);
						objects.add(k);
				}
			}
			
		}
	}
	
	
	//creation de blocs cassables a une position aleatoire avec un nombre de vie aleatoire sur certains plateaux
	public void createBreakableBlocks() {
		if(this.zone != 0 && (this.getPos()[0] != 1 || this.getPos()[1] != 0) && this.getPos()[2] != 7) {
	        Random rand = new Random();
		    for (int i = 0; i < numberOfBreakableBlocks; i++) {
		        int x1 = rand.nextInt(size-4) + 2; 
		        int y1 = rand.nextInt(size-4) + 2;
		            
		        if (isFreePos(x1,y1)) { 
		        	int lifepoints = rand.nextInt(5) + 1; 
		            BlockBreakable block = new BlockBreakable(x1, y1, lifepoints,this); 
		            block.attachDeletable(this); 
		            objects.add(block); 
	            }
	        }
        }
	}
	
	
	//creation des plateaux et decors pour la zone de jungle 
	public void jungle() {
		if(zone == 1) {
			for (int i = 0; i < 8; i++) {
	            int x1 = rand.nextInt(size-4) + 2; 
	            int y1 = rand.nextInt(size-4) + 2;
	            
	            if (isFreePos(x1,y1)) { 
	                objects.add(new BlockUnbreakable(x1, y1, this,false,"caillou"));  
	            }
	        }
			
	        for (int i = 0; i < 10; i++) {
	            int x1 = rand.nextInt(size-4) + 2; 
	            int y1 = rand.nextInt(size-4) + 2;
	            
	            if (isFreePos(x1,y1)) { 
	                objects.add(new BlockUnbreakable(x1, y1, this,false,"fleurs"));  
	            }
	        }
	        
	        for (int i = 0; i < 8; i++) {
	            int x1 = rand.nextInt(size-4) + 2; 
	            int y1 = rand.nextInt(size-4) + 2;
	            
	            if (isFreePos(x1,y1)) { 
	                objects.add(new BlockUnbreakable(x1, y1, this,false,"parterre"));  
	            }
	        }
	        
	        for (int i = 0; i < 5; i++) {
	            int x1 = rand.nextInt(size-4) + 2; 
	            int y1 = rand.nextInt(size-4) + 2;
	            
	            if (isFreePos(x1,y1)) { 
	                objects.add(new BlockUnbreakable(x1, y1, this,false,"souche"));  
	            }
	        }
		}
	}
	
	
	//creation des plateaux et decors pour la zone de desert
	public void desert(){
		if(zone == 2) {		
			for (int i = 0; i < 8; i++) {
		        int x1 = rand.nextInt(size-4) + 2; 
		        int y1 = rand.nextInt(size-4) + 2;
		            
		        if (isFreePos(x1,y1)) { 
		           objects.add(new BlockUnbreakable(x1, y1, this,false,"cactus1"));  
		        }
			}
				
				
		        
		    for (int i = 0; i < 10; i++) {
		        int x1 = rand.nextInt(size-4) + 2; 
	            int y1 = rand.nextInt(size-4) + 2;
		            
	            if (isFreePos(x1,y1)) { 
		            objects.add(new BlockUnbreakable(x1, y1, this,false,"grass1"));  
		        }
		    }
		        
		    for (int i = 0; i < 8; i++) {
		        int x1 = rand.nextInt(size-4) + 2; 
		        int y1 = rand.nextInt(size-4) + 2;
		            
		        if (isFreePos(x1,y1)) { 
		        	objects.add(new BlockUnbreakable(x1, y1, this,false,"cactus3"));  
		        }
		    }
		        
		    for (int i = 0; i < 5; i++) {
		        int x1 = rand.nextInt(size-4) + 2; 
		        int y1 = rand.nextInt(size-4) + 2;
		            
		        if (isFreePos(x1,y1)) { 
		        	objects.add(new BlockUnbreakable(x1, y1, this,false,"skeleton"));  
		        }
		    }
		        
		    for (int i = 0; i < 10; i++) {
		        int x1 = rand.nextInt(size-4) + 2; 
		        int y1 = rand.nextInt(size-4) + 2;
		            
		        if (isFreePos(x1,y1)) { 
		        	objects.add(new BlockUnbreakable(x1, y1, this,false,"stone"));  
		        }
		    }
		}
	}
	
	
	//creation des plateaux et decors pour la zone terresSombres
	public void terresSombres() {
		if (zone == 3) {
			for (int i = 0; i < 10; i++) {
	            int x1 = rand.nextInt(size-4) + 2; 
	            int y1 = rand.nextInt(size-4) + 2;
	            
	            if (isFreePos(x1,y1)) { 
	                objects.add(new BlockUnbreakable(x1, y1, this,false,"skull"));  
	            }
	        }
			
			
	        
	        for (int i = 0; i < 10; i++) {
	            int x1 = rand.nextInt(size-4) + 2; 
	            int y1 = rand.nextInt(size-4) + 2;
	            
	            if (isFreePos(x1,y1)) { 
	                objects.add(new BlockUnbreakable(x1, y1, this,false,"lava"));  
	            }
	        }
	        
	        for (int i = 0; i < 8; i++) {
	            int x1 = rand.nextInt(size-4) + 2; 
	            int y1 = rand.nextInt(size-4) + 2;
	            
	            if (isFreePos(x1,y1)) { 
	                objects.add(new BlockUnbreakable(x1, y1, this,false,"tombstone"));  
	            }
	        }
	        
		}
		
	}
	
	
	//methode pour faire apparaitre un objet de type ramassable a la position ou un bloc ou mechant a ete tue
	public void spawnLoot(GameObject go) {
		int proba = rand.nextInt(100);
		if (proba < 20) { //taux d apparition de 20% d un loot lors de la destruction d un objet
			if(go instanceof BlockBreakable) { 
				
				int indiceAleatoire = rand.nextInt(listloot.length); //les blocs donnent des objets aleatoire
				if(indiceAleatoire < listloot.length) {	
							
					String objetapparu = listloot[indiceAleatoire];
					GameObject lootapparu = lf.getInstance(objetapparu,(GameObject)go);
					objects.add(lootapparu);
							
					if(lootapparu instanceof Drink) {
						((Drink)lootapparu).attachDeletable(this);

					}
							
					if(lootapparu instanceof Food) {
						((Food)lootapparu).attachDeletable(this);
								
					}

					if(lootapparu instanceof Gold) {
						((Gold)lootapparu).attachDeletable(this);
								
						}
					}	
			}
			
			else if(go instanceof Mechant) {
				GameObject newfood = lf.getInstance("food", (GameObject)go); //les mechants ne peuvent donner que de la nourriture
				objects.add(newfood);
				((Food) newfood).attachDeletable(this);
				
			}
		}
	}
	

	//methode qui permet de voir si une position est deja occupee par un objet ou non sur un plateau
	public boolean isFreePos(int x, int y) { 
		boolean res = true;
		for (GameObject g: objects) {
			if (g.isAtPosition(x, y)) {
				res = false;
				break;
			}
		}
		return res;
	}
	
	//methode pour rajouter les missions correspondantes a la zone. La mission 2 est par exemple ajoutee sur la zone accsessibles a partir du grade de confirme
	private void addMissionPlateau() {
		if (this.getPos()[0] == -1 && this.getPos()[1] == 1) {
			Mission m = new Mission(size/2,size/2,1,this);

			objects.add(m);
		} else if (this.getPos()[0] == -1 && this.getPos()[1] == -1) {
			Mission m = new Mission(size/2,size/2,2,this);

			objects.add(m);
		} else if (this.getPos()[0] == 1 && this.getPos()[1] == -1) {
			Mission m = new Mission(size/2,size/2,3,this);

			objects.add(m);
		}
	}
	
	//le plateau est l observer des objets qu il detient
	public void delete(Deletable d, ArrayList<GameObject> loot, Player p) {
		if (d instanceof GameObject) {
			this.getObjects().remove((GameObject)d);
			spawnLoot((GameObject)d);
		}
		
	}
	
	public void addObject(GameObject go) {
		this.objects.add(go);
	}
	
	public void removeObject(GameObject go) {
		this.objects.remove(go);
	}
	
	
	//methode qui donne tous les objets a une position
	//il peut y avoir par exemple deux objets quand une fireball touche un joueur sans le tuer et cree une explosion a l endroit du joueur toujours vivant
	public ArrayList<GameObject> getAllGameObjectsAt(int x, int y) {
		ArrayList<GameObject> res = new ArrayList<GameObject>();
		for (GameObject go:objects) {

			if (go.isAtPosition(x, y)) { 
				res.add(go);
			}
		}
		return res;
	}
	
	//transforme un string en entier
	public String toString() {
		String res = null;
		res += String.valueOf(x);
		res += " ";
		res += String.valueOf(y);
		return res;
	}
	
	
	//Accesseurs
	
	
	public int[] getPos() {
		int[] res = {x,y,zone};
		return res;
	}
	public int getZone() {
		return this.zone;
	}

	public ArrayList<GameObject> getObjects(){
		return this.objects;
	}


	public int getSize() {
		return size;
	}
	

	public void setSize(int size) {
		this.size = size;
	}

	
	public int getNumberOfBreakablesBlocks() {
		return this.numberOfBreakableBlocks;
	}


}
