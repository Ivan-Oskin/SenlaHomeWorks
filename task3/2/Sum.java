import java.util.Scanner;

public class Sum {
    public double returnsum(Flower flower1, Flower flower2, Scanner scanner)
    {
        double price = 0;

        System.out.println("Какие "+flower1.GetName()+" вы хотите взять в букет:");
        System.out.println("1."+flower1.GetColor()+" "+flower1.GetPrice());
        System.out.println("2."+flower2.GetColor()+" "+flower2.GetPrice());
        while (true)
        {
            int n = scanner.nextInt();
            if(n == 1) {price = flower1.GetPrice();break;}
            if(n == 2) {price = flower2.GetPrice();break;}

        }
        System.out.println("Введите количество:");
        int m = scanner.nextInt();
        System.out.println(m*price);
        return m*price;
    }
}
