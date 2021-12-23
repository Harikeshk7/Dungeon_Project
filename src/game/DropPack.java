package game;
public class DropPack extends CreatureAction {
    private String name;
    private Creature owner;

    public DropPack(String _name, Creature _owner){
        super(_owner);
        System.out.println("    set DropBack");
        name = _name;
        owner = _owner;
    }
}
