package com.oskin.task3.three;

public class Factory {
    public static void main(String[] args) {
        EngineLineStep engine = new EngineLineStep();
        CassisLineStep cassis = new CassisLineStep();
        BodyLineStep body = new BodyLineStep();
        AssemblyLine Assembly = new AssemblyLine(engine, cassis, body);
        IProduct car = new Product();
        car = Assembly.assemblyProduct(car);
    }
}
