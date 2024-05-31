package trabalhofinalpdmpedroaugustovieiradefreitas.main.model;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class ProdutoDAO {

    public boolean insereProdutoNoBancoDeDados(Produto produto) {

        boolean adicionado = false;

        String insereProduto = "INSERT INTO produto (tipograo, pontotorra, valor, blend, habilitado) \n"
                + "VALUES (?,?,?,?,?)";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados()) {

            connection.setAutoCommit(false);

            try (PreparedStatement pstmInsereProduto = connection.prepareStatement(insereProduto)) {

                pstmInsereProduto.setString(1, produto.getTipoGraoAtributo());
                pstmInsereProduto.setString(2, produto.getPontoTorraAtributo());
                pstmInsereProduto.setDouble(3, produto.getValorAtributo());
                pstmInsereProduto.setBoolean(4, produto.getBlendAtributo());
                pstmInsereProduto.setBoolean(5, true);

                int linhasAfetadas = pstmInsereProduto.executeUpdate();

                if (linhasAfetadas > 0) {
                    adicionado = true;
                    connection.commit();
                } else {
                    connection.rollback();
                }

            } catch (SQLException erro) {
                connection.rollback();
                Log.i("Erro ao inserir cliente", Objects.requireNonNull(erro.getMessage()));
            }

        } catch (SQLException erro) {
            Log.i("Erro na conexão", Objects.requireNonNull(erro.getMessage()));
        }

        return adicionado;
    }
}
