package com.oskin.task3.four;

public class Place extends ObjectName {
    private int volume;

    Place(String name, int vol) {
        super(name);
        this.volume = vol;
    }

    int getVolume() {
        return this.volume;
    }
}
