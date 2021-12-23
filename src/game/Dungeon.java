package game;
import java.util.ArrayList;

public class Dungeon{
    public String name;
    public int width;
    public int gameHeight;
    public int bottomHeight;
    public int topHeight;
    
    public ArrayList<Room> rooms = new ArrayList<Room>(); // Room type array
    public ArrayList<Creature> creatures = new ArrayList<Creature>();// Creature type array
    public ArrayList<Passage> passages = new ArrayList<Passage>(); // Passage type array
    public ArrayList<Item> items = new ArrayList<Item>(); // Item type array
    public ArrayList<Player> players = new ArrayList<Player>(); // Player type array
    public ArrayList<Monster> monsters = new ArrayList<Monster>(); // Monster type Array
    public ArrayList<Sword> swords = new ArrayList<Sword>(); // Sword type Array
    public ArrayList<Scroll> scrolls = new ArrayList<Scroll>();
    public ArrayList<Armor> armors = new ArrayList<Armor>();

    public Dungeon(String _name,  int _width, int _gameHeight, int _bottomHeight, int _topHeight){
        name = _name;
        width = _width;
        gameHeight = _gameHeight;
        bottomHeight = _bottomHeight;
        topHeight = _topHeight;
    }
    public ArrayList<Room> getRooms(){
        return rooms;
    }
    public void addRoom(Room room){
        rooms.add(room);
    }
    public void addPlayer(Player player){
        players.add(player);
    }

    public void addMonster(Monster monster){
        monsters.add(monster);
    }


    public void addPassage(Passage passage){
        passages.add(passage);
    }

    public void addItem(Item item){
        items.add(item);
    }
    
    public void addSword(Sword sword){
        swords.add(sword);
    }

    public void addScroll(Scroll scroll){
        scrolls.add(scroll);
    }

    public void addArmor(Armor armor){
        armors.add(armor);
    }
    public void addCreature(Creature creature){
        creatures.add(creature);
    }
}
