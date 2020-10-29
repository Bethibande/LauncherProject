package de.bethibande.launcher.utils;

import de.bethibande.launcher.Core;

import java.sql.*;

public class SQL {
    public static Connection con;

    private final String host;

    private final String db;

    private final String user;

    private final String pass;

    private final String pr = "[MySQL] ";

    public SQL(String host, String database, String user, String password) {
        this.host = host;
        this.db = database;
        this.user = user;
        this.pass = password;
    }

    public void connect() {
        try {
            String port = "3306";
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db + "?characterEncoding=utf8&useSSL=false&autoReconnect=true",user,pass);

            Core.loggerInstance.logMessage("MySQL connected!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void close() {

        if (con!=null){
            try{
                con.close();
                Core.loggerInstance.logMessage("MySQL disconnected!");
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return con != null;
    }

    public void update(String query) {

        if(con != null) {
            try {
                if (!con.isValid(2)) {
                    close();
                    connect();
                }
                con.createStatement().executeUpdate(query);
            } catch(SQLException e) {
                e.printStackTrace();
                connect();
            }
        }
    }

    public ResultSet query(String qry) {
        try{
            if(con.isValid(2)) {
                PreparedStatement stmt = con.prepareStatement(qry);
                return stmt.executeQuery();
            }
            PreparedStatement stmt = con.prepareStatement(qry);

            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            connect();

            try {
                if(con.isValid(2)) {
                    PreparedStatement stmt = con.prepareStatement(qry);

                    return stmt.executeQuery();
                }
                PreparedStatement stmt = con.prepareStatement(qry);

                return stmt.executeQuery();
            } catch(SQLException ee) {
                ee.printStackTrace();
            }
        }
        return null;
    }

}
