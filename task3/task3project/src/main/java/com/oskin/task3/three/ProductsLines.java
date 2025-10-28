package com.oskin.task3.three;

public abstract class ProductsLines implements IProductLine{
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
