import java.util.Scanner;

void main()
{
    Scanner scanner = new Scanner(System.in);
    rose rose1 = new rose("Red", 150, true);
    rose rose2 = new rose("White", 150, false);
    tulip tulip1 = new tulip("Yellow", 200, true);
    tulip tulip2 = new tulip("Orange", 200, false);
    lily lily1 = new lily("Pink", 120);
    lily lily2 = new lily("Purple", 120);
    Sum sumflower = new Sum();
    int sum = 0;
    while (true)
    {
        System.out.println("Какие цветы вы хотите взять в букет:");
        System.out.println("1-Розы, 2-Лилии, 3-Тюльпаны\n0 - показать стоимость букета");
        int n = scanner.nextInt();
        if(n == 0)
        {
            System.out.println(sum);
            return;
        }
        if(n > 0 && n < 4)
        {
            switch (n)
            {
                case 1: sum+= sumflower.returnsum(rose1, rose2, scanner);break;
                case 2: sum+= sumflower.returnsum(lily1, lily2, scanner);break;
                case 3: sum+= sumflower.returnsum(tulip1, tulip2, scanner);break;
            }

        }

    }


}