import java.time.LocalDateTime;
import java.util.ArrayList;

abstract class Name
{
    private String name;

    String GetName()
    {
        return this.name;
    }
    Name(String name)
    {
        this.name = name;
    }
}

class master extends Name
{

    master(String name)
    {
        super(name);
    }
}

class place extends Name
{

    private int volume;



    place(String name, int vol)
    {
        super(name);
        this.volume = vol;
    }
    int GetVolume()
    {
        return this.volume;
    }

}

class order extends Name
{
    private String customer;
    private String status;
    private LocalDateTime time_start;
    private LocalDateTime time_complete;

    void close()
    {
        this.status = "close";
    }
    void cancel()
    {
        this.status = "cancel";
    }
    String GetStatus()
    {
        return this.status;
    }
    String GetCustomer()
    {
        return this.customer;
    }
    String GetTime()
    {
        return "start: "+this.time_start +"\n"+"complete: "+this.time_complete;
    }


    order(String name, String customer, LocalDateTime time_start, LocalDateTime time_complete)
    {
        super(name);
        this.customer = customer;
        this.time_start = time_start;
        this.time_complete = time_complete;
        this.status = "active";
    }

    void ChangeDay(int day)
    {
        this.time_start = time_start.plusDays(day);
        this.time_complete = time_complete.plusDays(day);
        System.out.println("Change:\nstart: " + time_start + "\ncomplete: " + time_complete);
    }
    void ChangeHour(int hour)
    {
        this.time_start = time_start.plusHours(hour);
        this.time_complete = time_complete.plusHours(hour);
        System.out.println("Change:\nstart: " + time_start + "\ncomplete: " + time_complete);
    }
}


class CarRepair
{
    private ArrayList<Name> masters = new ArrayList<>();
    private ArrayList<Name> garage = new ArrayList<>();
    private ArrayList<Name> orders = new ArrayList<>();

    //Метод для удаления
    private void Delete(String name, ArrayList<Name> list)
    {
        boolean flag = false;
        for (int i = 0; i < list.size(); i++)
        {
            if(name.equals(list.get(i).GetName()))
            {
                flag = true;
                System.out.println(list.get(i).GetName()+" - удалено");
                list.remove(i);

                break;
            }
        }
        if(!flag)
        {
            System.out.println("Имя не найдено");
        }
    }
    //Добавить/удалить мастера
    void AddMaster(String name)
    {
        master master = new master(name);
        masters.add(master);
        System.out.println("Мастер добавлен");
    }
    void DeleteMaster(String name)
    {
        Delete(name, masters);
    }
    //добавить/удалить место
    void AddPlace(String name, int volume)
    {
        place place = new place(name, volume);
        garage.add(place);
        System.out.println("Место в гараже добавлено");
    }

    void DeletePlace(String name)
    {
        Delete(name, garage);
    }
    //Добавить/удалить/закрыть/отменить заказ
    void AddOrder(String name, String customer, LocalDateTime timestart, LocalDateTime timecopmlete)
    {
        order order = new order(name, customer, timestart, timecopmlete);
        orders.add(order);
        System.out.println("Заказ добавлен");
    }
    void DeleteOrder(String name)
    {
        Delete(name, orders);
    }
    void CompleteOrder(String name)
    {
        boolean flag = false;
        for (int i = 0; i < orders.size(); i++)
        {
            if (name.equals(orders.get(i).GetName()))
            {
                flag = true;
                order order = (order) orders.get(i);
                order.close();
                System.out.println(order.GetName()+" закрыт");
                break;
            }
        }
        if(!flag)
        {
            System.out.println("Имя не найдено");
        }
    }
    void CancelOrder(String name)
    {
        boolean flag = false;
        for (int i = 0; i < orders.size(); i++)
        {
            if (name.equals(orders.get(i).GetName()))
            {
                flag = true;
                order order = (order) orders.get(i);
                order.cancel();
                System.out.println(order.GetName()+" отменён");
                break;
            }
        }
        if(!flag)
        {
            System.out.println("Имя не найдено");
        }
    }
    //метод для смещения времени
    private void offset(String name, int kol, boolean IsDay)
    {
        boolean flag = false;

        for(int i = 0; i < orders.size(); i++)
        {
            order order = (order) orders.get(i);
            if (name.equals(orders.get(i).GetName()))
            {

                flag = true;
            }
            if(flag)
            {
                if(IsDay) order.ChangeDay(kol);
                else order.ChangeHour(kol);
            }
        }
    }
    void offsetDay(String name, int day)
    {
        offset(name, day, true);
    }
    void offsetHour(String name, int hour)
    {
        offset(name, hour, false);
    }
}


class test
{
    void main(String[] args) {
        CarRepair admin = new CarRepair();
        admin.AddMaster("Сергей");
        admin.DeleteMaster("Сергей");
        admin.AddPlace("Офис", 20);
        admin.DeletePlace("Офис");
        LocalDateTime start_time1 = LocalDateTime.of(2025, 10, 12, 14, 0);
        LocalDateTime complete_time1 = LocalDateTime.of(2025, 10, 12, 18, 0);
        LocalDateTime start_time2 = LocalDateTime.of(2025, 10, 13, 15, 0);
        LocalDateTime complete_time2 = LocalDateTime.of(2025, 10, 13, 20, 0);
        admin.AddOrder("Сменить колёса", "Андрей", start_time1, complete_time1);
        admin.AddOrder("Покраска", "Артём", start_time2, complete_time2);
        admin.offsetDay("Покраска", 1);
        admin.offsetHour("Покраска", 2);
        admin.CompleteOrder("Сменить колёса");
        admin.DeleteOrder("Сменить колёса");
        admin.CancelOrder("Покраска");


    }
}