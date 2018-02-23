import org.newdawn.slick.SlickException;

import java.awt.*;
import java.util.ArrayList;

public class Worms {
    protected org.newdawn.slick.Color couleur;
    protected String name;
    protected int life;
    protected int x; //Les coordonnées x,y correspondent au coin en bas à gauche du Worms
    protected int y;
    protected int terrain[][];
    protected int blockSize;
    protected boolean isMoving; //Servira à savoir si un Worms est dans sa phase de déplacement
    protected boolean[] changementPrint;
    protected int orientation;
    protected double vitesse_x;
    protected double vitesse_y;
    protected double acceleration_x;
    protected double acceleration_y;
    //public double speed_force_x; //Résultante des vitesses amenées par les forces exterieurs sur x
    //public double speed_force_y;//Résultante des vitesses amenées par les forces exterieurs sur y
    protected final static double masse = 10;
    protected final static double g = 9.81;
    protected final static int hitBoxHauteur = 40;
    protected final static int hitBoxLargeur = 20;
    protected final static int blocIntraversables[] = {1};
    protected final static double facteurEchelle = 0.05;
    protected final static int climbAbility = 2; //Nombre de bloc que le Worms est capable d'escalader
    protected org.newdawn.slick.Image skinLeft;
    protected org.newdawn.slick.Image skinRight;

    public Worms(int t, String n,int[][] terrain,int blockSize,boolean[] changementPrint,int x,int y) throws SlickException { //t=0 ou 1, pour savoir quelle équipe
        if(t==0) couleur=org.newdawn.slick.Color.blue;
        else couleur=org.newdawn.slick.Color.red;
        name=n;
        life=200;
        this.terrain = terrain;
        this.blockSize = blockSize;
        isMoving = false;
        this.changementPrint =  changementPrint;
        this.x = x;
        this.y = y;
        orientation = 1;
        vitesse_x = 0;
        vitesse_y = 0;
        acceleration_x = 0;
        acceleration_y = 0;
        //speed_force_x = 0;
        //speed_force_y = 0;
        //générateur aléatoire position
        skinLeft = new org.newdawn.slick.Image("images/skin_worms_left.png");
        skinRight = new org.newdawn.slick.Image("images/skin_worms_right.png");
    }

    public void modifierVie(int hp){
        life+=hp;
    }

    public void deplacer(int direction){
        //direction=0 pour aller à gauche, 1 pour aller à droite
        //int[] actualCaseLeft = new int[2];
        //actualCaseLeft = blockEquivalent(x,y);
        //int[] actualCaseRight = new int[2];
        //actualCaseRight = blockEquivalent(x+hitBoxLargeur,y);
        //4 lignes inutiles en fin de compte ...
        int tempx = x;
        int tempy = y;
        if(direction==0){
            tempx -= 1;
        }
        else if(direction==1){
            tempx += 1;
        }
        orientation = direction;

        boolean mouvPossible = true;
        ArrayList<Block> BlockEnContact = getContactBlock(tempx,tempy);
        for(Block bContact:BlockEnContact){
            if(isIntraversable(bContact)){
                mouvPossible = false;
            }
        }
        if(mouvPossible){
            x = tempx;
            y = tempy;
            changementPrint[0] = true;
        }
        else{
            Block BlocBasWorms = blockEquivalent(tempx,tempy);
            int yGrilleBasWorms = BlocBasWorms.y;
            boolean sameYforAll = true;
            int max_diff = 1;
            for(Block bContact:BlockEnContact){
                if(bContact.y > yGrilleBasWorms + climbAbility -1){
                    sameYforAll = false;
                }
                else{
                    int test = bContact.y - yGrilleBasWorms + 1;
                    if(test>max_diff){
                        max_diff = test;
                    }
                }
            }
            if(sameYforAll){
                tempy -= (int)(blockSize*max_diff);
                x = tempx;
                y = tempy;
                changementPrint[0] = true;
            }
        }

    }

    public void applyForces(){
            //Applique aux Worms l'ensemble des forces auquel il est soumis, y compris la gravité
            int xtemp = x;
            int ytemp = y;
            acceleration_x = 0;
            acceleration_y = g;
            /*ArrayList<Block> BlockEnContact = getContactBlock(x,y-blockSize/2);
            Block BlocBasWorms = blockEquivalent(x,y-1);
            yGrilleBasWorms = BlocBasWorms.y;
            boolean surLeSol = false;
            for(Block bContact:BlockEnContact){
                if(bContact.y = yGrilleBasWorms){
                    surLeSol = true;
                }
            }
            if(surLeSol = true){
                acceleration -= g;
            }*/
            vitesse_x += acceleration_x * facteurEchelle;
            vitesse_y += acceleration_y * facteurEchelle;
            vitesse_x = limite(vitesse_x);
            vitesse_y = limite(vitesse_y);
            xtemp += (int)vitesse_x;
            ytemp += (int)vitesse_y;

            ArrayList<Block> BlockEnContact = getContactBlock(xtemp,ytemp);

            Block BlocBasWorms = blockEquivalent(xtemp,ytemp);
            int yGrilleBasWorms = BlocBasWorms.y;
            Block BlocHautWorms = blockEquivalent(xtemp,ytemp-hitBoxHauteur+1);
            int yGrilleHautWorms = BlocHautWorms.y;
            Block BlocGaucheWorms = blockEquivalent(xtemp,ytemp);
            int xGrilleGaucheWorms = BlocGaucheWorms.x;
            Block BlocDroiteWorms = blockEquivalent(xtemp+hitBoxLargeur-1,ytemp);
            int xGrilleDroiteWorms = BlocDroiteWorms.x;

            boolean one_change_x = true;
            boolean one_change_y = true;


            //Gestion des collisions (rebond à l'image de la reflexion en optique)
            for(Block bContact:BlockEnContact){
                if((bContact.y == yGrilleBasWorms || bContact.y == yGrilleHautWorms)&& one_change_y){
                    vitesse_x = (int)(vitesse_x/2.0);
                    vitesse_y = -(int)(vitesse_y/1.25);
                    one_change_y = false;
                    if(bContact.y == yGrilleBasWorms){
                        ytemp = yGrilleBasWorms*blockSize-1;
                    }
                    else{
                        ytemp = (yGrilleBasWorms+1)*blockSize-1;
                    }
                }
                else if((bContact.x == xGrilleGaucheWorms || bContact.x == xGrilleDroiteWorms)&& bContact.y != yGrilleBasWorms && bContact.y != yGrilleHautWorms && one_change_x){
                    vitesse_x = -(int)(vitesse_x/1.25);
                    vitesse_y = (int)(vitesse_y/2.0);
                    one_change_x = false;
                    if(bContact.x == xGrilleGaucheWorms){
                        xtemp = (xGrilleGaucheWorms+1)*blockSize;
                    }
                    else{
                        xtemp = xGrilleGaucheWorms*blockSize;
                    }
                }
            }

            /*boolean surLeSol = false;
            for(Block bContact:BlockEnContact){
                if(bContact.y == yGrilleBasWorms){
                    surLeSol = true;
                }
            }
            if(surLeSol){
                ytemp = yGrilleBasWorms*blockSize-1;
                vitesse_y = 0;
            }*/

            if(ytemp != y || xtemp != x){
                y = ytemp;
                x = xtemp;
                changementPrint[0] = true;
            }
    }

    public boolean getMovingState(){
        return isMoving;
    }

    public void setMovingState(boolean etat){
        isMoving = etat;
    }

    public void draw(org.newdawn.slick.Graphics g){
        //g.setColor(couleur);
        //g.fillRect(x,y-hitBoxHauteur+1,hitBoxLargeur,hitBoxHauteur);

        if(orientation==0){
            skinLeft.draw(x,y-hitBoxHauteur+1);
        }
        else{
            skinRight.draw(x,y-hitBoxHauteur+1);
        }

        //Debugage:
        /*ArrayList<Block> BlockEnContact = getContactBlock(x,y+1);
        for(Block bContact:BlockEnContact){
            g.setColor(Color.black);
            g.fillRect(bContact.x*blockSize,bContact.y*blockSize,blockSize,blockSize);
        }*/
    }

    public Block blockEquivalent(int xd,int yd){
        int XD = xd/blockSize;
        int YD = yd/blockSize;
        Block equiv = new Block(XD,YD,terrain[YD][XD]);
        return equiv;
    }

    public boolean isIntraversable(Block bloki){
        boolean intraversable = false;
        for(int blocz:blocIntraversables){
            if(terrain[bloki.y][bloki.x] == blocz){
                intraversable = true;
            }
        }
        return intraversable;
    }

    public ArrayList<Block> getContactBlock(int tempx,int tempy){
        ArrayList<Block> templist = new ArrayList<Block>();
        for(int i=tempx;i<=tempx+hitBoxLargeur-1;i+=hitBoxLargeur-1){
            for(int j=tempy-hitBoxHauteur+1;j<=tempy;j+=blockSize){
                Block actuBlock = blockEquivalent(i,j);
                if(isIntraversable(actuBlock)){
                    templist.add(actuBlock);
                }
            }
        }
        Block actuBlock1 = blockEquivalent(tempx,tempy);
        if(isIntraversable(actuBlock1)){
            templist.add(actuBlock1);
        }
        Block actuBlock2 = blockEquivalent(tempx+hitBoxLargeur-1,tempy);
        if(isIntraversable(actuBlock2)){
            templist.add(actuBlock2);
        }
        for(int j=tempy-hitBoxHauteur+1;j<=tempy;j+=hitBoxHauteur-1){
            for(int i=tempx+blockSize-1;i<=tempx+hitBoxLargeur-1-blockSize;i+=blockSize){
                Block actuBlock = blockEquivalent(i,j);
                if(isIntraversable(actuBlock)){
                    templist.add(actuBlock);
                }
            }
        }
        return templist;
    }

    public double limite(double speed){
        if(speed > blockSize){
            speed = blockSize;
        }
        else if(speed < -blockSize){
            speed = -blockSize;
        }
        return speed;
    }

    public void set_vitesse_x(double speed){
        vitesse_x = speed;
    }

    public void set_vitesse_y(double speed){
        vitesse_y = speed;
    }

    public int get_orientation(){
        return orientation;
    }
}
