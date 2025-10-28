package com.oskin.task3.three;

public class AssemblyLine implements IAssemblyLine {
    EngineLineStep first;
    CassisLineStep second;
    BodyLineStep third;

    public IProduct assemblyProduct(IProduct car) {


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

    AssemblyLine(EngineLineStep engine, CassisLineStep cassis, BodyLineStep body) {
        this.first = engine;
        this.second = cassis;
        this.third = body;
    }
}
