package connector;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DbConnect {

    private static Connection con = null;

    public static Connection getConnection(){
        if(con == null){
            try {
                System.out.println("Connecting...");
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                con = DriverManager.getConnection(url, props);
                System.out.println("Connect database success");
                System.out.println(" ");

            } catch (SQLException e){
                System.out.println("Error");
                throw new DbException(e.getMessage());
            }

        }
        return con;
    }

    public static void closeConnection(){
        if(con != null){
            try{
            	System.out.println(" ");
            	System.out.println("Closing database...");
                con.close();
            	System.out.println("Database closed");
            } catch (SQLException e){
                throw new DbException(e.getMessage());
            }
        }
    }

    private static Properties loadProperties(){
        try (FileInputStream fs = new FileInputStream("db.properties")){
            Properties props = new Properties();
            props.load(fs);
            return props;

        } catch (IOException e){
            throw new DbException(e.getMessage());
        }
    }
    
    public static void closeStatement(Statement st) {
    	if(st != null) {
    		try {
    			System.out.println("Statement closed");
    			st.close();
    		} catch(SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public static void closeResultSet(ResultSet rs) {
    	if(rs != null) {
    		try {
    			System.out.println("ResultSet closed");
    			rs.close();
    		} catch(SQLException e) {
    			e.printStackTrace();
    		}
    	}
    }


}
