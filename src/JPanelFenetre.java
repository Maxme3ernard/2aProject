/**
 * Created by Maxime on 15/02/2018.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JPanelFenetre extends JPanel implements ActionListener,KeyListener {
    int[][] terrain;
    int blockSize;
    int hauteur;
    int largeur;
    protected Timer mt;
    protected int vitesseDep; //Cette variable correspond à l'intervalle entre deux entrées claviers pour déplacer
    //le Worms. On peut donc l'associer en quelque sorte à la vitesse de déplacement.
    protected long antiRepeatTime;
    protected Worms[] joueurs;
    protected boolean[] changementPrint; //Permet de rafraichir l'écran uniquement si il y a eu un changement

    //s: la taille des block
    public JPanelFenetre(int s,int[][] t,Fenetre fParent){ //J'ai mis la fenetre principale comme argument pour écouter ses évenements ici
        //Ca permet de regrouper dessin, gestion des évenements, etc ... Au même endroit
        blockSize=s;
        terrain=t;
        hauteur = (terrain.length)*blockSize;
        largeur = (terrain[0].length)*blockSize;
        setSize(largeur,hauteur);

        vitesseDep = 10;
        antiRepeatTime = 0;

        fParent.addKeyListener(this);
        mt = new Timer(20,this); //L'horloge du jeu est bassé sur un timer se déclenchant toutes les 20ms
        //Cela permet donc en théorie d'avoir du 50 fps, ce qui est largement suffisant pour notre jeu
        mt.start();

        changementPrint = new boolean[1];
        changementPrint[0] = true;
        repaint();

        joueurs = new Worms[1];
        joueurs[0] = new Worms(1,"Popaul",terrain,blockSize,changementPrint,500,100);
        joueurs[0].setMovingState(true);
    }

    public void paint(Graphics g){
        changementPrint[0] = false;
        for(int i=0;i<terrain.length;i++){
                    for(int j=0;j<terrain[0].length;j++){
                        if(terrain[i][j]==0) g.setColor(Color.cyan);
                        if(terrain[i][j]==1) g.setColor(Color.gray);
                        if(terrain[i][j]==2) g.setColor(Color.blue);
                        g.fillRect(blockSize*j,blockSize*i,blockSize,blockSize);
                    }
                }
            for(Worms wor: joueurs){
                wor.draw(g);
            }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==mt){
            for(Worms wor: joueurs){
                wor.applyForces();
            }
            if(changementPrint[0]==true){
                repaint();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        for(Worms wor: joueurs){
            if(wor.getMovingState() == true){
                if((System.currentTimeMillis()-antiRepeatTime) >= vitesseDep){
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        wor.deplacer(0);
                        antiRepeatTime = System.currentTimeMillis();
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                        wor.deplacer(1);
                        antiRepeatTime = System.currentTimeMillis();
                    }
                    else if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        if(wor.get_orientation()==0){
                            wor.set_vitesse_x(-5);
                        }
                        else{
                            wor.set_vitesse_x(5);
                        }
                        wor.set_vitesse_y(5);
                        antiRepeatTime = System.currentTimeMillis();
                    }
                }
            }

        }
    }

    // méthode exécutée à chaque fois qu’une touche est relâchée
    public void keyReleased(KeyEvent e) {
    }

    // méthode exécutée à chaque fois qu’une touche unicode est utilisée (donc pas CTRL, SHIFT ou ALT par exemple)
    public void keyTyped(KeyEvent e) {
    }

}
