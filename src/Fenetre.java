/**
 * Created by Maxime on 15/02/2018.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Fenetre extends JFrame{
    int[][] terrain;
    int blockSize;

    public Fenetre(int s /*int[][] t*/){
        super("Worm FighterZ");
        genererTerrain(2*190,2*100);
        /* int[][] t={{0,0,0,0,0,0,0,0,0,0}, //amené à disparaitre quand la génération du terrain sera automatique
                {0,0,0,0,0,1,1,0,0,0},
                {0,0,0,0,0,1,1,1,1,0},
                {0,0,0,0,1,1,1,1,1,1},
                {0,0,0,1,1,1,1,1,1,1},
                {2,2,2,1,1,1,1,1,1,1},
                {2,2,2,1,1,1,1,1,1,1}};
        terrain=t;*/
        blockSize=s;
        setSize((terrain[0].length)*blockSize,(terrain.length+2)*blockSize);
        //Pourquoi un +1 ?
        setLocation(10,10);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanelFenetre f = new JPanelFenetre(blockSize, terrain,this);
        setContentPane(f);
        setVisible(true);
    }

    public void genererTerrain(int x, int y){
        int[][] t=new int[y][x];
        int p=(int) y/2;
        int r=0; //modification denivelé
        double montagnes=2.2; //coefficient montagnes
        double plaine=2.5; //coefficient plaines
        int oldp=p;
        for(int i=0;i<t[0].length;i++){
            if((Math.random()*montagnes)>1&&p!=oldp){
                r=(p-oldp)+(int)(Math.random()*2-1);
            }
            else{
                r=(int)((Math.random()*plaine-Math.random()*plaine));
            }
            oldp=p;
            if(p>y-24) p-=Math.abs(r);
            if(p<24) p+=Math.abs(r);
            else p+=r;
            if(p<t.length && p>0){
                t[p][i]=1;
                for(int j=p;j<t.length;j++){
                    t[j][i]=1;
                }
            }
        }
        terrain=t;
    }

}

