class main
{
    void main(String[] args)
    {
        int mas[] = new int[3];
        int sum = 0;
        for(int i = 0; i < 3; i++)
        {
            mas[i] = new java.util.Random().nextInt(100, 999);
            System.out.println(mas[i]);
            sum += mas[i]/100;
        }
        System.out.println(sum);
    }
}