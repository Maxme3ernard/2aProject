/**
 * Created by Maxime on 15/02/2018.
 */
import javax.swing.*;
import java.awt.*;

public class Fenetre extends JFrame {
    int[][] terrain;
    int blockSize;

    public Fenetre(int s /*int[][] t*/){
        super("Worm FighterZ");
        int[][] t={{0,0,0,0,0,0,0,0,0,0}, //amené à disparaitre quand la génération du terrain sera automatique
                {0,0,0,0,0,1,1,0,0,0},
                {0,0,0,0,0,1,1,1,1,0},
                {0,0,0,0,1,1,1,1,1,1},
                {0,0,0,1,1,1,1,1,1,1},
                {2,2,2,1,1,1,1,1,1,1},
                {2,2,2,1,1,1,1,1,1,1}};
        terrain=t;
        blockSize=s;
        setSize((t[0].length+1)*blockSize,(t.length+2)*blockSize);
        setLocation(10,10);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanelFenetre f=new JPanelFenetre(blockSize, terrain);
        setContentPane(f);
        setVisible(true);
    }
}
