package game;
public class Item extends Displayable{
    // No constructor for now - zero arg
    public Creature owner;
    public ItemAction item_action;
    public void setOwner(Creature _owner){
        System.out.println("    set Owner in Item class");
        owner = _owner;
    }
    public void setItemAction(ItemAction _action){
        item_action = _action;
    }
}