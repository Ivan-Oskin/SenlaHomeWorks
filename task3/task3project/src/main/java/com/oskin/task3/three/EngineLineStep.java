package com.oskin.task3.three;

class EngineLineStep implements ILineStep {
    public IProductLine buildProductPart() {
        Engine engine = new Engine();
        engine.SetName("Engine");
        System.out.println("Создание двигателя");
        return engine;
    }
}
