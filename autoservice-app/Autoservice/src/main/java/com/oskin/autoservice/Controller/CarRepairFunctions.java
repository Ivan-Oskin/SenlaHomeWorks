package com.oskin.autoservice.Controller;
import com.oskin.Annotations.*;
import com.oskin.autoservice.Model.*;

import java.util.ArrayList;


@Singleton
public class CarRepairFunctions {
    //Метод для поиска

    public  <T extends Nameable> int findByName(String name, ArrayList<T> list) {
        int number = -1;
        for (int i = 0; i < list.size(); i++) {
            if (name.equals(list.get(i).getName())) {
                number = i;
                break;
            }
        }
        return number;
    }
    public  <T extends Nameable> int findById(int id, ArrayList<T> list) {
        int number = -1;
        for(T element : list){
            if(id == element.getId())
                return list.indexOf(element);
        }
        return number;
    }


    // Метод для удаления

    public  <T extends Nameable> boolean delete(String name, ArrayList<T> list) {
        int i = findByName(name, list);
        if (i == -1) {
            return false;
        } else {
            list.remove(i);
            return true;
        }
    }
}



