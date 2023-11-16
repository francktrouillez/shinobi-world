package View;

import Model.Directable;
import Model.Explosion;
import Model.Game;
import Model.GameObject;
import Model.Plateau;
import Model.Player;
import Model.Wave;
import Model.Hit;
import Model.Mechant;
import Model.Mission;
import Model.Boss;

import java.awt.Color;
import java.awt.Graphics;

import java.util.ArrayList;



import javax.swing.JPanel;



public class MapFenetre extends JPanel{

    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private Game game;

    private Images img = new Images(0);

    //private Mouse mouseController = null;

    private int increment;
    private int plateauSize;

    //constantes
    public int BLOC_SIZE = 50;
    private int MAP_SIZE = 20; //taille de la fenetre en nombre de blocs



    //classe qui permet un fenetrage couvrant un carre de 20 bloc sur 20 bloc dans le plateau
    public MapFenetre(Game g, int sizeFenetre) {

    	this.game = g;
    	setBlockSize(sizeFenetre);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setOpaque(false);

    }

    //methode qui permet de definir la taille des blocs connaissant le nombre de blocs que la fenetre doit couvrir
    public void setBlockSize(int size) {
    	BLOC_SIZE = size/MAP_SIZE;
    }


    //dessin de la map ainsi que des elements qui la constituent
    public void paint(Graphics g) {
    	try {
    		setBlockSize(this.getWidth());
    		Player main = game.getActivePlayer(); //le fenetrage a lieu autour du joueur actif

    		objects = new ArrayList<GameObject>();

    	    Plateau plateau = game.getActivePlateau();
    	    plateauSize = plateau.getSize();


    	    //position reelle du joueur
    	    int realPosPlayerX = main.getPosX();
    	    int realPosPlayerY = main.getPosY();

    	    //position du centre de la mapfenetre
    	    int xCenterMap =  realPosPlayerX;
    	    int yCenterMap = realPosPlayerY;

    	    boolean moveX = true; //permet de savoir si le joueur se deplace dans la fenetre (cas aux bords) ou si c est la fenetre centree sur le joueur qui se deplace dans le plateau
    	    boolean moveY = true; //pareil en y

    	    //differents cas ou le joueur n est plus le centre de la mapfenetre

    	    //cas ou le joueur se trouve proche du bord droit mais ne pointe pas vers la droite, il conserve donc sa position en x dans la fenetre
    	    if (realPosPlayerX > plateau.getSize()- MAP_SIZE/2 -1 && !(realPosPlayerX == plateau.getSize() - MAP_SIZE/2 && main.getDirection() == Directable.EAST)) {
    	    	xCenterMap = plateau.getSize()-MAP_SIZE/2; //etant au bord, le centre de la mapfenetre ne peut plus etre le joueur
    	    	moveX = false; //la fenetre ne doit pas se deplacer en x
    	     }

    	    //cas ou le joueur se trouve proche du bord gauche mais ne pointe pas vers la gauche, il conserve donc sa position en x dans la fenetre
    	    else if (realPosPlayerX < MAP_SIZE/2 +1 && !(realPosPlayerX == MAP_SIZE/2 && main.getDirection() == Directable.WEST)) {
    	       	xCenterMap = MAP_SIZE/2;
    	       	moveX = false;
    	    }

    	    //cas ou le joueur se trouve proche du bord superieur mais ne pointe pas vers le haut, il conserve donc sa position en y dans la fenetre
    	    if (realPosPlayerY> plateau.getSize()- MAP_SIZE/2 -1 && !(realPosPlayerY == plateau.getSize() - MAP_SIZE/2 && main.getDirection() == Directable.SOUTH)) {
    	    	yCenterMap = plateau.getSize()-MAP_SIZE/2;
    	    	moveY = false;
    	    }

    	    //cas ou le joueur se trouve proche du bord inferieur mais ne pointe pas vers le bas, il conserve donc sa position en y dans la fenetre
    	    else if (realPosPlayerY < MAP_SIZE/2 +1 && !(realPosPlayerY == MAP_SIZE/2 && main.getDirection() == Directable.NORTH)) {
    	    	yCenterMap = MAP_SIZE/2;
    	    	moveY = false;
    	    }

    	    //on enregistre tous les objets autour du centre de la mapfenetre
    	    for (int i = xCenterMap - (MAP_SIZE/2)-1; i <= xCenterMap + (MAP_SIZE/2)+1;i++) {

    	    	for (int j = yCenterMap - (MAP_SIZE/2)-1; j <= yCenterMap + (MAP_SIZE/2)+1;j++) {

    	    		if ((i >= 0) && (i < plateau.getSize()) && (j >= 0) && (j < plateau.getSize())) {

    	    			ArrayList<GameObject> save = null;
    	    			save = plateau.getAllGameObjectsAt(i, j);

    		        	for (GameObject o:save) {
    		        		objects.add(o);
        		        }
    	           	}
    	        }
    	    }

    	    reorganizeObjects(); //on reorganise afin d ensuite pouvoir peindre les objets du haut vers le bas de la map


    	    int decX = 0;
    	    int decY = 0;

    	    //permet de calculer les futurs positions des objets lorsque le joueur actif se deplace
    	    if (main.isMoving()) {

    	    	int orientation = main.getDirection();

    	    	switch(orientation) {
    	    	case Directable.NORTH: decY = 1; break; //si le joueur se deplace d'un bloc vers le nord, dans son repere, les objets se deplace d un bloc vers le sud
    	    	case Directable.SOUTH: decY = -1; break;
    	    	case Directable.EAST: decX = -1; break;
    	    	case Directable.WEST: decX = 1; break;
    	        }
    	    }

    	    //dessin du fond

	    	int zone = plateau.getZone();
	    	drawFondZone(g,zone, xCenterMap,yCenterMap);

	    	try {

		    	GameObject object = null;

		    	//dessin de chaque objet dans la mapfenetre
		    	for (int i = 0; i < objects.size(); i++) {

		    		object = objects.get(i);

		    		//position en bloc de l'objet dans la fenetre lorsqu'elle ne se deplace pas
		    		int x = object.getPosX()-xCenterMap+(MAP_SIZE/2);
		    		int y = object.getPosY()-yCenterMap+(MAP_SIZE/2);


		    		String color = object.getColor(); //nom de l objet pour rechercher son image


		    	    try {
		    	    	//position de l objet dans la mapfenetre
		    	        int posOfBlockX;
		    	        int posOfBlockY;

		    	        //si la fenetre doit se deplacer en x, c est a dire dans un cas ou elle est centree en x sur le joueur, alors l objet doit se deplacer dans le repere du joueur
		    	        if (moveX) {
		    	        	posOfBlockX = (x-decX)*BLOC_SIZE + increment*decX; // l increment permet un deplacement fluide des objets pendant le deplacement de la fenetre
		    	        }
		    	        //si la fenetre ne doit pas se deplacer en x, c est a dire le cas ou elle n est plus centree sur le joueur (proche des bords gauche et droit), l objet ne doit donc pas bouger dans la fenetre
		    	        else {
		    	        	posOfBlockX = x*BLOC_SIZE;
		    	        }

		    	        if (moveY) {
		    	        	posOfBlockY = (y-decY)*BLOC_SIZE + increment*decY;
		    	        }

		    	        else {
		    	        	posOfBlockY = y*BLOC_SIZE;
		    	        }

		    	        if (object instanceof Player) {
		    	        	if (game.isActivePlayer((Player) object)) {

		    	        		//si la fenetre ne bouge pas en x, cas aux bords gauche et droit, alors le joueur doit se deplacer dans la fenetre
		    	        		if (!moveX) {
		    	        			posOfBlockX = (x+decX)*BLOC_SIZE - increment*decX;
		    	        		}

		    	        		//si la fenetre doit bouger en x, alors elle est centree sur le joueur en x
		    	        		else {
		    	        			posOfBlockX = (MAP_SIZE/2)*BLOC_SIZE;
		    	        		}

		    	        		if (!moveY) {
		    	        			posOfBlockY = (y+decY)*BLOC_SIZE - increment*decY;
		    	        		}

		    	        		else {
		    	        			posOfBlockY = (MAP_SIZE/2)*BLOC_SIZE;
		    	        		}
		    	        	}
		    	        }

		    	       //dessin des differents objets

		    	        if (color == null) {
		    	        }

		    	        else if (color.equals("hubTree")) {
		    	        	g.drawImage(img.arbre, posOfBlockX, posOfBlockY, BLOC_SIZE, BLOC_SIZE, this);

		    	        } else if (color.equals("forestTree")) {
		    	        		g.drawImage(img.getImage("forestTree.png"), posOfBlockX, posOfBlockY, BLOC_SIZE, BLOC_SIZE, this);

		    	        } else if (color.equals("valleeTree")) {
		    	        		g.drawImage(img.getImage("valleeTree.png"), posOfBlockX, posOfBlockY, BLOC_SIZE, BLOC_SIZE, this);

		    	        } else if (color.equals("darkTree")) {
		    	        	g.drawImage(img.getImage("darkTree.png"), posOfBlockX, posOfBlockY, BLOC_SIZE, BLOC_SIZE, this);

		    	        } else if (color.equals("door")) {
		    	        	double facteur = 1.5;
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	        	g.drawImage(img.getImage("door.png"), posOfBlockX, posOfBlockY, taille, taille, this);
		    	        } else if ( color.equals("grass1")) {
		    	            g.drawImage(img.grass1,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("grass2")) {
		    	        	g.drawImage(img.grass2,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("cactus1")) {
		    	            g.drawImage(img.cactus1,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("cactus2")) {
		    	            g.drawImage(img.cactus2,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("cactus3")) {
		    	            g.drawImage(img.cactus3,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("crate")) {
		    	            g.drawImage(img.crate,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("bush1")) {
		    	            	g.drawImage(img.bush1,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("bush2")) {
		    	            g.drawImage(img.bush2,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("sign")) {
		    	            g.drawImage(img.sign,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("skeleton")) {
		    	            g.drawImage(img.skeleton,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("skull")) {
		    	            g.drawImage(img.getImage("skull.png"),posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("lava")) {
		    	            g.drawImage(img.getImage("lava.png"),posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("tombstone")) {
		    	            g.drawImage(img.getImage("tombstone.png"),posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("caillou")) {
		    	           	g.drawImage(img.getImage("caillou.png"),posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("parterre")) {
		    	           	g.drawImage(img.getImage("parterre.png"),posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("souche")) {
		    	           	g.drawImage(img.getImage("souche.png"),posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("fleurs")) {
		    	           	g.drawImage(img.getImage("fleurs.png"),posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("maison")) {
		    	            g.drawImage(img.getImage("maison.png"),posOfBlockX-4*BLOC_SIZE,posOfBlockY-4*BLOC_SIZE, BLOC_SIZE*5, BLOC_SIZE*5,this);

		    	        } else if (color.equals("armurerie")) {
		    	            g.drawImage(img.getImage("armurerie.png"),posOfBlockX-4*BLOC_SIZE,posOfBlockY, BLOC_SIZE*5, BLOC_SIZE*6,this);

		    	        } else if (color.equals("coffre")) {
		    	           	g.drawImage(img.coffre,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("stone")) {
		    	           	g.drawImage(img.stone,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("fountain")) {
		    	            double facteur = 4;
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	            g.drawImage(img.fountain, posOfBlockX, posOfBlockY, taille, taille, this);


		    	        } else if (color.equals("flower")) {
		    	        	g.drawImage(img.flower,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        }  else if (color.equals("flowerpot")){
		    	        	g.drawImage(img.flowerpot,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("plant")) {
		    	        	g.drawImage(img.plant,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("flower")) {
		    	            g.drawImage(img.flower,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("armoire")) {
		    	            g.drawImage(img.armoire,posOfBlockX,posOfBlockY, 4*BLOC_SIZE -8, 2*BLOC_SIZE - 4,this);

		    	        } else if (color.equals("armoireCote")) {
		    	           	g.drawImage(img.armoireCote,posOfBlockX,posOfBlockY, BLOC_SIZE -2, 2*BLOC_SIZE - 4,this);

		    	        } else if (color.equals("biblio")) {
		    	           	g.drawImage(img.biblio,posOfBlockX,posOfBlockY, BLOC_SIZE -2, 2*BLOC_SIZE - 4,this);

		    	        } else if (color.equals("bureau")) {
		    	           	g.drawImage(img.bureau,posOfBlockX,posOfBlockY, 2*BLOC_SIZE -4, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("chaise0")) {
		    	           	g.drawImage(img.chaise0,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("chaise1")) {
		    	           	g.drawImage(img.chaise1,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("chaise2")) {
		    	           	g.drawImage(img.chaise2,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("chaise3")){
		    	           	g.drawImage(img.chaise3,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("cheminee")){
		    	           	g.drawImage(img.cheminee,posOfBlockX,posOfBlockY, 3*BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("commode")) {
		    	           	g.drawImage(img.commode,posOfBlockX,posOfBlockY, 2*BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if(color.equals("echec")) {
		    	           	g.drawImage(img.echec,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("lit1")) {
		    	          	g.drawImage(img.lit1,posOfBlockX,posOfBlockY, BLOC_SIZE -2, 2*BLOC_SIZE - 4,this);

		    	        } else if (color.equals("lit2")) {
		    	           	g.drawImage(img.lit2,posOfBlockX,posOfBlockY, BLOC_SIZE -2, 2*BLOC_SIZE - 4,this);

		    	        } else if (color.equals("stoneWall")){
		    	           	g.drawImage(img.stoneWall,posOfBlockX,posOfBlockY, BLOC_SIZE , BLOC_SIZE ,this);

		    	        } else if (color.equals("woodWall")) {
		    	           	g.drawImage(img.woodWall,posOfBlockX,posOfBlockY, BLOC_SIZE , BLOC_SIZE ,this);

		    	        } else if (color.equals("table")) {
		    	           	g.drawImage(img.table,posOfBlockX,posOfBlockY, 3*BLOC_SIZE -2, 3*BLOC_SIZE - 2,this);

		    	        } else if (color.equals("food")) {
		    	          	g.drawImage(img.steak,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("miroir")){
		    	           	g.drawImage(img.miroir,posOfBlockX,posOfBlockY, BLOC_SIZE -2, 2*BLOC_SIZE - 2,this);

		    	        } else if (color.equals("alcool")) {
		    	          	g.drawImage(img.vin,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        }
		    	        else if (color.equals("water")) {
		    	          	g.drawImage(img.getImage("water.png"),posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        }else if( color.equals("gold")) {
		    	          	g.drawImage(img.gold,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if( color.equals("canon")) {
		    	            g.drawImage(img.madara,posOfBlockX,posOfBlockY, BLOC_SIZE -2, 2*BLOC_SIZE - 4,this);

		    	        } else if (color.equals("potion")) {
		    	           	g.drawImage(img.potion,posOfBlockX,posOfBlockY, BLOC_SIZE -2, BLOC_SIZE - 2,this);

		    	        } else if (color.equals("fireball")) {
		    	            double facteur = 1.5;
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	            g.drawImage(img.fireball[((Directable)object).getDirection()], posOfBlockX, posOfBlockY, taille, taille, this);

		    	        } else if (color.equals("explosion")) {
		    	        	double facteur = 1.5;
		    	        	int taille = (int)(facteur*BLOC_SIZE);
		    	        	double decalage = (facteur-1)/2;
		    	        	posOfBlockX -= decalage * BLOC_SIZE;
		    	        	posOfBlockY -= decalage * BLOC_SIZE;
		    	        	g.drawImage(img.fireballExploded, posOfBlockX, posOfBlockY, taille, taille, this);

		    	        } else if (color.equals("breakableFull")) {
		    	        	double facteur = 1.2;
		    	        	int taille = (int)(facteur*BLOC_SIZE);
		    	        	double decalage = (facteur-1)/2;
		    	        	posOfBlockX -= decalage * BLOC_SIZE;
		    	        	posOfBlockY -= decalage * BLOC_SIZE;
		    	        	g.drawImage(img.rock[0], posOfBlockX, posOfBlockY, taille, taille, this);

		    	        } else if (color.equals("breakableMid")) {
		    	        	double facteur = 1.2;
		    	        	int taille = (int)(facteur*BLOC_SIZE);
		    	        	double decalage = (facteur-1)/2;
		    	        	posOfBlockX -= decalage * BLOC_SIZE;
		    	        	posOfBlockY -= decalage * BLOC_SIZE;
		    	        	g.drawImage(img.rock[1], posOfBlockX, posOfBlockY, taille, taille, this);

		    	        } else if (color.equals("breakableLow")) {
		    	            double facteur = 1.2;
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	            g.drawImage(img.rock[2], posOfBlockX, posOfBlockY, taille, taille, this);

		    	        } else if (color.equals("toilet")) {
		    	        	double facteur = 1.4;
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	        	g.drawImage(img.getImage("toilet.png"), posOfBlockX, posOfBlockY, taille, taille, this);

		    	        } else if(color.equals("hit")) {
		    	            double facteur = ((Hit)object).getSize();
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	            g.drawImage(img.getImage("hit"+((Hit)object).getDirection()+".png"), posOfBlockX, posOfBlockY, taille, taille, this);

		    	        } else if (color.equals("wolf")) {
		    	            double facteur = 1.8;
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	            g.drawImage(img.getPerso(((Mechant)object).getColor(), ((Mechant)object).getDirection()), posOfBlockX, posOfBlockY, taille, taille, this);


		    	        } else if (color.equals("rattata")) {
		    	        	double facteur = 1.3;
		    	        	int taille = (int)(facteur*BLOC_SIZE);
		    	        	double decalage = (facteur-1)/2;
		    	        	posOfBlockX -= decalage * BLOC_SIZE;
		    	        	posOfBlockY -= decalage * BLOC_SIZE;
		    	        	g.drawImage(img.getPerso(((Mechant)object).getColor(), ((Mechant)object).getDirection()), posOfBlockX, posOfBlockY, taille, taille, this);


		    	        } else if (color.equals("goblin")) {
		    	        	double facteur = 1.8;
		    	        	int taille = (int)(facteur*BLOC_SIZE);
		    	        	double decalage = (facteur-1)/2;
		    	        	posOfBlockX -= decalage * BLOC_SIZE;
		    	        	posOfBlockY -= decalage * BLOC_SIZE;
		    	        	g.drawImage(img.getPerso(((Mechant)object).getColor(), ((Mechant)object).getDirection()), posOfBlockX, posOfBlockY, taille, taille, this);


		    	        } else if (color.equals("skeletonWarrior")) {
		    	            double facteur = 1.8;
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	            g.drawImage(img.getPerso(((Mechant)object).getColor(), ((Mechant)object).getDirection()), posOfBlockX, posOfBlockY, taille, taille, this);


		    	        } else if (color.equals("orochimaru")) {
		    	            double facteur = 2.8;
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	            g.drawImage(img.getPerso(((Boss)object).getColor(), ((Boss)object).getDirection()), posOfBlockX, posOfBlockY, taille, taille, this);


		    	        } else if (color.equals("key")) {
		    	            g.drawImage(img.getImage("key.png"),posOfBlockX,posOfBlockY, BLOC_SIZE, BLOC_SIZE,this);

		    	        } else if (color.equals("apprenti")) {
		    	            double facteur = 1;
		    	            switch(((Player)object).getName()){
			    	            case "goku" : facteur = 2.3; break;
			    	            case "vegeta" : facteur = 2.3; break;
			    	            case "naruto" : facteur = 1.5; break;
			    	            case "sasuke" : facteur = 1.5; break;
			    	            case "dracaufeu" : facteur = 1.3; break;
			    	            case "pikachu" : facteur = 1.7; break;
			    	        }
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            int oldX = posOfBlockX;
		    	            int oldY = posOfBlockY;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	            g.drawImage(img.getPerso(((Player)object).getName()+"0", ((Player)object).getDirection(), ((Player)object).getSpeed() == 3), posOfBlockX, posOfBlockY, taille, taille, this);
	    	            	if (((Player)object).getInvincibility()) {
	    	            		facteur = 3;
	    	            		taille = (int)(facteur*BLOC_SIZE);
			    	            decalage = (facteur-1)/2;
			    	            posOfBlockX = oldX - (int)(decalage * BLOC_SIZE);
			    	            posOfBlockY = oldY - (int)(decalage * BLOC_SIZE);
	    	            		g.drawImage(img.getImage("shield.png"), posOfBlockX, posOfBlockY, taille, taille, this);
		    	            }
		    	        } else if (color.equals("confirme")) {
		    	            double facteur = 1;
		    	            switch(((Player)object).getName()){
		    	                case "goku" : facteur = 2.5; break;
		    	                case "vegeta" : facteur = 2.5; break;
		    	                case "naruto" : facteur = 1.5; break;
		    	                case "sasuke" : facteur = 1.5; break;
		    	                case "dracaufeu" : facteur = 1.3; break;
		    	                case "pikachu" : facteur = 1.7; break;
		    	            }
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            int oldX = posOfBlockX;
		    	            int oldY = posOfBlockY;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;

		    	            g.drawImage(img.getPerso(((Player)object).getName()+"1", ((Player)object).getDirection(), ((Player)object).getSpeed() == 3), posOfBlockX, posOfBlockY, taille, taille, this);

		    	            facteur = 4;
    	            		taille = (int)(facteur*BLOC_SIZE);
		    	            decalage = (facteur-1)/2;
		    	            posOfBlockX = oldX - (int)(decalage * BLOC_SIZE);
		    	            posOfBlockY = oldY - (int)(decalage*  BLOC_SIZE);
    	            		g.drawImage(img.getAura("aura1"), posOfBlockX, posOfBlockY, taille, taille, this);


		    	            if (((Player)object).getInvincibility()) {
	    	            		facteur = 3;
	    	            		taille = (int)(facteur*BLOC_SIZE);
			    	            decalage = (facteur-1)/2;
			    	            posOfBlockX = oldX - (int)(decalage * BLOC_SIZE);
			    	            posOfBlockY = oldY - (int)(decalage* BLOC_SIZE);
	    	            		g.drawImage(img.getImage("shield.png"), posOfBlockX, posOfBlockY, taille, taille, this);
		    	            }
		    	        } else if (color.equals("hokage")) {
		    	            double facteur = 1;
		    	            switch(((Player)object).getName()){
		    	                case "goku" : facteur = 2.5; break;
		    	                case "vegeta" : facteur = 2.5; break;
		    	                case "naruto" : facteur = 1.65; break;
		    	                case "sasuke" : facteur = 1.65; break;
		    	                case "dracaufeu" : facteur = 1.7; break;
		    	                case "pikachu" : facteur = 1.6; break;
		    	            }
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            int oldX = posOfBlockX;
		    	            int oldY = posOfBlockY;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	            g.drawImage(img.getPerso(((Player)object).getName()+"2", ((Player)object).getDirection(), ((Player)object).getSpeed() == 3), posOfBlockX, posOfBlockY, taille, taille, this);

		    	            facteur = 4;
    	            		taille = (int)(facteur*BLOC_SIZE);
		    	            decalage = (facteur-1)/2;
		    	            posOfBlockX = oldX - (int)(decalage * BLOC_SIZE);
		    	            posOfBlockY = oldY - (int)(decalage* BLOC_SIZE);
    	            		g.drawImage(img.getAura("aura2"), posOfBlockX, posOfBlockY, taille, taille, this);


		    	            if (((Player)object).getInvincibility()) {
	    	            		facteur = 3;
	    	            		taille = (int)(facteur*BLOC_SIZE);
			    	            decalage = (facteur-1)/2;
			    	            posOfBlockX = oldX - (int)(decalage * BLOC_SIZE);
			    	            posOfBlockY = oldY - (int)(decalage * BLOC_SIZE);
	    	            		g.drawImage(img.getImage("shield.png"), posOfBlockX, posOfBlockY, taille, taille, this);
		    	            }
		    	        } else if (color.equals("wave")) {
		    	            double facteur = ((Wave)object).getSize();
		    	            int taille = (int)(facteur*BLOC_SIZE);
		    	            double decalage = (facteur-1)/2;
		    	            posOfBlockX -= decalage * BLOC_SIZE;
		    	            posOfBlockY -= decalage * BLOC_SIZE;
		    	            g.drawImage(img.getImage("wave.png"), posOfBlockX, posOfBlockY, taille, taille, this);

		    	        } else if (color.equals("mission")) {
		    	        	double facteur = 2.4;
		    	        	int mission =((Mission)object).getMission();
		    	        	mission = mission+1;
		    	        	int taille = (int)(facteur*BLOC_SIZE);
		    	        	double decalage = (facteur-1)/2;
		    	        	posOfBlockX -= decalage * BLOC_SIZE;
		    	        	posOfBlockY -= decalage * BLOC_SIZE;
		    	        	g.drawImage(img.getImage("mission"+mission+".png"), posOfBlockX, posOfBlockY, taille, taille, this);

		    	            }
		    	    }

		    	    catch(Exception e){
                  System.out.println(e);
                  e.printStackTrace();
		    	        System.out.println("Erreur : objet");
		    	        System.out.println(color);
		    	    }
		    	}

		    	g.setColor(Color.BLACK);

		    	g.fillRect(0,(MAP_SIZE)*BLOC_SIZE,this.getWidth(), this.getHeight()-((MAP_SIZE)*BLOC_SIZE));
		    	g.drawImage(img.getImage("keyboard.png"), 3, (MAP_SIZE)*BLOC_SIZE+3, this.getWidth() - 6, this.getHeight()-((MAP_SIZE)*BLOC_SIZE)-6, this);


	    	}

	    	catch(Exception e) {
	    		System.out.println("Erreur paint moving");
	    	}

    	}

    	catch(Exception e) {
    		System.out.println("Erreur de drawing");
    		paint(g); //en cas d erreur, on reessaye de peindre
    	}
    }


    public void redraw() {
        this.repaint(); //permet de redessiner la map
    }


    //methode qui permet de dessiner le fond en fonction de la zone dans laquelle le personnage actif se trouve
    public void drawFondZone(Graphics g,int zone, int x,int y) {

    	int MAP_SIZE = plateauSize;

    	int xfenetre = -x+(MAP_SIZE/2)-2;
    	int yfenetre = -y+(MAP_SIZE/2)-2;

    	if(zone == 0) {
    		g.drawImage(img.grass, xfenetre*BLOC_SIZE, yfenetre*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, this);
    	}

    	else if(zone == 1) {
    		g.drawImage(img.getImage("forest.png"), xfenetre*BLOC_SIZE, yfenetre*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, this);
    	}

    	else if(zone == 2) {
    		g.drawImage(img.getImage("sand.png"), xfenetre*BLOC_SIZE, yfenetre*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, this);
    	}

    	 else if(zone == 3) {
     		g.drawImage(img.getImage("dark.png"), xfenetre*BLOC_SIZE, yfenetre*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, this);
     	}

    	 else if(zone == 4) {
     		g.drawImage(img.getImage("sand.png"), xfenetre*BLOC_SIZE, yfenetre*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, this);
     	}

    	 else if(zone == 5) {
     		g.drawImage(img.getImage("forest.png"), xfenetre*BLOC_SIZE, yfenetre*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, this);
     	}

    	 else if(zone == 6) {
    		 g.drawImage(img.getImage("purple.png"), xfenetre*BLOC_SIZE, yfenetre*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, this);

    	 }

    	 else if(zone == 7) {
    		 g.drawImage(img.getImage("parquet.png"), xfenetre*BLOC_SIZE, yfenetre*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, (MAP_SIZE+7)*BLOC_SIZE, this);
    	 }

    }

	//methode qui permet de d abords dessiner les objets du haut afin de donner une certaine perspective
	private void reorganizeObjects() {

		ArrayList<GameObject> list = new ArrayList<GameObject>();

		for (int i = 0; i <MAP_SIZE*2; i++) {

			for(GameObject go: objects) {

				if (go.getPosY() == i) {
					list.add(go);
				}
			}
		}
		objects = list;
		sendExplosionEnd();
	}


	//methode qui permet de placer les objets de type projectile a la fin de la liste d objet et donc de les dessiner en dernier
	private void sendExplosionEnd() {
		ArrayList<GameObject> save = new ArrayList<GameObject>();
		for (GameObject go:objects) {
			save.add(go);
		}
		objects = new ArrayList<GameObject>();
		for (GameObject go:save) {
			if ((!(go instanceof Wave) && !(go instanceof Explosion))) {
				objects.add(go);
			}
		}


		for (GameObject go:save) {
			if ((go instanceof Wave) || (go instanceof Explosion)) {
				objects.add(go);
			}
		}
	}


	public void callPaint(int increment) {
		this.increment = increment;
	}
}
