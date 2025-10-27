package com.oskin.task3.two;

import java.util.Scanner;

public class FlowerShop {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Rose roseRed = new Rose("Red", 150, true);
        Rose roseWhite = new Rose("White", 150, false);
        Tulip tulipYellow = new Tulip("Yellow", 200, true);
        Tulip tulipOrange = new Tulip("Orange", 200, false);
        Lily lilyPink = new Lily("Pink", 120);
        Lily lilyPurple = new Lily("Purple", 120);
        int sum = 0;
        while (true) {
            System.out.println("Какие цветы вы хотите взять в букет:");
            System.out.println("1-Розы, 2-Лилии, 3-Тюльпаны\n0 - показать стоимость букета");
            int n = scanner.nextInt();
            if (n == 0) {
                System.out.println(sum);
                return;
            }
            if (n > 0 && n < 4) {
                switch (n) {
                    case 1:
                        sum += getCount(roseRed, roseWhite, scanner);
                        break;
                    case 2:
                        sum += getCount(lilyPink, lilyPurple, scanner);
                        break;
                    case 3:
                        sum += getCount(tulipOrange, tulipYellow, scanner);
                        break;
                }
            }

        }
    }

    public static double getCount(Flower flower1, Flower flower2, Scanner scanner) {
        double price = 0;

        System.out.println("Какие " + flower1.getName() + " вы хотите взять в букет:");
        System.out.println("1." + flower1.getColor() + " " + flower1.getPrice());
        System.out.println("2." + flower2.getColor() + " " + flower2.getPrice());
        while (true) {
            int n = scanner.nextInt();
            if (n == 1) {
                price = flower1.getPrice();
                break;
            }
            if (n == 2) {
                price = flower2.getPrice();
                break;
            }

        }
        System.out.println("Введите количество:");
        int m = scanner.nextInt();
        System.out.println(m * price);
        return m * price;
    }

}
