package Model;

public abstract class Block extends GameObject {
    public Block(int x, int y, String color, Plateau p) { //position en block ainsi que chiffre en int
        super(x, y, color, p);
    }

}
