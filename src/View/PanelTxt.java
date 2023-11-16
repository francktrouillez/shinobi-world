package View;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;




public class PanelTxt extends JPanel{
	private ArrayList<String> interactionList = new ArrayList<String>();
	private Images img = new Images(0);
	
	public static Object keyPrint = new Object();
	
	
	public PanelTxt() {
        
	}
	
    public void paint(Graphics gr) {
    	super.paintComponent(gr);
    	
    	//background du panel
    	gr.drawImage(img.chatBackground, 0, 0,this.getWidth(),this.getHeight(), this);
    	
    	int i =0;
    	int gap = 20; //espacement entre les lignes
    	synchronized(keyPrint) {
    		for(String s:interactionList) {
        		gr.drawString(s, 70, 50+gap*i );
        		i+=1;
        	}
    	}
    	
    	
    }
   
    
    //methode pour faire afficher les messages du jeu dans le panel et passer a la ligne apres chaque message
	public void print(String s) {
		synchronized(keyPrint) {
			while(this.interactionList.size() >= 8) { //permet de ne faire afficher que 12 messages et de supprimer le plus ancien quand le nombre de messages devient superieur
				this.interactionList.remove(0); 
			}
			this.interactionList.add(s);
			
		}
		
	}
}
