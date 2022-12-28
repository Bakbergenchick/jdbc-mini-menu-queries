package com.jdbc.student;

import java.sql.*;
import java.util.Scanner;

public class StudentDB {
    private static Connection connection = null;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String dbURL = "jdbc:mysql://localhost:3306/jdbcdb";
            String username = "root";
            String password = "";
            connection = DriverManager.getConnection(dbURL, username, password);
            System.out.println("Choose the option of operation: ");
            System.out.println("[1] Insert the record");
            System.out.println("[2] Select the record by id");
            System.out.println("[3] Callable statement: Select all records");
            System.out.println("[4] Callable statement: Select record by id");
            System.out.println("[5] Update the record");
            System.out.println("[6] Delete the record");
            System.out.println("[7] Transaction example ");
            System.out.println("[8] Batch insert");
            int i = sc.nextInt();

            switch (i) {
                case 1:
                    insert();
                    break;
                case 2:
                    selectByID();
                    break;
                case 3:
                    calllableSelectAll();
                    break;
                case 4:
                    callableSelectById();
                    break;
                case 5:
                    update();
                    break;
                case 6:
                    delete();
                    break;
                case 7:
                    transactionExample();
                    break;
                case 8:
                    batchInsert();
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            throw new RuntimeException("Something went wrong...");
        }
    }

    public static void insert() throws SQLException {
        System.out.println("Inside insert() method...");

        String sql = "insert into student (name, percentage, address)" +
                " values (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        System.out.print("Enter the user name: ");
        preparedStatement.setString(1, sc.next());
        System.out.print("Enter the percentage: ");
        preparedStatement.setDouble(2, sc.nextDouble());
        System.out.print("Enter the address: ");
        preparedStatement.setString(3, sc.next());

        int rows = preparedStatement.executeUpdate();

        if (rows > 0) {
            System.out.println("Inserted successfully!");
        }
    }

    public static void selectByID() throws SQLException {
        System.out.println("Inside selectById() method...");
        System.out.print("Enter the user ID: ");
        int uid = sc.nextInt();
        String sql = "select * from student where id = " + uid;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        if (resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            double percentage = resultSet.getDouble("percentage");
            String address = resultSet.getString("address");

            System.out.println("ID = " + id);
            System.out.println("Name: " + name);
            System.out.println("Percentage: " + percentage);
            System.out.println("Address: " + address);
        }else {
            System.out.println("No records...");
        }

    }

    public static void calllableSelectAll() throws SQLException {
        System.out.println("Inside selectAll() method...");
        CallableStatement callableStatement = connection.prepareCall("{ call GET_ALL() }");

        ResultSet resultSet = callableStatement.executeQuery();

        while (resultSet.next()){
            System.out.println("Id: " + resultSet.getString("id"));
            System.out.println("Name: " + resultSet.getString("name"));
            System.out.println("Percentage: " + resultSet.getString("percentage"));
            System.out.println("Address: " + resultSet.getString("address"));
            System.out.println("------------------------");
        }

    }

    public static void callableSelectById() throws SQLException {
        System.out.println("Inside selectById() method...");
        CallableStatement callableStatement = connection.prepareCall("{ call GET_BY_ID(?) }");
        System.out.print("Enter the user ID: ");
        callableStatement.setInt(1, sc.nextInt());
        ResultSet resultSet = callableStatement.executeQuery();

        if (resultSet.next()){
            System.out.println("Id: " + resultSet.getString("id"));
            System.out.println("Name: " + resultSet.getString("name"));
            System.out.println("Percentage: " + resultSet.getString("percentage"));
            System.out.println("Address: " + resultSet.getString("address"));
        } else {
            System.out.println("No records...");
        }
    }

    public static void update() throws SQLException {
        System.out.println("Inside update() method...");
        System.out.print("Enter the user id: ");
        int uid = sc.nextInt();
        String sql = "select * from student where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, uid);

        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            System.out.println("Id: " + resultSet.getString("id"));
            System.out.println("Name: " + resultSet.getString("name"));
            System.out.println("Percentage: " + resultSet.getString("percentage"));
            System.out.println("Address: " + resultSet.getString("address"));

            System.out.println("Which parameter you want to update?");
            System.out.println("[1] Name");
            System.out.println("[2] Percentage");
            System.out.println("[3] Address");
            int n = sc.nextInt();
            String sqlUpdate = "update student set ";
            switch (n){
                case 1:
                    System.out.print("Enter the new name: ");
                    sqlUpdate=sqlUpdate + "name=? where id=" + uid;
                    PreparedStatement preparedStatement1 = connection.prepareStatement(sqlUpdate);
                    preparedStatement1.setString(1, sc.next());
                    int rows = preparedStatement1.executeUpdate();

                    if (rows > 0) {
                        System.out.println("Name updated successfully!");
                    } else{
                        System.out.println("Operation have been failed!");
                    }
                    break;
                case 2:
                    System.out.print("Enter the new percentage: ");
                    sqlUpdate=sqlUpdate + "percentage=? where id=" + uid;
                    PreparedStatement preparedStatement2 = connection.prepareStatement(sqlUpdate);
                    preparedStatement2.setDouble(1, sc.nextDouble());
                    int rows1 = preparedStatement2.executeUpdate();

                    if (rows1 > 0) {
                        System.out.println("Percentage updated successfully!");
                    } else{
                        System.out.println("Operation have been failed!");
                    }
                    break;
                case 3:
                    System.out.print("Enter the new address: ");
                    sqlUpdate=sqlUpdate + "address=? where id=" + uid;
                    PreparedStatement preparedStatement3 = connection.prepareStatement(sqlUpdate);
                    preparedStatement3.setString(1, sc.next());
                    int rows2 = preparedStatement3.executeUpdate();

                    if (rows2 > 0) {
                        System.out.println("Address updated successfully!");
                    } else{
                        System.out.println("Operation have been failed!");
                    }
                    break;
                default:
                    break;
            }
        } else {
            System.out.println("No records...");
        }
    }

    public static void delete() throws SQLException {
        System.out.println("Inside delete() method...");
        System.out.print("Enter the user ID: ");
        int uID = sc.nextInt();
        String sql = "delete from student where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, uID);
        int rows = preparedStatement.executeUpdate();

        if (rows > 0){
            System.out.println("User deleted successfully!");
        }
    }

    public static void transactionExample() throws SQLException {
        connection.setAutoCommit(false);

        String sql1 = "insert into student (name, percentage, address) values ('abc', 44, 'Almaty')";
        String sql2 = "inser into student (name, percentage, address) values ('edf', 44, 'Almaty')";

        Statement statement = connection.createStatement();
        int row1 = statement.executeUpdate(sql1);

        statement = connection.createStatement();
        int row2 = statement.executeUpdate(sql2);

        if (row1 > 0 && row2 > 0){
            connection.commit();
        }
        else {
            connection.rollback();
        }
    }

    public static void batchInsert() throws SQLException {
        connection.setAutoCommit(false);

        String sql1 = "insert into student (name, percentage, address) values ('sef', 3, 'ead')";
        String sql2 = "insert into student (name, percentage, address) values ('sef', 3, 'ead')";
        String sql3 = "insert into student (name, percentage, address) values ('sef', 3, 'ead')";

        Statement statement = connection.createStatement();
        statement.addBatch(sql1);
        statement.addBatch(sql2);
        statement.addBatch(sql3);

        int[] rows = statement.executeBatch();

        for (int i: rows) {
            if (i > 0) {
                continue;
            } else{
                connection.rollback();
            }
        }

        connection.commit();
    }
}
