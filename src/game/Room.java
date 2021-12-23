package game;
import java.util.ArrayList;
public class Room extends Structure{
    // Constructor of Room class
    public int s;
    public int room;
    public Creature Monster;
    
    public Room (int _s){
        System.out.println("Creating Room");
        s = _s;
    }

    public void setId(int _room){
        System.out.println("    id set");
        room = _room;
    }


    public void setCreature(Creature _Monster){
        System.out.println("Creating Creature");
        Monster = _Monster;
    }

}