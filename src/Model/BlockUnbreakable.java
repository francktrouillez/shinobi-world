package Model;

public class BlockUnbreakable extends Block {

	private boolean onBorder = false;
	
	
	
    public BlockUnbreakable(int X, int Y, Plateau p, boolean onBorder,String color) {
        super(X, Y, graphBlock(p.getZone(),onBorder,color),p); 
        
        this.onBorder = onBorder; //indique si le bloc est un bloc au bord de la map ou un bloc de decor interne
    }
   
    
    //permet de dessiner les blocs selon la zone et le fait qu ils soient aux bords ou non
    private static String graphBlock(int zone,boolean onBorder, String color) {
    	String res = null; //dessin du bloc qui varie en fonction de la zone dans lequel il est mis
    	if(onBorder) {
        	if(zone == 0) {
        		res = "hubTree";
        	}
        	else if(zone == 1 || zone == 5) {
        		res = "forestTree";
        	}
        	else if(zone == 2 || zone == 4) {
        		res = "valleeTree";
        	}
        	else if(zone == 3 || zone == 6) {
        		res = "darkTree";
        	}
        	else if(zone == 7) {
        		res = "stoneWall";
        	}
        	else {
        		res = "unbreakable";
        	}
    	}
    	else {
    		res = color; //si c est un bloc interne, alors la decoration est donnee par le color du constructeur
    	}

    	return res;
    }
    public boolean isObstacle() { 
        return true;
    }
    
    public int isOnBorder() {
    	int res = 0;
    	if (onBorder) {
    		res = 1;
    	}
    	return res;
    }
    
}