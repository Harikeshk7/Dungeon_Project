package game;
public class BlessCurseOwner extends ItemAction{
    private Item owner;
    public BlessCurseOwner(Item _owner){
        super(_owner);
        System.out.println("    set BlessCurseOwner");
        owner = _owner;
    }
}