package com.oskin.task3.four;

import java.time.LocalDateTime;

public class AdminCarRepairTest {
    public static void main(String[] args) {
        CarRepair admin = new CarRepair();
        admin.addMaster("Сергей");
        admin.deleteMaster("Сергей");
        admin.addPlace("Офис", 20);
        admin.deletePlace("Офис");
        LocalDateTime start_time1 = LocalDateTime.of(2025, 10, 12, 14, 0);
        LocalDateTime complete_time1 = LocalDateTime.of(2025, 10, 12, 18, 0);
        LocalDateTime start_time2 = LocalDateTime.of(2025, 10, 13, 15, 0);
        LocalDateTime complete_time2 = LocalDateTime.of(2025, 10, 13, 20, 0);
        admin.addOrder("Сменить колёса", "Андрей", start_time1, complete_time1);
        admin.addOrder("Покраска", "Артём", start_time2, complete_time2);
        admin.offsetDay("Покраска", 1);
        admin.offsetHour("Покраска", 2);
        admin.completeOrder("Сменить колёса");
        admin.deleteOrder("Сменить колёса");
        admin.cancelOrder("Покраска");


    }
}
