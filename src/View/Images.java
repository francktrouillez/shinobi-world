package View;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import View.Thread.AuraThread;
import View.Thread.PersoViewThread;

public class Images {
	//Contient toutes les images

	//Non orientable

	//listes des adresses images et des images correspondantes
	private ArrayList<String> paths = new ArrayList<String>();
	private ArrayList<Image> images = new ArrayList<Image>();

	//images objets
	public final Image coffre = getImage("coffre.png");
	public final Image missionletter = getImage("mission.png");
	public final Image vin = getImage("Wine.png");
	public final Image steak = getImage("Steak.png");
	public final Image potion = getImage("potion.png");
	public final Image gold = getImage("coins.png");

	public final Image madara = getImage("madara.png");

	//differents background propres aux maps
	public final Image jungleBackground = getImage("backgroundJungle.jpg");
	public final Image villageBackground = getImage("villageBackground.png");
	public final Image valleeBackground = getImage("backgroundValleedesolee.png");
	public final Image terresSombresBackground = getImage("backgroundTerresSombres.jpg");
	public final Image mission1Background = getImage("backgroundMission1.jpg");
	public final Image mission2Background = getImage("backgroundMission2.png");
	public final Image chatBackground = getImage("cadrechat.png");
	public final Image parquet = getImage("parquet.png");

	//decors exterieurs
	public final Image arbre = getImage("Tree_top.png");
	public final Image[] rock = {getImage("rock/full.png"),getImage("rock/half.png"),getImage("rock/low.png")};
	public final Image grass = getImage("Grass.png");
	public final Image bush1 = getImage("bush1.png");
	public final Image bush2 = getImage("bush2.png");
	public final Image cactus1 = getImage("cactus1.png");
	public final Image cactus2 = getImage("cactus2.png");
	public final Image cactus3 = getImage("cactus3.png");
	public final Image crate = getImage("Crate.png");
	public final Image grass1 = getImage("grass1.png");
	public final Image grass2 = getImage("grass2.png");
	public final Image sign = getImage("Sign.png");
	public final Image skeleton = getImage("Skeleton.png");
	public final Image stone = getImage("Stone.png");
	public final Image fountain = getImage("fountain.png");
	public final Image flower = getImage("flower2.png");
	public final Image plant = getImage("plant1.png");
	public final Image flowerpot = getImage("flower1.png");

	//decors maison
	public final Image stoneWall = getImage("stonewall.png");
	public final Image woodWall = getImage("woodwall.png");
	public final Image table = getImage("table.png");
	public final Image miroir = getImage("miroir.png");
	public final Image lit1 = getImage("lit1.png");
	public final Image lit2 = getImage("lit2.png");
	public final Image echec = getImage("echec.png");
	public final Image commode = getImage("commode.png");
	public final Image cheminee = getImage("cheminee.png");
	public final Image chaise0 = getImage("chaise0.png");
	public final Image chaise1 = getImage("chaise1.png");
	public final Image chaise2 = getImage("chaise2.png");
	public final Image chaise3 = getImage("chaise3.png");
	public final Image bureau = getImage("bureau.png");
	public final Image biblio = getImage("biblio.png");
	public final Image armoire = getImage("armoire.png");
	public final Image armoireCote = getImage("armoirecote.png");

	//decors batiments
	public final Image armurerie = getImage("armurerie.png");
	public final Image maison = getImage("maison.png");

	//Orientable

	public final Image[] fireball = {getImage("fireball/east.png"),getImage("fireball/north.png"),getImage("fireball/west.png"),getImage("fireball/south.png")};
	public final Image fireballExploded = getImage("fireball/explode.png");

	//Convention

	private final static int EAST = 0;
    private final static int NORTH = 1;
    private final static int WEST = 2;
    private final static int SOUTH = 3;

	//listes avec les noms des personnages, leur sprite sheet correspondante et les dimensions des images  aller chercher dans la sprite sheet
	private static ArrayList<String> nameList = new ArrayList<String>();
    private static ArrayList<BufferedImage> spriteList = new ArrayList<BufferedImage>();
    private static ArrayList<Double> widthList = new ArrayList<Double>();
    private static ArrayList<Double> heigthList = new ArrayList<Double>();

    //Thread necessaire pour passer d une image a une autre dans la sprite sheet
    private static PersoViewThread pvt;
    private static AuraThread at;

    //ajoute des sprites sheet, des dimensions et des noms pour les differents personnages
	public Images(int classe) {
		if (classe == 0) {
			loadImage("wolf");
			widthList.add(57.0);
			heigthList.add(50.0);

			loadImage("goblin");
			widthList.add(64.0);
			heigthList.add(64.0);

			loadImage("skeletonWarrior");
			widthList.add(59.0);
			heigthList.add(66.75);

			loadImage("rattata");
			widthList.add(29.6);
			heigthList.add(24.0);

			loadImage("orochimaru");
			widthList.add(60.0);
			heigthList.add(60.0);

			loadImage("goku0");
			widthList.add(36.0);
			heigthList.add(50.0);
			loadImage("goku1");
			widthList.add(36.0);
			heigthList.add(50.0);
			loadImage("goku2");
			widthList.add(36.0);
			heigthList.add(50.0);

			loadImage("vegeta0");
			widthList.add(36.0);
			heigthList.add(50.0);
			loadImage("vegeta1");
			widthList.add(36.0);
			heigthList.add(50.0);
			loadImage("vegeta2");
			widthList.add(36.0);
			heigthList.add(50.0);


			loadImage("naruto0");
			widthList.add(40.0);
			heigthList.add(50.0);
			loadImage("naruto1");
			widthList.add(40.0);
			heigthList.add(50.0);
			loadImage("naruto2");
			widthList.add(30.0);
			heigthList.add(61.75);

			loadImage("sasuke0");
			widthList.add(18.8);
			heigthList.add(35.0);
			loadImage("sasuke1");
			widthList.add(18.8);
			heigthList.add(35.0);
			loadImage("sasuke2");
			widthList.add(37.6);
			heigthList.add(60.0);

			loadImage("dracaufeu0");
			widthList.add(23.0);
			heigthList.add(25.0);
			loadImage("dracaufeu1");
			widthList.add(23.0);
			heigthList.add(25.0);
			loadImage("dracaufeu2");
			widthList.add(30.0);
			heigthList.add(30.0);

			loadImage("pikachu0");
			widthList.add(30.0);
			heigthList.add(30.0);
			loadImage("pikachu1");
			widthList.add(30.0);
			heigthList.add(30.0);
			loadImage("pikachu2");
			widthList.add(30.0);
			heigthList.add(30.0);

			loadImage("aura1");
			widthList.add(87.0);
			heigthList.add(140.0);

			loadImage("aura2");
			widthList.add(87.0);
			heigthList.add(140.0);



			pvt = new PersoViewThread(); //lancement du Thread pour les animations personnage
			Thread t1 = new Thread(pvt);
			t1.start();

			at = new AuraThread();
			Thread t2 = new Thread(at);
			t2.start();

		}

		}

		//methode qui permet d aller chercher la sprite sheet correspondante dans le dossier resources et de l'ajouter  la liste des sprites
		public void loadImage(String path) {

      nameList.add(path);
      path = "images/"+path+".png";
      BufferedImage bfi;
			try {
				bfi = ImageIO.read(new File(getClass().getClassLoader().getResource(path).getFile()));
        spriteList.add(bfi);
			} catch(IOException e) {}
      catch(Exception e) {
        try {

          bfi = ImageIO.read(new File("resources/"+path));
          spriteList.add(bfi);
          System.out.println(bfi.toString());
        } catch(IOException e2) {}
      }
      System.out.println(spriteList.size());
		}

		public Image getAura(String name) {
			Image res = null;
			int place = nameList.indexOf(name);
			BufferedImage bfi = spriteList.get(place);
			int colomn = at.getNumberOf(name);
			double width = widthList.get(place);
			double heigth = heigthList.get(place);

			res = bfi.getSubimage((int)(colomn*width), 0,(int)(width),(int)(heigth)); //permet de rechercher l image correspondante dans la sprite sheet
			return res;
		}

		//methode pour retourner l image d un personnage a partir de sa sprite sheet
		public Image getPerso(String name, int direction) {

			Image res = null;
			int place = nameList.indexOf(name);
			BufferedImage bfi = spriteList.get(place); //on cherche la sprite sheet correspondante au personnage
			int colomn = 0;
			int line = 0;

			//selon la direction du personnage, on doit aller chercher la ligne correspondante de la sprite sheet
			switch(direction) {
			case NORTH : line = 3; break;
			case SOUTH : line = 0; break;
			case EAST : line = 2; break;
			case WEST : line = 1; break;
			}

			colomn = pvt.getNumberOf(name); //permet de connaitre a quel image dans la direction le personnage se situe
			//le thread pvt permet de faire passer d une image a l autre en changeant la colomne

			//dimensions de la case image
			double width = widthList.get(place);
			double heigth = heigthList.get(place);

			res = bfi.getSubimage((int)(colomn*width), (int)(line*heigth),(int)(width),(int)(heigth)); //permet de rechercher l image correspondante dans la sprite sheet

			return res;
		}

		//meme fonction que prcdement sauf qu'elle permet au sprite de savoir si le perso sprint
		public Image getPerso(String name, int direction, boolean sprinting) {
			pvt.setSpeed(name,sprinting);
			return getPerso(name,direction);
		}

		//methode pour obtenir les images autres que les sprites
		public Image getImage(String path) {
			Image res;

			int indexPath;

			try {
				indexPath = paths.indexOf(path);
			}
			catch(Exception e) {
				indexPath = -1;
			}

			if (indexPath == -1 ) { //s il ne trouve pas le path dans la liste, alors il le rajoute et met l image correspondante dans la liste d images
				paths.add(path);
				res = getSourceImage(path);
				images.add(getSourceImage(path));
			}
			else {
				int index = paths.indexOf(path);
				res = images.get(index);
			}
			return res;
		}

		//methode qui va chercher l image correspondante au path dans le fichier resources
		public Image getSourceImage(String path) {
			path = "images/"+path;
			Image res = null;
			try {
				res = ImageIO.read(new File(getClass().getClassLoader().getResource(path).getFile()));
			} catch(IOException e) {}
      catch (Exception e) {
        try {
				  res = ImageIO.read(new File("resources/"+path));
        } catch(IOException e2) {}
      }

			return res;
		}
}
