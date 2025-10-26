public class rose extends Flower
{
    private boolean needles;
    public rose(String color, int price, boolean needles)
    {
        double priceSum = price;
        if(needles)
        {
            priceSum += 50;
        }
        super("Rose", color, priceSum);
        this.needles = needles;
    }
    public boolean IsNeedles()
    {
        return this.needles;
    }
}
