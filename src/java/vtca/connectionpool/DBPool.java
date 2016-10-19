/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vtca.connectionpool;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author BanNT
 */
public class DBPool {

    private static Logger logger = new Logger("DBPool"); // biến logger của lớp Logger trên
    private static LinkedList pool = new LinkedList(); // pool để chứa các connections
    public final static int MAX_CONNECTIONS = 10;  // số connection tối đa trong pool là 10, tuỳ ý!!
    public final static int INI_CONNECTIONS = 2; // số connection khi bắt đầu khởi tạo là 2

    public DBPool() {
        build(INI_CONNECTIONS);
    }

    public static Connection makeDBConnection() throws SQLException {
        Connection conn = null;
        try {
            /* ket noi su dung cho he quan tri csdl oracle
             Class.forName("oracle.jdbc.driver.OracleDriver");
             String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
             String username = "TS_TRAINING";
             String password = "ts";
             logger.log("URL:" + url);
             logger.log("User:" + "ts_training");
             conn = DriverManager.getConnection(url, username, password);
             */
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            String dbURL = "jdbc:sqlserver://localhost\\TELSOFT;databaseName=itplus;integratedSecurity=true";
            String dbURL = "jdbc:sqlserver://localhost\\TELSOFT;databaseName=itplus";
            String user = "itplus";
            String pass = "itplus";
            conn = DriverManager.getConnection(dbURL, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    //khoi tao so connection

    public static void build(int number) {
        logger.log("Establishing " + number + " connections...");
        Connection conn = null;
        release();//đóng tất cả các kết nối nếu còn khi khởi tạo
        for (int i = 0; i < number; i++) {
            try {
                conn = makeDBConnection();//tạo 1 kết nối mới
            } catch (SQLException ex) {
                logger.log("Error: " + ex.getMessage());
            }
            if (conn != null) {
                pool.addLast(conn);
            }
        }
        logger.log("Number of connection: " + number);
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            synchronized (pool) {
                conn = (java.sql.Connection) pool.removeFirst();
            }
            if (conn == null) {
                conn = makeDBConnection();
            }
            try {
                conn.setAutoCommit(true);
            } catch (Exception ex) {
            }

        } catch (Exception e) {
            logger.error("Method getConnection(): Error executing >>>" + e.toString());
            try {
                logger.log("Make connection again.");
                conn = makeDBConnection();
                conn.setAutoCommit(true);
            } catch (SQLException e1) {
            }
            logger.log("" + conn);
        }
        return conn;
    }
    //close connection

    public static void putConnection(java.sql.Connection conn) {
        try { // Ignore closed connection
            if (conn == null || conn.isClosed()) {
                logger.log("putConnection: conn is null or closed: " + conn);
                return;
            }
            if (pool.size() >= MAX_CONNECTIONS) {
                conn.close();
                return;
            }
        } catch (SQLException ex) {
        }

        synchronized (pool) {
            pool.addLast(conn);
            pool.notify();
        }
    }
    //xoa tat ca cac connection trong pool

    public static void release() {
        logger.log("Closing connections in pool...");
        synchronized (pool) {
            for (Iterator it = pool.iterator(); it.hasNext();) {
                Connection conn = (Connection) it.next();
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(
                            "release: Cannot close connection! (maybe closed?)");
                }
            }
            pool.clear();
        }
        logger.log("Release connection OK");
    }
    ////  phương thức close một connection và preparedStatement

    public static void releaseConnection(Connection conn, PreparedStatement preStmt) {
        putConnection(conn);
        try {
            if (preStmt != null) {
                preStmt.close();
            }
        } catch (SQLException e) {
        }
    }
    //dong connection, preparedstatement, resultset

    public static void releaseConnection(Connection conn, PreparedStatement preStmt, ResultSet rs) {
        releaseConnection(conn, preStmt);
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
        }
    }

    public static void releaseConnection(Connection conn, Statement Stmt, ResultSet rs) {
        putConnection(conn);
        try {
            if (Stmt != null) {
                Stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
        }
    }

    public static void releaseConnection(Connection conn, PreparedStatement preStmt, Statement stmt, ResultSet rs) {
        releaseConnection(conn, preStmt, rs);
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
        }
    }

    public static void releaseConnection(Connection conn, CallableStatement cs, ResultSet rs) {
        putConnection(conn);
        try {
            if (cs != null) {
                cs.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
        }
    }
}
