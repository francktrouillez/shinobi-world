package View;

import Model.Game;
import Model.Player;// on va utiliser des joueurs

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Window extends JFrame {

	private JPanel groupPanel = new JPanel(new GridBagLayout()); //panel conteneur des panels de jeu

	private JPanel allPanel = new JPanel(); //panel global

    private JPanel activePanel;

	private ArrayList<String> listPanel = new ArrayList<String>();

	//panels de jeu
    private Status status;
    private PanelTxt txt;
    private Loot loot;
    private MapFenetre map;
    //private MapLabyrinthe map;

    //panel de pause ou d accueil ou de sauvegarde
    private MenuPause menuPause = new MenuPause(this);
    private MenuStart menuStart = new MenuStart(this);
    private MenuSave menuSave = new MenuSave(this);

    private Game game;

    private CardLayout cl;
    //private Mouse mouse;

    private int height;
    private int width;

    private double echelle;


    public Window(String title, Game g) {
    	super(title);

    	this.game = g;
    	game.setWindow(this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.getContentPane().setBackground(Color.YELLOW);

        Rectangle dimension = getGraphicsConfiguration().getBounds();

        width = (int) dimension.getWidth();
        height = (1080*width)/1920;
        this.setBounds(0, 0, width, height);

        int widthBase = 1000;//utilis lors des tests (pour que le jeu s'adapte  chaque ordi)

        echelle = (double)(width)/widthBase;

        //layout manager du panel global permet de passer du jeu aux ecrans d accueil,de pause ou de sauvegarde
        cl = new CardLayout();
        allPanel.setLayout(cl);

        //intialisation des panels de jeu
        map = new MapFenetre(g, (int)(0.4*width));

        loot = new Loot(g);
        loot.setDimensions(width,height);
        loot.setEchelle(echelle);

        status = new Status();
        status.setDimensions(width, height);
        status.setEchelle(echelle);

        txt = new PanelTxt();

        GridBagConstraints c = new GridBagConstraints();

        c.ipadx = 0;
        c.ipady = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.weightx = 0.4;
        c.weighty = 1;
        groupPanel.add(map,c);

        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.weightx = 0.25;
        c.weighty = 1;
        groupPanel.add(status, c);

        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.35;
        c.weighty = 0.3;
        groupPanel.add(txt,c);

        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.35;
        c.weighty = 0.7;
        groupPanel.add(loot,c);


        //ajouts des panel secondaires au panel global
        allPanel.add(groupPanel);
        listPanel.add("groupPanel");

        allPanel.add(menuPause);
        listPanel.add("menuPause");

        allPanel.add(menuStart);
        listPanel.add("menuStart");

        allPanel.add(menuSave);
        listPanel.add("menuSave");

        //ajout du panel global au conteneur de la feneter
        this.getContentPane().add(this.allPanel);

        activePanel = groupPanel;
        changePanel("menuStart");// on initialise le panel a celui d accueil
        this.setVisible(true);
    }


    //permet de remettre a dimensions les panel et les update
    public void update() {
    	Rectangle dimension = getGraphicsConfiguration().getBounds();

        width = (int) dimension.getWidth();
        height = (int)(((1080*(double)width)/1920));

        status.setDimensions(width,height);
        loot.setDimensions(width,height);
        updatePanel();

    }

    //update le panel global et donc tout les panels qui s y trouvent a l interieur
    public void updatePanel() {
    	this.allPanel.revalidate();
        this.allPanel.repaint();
    }

	//permet d ecrire dans le pane texte
	public void print(String s) {
		this.txt.print(s);
	}

	public void callPaint(int i) {
		map.callPaint(i);
	}

	//retourne la taille en bloc de la mapfenetre
	public int getSizeOfBlock() {
		return map.BLOC_SIZE;
	}

	//permet de changer la carte du cardLayout pour afficher le panel voulu et le rendre actif
	public void changePanel(String name) {
		int place = -1;
		for (String a:listPanel) {
			place++;

			if (a.equals(name)) {
				break;
			}
		}

		cl.first(allPanel);
		for (int i = 0; i<place;i++) {
			cl.next(allPanel);
		}
		activePanel.setFocusable(false);
		switch(place) {
		case 0 : activePanel = groupPanel;break;
		case 1 : activePanel = menuPause; break;
		case 2 : activePanel = menuStart; break;
		case 3 : activePanel = menuSave; break;
		}
		activePanel.setFocusable(true);
		activePanel.requestFocusInWindow();
		this.revalidate();
	}

	//methode qui permet de donner un nom a un panel
	public String getNameActivePanel() {
		String res = "";
		if (activePanel == groupPanel) {
			res = "groupPanel";
		}
		else if (activePanel == menuStart) {
			res = "menuStart";
		}
		else if (activePanel == menuSave) {
			res = "menuSave";
		}
		else if (activePanel == menuPause) {
			res = "menuPause";
		}
		return res;
	}

	//accesseurs

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Game getGame() {
		return game;
	}


    public void setKeyListener(KeyListener keyboard) {
    	groupPanel.addKeyListener(keyboard);
    }


	public void setPlayer(Player p) {
		status.setPlayer(p);
	}

	public void setActivePlayer(Player p) {
		status.setActivePlayer(p);
	}

	public void removePlayer(Player p) {
		status.removePlayer(p);
	}

	public ArrayList<Player> getPlayers() {
		return status.getPlayers();
	}
}
