import java.awt.*;
import java.util.ArrayList;

public class Worms {
    protected Color couleur;
    protected String name;
    protected int life;
    protected int x; //Les coordonnées x,y correspondent au coin en bas à gauche du Worms
    protected int y;
    protected int terrain[][];
    protected int blockSize;
    protected boolean isMoving; //Servira à savoir si un Worms est dans sa phase de déplacement
    protected boolean[] changementPrint;
    public double vitesse;
    public double acceleration;
    protected final static double masse = 10;
    protected final static double g = 9.81;
    protected final static int hitBoxHauteur = 40;
    protected final static int hitBoxLargeur = 30;
    protected final static int blocIntraversables[] = {1};
    protected final static double facteurEchelle = 0.05;
    protected final static int climbAbility = 2; //Nombre de bloc que le Worms est capable d'escalader

    public Worms(int t, String n,int[][] terrain,int blockSize,boolean[] changementPrint,int x,int y){ //t=0 ou 1, pour savoir quelle équipe
        if(t==0) couleur=Color.blue;
        else couleur=Color.red;
        name=n;
        life=200;
        this.terrain = terrain;
        this.blockSize = blockSize;
        isMoving = false;
        this.changementPrint =  changementPrint;
        this.x = x;
        this.y = y;
        vitesse = 0;
        acceleration = 0;
        //générateur aléatoire position
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
            Block BlocBasWorms = blockEquivalent(tempx,tempy-1);
            int yGrilleBasWorms = BlocBasWorms.y;
            boolean sameYforAll = true;
            for(Block bContact:BlockEnContact){
                if(bContact.y > yGrilleBasWorms + climbAbility -1){
                    sameYforAll = false;
                }
            }
            if(sameYforAll){
                tempy -= blockSize;
                x = tempx;
                y = tempy;
                changementPrint[0] = true;
            }
        }

    }

    public void applyGravity(){
            int xtemp = x;
            int ytemp = y;
            acceleration = g;
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
            vitesse += acceleration;
            ytemp += limite(vitesse * facteurEchelle);
            ArrayList<Block> BlockEnContact = getContactBlock(xtemp,ytemp);
            Block BlocBasWorms = blockEquivalent(xtemp,ytemp);
            int yGrilleBasWorms = BlocBasWorms.y;
            boolean surLeSol = false;
            for(Block bContact:BlockEnContact){
                if(bContact.y == yGrilleBasWorms){
                    surLeSol = true;
                }
            }
            if(surLeSol){
                ytemp = yGrilleBasWorms*blockSize;
                vitesse = 0;
            }
            if(ytemp != y){
                y = ytemp;
                changementPrint[0] = true;
            }
    }

    public boolean getMovingState(){
        return isMoving;
    }

    public void setMovingState(boolean etat){
        isMoving = etat;
    }

    public void draw(Graphics g){
        g.setColor(couleur);
        g.fillRect(x,y-hitBoxHauteur,hitBoxLargeur,hitBoxHauteur);

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
        for(int i=tempx;i<=tempx+hitBoxLargeur;i+=hitBoxLargeur){
            for(int j=tempy-hitBoxHauteur;j<=tempy;j+=blockSize){
                Block actuBlock = blockEquivalent(i,j-1);
                if(isIntraversable(actuBlock)){
                    templist.add(actuBlock);
                }
            }
        }
        for(int j=tempy-hitBoxHauteur;j<=tempy;j+=hitBoxHauteur){
            for(int i=tempx+blockSize;i<=tempx+hitBoxLargeur-blockSize;i+=blockSize){
                Block actuBlock = blockEquivalent(i,j-1);
                if(isIntraversable(actuBlock)){
                    templist.add(actuBlock);
                }
            }
        }
        return templist;
    }

    public double limite(double speed){
        if(speed > blockSize-1){
            speed = blockSize-1;
        }
        return speed;
    }
}
