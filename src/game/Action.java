package game;
public class Action {
    public String msg;
    public int v;
    public char c;
    public char actionChar;
    public int actionInt;
    public String actionMsg;
    public void setMessage(String _msg){
        
        msg = _msg;
        System.out.println("    set action message");
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
    public void setIntValue(int _v){
        v = _v;
    }

    public void setCharValue(char _c){
        c = _c;
    }
}
