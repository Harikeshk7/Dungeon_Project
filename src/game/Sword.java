package game;
public class Sword extends Item{
    public String name;
    public int room;
    public int serial;
    
    public Sword(String _name){
        System.out.println("    set name");
        name = _name;
    }

    public void setId(int _room, int _serial){
        System.out.println("    set room");
        room = _room;
        System.out.println("    set serial");
        serial = _serial;

    }
}