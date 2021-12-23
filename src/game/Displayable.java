package game;
public class Displayable{
public int maxhit;
public int hpMoves;
public int Hp;
public int v;
public int x1;
public int x2;
public int y1;
public int y2;
public char t;
public int visible;
public int invisible;
public char character;
public char orig_character;
public char actionChar;
public int actionInt;
public String actionMsg;
     // No constructor called for now - zero arg constructor

    public char getChar(){
        return character;
    }
    public void setActionMsg(String _msg){
        actionMsg = _msg;
    }
    public void setActionInt(int _int){
        actionInt = _int;
    }
    public void setActionChar(char _char){
        actionChar = _char;
    }
    public void setInvisible(int _invisible){
        // If invisble, visible is set to 0
        invisible = _invisible;
        System.out.println("    invisible");
        
    }   

    public void setVisible(int _visible){
        // If visble, invisible is set to 0
       visible = _visible;
       System.out.println("     visible");
    }

    public void setMaxHit(int _maxhit){
        System.out.println("    max hits set");
        maxhit = _maxhit;
    }

    public void setType(char _t){
        System.out.println("    type set");
        t = _t;
    }

    public void HPMove(int _hpMoves){
        System.out.println("    set HP Moves");
        hpMoves = _hpMoves;
    }

    public void setHp(int _Hp){
        System.out.println("    setHP");
        Hp = _Hp;
    }

    public void setIntValue(int _v){
        System.out.println("    set int value");
        v = _v;
    }
    
    public void SetPosX(int _x1){
        System.out.println("    x pos set");
        x1 = _x1;
    }

    public void setPosY(int _y1){
        System.out.println("    y pos set");
        y1 = _y1;
    }

    public void SetWidth(int _x2){
        System.out.println("    set width");
        x2 = _x2;
    }

    public void setHeight(int _y2){
        System.out.println("    set height");
        y2 = _y2;
    }


}


