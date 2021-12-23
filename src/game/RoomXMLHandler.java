package game;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
public class RoomXMLHandler extends DefaultHandler{
    private static final int DEBUG = 1;
    private static final String CLASSID = "RoomXMLHandler";

    Stack<Displayable> displayableStack = new Stack<>();
    Stack<Action> actionStack = new Stack<>();

    private StringBuilder data = null;

    //private ArrayList<Room> rooms = new ArrayList<Room>();
    //private ArrayList<Monster> monsters = new ArrayList<Monster>();
    //private ArrayList<Player> players = new ArrayList<Player>();
    //private ArrayList<Item> items = new ArrayList<Item>();

    public Dungeon getDungeon(){
        return dungeonBeingParsed;
    }
    private Dungeon dungeonBeingParsed = null;
    
    public RoomXMLHandler(){
    }
    boolean Element_is_open = false;
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
        if (DEBUG > 1) {
            System.out.println(CLASSID + ".startElement qName: " + qName);
        }
        if (qName.equalsIgnoreCase("Dungeon")) {
            int bottomHeight = Integer.parseInt(attributes.getValue("bottomHeight"));
            int gameHeight = Integer.parseInt(attributes.getValue("gameHeight"));
            int topHeight = Integer.parseInt(attributes.getValue("topHeight"));
            int width = Integer.parseInt(attributes.getValue("width"));
            String name = attributes.getValue("name");
            dungeonBeingParsed = new Dungeon(name, width, gameHeight, bottomHeight, topHeight);
            System.out.println("Running the Parser:");
        }
        else if (qName.equalsIgnoreCase("Room")){
            int roomNum = Integer.parseInt(attributes.getValue("room"));
            Room room = new Room(roomNum); // dont know if string or int
            dungeonBeingParsed.addRoom(room);
            displayableStack.push(room);

            //rooms.add(room);
        }        
        else if (qName.equalsIgnoreCase("Monster")){
            Element_is_open = true;
            String monsterName = attributes.getValue("name");
            int roomNum = Integer.parseInt(attributes.getValue("room"));
            int serialNum = Integer.parseInt(attributes.getValue("serial"));
            
            Monster monster = new Monster();
            System.out.println("Monster Created");
            monster.setName(monsterName);
            monster.setID(roomNum,serialNum);
            dungeonBeingParsed.addCreature(monster);
            dungeonBeingParsed.addMonster(monster);
            displayableStack.push(monster);
            
            //monsters.add(monster);
        }
        else if (qName.equalsIgnoreCase("Player")){
            Element_is_open = true;
            String playerName = attributes.getValue("name");
            //int roomNum = Integer.parseInt(attributes.getValue("room")); // Must change this, should not take this roomNum, takes top of displayable stack's number
            Room temp_room = dungeonBeingParsed.rooms.get(dungeonBeingParsed.rooms.size()-1);
            int roomNum = temp_room.s;
            int serialNum = Integer.parseInt(attributes.getValue("serial"));
            
            Player player = new Player();
            System.out.println("Player Created");
            player.setName(playerName);
            player.setID(roomNum, serialNum);
            dungeonBeingParsed.addCreature(player);
            dungeonBeingParsed.addPlayer(player);
            displayableStack.push(player);

            //players.add(player);
        }
        else if (qName.equalsIgnoreCase("CreatureAction")){
            String actionName = attributes.getValue("name");
            String actionType = attributes.getValue("type");
            Creature owner = new Creature(); //check later
            owner = (Creature) displayableStack.peek();
            
            CreatureAction action = new CreatureAction(owner);
            System.out.println("CreatureAction Created");
            action.setName(actionName);
            action.setType(actionType);
            actionStack.push(action);
            owner.addCAction(action);
        }
         

        else if (qName.equalsIgnoreCase("Scroll")){
            String scrollName = attributes.getValue("name");
            int scrollroom = Integer.parseInt(attributes.getValue("room"));
            int scrollserial = Integer.parseInt(attributes.getValue("serial"));
            Scroll scroll = new Scroll(scrollName);
            scroll.character = '?';
            System.out.println("Scroll Created");
            scroll.setId(scrollroom, scrollserial);
            if (displayableStack.peek() instanceof Player){
                Player temp_player = (Player) displayableStack.peek();
                temp_player.addItem(scroll);
                displayableStack.push(scroll);
            }
            else{
                dungeonBeingParsed.addItem(scroll);
                dungeonBeingParsed.addScroll(scroll);
                displayableStack.push(scroll); 
            }
        }
        
        else if (qName.equalsIgnoreCase("ItemAction")){ // Not sure if this is correct
            String itemName = attributes.getValue("name");
            String itemType = attributes.getValue("type");        
            Item owner = (Item) displayableStack.peek();
            ItemAction itemaction = new ItemAction(owner);
            System.out.println("ItemAction Created");
            itemaction.setName(itemName);
            itemaction.setType(itemType);
            owner.setItemAction(itemaction);
            actionStack.push(itemaction);
        }
        else if (qName.equalsIgnoreCase("Armor")){
            String armorName = attributes.getValue("name");
            int armorRoom = Integer.parseInt(attributes.getValue("room"));
            int armorSerial = Integer.parseInt(attributes.getValue("serial"));
            Armor armor = new Armor(armorName);
            armor.character = ']';
            System.out.println("Armor Created");
            armor.setId(armorRoom, armorSerial);
            if (displayableStack.peek() instanceof Player){
                Player temp_player = (Player) displayableStack.peek();
                temp_player.addItem(armor);
                displayableStack.push(armor);
            }
            else{
                dungeonBeingParsed.addItem(armor);
                dungeonBeingParsed.addArmor(armor);
                displayableStack.push(armor);
            }
            
            
            // MAYBE USE A BOOLEAN (CREATE ABOVE START ELEMENT) THAT SHOWS IF WE ARE WITHIN CREATURE (CREATURE HAS NOT BEEN END ELEMENTED), IF YES CALL TO DISPLAYABLABLE STACK AND DUNGEONBEINGPARSED ELSE DONT
        }
        else if (qName.equalsIgnoreCase("Sword")){
            String swordName = attributes.getValue("name");
            int swordNum = Integer.parseInt(attributes.getValue("room"));
            int swordSerial = Integer.parseInt(attributes.getValue("serial"));
            Sword sword = new Sword(swordName);
            sword.character = '|';
            System.out.println("Sword Created");
            sword.setId(swordNum, swordSerial);
            if(displayableStack.peek() instanceof Player){
                Player temp_player = (Player) displayableStack.peek();
                temp_player.addItem(sword);
                displayableStack.push(sword);
            }
            else{
                dungeonBeingParsed.addItem(sword);
                dungeonBeingParsed.addSword(sword);
                displayableStack.push(sword);
            }
            
        }
        else if (qName.equalsIgnoreCase("Passage")){
            int room2 = Integer.parseInt(attributes.getValue("room2"));
            int room1 = Integer.parseInt(attributes.getValue("room1"));
            Passage passage = new Passage();
            System.out.println("Passage Created");
            passage.setID(room1, room2);
            dungeonBeingParsed.addPassage(passage);
            displayableStack.push(passage);
        }
        data = new StringBuilder();

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
        if (qName.equalsIgnoreCase("visible")){   
            Displayable element = displayableStack.peek();
            
            element.setVisible(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("posX")){
            Displayable element = displayableStack.peek();
            element.SetPosX(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("posY")){
            Displayable element = displayableStack.peek();
            element.setPosY(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("width")){
            Displayable element = displayableStack.peek();
            element.SetWidth(Integer.parseInt(data.toString()));
        }
        
        else if (qName.equalsIgnoreCase("height")){
            Displayable element = displayableStack.peek();
            element.setHeight(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("actionMessage")){
            Action element2 = actionStack.peek();
            element2.setActionMsg(data.toString());
            element2.setMessage(data.toString());
            //Displayable temp_disp = displayableStack.peek();
            //temp_disp.setActionMsg(data.toString());
        }

        else if (qName.equalsIgnoreCase("actionIntValue")){
            Action element2 = actionStack.peek();
            element2.setIntValue(Integer.parseInt(data.toString()));
            element2.setActionInt(Integer.parseInt(data.toString()));
            //Displayable temp_disp = displayableStack.peek();
            //temp_disp.setActionInt(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("actionCharValue")){ 
            Action element2 = actionStack.peek();
            element2.setCharValue(data.toString().charAt(0));
            element2.setActionChar(data.toString().charAt(0));
            //Displayable temp_disp = displayableStack.peek();
            //temp_disp.setActionChar(data.toString().charAt(0));
        }

        else if (qName.equalsIgnoreCase("hp")){
            Displayable element = displayableStack.peek();
            element.setHp(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("hpMoves")){
            Displayable element = displayableStack.peek();
            element.HPMove(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("maxhit")){
            Displayable element = displayableStack.peek();
            element.setMaxHit(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("ItemIntValue")){
            Displayable element = displayableStack.peek();
            element.setIntValue(Integer.parseInt(data.toString()));
        }

        else if (qName.equalsIgnoreCase("type")){
            Displayable element = displayableStack.peek();
            element.setType(data.toString().charAt(0));
        }

        else if (qName.equalsIgnoreCase("Room")){
            displayableStack.pop();
        }
        else if (qName.equalsIgnoreCase("Monster")){
            Element_is_open = false;
            displayableStack.pop();
        }
        else if (qName.equalsIgnoreCase("Player")){
            Element_is_open = false;
            displayableStack.pop();
        }
        else if (qName.equalsIgnoreCase("Scroll")){
            displayableStack.pop();
        }

        else if (qName.equalsIgnoreCase("Sword")){
            displayableStack.pop();    
        }

        else if (qName.equalsIgnoreCase("Armor")){
            displayableStack.pop();      
        }

        else if (qName.equalsIgnoreCase("Passage")){
            displayableStack.pop();
        }

        else if (qName.equalsIgnoreCase("CreatureAction")){
            actionStack.pop();
        }

        else if (qName.equalsIgnoreCase("ItemAction")){
            actionStack.pop();
        }
        
    
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
        if (DEBUG > 1) {
            System.out.println(CLASSID + ".characters: " + new String(ch, start, length));
            System.out.flush();
        }
    }

   
}
