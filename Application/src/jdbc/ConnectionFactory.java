package jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static ConnectionFactory instance = null;

    private String dbHost;
    private String dbPort;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    private ConnectionFactory() {
    }


    public static ConnectionFactory getInstance(){
        if(instance==null) instance = new ConnectionFactory();
        return instance;
    }


    public void readProperties() throws IOException {
        Properties properties = new Properties();

        try{
            String path = "/jdbc/datasource.properties";
            InputStream input = this.getClass().getClassLoader().getResourceAsStream(path);
            properties.load(input);

            dbHost = properties.getProperty("host");
            dbPort = properties.getProperty("port");
            dbName = properties.getProperty("name");
            dbUser = properties.getProperty("user");
            dbPassword = properties.getProperty("password");
        }catch (IOException e){
            System.err.println(e.getMessage());
            throw new IOException("Erro ao obter informações do banco de dados");
        }
    }



    public Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
        Connection connection = null;


        try{
            Class.forName("org.postgresql.Driver");

            readProperties();

            String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

            /*Class.forName("com.mysql.jdbc.Driver");
            readProperties();
            String url = "jdbc:mysql://" + dbHost + "/" + dbName + "?useTimezone=true&serverTimezone=UTC";*/

                connection = DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();

            throw new ClassNotFoundException("Erro de conexão ao banco de dados.");
        } catch (SQLException ex) {

            ex.printStackTrace();
            throw new SQLException("Erro de conexão ao banco de dados.");
        }

        return connection;
    }
}
