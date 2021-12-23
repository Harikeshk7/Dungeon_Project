package game;
public class ItemAction extends Action{
    public Item ownernew;
    public String s;
    public String t;
    public ItemAction(Item _ownernew){
        System.out.println("    set ItemAction");
        ownernew = _ownernew;
    }
    public void setName(String _s){
        System.out.println("    set name");
        s = _s;
    }

    public void setType(String _t){
        System.out.println("    set type");
        t = _t;
    }
}

