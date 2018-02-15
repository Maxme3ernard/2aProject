/**
 * Created by Maxime on 15/02/2018.
 */
public abstract class Projectile {
    protected int x;
    protected int y;
    protected int poids;
    protected int degat;

    public abstract void deplacer (int xd, int yd);
}
