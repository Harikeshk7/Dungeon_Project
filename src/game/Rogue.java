package game; // FIGURE OUT HOW TO PACKAGE FILES SO THAT YOU CAN RUN THE ASCII PANEL CODE

import java.io.File;
import java.io.IOException;

import javax.security.auth.x500.X500Principal;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.Arrays;

import org.xml.sax.SAXException;

public class Rogue implements Runnable {
    private static Dungeon dungeon;
    private static ObjectDisplayGrid displayGrid = null;
    private Thread keyStrokePrinter;

    public Rogue(int width, int height) {
        displayGrid = new ObjectDisplayGrid(width, height);
    }

    @Override
    public void run() {
        
        displayGrid.fireUp();
        int top_offset = dungeon.topHeight;
        for (int counter = 0; counter < dungeon.rooms.size(); counter++){
            Room temp_room = dungeon.rooms.get(counter);
            RoomWall wall = new RoomWall(temp_room.x1, temp_room.y1 + top_offset);
           
            RoomFloor floor = new RoomFloor(temp_room.x1 + 1, temp_room.y1 + 1 + top_offset);
            for (int i = temp_room.x1; i < temp_room.x2 + temp_room.x1; i++){
                for (int j = temp_room.y1; j < temp_room.y2 + temp_room.y1; j++){
                    if (i == temp_room.x1 || i == (temp_room.x1 + temp_room.x2) - 1 || j == temp_room.y1 || j == (temp_room.y1 + temp_room.y2) - 1){
                        wall.character = 'X';
                        wall.orig_character = 'X';      
                        displayGrid.addObjectToDisplay((Displayable) wall, i, j + top_offset);
                    }
                }
            }

            for (int i = temp_room.x1 + 1; i < (temp_room.x2 + temp_room.x1) - 1; i++){
                for (int j = temp_room.y1 + 1; j < (temp_room.y2 + temp_room.y1 - 1); j++){
                    floor.character = '.';
                    floor.orig_character = '.';
                    displayGrid.addObjectToDisplay((Displayable) floor, i, j + top_offset);
                }
            }
            
        }
        for (int counter = 0; counter < dungeon.scrolls.size(); counter++){
            Scroll temp_scroll = dungeon.scrolls.get(counter);
            Room temp_room = dungeon.rooms.get(temp_scroll.room - 1);
            temp_scroll.character = '?';
            temp_scroll.orig_character = '?';
            displayGrid.addObjectToDisplay((Displayable) temp_scroll, temp_room.x1 + temp_scroll.x1, temp_room.y1 + temp_scroll.y1 + top_offset); // definitely change positions relative to room not dungeon
        }

        for (int counter = 0; counter < dungeon.swords.size(); counter++){
            Sword temp_sword = dungeon.swords.get(counter);
            Room temp_room = dungeon.rooms.get(temp_sword.room - 1);
            temp_sword.character = '|';
            temp_sword.orig_character = '|';
            displayGrid.addObjectToDisplay((Displayable) temp_sword, temp_room.x1 + temp_sword.x1, temp_room.y1 + temp_sword.y1 + top_offset); // definitely change positions relative to room not dungeon
        }

        for (int counter = 0; counter < dungeon.armors.size(); counter++){
            Armor temp_armor = dungeon.armors.get(counter);
            Room temp_room = dungeon.rooms.get(temp_armor.room - 1);
            temp_armor.character = ']';
            temp_armor.orig_character = ']';
            displayGrid.addObjectToDisplay((Displayable) temp_armor , temp_room.x1 + temp_armor.x1, temp_room.y1 + temp_armor.y1 + top_offset); // definitely change positions relative to room not dungeon
        }

        for (int counter = 0; counter < dungeon.monsters.size(); counter++){
            Monster temp_monster = dungeon.monsters.get(counter);
            Room temp_room = dungeon.rooms.get(temp_monster.room - 1);
            if (temp_monster.t == 'T'){
                temp_monster.character = 'T';
                temp_monster.orig_character = 'T';
                displayGrid.addObjectToDisplay((Displayable)temp_monster, temp_room.x1 + temp_monster.x1, temp_room.y1 + temp_monster.y1 + top_offset); // Check positions
            }
            else if (temp_monster.t == 'H'){
                temp_monster.character = 'H';
                temp_monster.orig_character = 'H';
                displayGrid.addObjectToDisplay((Displayable)temp_monster, temp_room.x1 + temp_monster.x1, temp_room.y1 + temp_monster.y1 + top_offset); // Check positions
            }
            else if (temp_monster.t == 'S'){
                temp_monster.character = 'S';
                temp_monster.orig_character = 'S';
                displayGrid.addObjectToDisplay((Displayable)temp_monster, temp_room.x1 + temp_monster.x1, temp_room.y1 + temp_monster.y1 + top_offset); // Check positions
            }
        }
        for (int counter = 0; counter < dungeon.passages.size(); counter++){
            Passage temp_passage = dungeon.passages.get(counter);
            

            for (int pair_index = 0; pair_index + 1 < temp_passage.Passage_list.size(); pair_index++){

                ArrayList<Integer> first_pair = temp_passage.Passage_list.get(pair_index);
                ArrayList<Integer> second_pair = temp_passage.Passage_list.get(pair_index + 1);
                if (pair_index == 0){
                    PassageJunction junction = new PassageJunction(first_pair.get(0), first_pair.get(1));
                    junction.character = '+';
                    junction.orig_character = '+';
                    displayGrid.addObjectToDisplay((Displayable) junction , first_pair.get(0), first_pair.get(1) + top_offset);
                }
                if (pair_index == temp_passage.Passage_list.size() - 2){
                    PassageJunction junction = new PassageJunction(second_pair.get(0), second_pair.get(1));
                    junction.character = '+';
                    junction.orig_character = '+';
                    displayGrid.addObjectToDisplay((Displayable) junction , second_pair.get(0), second_pair.get(1) + top_offset);
                }
            }
            int last_pair = 0;
            for (int pair_index = 0; pair_index + 1 < temp_passage.Passage_list.size(); pair_index++){
                ArrayList<Integer> first_pair = temp_passage.Passage_list.get(pair_index);
                ArrayList<Integer> second_pair = temp_passage.Passage_list.get(pair_index + 1);
                
                if (first_pair.get(0) == second_pair.get(0)){
                    if (first_pair.get(1) <= second_pair.get(1)){
                        if (pair_index == temp_passage.Passage_list.size() - 2){
                            last_pair = 1;
                        }
                        else{
                            last_pair = 0;
                        }
                        for (int i = first_pair.get(1) + 1; i <= second_pair.get(1) - last_pair; i++){
                            temp_passage.character = '#';
                            temp_passage.orig_character = '#';
                            displayGrid.addObjectToDisplay((Displayable) temp_passage , first_pair.get(0), i + top_offset);
                            
                        }
                    }
                    else{
                        if (pair_index == temp_passage.Passage_list.size() - 2){
                            last_pair = 1;
                        }
                        else{
                            last_pair = 0;
                        }
                        for (int i = second_pair.get(1) + 1; i <= first_pair.get(1) - last_pair; i++){
                            temp_passage.character = '#';
                            temp_passage.orig_character = '#';
                            displayGrid.addObjectToDisplay((Displayable) temp_passage , first_pair.get(0), i + top_offset);
                            
                        }
                    }
                }
                else if (first_pair.get(1) == second_pair.get(1)){
                    if (first_pair.get(0) <= second_pair.get(0)){
                        if (pair_index == temp_passage.Passage_list.size() - 2){
                            last_pair = 1;
                        }
                        else{
                            last_pair = 0;
                        }
                        for (int i = first_pair.get(0) + 1; i <= second_pair.get(0) - last_pair; i++){
                            temp_passage.character = '#';
                            temp_passage.orig_character = '#';
                            displayGrid.addObjectToDisplay((Displayable) temp_passage, i, first_pair.get(1) + top_offset);
                        }
                    }
                    else{
                        if (pair_index == temp_passage.Passage_list.size() - 2){
                            last_pair = 1;
                        }
                        else{
                            last_pair = 0;
                        }
                        for (int i = second_pair.get(0) + 1; i <= first_pair.get(0) - last_pair; i++){
                            temp_passage.character = '#';
                            temp_passage.orig_character = '#';
                            displayGrid.addObjectToDisplay((Displayable) temp_passage, i, first_pair.get(1) + top_offset);
                        }
                    }
                }
            }
            
        }
        for (int counter = 0; counter < dungeon.players.size(); counter++){
            Player temp_player = dungeon.players.get(counter);
            Room temp_room = dungeon.rooms.get(temp_player.room - 1); // Fix that only displayable objs should be on floor
            Displayable fill_char = new Displayable();
            fill_char.character = 'H';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 0, 0);
            fill_char.character = 'P';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 1, 0);
            fill_char.character = ':';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 2, 0);
            fill_char.character = ' ';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 3, 0);

            fill_char.character = ' ';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 4, 0);

            fill_char.character = ' ';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 5, 0);

            fill_char.character = ' ';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 6, 0);

            displayGrid.displayHP(temp_player.Hp);

            fill_char.character = ' ';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 7, 0);

            fill_char.character = 'S';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 8, 0);
            fill_char.character = 'C';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 9, 0);
            fill_char.character = 'O';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 10, 0);
            fill_char.character = 'R';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 11, 0);
            fill_char.character = 'E';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 12, 0);
            fill_char.character = ':';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 13, 0);
            fill_char.character = ' ';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 14, 0);
            fill_char.character = ' ';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 15, 0);
            fill_char.character = '0';
            displayGrid.addObjectToDisplay((Displayable) fill_char, 16, 0);

            Displayable pack1 = new Displayable();
            pack1.character = 'P';
            displayGrid.addObjectToDisplay((Displayable) pack1, 0, dungeon.gameHeight + dungeon.topHeight + 1);

            pack1.character = 'a';
            displayGrid.addObjectToDisplay((Displayable) pack1, 1, dungeon.gameHeight + dungeon.topHeight + 1);

            pack1.character = 'c';
            displayGrid.addObjectToDisplay((Displayable) pack1, 2, dungeon.gameHeight + dungeon.topHeight + 1);

            pack1.character = 'k';
            displayGrid.addObjectToDisplay((Displayable) pack1, 3, dungeon.gameHeight + dungeon.topHeight + 1);

            pack1.character = ':';
            displayGrid.addObjectToDisplay((Displayable) pack1, 4, dungeon.gameHeight + dungeon.topHeight + 1);

            pack1.character = ' ';
            displayGrid.addObjectToDisplay((Displayable) pack1, 5, dungeon.gameHeight + dungeon.topHeight + 1);

            for (int i = 0; i < dungeon.width; i++){
                pack1.character = ' ';
                displayGrid.addObjectToDisplay((Displayable) pack1, i + 6, dungeon.gameHeight + dungeon.topHeight + 1);
            }

            Displayable info = new Displayable();
            info.character = 'I';
            displayGrid.addObjectToDisplay((Displayable) info, 0, dungeon.gameHeight + dungeon.topHeight + 3);

            info.character = 'n';
            displayGrid.addObjectToDisplay((Displayable) info, 1, dungeon.gameHeight + dungeon.topHeight + 3);

            info.character = 'f';
            displayGrid.addObjectToDisplay((Displayable) info, 2, dungeon.gameHeight + dungeon.topHeight + 3);

            info.character = 'o';
            displayGrid.addObjectToDisplay((Displayable) info, 3, dungeon.gameHeight + dungeon.topHeight + 3);

            info.character = ':';
            displayGrid.addObjectToDisplay((Displayable) info, 4, dungeon.gameHeight + dungeon.topHeight + 3);

            info.character = ' ';
            displayGrid.addObjectToDisplay((Displayable) info, 5, dungeon.gameHeight + dungeon.topHeight + 3);

            for (int j = 0; j < dungeon.width; j++){
                pack1.character = ' ';
                displayGrid.addObjectToDisplay((Displayable) pack1, j + 6, dungeon.gameHeight + dungeon.topHeight + 3);
            }

            temp_player.character = '@';
            temp_player.orig_character = '@';
            displayGrid.addToPack(temp_player.item_list);
            displayGrid.addObjectToDisplay((Displayable) temp_player, temp_room.x1 + temp_player.x1, temp_room.y1 + temp_player.y1 + top_offset); // Check positions
            
        }
    }
    

    public static void main(String[] args) throws Exception {
    // check if a filename is passed in.  If not, print a usage message.
    // If it is, open the file
    String fileName = null;
    switch (args.length) {
        case 1:
                   /******************************************************************
                    * note that the relative file path may depend on what IDE you are
                * using.  You might needs to add to the beginning of the filename, 
                    * e.g., filename = "src/xmlfiles/" + args[0]; worked for me in
                    * netbeans, what is here works for me from the command line in
                    * linux.
                    ******************************************************************/
        fileName = "game/xmlFiles/"+ args[0];
        break;
        default:
        System.out.println("java Rogue <xmlfilename>");
        return;
    }
        
            // Create a saxParserFactory, that will allow use to create a parser
            // Use this line unchanged
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        
            // We haven't covered exceptions, so just copy the try { } catch {...}
            // exactly, // filling in what needs to be changed between the open and 
            // closed braces.
    try {
                // just copy this
        SAXParser saxParser = saxParserFactory.newSAXParser();
                // just copy this
        RoomXMLHandler handler = new RoomXMLHandler();
                // just copy this.  This will parse the xml file given by fileName
        saxParser.parse(new File(fileName), handler);
                // This will change depending on what kind of XML we are parsing
        dungeon = handler.getDungeon();
                // print out all of the students.  This will change depending on 
                // what kind of XML we are parsing
        Rogue rogue = new Rogue(dungeon.width, dungeon.gameHeight + dungeon.topHeight + dungeon.bottomHeight);
        
        Thread testThread = new Thread(rogue); 
        testThread.start();
 
        rogue.keyStrokePrinter = new Thread(new KeyStrokePrinter(displayGrid));
        rogue.keyStrokePrinter.start();

        testThread.join();
        rogue.keyStrokePrinter.join();
            // these lines should be copied exactly.
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace(System.out);
        }
    }
}

