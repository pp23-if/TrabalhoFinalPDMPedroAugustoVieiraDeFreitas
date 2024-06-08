package trabalhofinalpdmpedroaugustovieiradefreitas.main.model;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ItemPedidoDAO {

    public boolean insereItemPedidoNoBancoDeDados(ItemPedido itemPedido) {

        boolean adicionado = false;

        String insereItemPedido = "INSERT INTO itempedido (idpedido, idproduto, quantidade, habilitado) \n"
                + "VALUES (?,?,?,?)";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados()) {

            connection.setAutoCommit(false);

            try (PreparedStatement pstmInsereItemPedido = connection.prepareStatement(insereItemPedido)) {

                pstmInsereItemPedido.setInt(1, itemPedido.getPedidoAtributo().getIdPedidoAtributo());
                pstmInsereItemPedido.setInt(2, itemPedido.getProdutoAtributo().getIdProdutoAtributo());
                pstmInsereItemPedido.setDouble(3, itemPedido.getQuantidadeAtributo());
                pstmInsereItemPedido.setBoolean(4, true);

                int linhasAfetadas = pstmInsereItemPedido.executeUpdate();

                if (linhasAfetadas > 0) {
                    adicionado = true;
                    connection.commit();
                } else {
                    connection.rollback();
                }

            } catch (SQLException erro) {
                connection.rollback();
                Log.i("Erro ao inserir ItemPedido", Objects.requireNonNull(erro.getMessage()));
            }

        } catch (SQLException erro) {
            Log.i("Erro na conexão", Objects.requireNonNull(erro.getMessage()));
        }

        return adicionado;
    }


}
