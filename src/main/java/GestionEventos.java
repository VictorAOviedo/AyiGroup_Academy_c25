import java.sql.*;
import java.util.Scanner;

public class GestionEventos {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestion_de_eventos"; // URL de la base de datos
    private static final String USER = "root"; // Usuario de la base de datos
    private static final String PASS = "4852375"; // Contraseña de la base de datos

    private static Connection conn = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Establecer la conexión a la base de datos
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            int option;
            do {
                System.out.println("\nMenú de Opciones");
                System.out.println("1. Insertar datos en Eventos");
                System.out.println("2. Insertar datos en Participantes");
                System.out.println("3. Insertar datos en Inscripciones");
                System.out.println("4. Insertar datos en Organizadores");
                System.out.println("5. Consultar todos los datos (multi-tabla)");
                System.out.println("6. Eliminar todos los datos");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");
                option = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                switch (option) {
                    case 1 -> insertarEvento();
                    case 2 -> insertarParticipante();
                    case 3 -> insertarInscripcion();
                    case 4 -> insertarOrganizador();
                    case 5 -> consultarDatos();
                    case 6 -> eliminarTodosLosDatos();
                    case 0 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción inválida");
                }
            } while (option != 0);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para insertar un evento
    public static void insertarEvento() throws SQLException {
        System.out.print("Nombre del evento: ");
        String nombre = scanner.nextLine();
        System.out.print("Fecha del evento (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();
        System.out.print("Lugar del evento: ");
        String lugar = scanner.nextLine();

        String query = "INSERT INTO Eventos (nombre_evento, fecha_evento, lugar_evento) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, nombre);
        pstmt.setString(2, fecha);
        pstmt.setString(3, lugar);
        pstmt.executeUpdate();
        System.out.println("Evento insertado con éxito.");
    }

    // Método para insertar un participante
    public static void insertarParticipante() throws SQLException {
        System.out.print("Nombre del participante: ");
        String nombre = scanner.nextLine();
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        String query = "INSERT INTO Participantes (nombre_participante, correo_participante, telefono_participante) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, nombre);
        pstmt.setString(2, correo);
        pstmt.setString(3, telefono);
        pstmt.executeUpdate();
        System.out.println("Participante insertado con éxito.");
    }

    // Método para insertar una inscripción
    public static void insertarInscripcion() throws SQLException {
        System.out.print("ID del evento: ");
        int idEvento = scanner.nextInt();
        System.out.print("ID del participante: ");
        int idParticipante = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        System.out.print("Fecha de inscripción (YYYY-MM-DD): ");
        String fechaInscripcion = scanner.nextLine();

        String query = "INSERT INTO Inscripciones_a_los_eventos (id_evento, id_participante, fecha_inscripcion) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idEvento);
        pstmt.setInt(2, idParticipante);
        pstmt.setString(3, fechaInscripcion);
        pstmt.executeUpdate();
        System.out.println("Inscripción insertada con éxito.");
    }

    // Método para insertar un organizador
    public static void insertarOrganizador() throws SQLException {
        System.out.print("Nombre del organizador: ");
        String nombre = scanner.nextLine();
        System.out.print("Contacto del organizador: ");
        String contacto = scanner.nextLine();
        System.out.print("Descripción de la organización: ");
        String descripcion = scanner.nextLine();

        String query = "INSERT INTO Organizadores (nombre_organizador, contacto_organizador, descripcion_organizador) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, nombre);
        pstmt.setString(2, contacto);
        pstmt.setString(3, descripcion);
        pstmt.executeUpdate();
        System.out.println("Organizador insertado con éxito.");
    }

    // Método para consultar todos los datos (multi-tabla)
    public static void consultarDatos() throws SQLException {
        String query = """
            SELECT e.nombre_evento, e.fecha_evento, e.lugar_evento, 
                   p.nombre_participante, p.correo_participante, p.telefono_participante,
                   i.fecha_inscripcion, o.nombre_organizador, o.contacto_organizador
            FROM Inscripciones_a_los_eventos i
            INNER JOIN Eventos e ON i.id_evento = e.id_evento
            INNER JOIN Participantes p ON i.id_participante = p.id_participante
            LEFT JOIN Organizadores o ON o.id_organizador = e.id_evento
            """;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            System.out.println("Evento: " + rs.getString("nombre_evento") +
                    ", Fecha: " + rs.getDate("fecha_evento") +
                    ", Lugar: " + rs.getString("lugar_evento"));
            System.out.println("Participante: " + rs.getString("nombre_participante") +
                    ", Correo: " + rs.getString("correo_participante") +
                    ", Teléfono: " + rs.getString("telefono_participante"));
            System.out.println("Fecha de inscripción: " + rs.getDate("fecha_inscripcion"));
            System.out.println("Organizador: " + rs.getString("nombre_organizador") +
                    ", Contacto: " + rs.getString("contacto_organizador"));
            System.out.println("-----------------------------------------");
        }
    }

    // Método para eliminar todos los datos de todas las tablas
    public static void eliminarTodosLosDatos() throws SQLException {
        String[] tablas = {"Inscripciones_a_los_eventos", "Eventos", "Participantes", "Organizadores"};
        for (String tabla : tablas) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM " + tabla);
            System.out.println("Datos eliminados de la tabla: " + tabla);
        }
    }
}
