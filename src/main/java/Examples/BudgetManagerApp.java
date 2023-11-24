package Examples;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
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
                System.out.println("5. Удалить запись");
                System.out.println("6. Рассчитать общий баланс");
                System.out.println("7. Вывести диаграмму структурирования бюджета");
                System.out.println("8. Выйти");

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
                        System.out.print("Введите ID записи для удаления: ");
                        int recordId = scanner.nextInt();
                        deleteTransaction(connection, recordId);
                        System.out.println("Запись удалена.");
                        break;
                    case 6:
                        double balance = calculateBalance(connection);
                        System.out.println("Общий баланс: " + balance);
                        break;
                    case 7:
                        showBudgetStructureChart(connection);
                        break;
                    case 8:
                        System.out.println("Программа завершена.");
                        connection.close();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, выберите действие от 1 до 8.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    private static void deleteTransaction(Connection connection, int recordId) throws SQLException {
        String deleteSQL = "DELETE FROM transactions WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, recordId);
            preparedStatement.executeUpdate();
        }
    }

    private static double calculateBalance(Connection connection) throws SQLException {
        String selectIncomeSQL = "SELECT SUM(amount) FROM transactions WHERE type = 'Доход'";
        String selectExpenseSQL = "SELECT SUM(amount) FROM transactions WHERE type = 'Расход'";
        double income = 0.0;
        double expense = 0.0;

        try (Statement statement = connection.createStatement()) {
            ResultSet incomeResult = statement.executeQuery(selectIncomeSQL);
            if (incomeResult.next()) {
                income = incomeResult.getDouble(1);
            }

            ResultSet expenseResult = statement.executeQuery(selectExpenseSQL);
            if (expenseResult.next()) {
                expense = expenseResult.getDouble(1);
            }
        }

        return income - expense;
    }

    private static void showBudgetStructureChart(Connection connection) throws SQLException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        String selectExpenseByTypeSQL = "SELECT type, SUM(amount) FROM transactions WHERE type = 'Расход' GROUP BY type";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectExpenseByTypeSQL);
            while (resultSet.next()) {
                String type = resultSet.getString(1);
                double amount = resultSet.getDouble(2);
                dataset.setValue(type, amount);
            }
        }

        JFreeChart chart = ChartFactory.createPieChart("Структура бюджета", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        JFrame frame = new JFrame("Диаграмма структурирования бюджета");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
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
