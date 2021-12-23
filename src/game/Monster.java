package game;
public class Monster extends Creature{
    // no constructor 
    public String s;
    public int room;
    public int serial;
    
    public void setName(String _s){
        System.out.println("Add Creature");
        System.out.println("Creating Creature");
        //System.out.println("Monster Created");
        System.out.println("    name set");
        s = _s;
    }
    public void setID(int _room, int _serial){
        System.out.println("    id set");
        room = _room;
        serial = _serial;
    }
}
