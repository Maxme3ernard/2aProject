import java.awt.*;

/**
 * Created by Maxime on 15/02/2018.
 */
public class Ver {
    private Color couleur;
    private String name;
    private int life;
    private int x;
    private int y;


    public Ver(int t, String n){ //t=0 ou 1, pour savoir quelle équipe
        if(t==0) couleur=Color.blue;
        else couleur=Color.red;
        name=n;
        life=200;
        //générateur aléatoire position
    }
    public void modifierVie(int hp){
        life+=hp;
    }
}
