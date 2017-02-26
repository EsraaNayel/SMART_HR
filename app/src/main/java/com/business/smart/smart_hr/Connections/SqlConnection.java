package com.business.smart.smart_hr.Connections;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by wahib-it on 2016-04-18.
 */
public class SqlConnection {
    String ip = "197.45.131.66";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "SMRT_HR";
    String un = "sa";
    String password = "SMART@smart.local";

    @SuppressLint("NewApi")
    public Connection CONN(/*String _user, String _pass, String _DB, String _server*/) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}
