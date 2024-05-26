package trabalhofinalpdmpedroaugustovieiradefreitas.main.model;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class ConexaoBancoDeDados {


    public Connection ConectaBancoDeDados() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Connection connect = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/testejdbcpdm", "ppteste", "Bil4da2015#");

        } catch (Exception e) {
            Log.i("Erro", Objects.requireNonNull(e.getMessage()));
        }

        return connect;

    }
}
