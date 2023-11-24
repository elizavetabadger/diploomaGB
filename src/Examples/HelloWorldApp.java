package Examples;

import java.util.Scanner;
public class HelloWorldApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите ваше имя: ");
        String name = scanner.nextLine();

        System.out.println("Привет, " + name + "!");
    }
}

  // Объяснение выбранных функций и техник разработки:
  // import java.util.Scanner; - импорт класса Scanner из пакета java.util, который позволяет считывать ввод пользователя.
  // public class HelloWorldApp { - объявление публичного класса HelloWorldApp.
  // public static void main(String[] args) { - объявление точки входа в программу, метода main.
  // Scanner scanner = new Scanner(System.in); - создание экземпляра класса Scanner для считывания ввода пользователя из консоли.
  // System.out.print("Введите ваше имя: "); - вывод строки с приглашением пользователя ввести имя.
  // String name = scanner.nextLine(); - считывание введенного пользователем имени и сохранение его в переменную name типа String.
  // System.out.println("Привет, " + name + "!"); - вывод приветственного сообщения с использованием введенного имени.

