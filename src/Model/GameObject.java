package Model;

public abstract class GameObject {
    protected int posX;
    protected int posY;
    protected String color;
    private Plateau plateau;

    //constructeur pour la sauvegarde
    public GameObject(int X, int Y, String color) {
    	this.posX = X;
        this.posY = Y;
        this.color = color;
    }

    //constructeur en jeu
    public GameObject(int X, int Y, String color, Plateau p) {
        this.posX = X;
        this.posY = Y;
        this.color = color;
        this.plateau =p;
    }


    //Accesseurs



    public Plateau getPlateau() {
    	return this.plateau;
    }

    public void setPlateau(Plateau p) {
    	this.plateau = p;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String c) {
    	this.color = c;
    }

    public boolean isAtPosition(int x, int y) {
        return this.posX == x && this.posY == y; //on regarde si l'objet est à la position (x,y)
    }

    public abstract boolean isObstacle();
}
