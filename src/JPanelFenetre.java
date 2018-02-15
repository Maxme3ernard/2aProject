/**
 * Created by Maxime on 15/02/2018.
 */
import javax.swing.*;
import java.awt.*;

public class JPanelFenetre extends JPanel {
    int[][] terrain;
    int blockSize;
    boolean setup;


    //s: la taille des block
    public JPanelFenetre(int s,int[][] t){
        setup=false;
        blockSize=s;
        terrain=t;
        repaint();
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
