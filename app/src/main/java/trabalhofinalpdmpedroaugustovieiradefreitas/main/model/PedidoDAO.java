package trabalhofinalpdmpedroaugustovieiradefreitas.main.model;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PedidoDAO {

    public boolean inserePedidoNoBancoDeDados(Cliente cliente) {

        boolean adicionado = false;

        DateTimeFormatter fd = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        String inserePedido = "INSERT INTO pedido (datapedido, cpf, habilitado) \n"
                + "VALUES (?,?,?)";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados()) {

            connection.setAutoCommit(false);

            try (PreparedStatement pstmInserePedido = connection.prepareStatement(inserePedido)) {

                pstmInserePedido.setTimestamp(1, Timestamp.valueOf(fd.format(LocalDateTime.now())));
                pstmInserePedido.setString(2, cliente.getCpfAtributo());
                pstmInserePedido.setBoolean(3, true);


                int linhasAfetadas = pstmInserePedido.executeUpdate();

                if (linhasAfetadas > 0) {
                    adicionado = true;
                    connection.commit();
                } else {
                    connection.rollback();
                }

            } catch (SQLException erro) {
                connection.rollback();
                Log.i("Erro ao inserir Pedido", Objects.requireNonNull(erro.getMessage()));
            }

        } catch (SQLException erro) {
            Log.i("Erro na conexão", Objects.requireNonNull(erro.getMessage()));
        }

        return adicionado;
    }

}
