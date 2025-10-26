public class tulip extends Flower
{
    private boolean openedBud;

    public tulip(String color, int price, boolean openedBud)
    {
        super("tulip", color, price);
        this.openedBud = openedBud;
    }
    public boolean IsOpenBud()
    {
        return openedBud;
    }
}
