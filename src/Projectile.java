/**
 * Created by Maxime on 15/02/2018.
 */
public abstract class Projectile {
    private int x;
    private int y;
    private int poids;
    private int degat;

    public abstract void deplacer (int xd, int yd);
}
