package game;
import game.asciiPanel.AsciiPanel;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Random;
import java.util.*;

public class ObjectDisplayGrid extends JFrame implements KeyListener, InputSubject {

    private static final int DEBUG = 0;
    private static final String CLASSID = ".ObjectDisplayGrid";
    public int x_p = 0;
    public int y_p = 0;
    public int num_moves = 0;
    private static AsciiPanel terminal;
    //private Char[][] objectGrid = null;
    public Stack<Displayable>[][] objectGrid = null;
    private ArrayList<Displayable> pack = new ArrayList <Displayable>();
    private ArrayList<Character> character_list = new ArrayList<Character>();
    private List<InputObserver> inputObservers = null;
    public boolean game_over = false;
    public boolean part1_end = false;
    public boolean part2_end = false;
    public boolean command_info = false;
    public char pack_op;
    public int armor_offset = 0;
    public int sword_offset = 0;
    public int change_index = -1;
    public int worn_index = -1;
    public int wielded_index = -1;
    public int hallucinate_ind = 0;
    public int hallucinate_max = 0;
    public boolean hallucinating = false;
    public boolean scroll_read = false;
    private static int height;
    private static int width;
    public String action_type = "hit";
    public int drop_pack_counter;

    public void addToPack(ArrayList<Item> items){
        if (!items.isEmpty()){
            for (int counter = 0; counter < items.size(); counter++){
                pack.add(items.get(counter));
            }
        }
    }

    public ObjectDisplayGrid(int _width, int _height) {
        width = _width;
        height = _height;

        terminal = new AsciiPanel(width, height);

        //objectGrid = new [width][height];
        objectGrid = new Stack[width][height];
        //System.out.println(objectGrid[1][2]);
        //initializeDisplay();
        for (int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                objectGrid[i][j] = new Stack<Displayable>();
            }
        }
        character_list.add('|');
        character_list.add(']');
        character_list.add('?');
        character_list.add('@');
        character_list.add('T');
        character_list.add('H');
        character_list.add('S');
        character_list.add('.');
        character_list.add('X');
        character_list.add('+');
        character_list.add('#');

        super.add(terminal);
        super.setSize(width * 9, height * 16);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // super.repaint();
        // terminal.repaint( );
        super.setVisible(true);
        terminal.setVisible(true);
        terminal.addKeyListener(this);
        super.addKeyListener(this);
        inputObservers = new ArrayList<>();
        super.repaint();
    }


    @Override
    public void registerInputObserver(InputObserver observer) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".registerInputObserver " + observer.toString());
        }
        inputObservers.add(observer);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (DEBUG > 0) {
            System.out.println(CLASSID + ".keyTyped entered" + e.toString());
        }
        KeyEvent keypress = (KeyEvent) e;
        notifyInputObservers(keypress.getKeyChar());
    }

    private void notifyInputObservers(char ch) {
        for (InputObserver observer : inputObservers) {
            observer.observerUpdate(ch);
            if (!game_over){ 
                clear_pack();
                clear_info();
                updatePlayer(ch);
                if (hallucinate_ind > hallucinate_max){
                    hallucinating = false;
                    HallucinateReverse();
                }
                //monster_check();
            }
            if (DEBUG > 0) {
                System.out.println(CLASSID + ".notifyInputObserver " + ch);
            }
        }
    }

    public void clear_pack(){
        String str1 = " ";
        for (int n = 0; n < (width - 6); n++){
            str1 = str1 + " ";
        }
        Displayable out_string = new Displayable();
        /*for (int j = 6; j < width; j++){
            out_string.character = ' ';
            addObjectToDisplay((Displayable) out_string, j, height);
        }*/

        char[] ch = str1.toCharArray();
        int i = 0;
        for (i = 0; i < ch.length; i++){
            out_string.character = ch[i];
            addObjectToDisplay((Displayable)out_string, 6 + i, height - 1);
        }
    }

    public void clear_info(){
        String str = " ";
        for (int i = 0; i < (width - 6); i++){
            str = str + " ";
        }
        displayInfo(str);
    }    

    public boolean monster_check(char ch, int x_ps,int y_ps){
        Displayable temp_player = objectGrid[x_p][y_p].peek();
        //objectGrid[x_p][y_p].pop();
        boolean monster_found = false;
        //System.out.println(temp_player.Hp);
        if (objectGrid[x_ps][y_ps].peek() instanceof Monster){
            monster_found = true;
            Displayable temp_monster = objectGrid[x_ps][y_ps].peek();
            //objectGrid[x_ps][y_ps].pop();
            action_type = "hit";
            fight_sequence(temp_player, temp_monster, ch);
            if (temp_player.Hp < 0){ // HERE DO ALL Player type = "death" actions
                clear_info();
                action_type = "death";
                performAction((Creature)temp_player, ch);
                //objectGrid[x_p][y_p].pop();
                //objectGrid[x_ps][y_ps].pop();
                //addObjectToDisplay(temp_monster, x_p, y_p);
                //addObjectToDisplay(objectGrid[x_ps][y_ps].peek(), x_ps, y_ps);
                //displayInfo("Game is over good try!");
            }
            else if (temp_monster.Hp < 0){ // HERE DO ALL MONSTER type = "death" actions
                clear_info();
                action_type = "death";
                performAction((Creature)temp_monster, ch);
                /*if (ch == 'h'){
                    num_moves = move_update(x_p, y_p, ++num_moves);
                    objectGrid[x_p][y_p].pop();
                    objectGrid[x_p-1][y_p].pop();
                    addObjectToDisplay(temp_player, x_p-1, y_p);
                    addObjectToDisplay(objectGrid[x_p+1][y_p].peek(), x_p+1, y_p);
                    displayInfo("Monster is dead      ");
                }
                if (ch == 'l'){
                    num_moves = move_update(x_p, y_p, ++num_moves);
                    objectGrid[x_p][y_p].pop();
                    objectGrid[x_p+1][y_p].pop();
                    addObjectToDisplay(temp_player, x_p+1, y_p);
                    addObjectToDisplay(objectGrid[x_p-1][y_p].peek(), x_p-1, y_p);
                    displayInfo("Monster is dead     ");
                }
                if (ch == 'j'){
                    num_moves = move_update(x_p, y_p, ++num_moves);
                    objectGrid[x_p][y_p].pop();
                    objectGrid[x_p][y_p+1].pop();
                    addObjectToDisplay(temp_player, x_p, y_p+1);
                    addObjectToDisplay(objectGrid[x_p][y_p-1].peek(), x_p, y_p-1);
                    displayInfo("Monster is dead     ");
                }
                if (ch == 'k'){
                    num_moves = move_update(x_p, y_p, ++num_moves);
                    objectGrid[x_p][y_p].pop();
                    objectGrid[x_p][y_p-1].pop();
                    addObjectToDisplay(temp_player, x_p, y_p-1);
                    addObjectToDisplay(objectGrid[x_p][y_p+1].peek(), x_p, y_p+1);
                    displayInfo("Monster is dead     ");
                }*/
                
            }
        }
        else{
            monster_found = false;
            //addObjectToDisplay(temp_player, x_p, y_p);  
        }
        return monster_found;
    }

    public void fight_sequence(Displayable player, Displayable monster, char ch){
        Random rand = new Random();

        int player_hit = rand.nextInt(player.maxhit + 1);
        int monster_hit = rand.nextInt(monster.maxhit + 1);
        if ((monster_hit - armor_offset) < 0){
            monster_hit = 0;
        }
        else{
            monster_hit -= armor_offset;
        }
        player_hit += sword_offset; 
           
        monster.Hp -= player_hit;
        if (monster.Hp > 0){
            displayInfo("Mnstr Dmg taken " + player_hit);  
            performAction((Creature)monster, ch);
            player.Hp -= monster_hit;
        }
            // PERFORM all monster's hitactions
        if (player.Hp > 0){
            performAction((Creature)player, ch);
        }
            // PERFORM all player's hitactions
        displayHP(player.Hp);
              
    }
    public void performAction(Creature creature, char ch){
        for (int i = 0; i < creature.creature_actions.size(); i++){
            CreatureAction temp_action = creature.creature_actions.get(i);
            if ((temp_action.s).equals("Remove") && (temp_action.t).equals(action_type)){
                Remove(creature, ch);
            }
            else if ((temp_action.s).equals("Teleport") && (temp_action.t).equals(action_type)){
                Teleport(creature, ch);
                displayInfo(temp_action.actionMsg);
            }
            else if ((temp_action.s).equals("YouWin") && (temp_action.t).equals(action_type)){
                displayInfo(temp_action.actionMsg);
            }
            else if ((temp_action.s).equals("ChangeDisplayedType") && (temp_action.t).equals(action_type)){
                creature.character = temp_action.actionChar;
                Displayable temp_player = objectGrid[x_p][y_p].peek();
                objectGrid[x_p][y_p].pop();
                addObjectToDisplay(temp_player, x_p, y_p);
            }
            else if ((temp_action.s).equals("DropPack") && (temp_action.t).equals(action_type)){
                DropPack(temp_action);
            }
            else if ((temp_action.s).equals("EmptyPack") && (temp_action.t).equals(action_type)){
                EmptyPack();
            }
            else if ((temp_action.s).equals("EndGame") && (temp_action.t).equals(action_type)){
                game_over = true;
                displayInfo(temp_action.actionMsg);
            }
        }
    }
    
    public void Teleport(Creature creature, char ch){
        Random rd1 = new Random();
        int random1 = rd1.nextInt(width - 1);
        Random rd2 = new Random();
        int random2 = rd2.nextInt(height - 1);
        while (objectGrid[random1][random2].empty() || !((objectGrid[random1][random2].peek() instanceof RoomFloor) || (objectGrid[random1][random2].peek() instanceof Passage))){
            random1 = rd1.nextInt(((width - 1) - 0) + 0);
            random2 = rd2.nextInt(((height - 1) - 0) + 0);
        }
        if (ch == 'h'){
            Displayable temp_creature = objectGrid[x_p - 1][y_p].peek();
            objectGrid[x_p-1][y_p].pop();
            addObjectToDisplay(objectGrid[x_p-1][y_p].peek(), x_p-1, y_p);
            addObjectToDisplay(temp_creature, random1, random2);
        }
        else if (ch == 'j'){
            Displayable temp_creature = objectGrid[x_p][y_p+1].peek();
            objectGrid[x_p][y_p+1].pop();
            addObjectToDisplay(objectGrid[x_p][y_p+1].peek(), x_p, y_p+1);
            addObjectToDisplay(temp_creature, random1, random2);
        }
        else if (ch == 'k'){
            Displayable temp_creature = objectGrid[x_p][y_p-1].peek();
            objectGrid[x_p][y_p-1].pop();
            addObjectToDisplay(objectGrid[x_p][y_p-1].peek(), x_p, y_p-1);
            addObjectToDisplay(temp_creature, random1, random2);
        }
        else if (ch == 'l'){
            Displayable temp_creature = objectGrid[x_p + 1][y_p].peek();
            objectGrid[x_p+1][y_p].pop();
            addObjectToDisplay(objectGrid[x_p+1][y_p].peek(), x_p+1, y_p);
            addObjectToDisplay(temp_creature, random1, random2);
        }
    }
    public void Remove(Creature creature, char ch){
        if (creature instanceof Player){
            objectGrid[x_p][y_p].pop();
            addObjectToDisplay(objectGrid[x_p][y_p].peek(), x_p, y_p);
        }    
        else{
            if (ch == 'h'){
                objectGrid[x_p-1][y_p].pop();
                addObjectToDisplay(objectGrid[x_p-1][y_p].peek(), x_p-1, y_p);
                //displayInfo("Monster is dead      ");
            }
            if (ch == 'l'){
                objectGrid[x_p+1][y_p].pop();
                addObjectToDisplay(objectGrid[x_p+1][y_p].peek(), x_p+1, y_p);
                //displayInfo("Monster is dead     ");
            }
            if (ch == 'j'){
                objectGrid[x_p][y_p+1].pop();
                addObjectToDisplay(objectGrid[x_p][y_p+1].peek(), x_p, y_p+1);
                //displayInfo("Monster is dead     ");
            }
            if (ch == 'k'){
                objectGrid[x_p][y_p-1].pop();
                addObjectToDisplay(objectGrid[x_p][y_p-1].peek(), x_p, y_p-1);
                //displayInfo("Monster is dead     ");
            }
        }
    }
    public void ChangeDisplayType(Creature creature){

    }
    public void YouWin(Creature creature){
        
    }
    public void DropPack(CreatureAction temp_action){
        if (!pack.isEmpty()){
            Displayable temp_player = objectGrid[x_p][y_p].peek();
            objectGrid[x_p][y_p].pop();
            Displayable temp_item2 = new Displayable(); 
            
            temp_item2 = pack.get(0);
            if ((temp_item2 instanceof Sword) && (sword_offset != 0)){
                (((Sword)temp_item2).name) = (((Sword)temp_item2).name).replace("(w)", ""); // Then finish reading scroll, then Help ? and Help <cmd>, and do Change c
                sword_offset = 0;
            }
            else if ((temp_item2 instanceof Armor) && (armor_offset != 0)){
                (((Armor)temp_item2).name) = (((Armor)temp_item2).name).replace("(a)", "");
                armor_offset = 0;
            }
            pack.remove(0);
            addObjectToDisplay(temp_item2, x_p, y_p);
            displayInfo(temp_action.actionMsg);
            addObjectToDisplay(temp_player, x_p, y_p);
        }
        else{
            displayInfo("Pack is empty");
        }
    }
    public void EmptyPack(){
        if (!pack.isEmpty()){
            Displayable temp_player = objectGrid[x_p][y_p].peek();
            objectGrid[x_p][y_p].pop();
            Displayable temp_item2 = new Displayable(); 
            for (int i = 0; i <= pack.size(); i++){
                temp_item2 = pack.get(i);
                if ((temp_item2 instanceof Sword) && (sword_offset != 0)){
                    (((Sword)temp_item2).name) = (((Sword)temp_item2).name).replace("(w)", ""); // Then finish reading scroll, then Help ? and Help <cmd>, and do Change c
                    sword_offset = 0;
                }
                else if ((temp_item2 instanceof Armor) && (armor_offset != 0)){
                    (((Armor)temp_item2).name) = (((Armor)temp_item2).name).replace("(a)", "");
                    armor_offset = 0;
                }
                pack.remove(i);
                addObjectToDisplay(temp_item2, x_p, y_p);
            }
            addObjectToDisplay(temp_player, x_p, y_p);
        }
        else{
            displayInfo("Pack is empty");
        }
    }
    public void EndGame(){
    }

    public void displayInfo(String string){
        Displayable out_string = new Displayable();
        /*for (int j = 6; j < width; j++){
            out_string.character = ' ';
            addObjectToDisplay((Displayable) out_string, j, height);
        }*/

        char[] ch = string.toCharArray();
        int i = 0;
        for (i = 0; i < ch.length; i++){
            out_string.character = ch[i];
            addObjectToDisplay((Displayable)out_string, 6 + i, height - 1);
        }
    }

    public void displayPack(String string){
        Displayable out_string = new Displayable();
        
        char[] ch = string.toCharArray();
        int i = 0;
        for (i = 0 ; i < ch.length; i++){
            out_string.character = ch[i];
            addObjectToDisplay((Displayable) out_string, i + 6 + drop_pack_counter, height - 3);

        }
        drop_pack_counter = i + 6 + drop_pack_counter + 1;
    }

    public void displayHP(int hp){
        Displayable init_hp = new Displayable();
        for (int j = 4; j <= 7; j++){
            init_hp.character = ' ';
            addObjectToDisplay((Displayable) init_hp, j, 0);
        }
        Displayable out_hp = objectGrid[4][0].peek();
        //System.out.println("REACHED");
        char[] chars = ("" + hp).toCharArray();
        for (int i = 0; i < chars.length; i++){
            out_hp.character = chars[i];
            //System.out.println(out_hp.character);
            addObjectToDisplay(out_hp, 4+i, 0);
        }
    }

    public int move_update(int x_p,int y_p, int num_moves){
        if(num_moves == (objectGrid[x_p][y_p].peek()).hpMoves){
            objectGrid[x_p][y_p].peek().Hp += 1;
            displayHP(objectGrid[x_p][y_p].peek().Hp);
            num_moves = 0;
        }
        return num_moves;
    }    

    public void updatePlayer(char ch){
        if (!command_info){
            if (ch == 'h'){
                displayInfo("                                ");
                Displayable temp_player = objectGrid[x_p][y_p].peek();
                if(objectGrid[x_p-1][y_p].empty()){  
                    displayInfo("Error! Out of bounds");
                }
                else{
                    if(!(objectGrid[x_p-1][y_p].peek() instanceof RoomWall)){
                        if (hallucinating == true){
                            Hallucinate();
                        }    
                        boolean mnstr_found = monster_check(ch, x_p-1,y_p);
                        if (!mnstr_found){
                            num_moves = move_update(x_p, y_p, ++num_moves);     
                            addObjectToDisplay(temp_player, x_p - 1, y_p);
                            objectGrid[x_p + 1][y_p].pop();
                            Displayable temp = objectGrid[x_p+1][y_p].peek();
                            objectGrid[x_p+1][y_p].pop();
                            addObjectToDisplay(temp, x_p+1, y_p);
                        }
                    }
                    else{
                        displayInfo("Error! Boundary ahead");
                    }
                }
                
            }
            else if (ch == 'E'){
                part1_end = true;
            }
            else if ((part1_end) && (ch =='Y' || ch == 'y')){
                game_over = true;
                displayInfo("Game is over good try!"); // Clearer statement that also shows why game ended
            }
            else if (ch == 'l'){
                displayInfo("                                   ");
                Displayable temp_player = objectGrid[x_p][y_p].peek();
                if(objectGrid[x_p+1][y_p].empty()){  
                    displayInfo("Error! Out of bounds");
                }
                else{
                    if(!(objectGrid[x_p+1][y_p].peek() instanceof RoomWall)){
                        if (hallucinating == true){
                            Hallucinate();
                        }
                        boolean mnstr_found = monster_check(ch, x_p+1,y_p);
                        if(!mnstr_found){
                            num_moves = move_update(x_p, y_p, ++num_moves);
                            addObjectToDisplay(temp_player, x_p + 1, y_p);
                            objectGrid[x_p - 1][y_p].pop();
                            Displayable temp = objectGrid[x_p-1][y_p].peek();
                            objectGrid[x_p-1][y_p].pop();
                            addObjectToDisplay(temp, x_p-1, y_p);
                            
                        }                
                    }
                    else{
                        displayInfo("Error! Boundary ahead");
                    }
                }        
            }
            else if (ch == 'j'){
                displayInfo("                                 ");
                Displayable temp_player = objectGrid[x_p][y_p].peek();
                if(objectGrid[x_p][y_p+1].empty()){  
                    displayInfo("Error! Out of bounds");
                }
                else{
                    if(!(objectGrid[x_p][y_p+1].peek() instanceof RoomWall)){
                        if (hallucinating == true){
                            Hallucinate();
                        }
                        boolean mnstr_found = monster_check(ch, x_p,y_p+1);
                        if(!mnstr_found){
                            num_moves = move_update(x_p, y_p, ++num_moves);
                            addObjectToDisplay(temp_player, x_p, y_p + 1);
                            objectGrid[x_p][y_p - 1].pop();
                            Displayable temp = objectGrid[x_p][y_p - 1].peek();
                            objectGrid[x_p][y_p-1].pop();
                            addObjectToDisplay(temp, x_p, y_p - 1);
                        }
                    }
                    else{
                        displayInfo("Error! Boundary ahead");
                    }
                }
            }
            else if (ch == 'k'){
                displayInfo("                                 ");
                Displayable temp_player = objectGrid[x_p][y_p].peek();
                if(objectGrid[x_p][y_p-1].empty()){  
                    displayInfo("Error! Out of bounds");
                }
                else{
                    if(!(objectGrid[x_p][y_p-1].peek() instanceof RoomWall)){
                        if (hallucinating == true){
                            Hallucinate();
                        }
                        boolean mnstr_found = monster_check(ch, x_p,y_p-1);
                        if(!mnstr_found){
                            num_moves = move_update(x_p, y_p, ++num_moves);
                            addObjectToDisplay(temp_player, x_p, y_p - 1);
                            objectGrid[x_p][y_p + 1].pop();
                            Displayable temp = objectGrid[x_p][y_p + 1].peek();
                            objectGrid[x_p][y_p+1].pop();
                            addObjectToDisplay(temp, x_p, y_p + 1);
                        }
                    }
                    else{
                        displayInfo("Error! Boundary ahead");
                    }
                }
            }
    
            else if (ch == 'p'){
                Displayable temp_player = objectGrid[x_p][y_p].peek();
                objectGrid[x_p][y_p].pop();
                if (objectGrid[x_p][y_p].peek() instanceof RoomFloor){
                    addObjectToDisplay(temp_player, x_p, y_p);
                    displayInfo("Error! No item on floor");
                }
                else if (objectGrid[x_p][y_p].peek() instanceof Passage){
                    addObjectToDisplay(temp_player, x_p, y_p);
                    displayInfo("Error! No item on floor");
                }
                else if (objectGrid[x_p][y_p].peek() instanceof PassageJunction){
                    addObjectToDisplay(temp_player, x_p, y_p);
                    displayInfo("Error! No item on floor");
                }
                else{
                    pack.add(objectGrid[x_p][y_p].pop());
                    addObjectToDisplay(temp_player, x_p, y_p);
                }
            }
            else if (ch == 'c'){
                if (change_index >= 0){
                    Displayable temp_item3 = pack.get(change_index);
                    if ((temp_item3 instanceof Armor) && (armor_offset != 0)){
                        (((Armor)temp_item3).name) = (((Armor)temp_item3).name).replace("(a)", "");
                        armor_offset = 0;
                        change_index = -1;
                    }
                }
                else{
                    displayInfo("No armor being worn");
                }
            }
            else if (ch == 'd'){
                pack_op = 'd';
                displayInfo("Pick Item to drop");
            }
            else if (ch == 'w'){
                pack_op = 'w';
                displayInfo("Pick Armor to wear");
            }
            else if (ch == 'T'){
                pack_op = 'T';
                displayInfo("Pick weapon");
            }
            else if (ch == 'r'){
                pack_op = 'r';
                displayInfo("Read item");
            }
            else if (Character.getNumericValue(ch) >= 0 && (Character.getNumericValue(ch) <= 9)){
                int pack_index = Character.getNumericValue(ch);
                if (pack_op == 'w'){
                    if(pack_index > pack.size()){
                        displayInfo("index out of bounds");
                    }
                    else if (!(pack.get(pack_index - 1) instanceof Armor)){
                        displayInfo("item is not armor");
                    }
                    else{
                        change_index = pack_index - 1;
                        ((Armor)pack.get(pack_index - 1)).name = ((Armor)pack.get(pack_index - 1)).name + "(a)";
                        armor_offset = ((Armor)pack.get(pack_index - 1)).v;
                        worn_index = change_index;
                    }
                }
    
                else if (pack_op == 'r'){
                    if (pack_index > pack.size()){
                        displayInfo("Index out of bounds");
                    }
                    else if (!(pack.get(pack_index - 1) instanceof Scroll)){
                        displayInfo("item is not scroll");
                    }
                    else{
                        Item temp_item = (Item)pack.get(pack_index - 1);
                        if ((worn_index > -1) || (wielded_index > -1))
                        System.out.println("");
                            if (temp_item.item_action.actionChar == 'a'){
                                armor_offset += temp_item.item_action.actionInt;
                                displayInfo(temp_item.item_action.s + " changes armor effectiveness by " + temp_item.item_action.actionInt);
                                pack.remove(pack_index - 1);
                                scroll_read = true;
                            }
                            else if (temp_item.item_action.actionChar == 'w'){
                                sword_offset += temp_item.item_action.actionInt;
                                displayInfo(temp_item.item_action.s + " changes sword effectiveness by " + temp_item.item_action.actionInt);
                                pack.remove(pack_index - 1);
                            }
                        else{
                            displayInfo(temp_item.item_action.s + " nothing worn or wielded");
                        }
                        if (temp_item.item_action.s.equals("Hallucinate")){ // Hallucinate should happen for every movement, put hallucinate calls in h,j,k,l use boolean to keep it running
                            //for (int i = 0; i < temp_item.item_action.actionInt; i++){ // might change to actionInt - 1
                            hallucinating = true;
                            hallucinate_max = temp_item.item_action.actionInt;
                            //Hallucinate();
                            displayInfo("Hallucinate will last for " +(temp_item.item_action.actionInt)+ " more moves");
                            pack.remove(pack_index - 1);
                            //}
                            //HallucinateReverse();
                        }
                        // If actionChar == a, then reduce armor value by actionInt, display actionMsg
                        // hallucinate IDEAS: make sure any type checking is with instanceof not ch == ')' or sth. run double for loop through all object displaygrid stacks if hallucinate = true and while < actionInt val, and generate random character for each Displayable (series of if else statements for each displayable type) if still hallcuinating, and you reach actionint value, then reset every displayable.character to original characters then set hallucinate to false.
                    }
                }
                else if (pack_op == 'T'){
                    if(pack_index > pack.size()){
                        displayInfo("index out of bounds");
                    }
                    else if (!(pack.get(pack_index - 1) instanceof Sword)){
                        displayInfo("item is not sword");
                    }
                    else{
                        ((Sword)pack.get(pack_index - 1)).name = ((Sword)pack.get(pack_index - 1)).name + "(w)";
                        sword_offset = ((Sword)pack.get(pack_index - 1)).v;
                        wielded_index = pack_index - 1;
                    }
                }
                else if (pack_op == 'd'){ 
                    if (!pack.isEmpty()){
                        Displayable temp_player = objectGrid[x_p][y_p].peek();
                        objectGrid[x_p][y_p].pop();
                        Displayable temp_item2 = new Displayable(); 
                        if (pack_index <= pack.size()){
                            temp_item2 = pack.get(pack_index - 1);
                            if ((temp_item2 instanceof Sword) && (sword_offset != 0)){
                                (((Sword)temp_item2).name) = (((Sword)temp_item2).name).replace("(w)", ""); // Then finish reading scroll, then Help ? and Help <cmd>, and do Change c
                                sword_offset = 0;
                                wielded_index = -1;
                            }
                            else if ((temp_item2 instanceof Armor) && (armor_offset != 0)){
                                (((Armor)temp_item2).name) = (((Armor)temp_item2).name).replace("(a)", "");
                                armor_offset = 0;
                                worn_index = -1;
                            }
                            pack.remove(pack_index -1);
                            addObjectToDisplay(temp_item2, x_p, y_p);
                        }
                        else{
                            displayInfo("index out of bounds");
                        }
                        addObjectToDisplay(temp_player, x_p, y_p);
                    }
                    else{
                        displayInfo("Pack is empty");
                    }
                }
            }
    
            else if (ch == '?'){
                displayInfo("<h>, <l>, <j>, <k>, <c>, <d>, <E>, <Y>, <y>, <i>, <p>, <r>, <T>, <w>");
            }
    
            else if (ch == 'H'){
                command_info = true;
                displayInfo("Enter Command for more info");
            }
    
            else if (ch == 'i'){
                Displayable init_string = new Displayable();
                drop_pack_counter = 0;
                for (int j = 0; j < width; j++){
                    init_string.character = ' ';
                    addObjectToDisplay((Displayable) init_string, j+6, height - 3);
                }
                if(pack.isEmpty()){
                    displayInfo("Pack is empty");
                }
                for (int k = 0; k < pack.size(); k++){
                    Displayable temp_item = pack.get(k);
                    if (temp_item instanceof Armor){ 
                        if (scroll_read == true){
                            char armor_output = (char)(armor_offset + 48);
                            String final_output = String.valueOf(armor_output);
                            ((Armor)temp_item).name = ((Armor)temp_item).name.replaceAll("[0-9]", final_output);
                            scroll_read = false;
                        }
                        displayPack(+ k+1 + "." + ((Armor)temp_item).name);
                    }
                    else if (temp_item instanceof Sword){
                        displayPack(+ k+1 + "." + ((Sword)temp_item).name);
                    }
                    else if (temp_item instanceof Scroll){
                        displayPack(+ k+1 + "." + ((Scroll)temp_item).name);
                    }
                }
            }
        }
        else{
            if (ch == 'h'){
                displayInfo("Moves the player to the left by 1");
            }
            else if (ch == 'E'){
                displayInfo("Part 1 of ending the game, enter command Y or y to end");
            }
            else if (ch == 'Y'){
                displayInfo("Ends the game after entering E command");
            }
            else if (ch == 'y'){
                displayInfo("Ends the game after entering E command");
            }
            else if (ch == 'l'){
                displayInfo("Moves the player to the right by 1");
            }
            else if (ch == 'j'){
                displayInfo("Moves the player down by 1");
            }
            else if (ch == 'k'){
                displayInfo("Moves the player up by 1");
            }
            else if (ch == 'c'){
                displayInfo("Changes or takes off armor");
            }
            else if (ch == 'p'){
                displayInfo("Picks up an item from the floor");
            }
            else if (ch == 'w'){
                displayInfo("Wear an armor from the pack");
            }
            else if (ch == 'r'){
                displayInfo("read a scroll from the pack");
            }
            else if (ch == 'T'){
                displayInfo("Wield a sword from the pack");
            }
            else if (ch == 'd'){
                displayInfo("drop an item from the pack");
            }
            else if (ch == 'i'){
                displayInfo("Display the items in the pack");
            }
            else{
                displayInfo("Command does not exist");
            }
            command_info = false;
        }       
            
        
    }
    public void Hallucinate(){
        hallucinate_ind++;
        Random rand = new Random();
        for (int x = 0; x < width - 1; x++){
            for (int y = 2; y < height - 4; y++){
                if (!objectGrid[x][y].empty()){
                    Displayable temp_disp = objectGrid[x][y].peek();
                    objectGrid[x][y].pop();
                    int rand1int = rand.nextInt(11);
                    temp_disp.character = character_list.get(rand1int);
                    addObjectToDisplay(temp_disp, x, y);
                }   
            }
        }
    }
    public void HallucinateReverse(){
        for (int x = 0; x < width - 1; x++){
            for (int y = 2; y < height - 4; y++){
                if (!objectGrid[x][y].empty()){
                    Displayable temp_disp = objectGrid[x][y].peek();
                    objectGrid[x][y].pop();
                    temp_disp.character = temp_disp.orig_character;
                    addObjectToDisplay(temp_disp, x, y);
                }   
            }
        }
    }
    // we have to override, but we don't use this
    @Override
    public void keyPressed(KeyEvent even) {
    }

    // we have to override, but we don't use this
    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void fireUp() {
        if (this.requestFocusInWindow()) {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow Succeeded");
        } else {
            System.out.println(CLASSID + ".ObjectDisplayGrid(...) requestFocusInWindow FAILED");
        }
    }

    public void addObjectToDisplay(Displayable ch, int x, int y) {
        if (ch instanceof Player){
            x_p = x;
            y_p = y;
        }
        if ((0 <= x) && (x < objectGrid.length)) {
            if ((0 <= y) && (y < objectGrid[0].length)) {
                
                objectGrid[x][y].push(ch);
                writeToTerminal(x, y);
            }
        }
    }

    private void writeToTerminal(int x, int y) {
        char ch = objectGrid[x][y].peek().getChar();
        terminal.write(ch, x, y);
        terminal.repaint();
    }
}
