package game;
import java.util.ArrayList;
public class Creature extends Displayable{
    //no constructor for now
    public int h;
    public int hpm;
    public CreatureAction da;
    public CreatureAction ha;
    public ArrayList<CreatureAction> creature_actions = new ArrayList<CreatureAction>();

    public void addCAction(CreatureAction _action){
        creature_actions.add(_action);
    }
    /*public void setHp(int _h){
        System.out.println("    set hp");
        h = _h;
    }*/

    /*public void setHpMoves(int _hpm){
        System.out.println("    set Hp moves");
        hpm = _hpm;
    }*/

    public void setDeathAction(CreatureAction _da){
        System.out.println("    set Death Action");
        da = _da;
    }
    public void setHitAction(CreatureAction _ha){
        System.out.println("    set Hit Action");
        ha = _ha;
    }
}