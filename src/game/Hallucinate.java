package game;
public class Hallucinate extends ItemAction{
    private Item owner;
    public Hallucinate(Item _owner){
        super(_owner);
        System.out.println("    set Hallucinate");
        owner = _owner;
    }
}