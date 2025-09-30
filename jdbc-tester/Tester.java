import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Tester {
    public static void main(String[] args) {
        Map<String, String> params = parseArgs(args);

        String user                   = params.get("user");
        String pass                   = params.get("pass");
        String host                   = params.get("host");
        String port                   = params.getOrDefault("port", "1433");
        String instance               = params.getOrDefault("instance", "");
        String base                   = params.get("base");
        String encrypt                = params.getOrDefault("encrypt", "true");
        String trustServerCertificate = params.getOrDefault("trustServerCertificate", "true");

        if (user == null || pass == null || host == null || base == null) {
            System.out.println("Usage: java -cp .:mssql-jdbc.jar Tester "
                    + "-user <db_user> -pass <db_pass> -host <db_host> -base <db_base> "
                    + "[-port <port>] [-instance <instance>] "
                    + "[-encrypt true|false] [-trustServerCertificate true|false]");
            System.exit(1);
        }

        // Build the connection URL dynamically
        String serverPart = host;
        if (instance != null && !instance.isEmpty()) {
            serverPart += "\\" + instance;  // named instance
        }
        serverPart += ":" + port;           // port (still works even with instance if needed)

        String url = String.format(
            "jdbc:sqlserver://%s;databaseName=%s;encrypt=%s;trustServerCertificate=%s",
            serverPart, base, encrypt, trustServerCertificate
        );

        System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(" 🔌 Connecting to: " + url);
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("✅ Connected successfully");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        } catch (SQLException e) {
            System.err.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.err.println("❌ Connection failed (SQLException):");
            System.err.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            printSqlExceptionDetails(e);
            System.err.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        } catch (Exception e) {
            System.err.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.err.println("❌ Connection failed (General Exception):");
            System.err.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            System.err.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        }
    }

    private static void printSqlExceptionDetails(SQLException e) {
        SQLException current = e;
        while (current != null) {
            System.err.println("Message: " + current.getMessage());
            System.err.println("SQLState: " + current.getSQLState());
            System.err.println("ErrorCode: " + current.getErrorCode());
            current.printStackTrace(System.err);
            System.err.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            current = current.getNextException();
        }

        Throwable cause = e.getCause();
        while (cause != null) {
            System.err.println("Caused by: " + cause.getClass().getName() + ": " + cause.getMessage());
            cause.printStackTrace(System.err);
            System.err.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            cause = cause.getCause();
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].startsWith("-")) {
                params.put(args[i].substring(1), args[i + 1]);
                i++;
            }
        }
        return params;
    }
}
