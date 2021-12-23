package game;
import java.util.ArrayList;
public class Player extends Creature{
    public String s;
    public int room;
    public int serial;
    public Item sword;
    public Item armor;
    public ArrayList<Item> item_list = new ArrayList<Item>(); // Room type array

    public void setName(String _s){
        System.out.println("Add Creature");
        System.out.println("Creating Creature");
        //System.out.println("Player Created");
        System.out.println("    set name");
        s = _s;
    }

    public void setID(int _room, int _serial){
        //System.out.println("room");
        room = _room;
        System.out.println("    set serial");
        serial = _serial;
    }

    public void addItem(Item _item){
        item_list.add(_item);
    }

    public void setWeapon (Item _sword){
        System.out.println("    set weapon");
        sword = _sword;
    }

    public void setArmor(Item _armor){
        System.out.println("    set Armor");
        armor = _armor;
    }

}