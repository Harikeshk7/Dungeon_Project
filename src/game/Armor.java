package game;
public class Armor extends Item{
    public String name;
    public int room;
    public int serial;
    
    public Armor(String _name){
        System.out.println("Creating Armor\n");
        name = _name;
    }

    public void setName(String _name){
        System.out.println("    set name");
        name = _name;
    }

    public void setId(int _room, int _serial){
        room = _room;
        System.out.println("    set room");
        serial = _serial;
        System.out.println("    set serial");
    }
}