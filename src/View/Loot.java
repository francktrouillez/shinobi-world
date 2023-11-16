package View;

import javax.swing.JPanel;

import Model.Game;
import Model.Player;
import Model.Potion;
import Model.Water;
import Model.Alcool;
import Model.Apprenti;
import Model.Confirme;
import Model.Food;

import java.awt.Color;
import java.awt.Graphics;

public class Loot extends JPanel {

    private Game g;
	private Player active_player;
	
	private Images img = new Images(1);

	private int height;
	private int width;
	
	private double echelle = 1;
	
	//constantess
	private int AVATAR_SIZE = 150;
	private int BAR_LENGTH = 200; 
	private int BAR_WIDTH = 20;
	
	//panel qui montre les stats du joueur actif en permanence ainsi que son inventaire
    public Loot(Game g) {  
        
        this.g = g;
        changeCollectionActivePlayer();   
    }
    
    //methode qui donne la taille du panel en fonction de la taille de l ecran du pc
    public void setDimensions(int width, int height) {
    	setEchelle((double)width/1000);
    }
    
    //methode qui permet d adapter en permanence ce panel en fonction du joueur actif
    public void changeCollectionActivePlayer() {
    	active_player = g.getActivePlayer();
    	
    }
    //methode pour peindre le panel
    public void paint(Graphics gr) {
    	
    	super.paintComponent(gr);
    	
    	Player ap = g.getActivePlayer();
    	
    	changeFond(gr,ap);
    	
    	//dessin de l avatar et de son fond de manga correspondant
    	gr.setColor(Color.red);
		gr.fillRect((int)(20*echelle), (int)((20)*echelle), (int)((AVATAR_SIZE +20)*echelle), (int)((AVATAR_SIZE +20)*echelle));
		gr.setColor(Color.white);
		gr.fillRect((int)((20+5)*echelle), (int)(((20+5))*echelle), (int)((AVATAR_SIZE +10)*echelle), (int)((AVATAR_SIZE +10)*echelle));
    	gr.drawImage(img.getImage("logo"+ap.getManga()+".png"), (int)(30*echelle), (int)((30)*echelle), (int)(AVATAR_SIZE*echelle), (int)(AVATAR_SIZE*echelle), this); 
		gr.drawImage(img.getImage(this.getAvatarPlayer(ap)), (int)(30*echelle), (int)(30*echelle), (int)(AVATAR_SIZE*echelle),(int)( AVATAR_SIZE*echelle), this);
    	
		//postionnement des barres de statistique du joueur et espacement entre elles
		int posXBarre = (int)(200*echelle);
        int posYBarre = (int)(30*echelle);
        int spaceYBarre = (int)(50*echelle);
        
        gr.setColor(Color.RED);
        for(int i = 0; i<7;i++) {
        gr.fillRect(posXBarre, posYBarre+i*spaceYBarre , (int)(BAR_LENGTH*echelle), (int)(BAR_WIDTH*echelle)); 
        }

        gr.setColor(Color.GREEN);
        int life = (int) Math.round((int)(BAR_LENGTH*echelle)*(((double)ap.getLife())/ap.getMaxLife()));
        int energie = (int) Math.round((int)(BAR_LENGTH*echelle)*ap.getRealEnergy()/Player.MAX_ENERGY); 
        int alcoolemie = (int) Math.round((int)(BAR_LENGTH*echelle)*(1.0 -ap.getAlcoolemie()));
        int faim = (int) Math.round((int)(BAR_LENGTH*echelle)*(1.0 -((double)ap.getFaim())/Player.MAX_FAIM));
        int soif = (int) Math.round((int)(BAR_LENGTH*echelle)*(1.0 -((double)ap.getSoif())/Player.MAX_SOIF));
        int pipi = (int) Math.round((int)(BAR_LENGTH*echelle)*(1.0 -((double)ap.getVessie())/Player.MAX_VESSIE));
        int xp = (int) Math.round((int)(BAR_LENGTH*echelle)*(((double)ap.getXP())/ap.getEvolutionXP()));
        
        gr.fillRect(posXBarre, posYBarre+0*spaceYBarre , life, (int)(BAR_WIDTH*echelle));
        gr.fillRect(posXBarre, posYBarre+2*spaceYBarre, energie, (int)(BAR_WIDTH*echelle));
        gr.fillRect(posXBarre, posYBarre+3*spaceYBarre, alcoolemie, (int)(BAR_WIDTH*echelle));
        gr.fillRect(posXBarre, posYBarre+4*spaceYBarre, soif,(int)(BAR_WIDTH*echelle));
        gr.fillRect(posXBarre, posYBarre+5*spaceYBarre, faim, (int)(BAR_WIDTH*echelle));
        gr.fillRect(posXBarre, posYBarre+6*spaceYBarre, pipi, (int)(BAR_WIDTH*echelle));
        gr.setColor(Color.ORANGE);
        gr.fillRect(posXBarre, posYBarre+1*spaceYBarre, xp, (int)(BAR_WIDTH*echelle));
    	gr.setColor(Color.BLACK);
    	
    	//information supplementaire sur le joueur actif : degat, mission en cours et argent
    	int taille = (int)(40*echelle); 
    	int posXInfo = (int)(30*echelle);
    	int posYInfo = (int)(200*echelle);
    	int spaceYInfo = (int)(60*echelle);
    	gr.drawImage(img.missionletter,posXInfo, posYInfo+0*spaceYInfo, taille, taille,this);
    	gr.drawImage(img.gold,posXInfo,posYInfo+1*spaceYInfo, taille, taille,this);
    	gr.drawImage(img.fireball[0],posXInfo,posYInfo+2*spaceYInfo, taille, taille,this);
    	
    	
    	int posXImage = (int)(80*echelle);
    	
    	//information ecrites 
    	gr.setColor(Color.white);
    	if(g.getActivePlayer().haveMission()) {
    		gr.drawString("Mission: "+ (ap.getMission()+1), posXImage, posYInfo + 0*spaceYInfo + taille/2);
    	}
    	else {
    		gr.drawString("No Mission", posXImage, posYInfo + 0*spaceYInfo + taille/2);
    	}
    	gr.drawString(""+ap.getGold(), posXImage, posYInfo + 1*spaceYInfo + taille/2);
    	gr.drawString(""+ap.getDamage(), posXImage, posYInfo + 2*spaceYInfo + taille/2);
    	
        gr.drawString("Life", posXBarre, posYBarre+0*spaceYBarre); 
        gr.drawString("XP", posXBarre, posYBarre+1*spaceYBarre); 
        gr.drawString("Energy", posXBarre, posYBarre+2*spaceYBarre);
        gr.drawString("Alcool:", posXBarre, posYBarre+3*spaceYBarre);
        gr.drawString("Drink:", posXBarre, posYBarre+4*spaceYBarre);
        gr.drawString("Food", posXBarre, posYBarre+5*spaceYBarre);
        gr.drawString("Vessie", posXBarre, posYBarre+6*spaceYBarre);
        gr.drawString("Max Life: "+ap.getMaxLife(), posXBarre+5, posYBarre+(int)((2*BAR_WIDTH/3)*echelle));
        
        
        //creation de l inventaire avec les places disponibles et indisponibles ainsi que les consommables possedes
    	gr.setColor(Color.gray);
    	
    	int tailleCase = (int)(40*echelle);
    	int spaceCase = (int)(50*echelle);
    	int posXCase = (int)(20*echelle);
    	int n = ap.getLoot().size();
    	
    	if(ap.getLoot().size()!= 0) {
    		for(int j =0; j< n;j++) {


    			if(ap.getLoot().get(j) instanceof Alcool) {
    	    		gr.drawImage(img.vin, posXCase+spaceCase*j, posYBarre+8*spaceYBarre, tailleCase, tailleCase, this);
    	    	}
    	    	else if(ap.getLoot().get(j) instanceof Food) {
    	    		gr.drawImage(img.steak, posXCase+spaceCase*j, posYBarre+8*spaceYBarre, tailleCase, tailleCase, this);
    	    	}
    	    	else if(ap.getLoot().get(j) instanceof Potion) {
    	    		gr.drawImage(img.potion, posXCase+spaceCase*j, posYBarre+8*spaceYBarre, tailleCase, tailleCase, this);
    	    	}
    	    	else if(ap.getLoot().get(j) instanceof Water) {
    	    		gr.drawImage(img.getImage("water.png"), posXCase+spaceCase*j, posYBarre+8*spaceYBarre, tailleCase, tailleCase, this);
    	    	}
    	    }		
    	}
    	for(int i = n; i<ap.getinventaireMax();i++) {
    		gr.setColor(Color.gray);
    		gr.fillRect(posXCase+spaceCase*i, posYBarre+8*spaceYBarre, tailleCase, tailleCase);
    	}
    		
    	
    	gr.setColor(Color.red);
    	for(int i = ap.getinventaireMax(); i < 9;i++) {
    		gr.fillRect(posXCase+spaceCase*i, posYBarre+8*spaceYBarre, tailleCase, tailleCase);
    	}

    	}
    

	public Player getPlayer(){
		return active_player;
	}
	public void setPlayer(Player player) {
		this.active_player = player;
	}
	
	//methode qui permet de dessiner l avatar du joueur actif dans son stade d evolution actuel
	public String getAvatarPlayer(Player p) {
		String res = "avatar";
		String name = p.getName();
		String grade = null;
		if(p instanceof Apprenti) {
			grade = "0";
		}
		else if (p instanceof Confirme) {
			grade = "1";
		}
		else {
			grade = "2";
		}
		res += name+grade+".png";
		return res;
	}
	
	//methode qui dessine le fond en fonction de la zone dans laquelle se trouve le joueur actif
	public void changeFond(Graphics gr, Player p) {
		int zone = p.getPlateau().getZone();
		
		gr.setColor(Color.black);
		this.width = getWidth();
		this.height = getHeight();

		if(zone == 0) {
			gr.drawImage(img.villageBackground, 0, 0,this.width, this.height, this);
		}
		
		else if(zone == 1) {
			gr.drawImage(img.jungleBackground, 0, 0,this.width, this.height, this);
		}
		
		else if(zone == 2) {
			gr.drawImage(img.valleeBackground, 0, 0,this.width, this.height, this);
		}
		
		else if(zone == 3) {
			gr.drawImage(img.terresSombresBackground, 0, 0,this.width, this.height, this);
		}
		
		else if(zone == 4) {
			gr.drawImage(img.mission1Background, 0, 0,this.width, this.height, this);
		}
		
		else if(zone == 5) {
			gr.drawImage(img.mission2Background, 0, 0,this.width, this.height, this);
		}
		else if(zone == 6) {
			gr.drawImage(img.getImage("backgroundMission3.png"), 0, 0,this.width, this.height, this);
			
		}
		else if (zone == 7) {
			gr.drawImage(img.getImage("home.png"), 0, 0,this.width, this.height, this);
			
		}
		

		
	}
	//methode qui donne l echelle
	public void setEchelle(double echelle) {
		this.echelle = echelle/1.35;
	}


}