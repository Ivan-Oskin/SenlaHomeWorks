package com.oskin.task3.three;

class CassisLineStep implements ILineStep {

    public IProductLine buildProductPart() {
        Cassis cassis = new Cassis();
        cassis.SetName("cassis");
        System.out.println("Создание шасси");
        return cassis;
    }
}