package gestionHospital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/gestion_hospital";
    public static final String USER = "root";
    public static final String PASS = "4852375";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}
