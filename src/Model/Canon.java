package Model;




public class Canon extends GameObject {
	
	private Game game;
	
	private int direction;

	private double cooldownFireball = 0;
	
	public Canon(int x, int y, int direction, Plateau p, Game g) {
		super(x,y,"canon",p);
		this.direction = direction;
		
		setGame(g);
		g.getCanons().add(this);
	
	}
	
    public void setGame(Game g) {
    	this.game = g;
    }
    
    //creation d une fireball en verifiant que le temps entre deux fireball succssives est plus petit que le cooldown 
    public void tire() {
    	if ((game.getTime() - cooldownFireball) > Fireball.getCooldown()) {
    		try {
    			new Fireball(this, 20, 50, game.getTimeObject());
   				cooldownFireball = game.getTime();	
    		}	
    		catch(Exception e) {
    				
    		}
    	}
    }
    
    //Accesseurs
    
    
    public int getDirection() {
    	return this.direction;
    }
    
	
	public boolean isObstacle() {
		return true;
	}
}
    
    	
