package Examples;


import java.util.Scanner;

public class BudgetTrackerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double balance = 0.0;

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1. Добавить доход");
            System.out.println("2. Добавить расход");
            System.out.println("3. Показать баланс");
            System.out.println("4. Выйти");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Введите сумму дохода: ");
                    double income = scanner.nextDouble();
                    balance += income;
                    System.out.println("Доход добавлен.");
                    break;
                case 2:
                    System.out.print("Введите сумму расхода: ");
                    double expense = scanner.nextDouble();
                    balance -= expense;
                    System.out.println("Расход добавлен.");
                    break;
                case 3:
                    System.out.println("Текущий баланс: " + balance);
                    break;
                case 4:
                    System.out.println("Программа завершена.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, выберите действие от 1 до 4.");
            }
        }
    }
}

