package com.example;

import com.example.model.Member;

import java.sql.*;

public class MySqlMain {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "myuser", "mypass");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member");
//        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM member WHERE id=?");
//        preparedStatement.setInt(1, 2);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            var member = new Member(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getInt("age"));
            System.out.println("회원 = " + member);
        }
        connection.close();
    }
}
