/**
 * Created by Maxime on 15/02/2018.
 */
import javax.swing.*;
import java.awt.*;

public class Fenetre extends JFrame {
    int[][] terrain;
    int blockSize;
    boolean setup;


    //s: la taille des block
    public Fenetre(int s){
        super("Worm fighterZ");
        setup=false;
        blockSize=s;
        int[][] t={{0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,1,1,0,0,0},
                {0,0,0,0,0,1,1,1,1,0},
                {0,0,0,0,1,1,1,1,1,1},
                {0,0,0,1,1,1,1,1,1,1},
                {2,2,2,1,1,1,1,1,1,1},
                {2,2,2,1,1,1,1,1,1,1}};
        terrain=t;
        //setSize(1000,1000);
        setSize(t[0].length*blockSize,t.length*blockSize);
        System.out.println(this.getWidth());
        System.out.println(this.getWidth());
        setResizable(false);
        setLocation(10,10);
        repaint();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void paint(Graphics g){
        if(!setup){
            for(int i=0;i<terrain.length;i++){
                for(int j=0;j<terrain[0].length;j++){
                    if(terrain[i][j]==0) g.setColor(Color.cyan);
                    if(terrain[i][j]==1) g.setColor(Color.gray);
                    if(terrain[i][j]==2) g.setColor(Color.blue);
                    g.fillRect(blockSize*j,blockSize*i,blockSize,blockSize);
                }
            }
            setup=true;
        }
    }

}
