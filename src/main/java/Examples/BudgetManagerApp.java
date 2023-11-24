package Examples;

import java.sql.*;
import java.util.Scanner;

public class BudgetManagerApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:budget.db");
            createTable(connection);

            while (true) {
                System.out.println("Выберите действие:");
                System.out.println("1. Добавить доход");
                System.out.println("2. Добавить расход");
                System.out.println("3. Показать список доходов");
                System.out.println("4. Показать список расходов");
                System.out.println("5. Выйти");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Введите сумму дохода: ");
                        double incomeAmount = scanner.nextDouble();
                        addTransaction(connection, "Доход", incomeAmount);
                        System.out.println("Доход добавлен.");
                        break;
                    case 2:
                        System.out.print("Введите сумму расхода: ");
                        double expenseAmount = scanner.nextDouble();
                        addTransaction(connection, "Расход", expenseAmount);
                        System.out.println("Расход добавлен.");
                        break;
                    case 3:
                        listTransactions(connection, "Доход");
                        break;
                    case 4:
                        listTransactions(connection, "Расход");
                        break;
                    case 5:
                        System.out.println("Программа завершена.");
                        connection.close();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите действие от 1 до 5.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS transactions ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "type TEXT,"
                + "amount REAL,"
                + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(createTableSQL);
    }

    private static void addTransaction(Connection connection, String type, double amount) throws SQLException {
        String insertSQL = "INSERT INTO transactions (type, amount) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, type);
            preparedStatement.setDouble(2, amount);
            preparedStatement.executeUpdate();
        }
    }

    private static void listTransactions(Connection connection, String type) throws SQLException {
        String selectSQL = "SELECT * FROM transactions WHERE type = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Список " + type.toLowerCase() + "ов:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double amount = resultSet.getDouble("amount");
                Timestamp timestamp = resultSet.getTimestamp("timestamp");
                System.out.println(id + ". Сумма: " + amount + ", Дата: " + timestamp);
            }
        }
    }
}

