package com.oskin.task3.three;

class Product implements IProduct {
    private ProductsLines First;
    private ProductsLines Second;
    private ProductsLines Third;

    public void installFirstPart(IProductLine a) {
        this.First = (ProductsLines) a;
        System.out.println("Первая часть установлена: " + First.GetName());
    }

    public void installSecondPart(IProductLine b) {
        this.Second = (ProductsLines) b;
        System.out.println("Вторая часть установлена: " + Second.GetName());
    }

    public void installThirdPart(IProductLine c) {
        this.Third = (ProductsLines) c;
        System.out.println("Третья часть установлена: " + Third.GetName());
    }
}
