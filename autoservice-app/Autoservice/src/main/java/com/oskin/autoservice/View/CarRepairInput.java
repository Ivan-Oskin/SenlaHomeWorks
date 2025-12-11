package com.oskin.autoservice.View;
import com.oskin.DI.Inject;
import com.oskin.DI.Singleton;
import com.oskin.autoservice.Model.*;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

@Singleton
public class CarRepairInput {
    Scanner scanner = new Scanner(System.in);
    private static CarRepairInput instance;

    @Inject
    CarRepairOutput carRepairOutput;

    public CarRepairInput() {
    }
    public int inputInt(){
        int input = 0;
        try{
            input = scanner.nextInt();
            scanner.nextLine();
        }
        catch (InputMismatchException e){
            scanner.nextLine();
            System.err.println("\nНадо ввести только цифру!!!\n");
        }
        return input;
    }

    public SortTypeOrder whatSortTypeOrder(){
        System.out.println("Выберите порядок:\n1.По ID 2. По дате подачи 3. По дате начала 4.По дате выполнения 5. по цене");
        int x;
        while (true){
            x = inputInt();
            if(x > 0 && x < 6) break;
            System.out.println("Неправильный ввод");
        }
        if(x == 1) return SortTypeOrder.ID;
        if(x == 2) return SortTypeOrder.CREATE;
        if(x == 3) return SortTypeOrder.START;
        if(x == 4) return SortTypeOrder.COMPLETE;
        return SortTypeOrder.COST;
    }

    public SortTypeMaster whatSortTypeMaster(){
        System.out.println("Выберите порядок:\n1.По ID 2.По алфавиту 3.По занятности");
        int x;
        while (true){
            x = inputInt();
            if(x > 0 && x < 4) break;
            System.out.println("Неправильный ввод");
        }
        if(x == 1) return SortTypeMaster.ID;
        if(x == 2) return SortTypeMaster.ALPHABET;
        return SortTypeMaster.BUSYNESS;
    }
    public String inputName(String about){
        carRepairOutput.infAboutInput(about);
        return scanner.nextLine();
    }
    public LocalDateTime createTime() {

        carRepairOutput.infAboutInput("год");
        int year;
        int month;
        int day;
        int hour;
        try{
            while (true) {
                year = scanner.nextInt();
                scanner.nextLine();
                if (year >= LocalDateTime.now().getYear() && year < 2030) break;
                System.out.println("Неправильный ввод");
            }
            carRepairOutput.infAboutInput("Месяц");
            while (true) {
                month = scanner.nextInt();
                scanner.nextLine();
                if (month >= 1 && month <= 12) {
                    break;
                }
                System.out.println("Неправильный ввод");
            }
            carRepairOutput.infAboutInput("День");
            while (true) {
                day = scanner.nextInt();
                scanner.nextLine();
                if (day >= 1 && day <= 31) {
                    break;
                }
                System.out.println("Неправильный ввод");
            }
            carRepairOutput.infAboutInput("Часы");
            while (true) {
                hour = scanner.nextInt();
                scanner.nextLine();
                if (hour >= 9 && hour < 18) {
                    break;
                }
                System.out.println("Неправильный ввод");
            }
            LocalDateTime time = LocalDateTime.of(year, month, day, hour, 0);
            return time;
        }
        catch (InputMismatchException e){
            scanner.nextLine();
            System.err.println("\nНеправильный ввод, нужно вводить только цифры\n");
            return null;
        }
    }

    public StatusOrder whatStatus(){
        System.out.println("Выберите статус");
        System.out.println("1. активный 2. закрытый 3. отменённый ");
        int x;
        while (true){
            x = inputInt();
            if(x > 0 && x < 4) break;
            System.out.println("Неправильный ввод");
        }
        if(x == 1) return StatusOrder.ACTIVE;
        if(x == 2) return StatusOrder.CLOSE;
        return StatusOrder.CANCEL;
    }
}
