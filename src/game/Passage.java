package game;
import java.util.ArrayList;
public class Passage extends Structure{
    // No constructor for now - zero arg 
    public String s;
    public int room1;
    public int room2;
    public int p_x;
    public ArrayList<ArrayList<Integer>> Passage_list = new ArrayList<ArrayList<Integer>>(); 

    public void setName(String _s){
        System.out.println("    set name");
        s = _s;
    }

    
    public void setID(int _room1, int _room2){
        System.out.println("    room1");
        room1 = _room1;
        System.out.println("    room2");
        room2 = _room2;
    }
    
    @Override
    public void SetPosX(int _p_x){
        p_x = _p_x;
        // FIGURE OUT WHAT TO DO HERE. LIKELY CREATE AN ARRAYLIST OF x,y and create links between each consecutive pair
    }
    @Override
    public void setPosY(int _p_y){
        ArrayList<Integer> temp_psg = new ArrayList<Integer>();
        temp_psg.add(p_x);
        temp_psg.add(_p_y);
        Passage_list.add(temp_psg);
    }
}