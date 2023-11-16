package View;

import java.awt.Color;

import java.awt.Graphics;

import javax.swing.JPanel;

import Model.Apprenti;
import Model.Confirme;
import Model.Player;
import java.util.*;



public class Status extends JPanel {

	private ArrayList<Player> players = new ArrayList<Player>();

	private Player active_player;

	private Images img = new Images(0);

	private double echelle = 1;

	//constantes
	private int BAR_LENGTH = 100;
	private int BAR_WIDTH = 20;
	private int AVATAR_SIZE = 90;


	//panel qui contient les personnages d une famille ainsi que leur vie, experience et position sur la minimap
    public Status() {
        this.setBackground(Color.LIGHT_GRAY);
    }

	public void paint(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img.getImage("manga"+active_player.getManga()+".png"), 0, 0, this.getWidth(), this.getHeight(), this);

		// on n affiche que les personnages de la famille du joueur actif
		ArrayList<Player> playersManga = new ArrayList<Player>();
		for (Player p : players) {
			if (p.getManga().equals(active_player.getManga())){
				playersManga.add(p);
			}
		}

		int n = playersManga.size();

		int j = playersManga.indexOf(active_player);
		int i =0;
		int gap = 250;

		//fond derriere l avatar
		g.setColor(Color.red);
		g.fillRect((int)(20*echelle), (int)((15+gap*j)*echelle), (int)((AVATAR_SIZE +20)*echelle), (int)((AVATAR_SIZE +20)*echelle));
		g.setColor(Color.white);
		g.fillRect((int)((20+5)*echelle), (int)(((15+5)+gap*j)*echelle), (int)((AVATAR_SIZE +10)*echelle), (int)((AVATAR_SIZE +10)*echelle));

		while(i<n) {

			Player perso;
			perso = playersManga.get(i);

			//dessin de l avatar et de son ensigne
			g.drawImage(img.getImage("logo"+perso.getManga()+".png"), (int)(30*echelle), (int)((25 + gap*i)*echelle), (int)(AVATAR_SIZE*echelle), (int)(AVATAR_SIZE*echelle), this);
			g.drawImage(img.getImage(this.getAvatarPlayer(perso)), (int)(30*echelle), (int)((25 + gap*i)*echelle), (int)(AVATAR_SIZE*echelle), (int)(AVATAR_SIZE*echelle), this);

			//barres de vie et experience
	        g.setColor(Color.RED);
	        g.fillRect((int)(30*echelle), (int)((AVATAR_SIZE + 55 + gap*i)*echelle), (int)(BAR_LENGTH*echelle), (int)(BAR_WIDTH*echelle)); //on cre en rouge un rectangle avec coin sup g en (0,200)

	        g.setColor(Color.GREEN);
	        int life = (int) Math.round((int)(BAR_LENGTH*echelle)*(((double)perso.getLife())/perso.getMaxLife()));
	        g.fillRect((int)(30*echelle), (int)((AVATAR_SIZE + 55 + gap*i)*echelle), life, (int)(BAR_WIDTH*echelle));

	        g.setColor(Color.BLACK);
	        g.fillRect((int)(30*echelle), (int)((AVATAR_SIZE + 90 + gap*i)*echelle), (int)(BAR_LENGTH*echelle), (int)(BAR_WIDTH*echelle)); //on cre en rouge un rectangle avec coin sup g en (0,200)

	        g.setColor(Color.ORANGE);
	        int xp = (int) Math.round((int)(BAR_LENGTH*echelle)*(((double)(perso.getXP())/perso.getEvolutionXP())));
	        g.fillRect((int)(30*echelle), (int)((AVATAR_SIZE + 90 + gap*i)*echelle), xp, (int)(BAR_WIDTH*echelle));

	        g.setColor(Color.black);

	        g.setColor(Color.BLACK);

	        //texte ecrit
	        g.drawString("Life", (int)(30*echelle), (int)((AVATAR_SIZE + 50 + gap*i)*echelle));

	        //affichage minimap
	    	drawPlateau(g,i,(int)(gap*echelle), playersManga.get(i));
	    	int[] coordplateau = perso.getPlateau().getPos();

	    	g.setColor(Color.white);
	    	g.drawString(writeArea(coordplateau, perso), (int)(150*echelle), (int)((110+gap*i)*echelle));
			i++;
		}

    }
	//methode qui permet d afficher la minimap et la position de la zone du joueur
    public void drawPlateau(Graphics gr, int j, int gap, Player p) {
    	gr.setColor(Color.white);
    	int uniteX = (int)(20*echelle);
    	int uniteY = (int)(20*echelle);
    	int i_max = 4;
    	int xgrid = (int)(150*echelle);
    	int ygrid = (int)(25*echelle);

    	for(int i=0; i<i_max; i++){
    		gr.drawLine(xgrid+uniteX*i, ygrid+gap*j, xgrid+uniteX*i, ygrid+uniteY*(i_max-1)+gap*j);
    		gr.drawLine(xgrid, ygrid+uniteY*i+gap*j, xgrid+uniteX*(i_max-1), ygrid+uniteY*i+gap*j);
    	}

    	int[] coordplateau = p.getPlateau().getPos();
    	int x = coordplateau[0];
    	int y = coordplateau[1];

    	if(x>1) {
    		x = 1;
    	}
    	if(x < -1) {
    		x = 0;
    	}
    	gr.fillRect(xgrid+(x+1)*uniteX, ygrid+(-y+1)*uniteY +gap*j ,uniteY ,uniteX);

    	gr.setColor(Color.black);
    }

    //methode qui permet de dire dans quelle zone se situe le joueur
    public String writeArea(int[] coordplateau, Player p) {
    	String area = null;
    	int x = coordplateau[0];
    	int y = coordplateau[1];
    	if(x==-1 && y<1) {
    		area = "Valle dsole";
    	}
    	else if(y==1 && x <1) {
    		area = "Fort vierge";
    	}
    	else if(x >= 1 && y>=0) {
    		area = "Zone de mission"+ (p.getMission()+1);
    	}
    	else if(y==-1 && x >= 0) {
    		area = "Terres sombres";
    	}
    	else if(x == 0 && y == 0) {
    		area = "Village";
    	}
    	else {
    		area = "Maison";
    	}
    	return area;

    }

    //methode pour repeindre le panel
    public void redraw() {
        this.repaint();
    }

    //accesseurs
	public void setPlayer(Player p) {
		this.players.add(p);
	}
	public void setActivePlayer(Player p) {
		this.active_player = p;
	}

	public void removePlayer(Player p) {
		this.players.remove(p);
	}

	public ArrayList<Player> getPlayers(){
		return this.players;
	}

	public int getAvatarsize() {
		return AVATAR_SIZE;
	}

	public void setDimensions(int width, int height) {
		echelle = (double)width/1000;
	}

	public void setEchelle(double echelle) {
		this.echelle = echelle;
	}

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



}
