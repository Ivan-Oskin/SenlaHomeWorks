public abstract class Flower
{
    private String name;
    private double price;
    private String color;
    public Flower(String name, String color, double price)
    {
        this.name = name;
        this.price = price;
        this.color = color;

    }
    public String GetName()
    {
        return this.name;
    }
    public  String GetColor()
    {
        return  this.color;
    }
    public double GetPrice()
    {
        return this.price;
    }

}
