package gestionHospital;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

public class GestionHospital {

    public static Connection con = null;
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {

        try {
            con = DBConnection.getConnection();
            System.out.println("Conexión establecida.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
//                throw new RuntimeException(e);
                System.out.println("Error al cerrar Connection");
            }
        }

    }
}
