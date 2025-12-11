package com.oskin.DI;

public interface IBuilder {
    public <T> T create(Class<T> type);


}
