package game;
import java.util.ArrayList;

public class CreatureAction extends Action{
    public Creature owner;
    public String s;
    public String t;
    

    public CreatureAction(Creature _owner){
        System.out.println("Creating Action");
        owner = _owner;
    }

    public void setName(String _s){
        System.out.println("    set name");
        s = _s;
        
    }

    public void setType(String _t){
        System.out.println("    set type");
        t = _t;
    }
}
