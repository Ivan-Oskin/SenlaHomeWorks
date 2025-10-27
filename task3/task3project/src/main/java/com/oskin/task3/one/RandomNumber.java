package com.oskin.task3.one;

public class RandomNumber {
    static public void main(String[] args) {
        int mas[] = {0, 0, 0};
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            while (mas[i] < 100) {
                mas[i] = new java.util.Random().nextInt(1000);
            }
            System.out.println(mas[i]);
            sum += mas[i] / 100;
        }
        System.out.println(sum);
    }
}
