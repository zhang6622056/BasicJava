package loadclass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlDriverLoad {



    /***
     *
     * 通过spi的方式，自动加载Driver
     * /META-INF/services/java.sql.Driver
     * @author Nero
     * @date 2020-03-31
     * *@param: args
     * @return void
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //- Driver 是用什么加载器加载的? 根据全盘负责委托机制，当一个ClassLoader加载
        //Class.forName("com.mysql.jdbc.Driver");
        Connection connection =  DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/secoo_test","root","root");
        System.out.println(connection);
    }




}
