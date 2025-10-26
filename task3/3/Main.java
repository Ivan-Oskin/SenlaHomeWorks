import java.lang.classfile.Interfaces;

interface IProductLine
{
    public String GetName();
}

interface ILineStep
{
    IProductLine buildProductPart();
}

interface IProduct
{
    void installFirstPart(IProductLine a);
    void installSecondPart(IProductLine b);
    void installThirdPart(IProductLine c);
}

interface IAssemblyLine
{
    IProduct assemblyProduct(IProduct a);
}
abstract class Products
{
    String name;
    public String GetName()
    {
        return this.name;
    }
    public void SetName(String name)
    {
        this.name = name;
    }
}
class Engine extends Products implements IProductLine
{

}
class Cassis extends Products implements IProductLine
{

}
class Body extends Products implements IProductLine
{

}

class Product implements IProduct
{
    private IProductLine First;
    private IProductLine Second;
    private IProductLine Third;


    @Override
    public void installFirstPart(IProductLine a)
    {
        this.First = a;
        System.out.println("Первая часть установлена: "+ a.GetName());
    }
    public void installSecondPart(IProductLine b)
    {
        this.First = b;
        System.out.println("Вторая часть установлена: "+b.GetName());
    }
    public void installThirdPart(IProductLine c)
    {
        this.First = c;
        System.out.println("Третья часть установлена: "+c.GetName());
    }
}

class EngineLineStep implements ILineStep
{
    @Override
    public IProductLine buildProductPart()
    {
        Engine engine = new Engine();
        engine.SetName("Engine");
        System.out.println("Создание двигателя");
        return engine;
    }
}
class CassisLineStep implements ILineStep
{

    public IProductLine buildProductPart()
    {
        Cassis cassis = new Cassis();
        cassis.SetName("cassis");
        System.out.println("Создание шасси");
        return cassis;
    }
}
class BodyLineStep implements ILineStep
{

    public IProductLine buildProductPart()
    {
        Body body = new Body();
        body.SetName("body");
        System.out.println("Создание корпуса");
        return body;
    }

}

class AssemblyCar implements IAssemblyLine
{
    EngineLineStep first;
    CassisLineStep second;
    BodyLineStep third;

    public IProduct assemblyProduct(IProduct car)
    {


        System.out.println("Начало сборки");
        IProductLine engine = first.buildProductPart();
        car.installFirstPart(engine);
        IProductLine cassis = second.buildProductPart();
        car.installSecondPart(cassis);
        IProductLine body = third.buildProductPart();
        car.installThirdPart(body);
        System.out.println("Машина собрана");
        return car;
    }

    AssemblyCar(EngineLineStep engine, CassisLineStep cassis, BodyLineStep body)
    {
        this.first = engine;
        this.second = cassis;
        this.third = body;
    }
}


class main
{
    public static void main(String[] args)
    {
        EngineLineStep engine = new EngineLineStep();
        CassisLineStep cassis = new CassisLineStep();
        BodyLineStep body = new BodyLineStep();
        AssemblyCar Assembly = new AssemblyCar(engine, cassis, body);
        IProduct car = new Product();
        car = Assembly.assemblyProduct(car);
    }
}
