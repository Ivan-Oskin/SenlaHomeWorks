package com.oskin.task3.three;

class BodyLineStep implements ILineStep {

    public IProductLine buildProductPart() {
        Body body = new Body();
        body.SetName("body");
        System.out.println("Создание корпуса");
        return body;
    }

}
