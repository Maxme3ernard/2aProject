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
        genererTerrain(100,50);
        /* int[][] t={{0,0,0,0,0,0,0,0,0,0}, //amené à disparaitre quand la génération du terrain sera automatique
                {0,0,0,0,0,1,1,0,0,0},
                {0,0,0,0,0,1,1,1,1,0},
                {0,0,0,0,1,1,1,1,1,1},
                {0,0,0,1,1,1,1,1,1,1},
                {2,2,2,1,1,1,1,1,1,1},
                {2,2,2,1,1,1,1,1,1,1}};
        terrain=t;*/
        blockSize=s;
        setSize((terrain[0].length+1)*blockSize,(terrain.length+2)*blockSize);
        setLocation(10,10);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanelFenetre f=new JPanelFenetre(blockSize, terrain);
        setContentPane(f);
        setVisible(true);
    }

    public void genererTerrain(int x, int y){
        int[][] t=new int[y][x];
        int p=(int) y/2;
        int r;
        for(int i=0;i<t[0].length;i++){
            r=(int)((Math.random()*4-Math.random()*4));
            System.out.println(r);
            if(p>y-10) p-=Math.abs(r);
            if(p<10) p+=Math.abs(r);
            else p+=r;
            if(p<t.length && p>0){
                System.out.println(p+"   "+i);
                t[p][i]=1;
                for(int j=p;j<t.length;j++){
                    t[j][i]=1;
                }
            }
        }
        terrain=t;
    }
}
