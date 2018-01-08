import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionConfig {
    public static Connection getConnection(){
        Connection conn=null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/calculatorDB","root","Passw0rd");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }
}
