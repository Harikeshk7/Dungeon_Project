package game;
public class UpdateDisplay extends CreatureAction{
    private String name;
    private Creature owner;

    public UpdateDisplay(String _name, Creature _owner){
        super(_owner);
        System.out.println("Updating Display");
        name = _name;
        owner = _owner;
    }
}