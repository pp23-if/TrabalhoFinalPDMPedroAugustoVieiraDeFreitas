package trabalhofinalpdmpedroaugustovieiradefreitas.main.model;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
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

    public List<ItemPedido> BuscaItemPedidoNoBancoDeDados(Pedido pedido) {

        List<ItemPedido> listaDeItemPedido = new LinkedList<>();

        String buscaItemPedido = "select prod.idproduto, prod.tipograo, prod.pontotorra, prod.valor,\n" +
                "prod.blend, ip.iditempedido,\n" +
                "ip.quantidade\n" +
                "from pedido p inner join itempedido ip\n" +
                "on p.idpedido = ip.idpedido inner join produto prod\n" +
                "on ip.idproduto = prod.idproduto WHERE ip.habilitado = ? AND ip.idpedido = ?;";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados();
             PreparedStatement pstm = connection.prepareStatement(buscaItemPedido)) {

            pstm.setBoolean(1, true);
            pstm.setInt(2, pedido.getIdPedidoAtributo());

            try (ResultSet rs = pstm.executeQuery()) {

                while (rs.next()) {

                    int idProduto = rs.getInt("idproduto");
                    String tipoGrao = rs.getString("tipograo");
                    String pontoTorra = rs.getString("pontotorra");
                    double valorProduto = rs.getDouble("valor");
                    boolean blend = rs.getBoolean("blend");

                    Produto produto = new Produto(idProduto, tipoGrao, pontoTorra, valorProduto, blend);

                    int idItemPedido = rs.getInt("iditempedido");
                    double quantidadeItemPedido = rs.getDouble("quantidade");

                    ItemPedido itemPedido = new ItemPedido(idItemPedido, pedido, produto, quantidadeItemPedido);

                    listaDeItemPedido.add(itemPedido);

                }
            } catch (SQLException erro) {
                Log.i("Erro na busca de Itens do Pedido", Objects.requireNonNull(erro.getMessage()));
            }

        } catch (SQLException erro) {
            Log.i("Erro na conexão", Objects.requireNonNull(erro.getMessage()));
        }

        return listaDeItemPedido;
    }


    public boolean AtualizaItemPedidoNoBancoDeDados(ItemPedido itemPedido, ItemPedido itemPedidoAtualizado) {

        boolean atualizado = false;

        String atualizaItemPedido = "UPDATE itempedido SET idproduto = ?, quantidade = ? WHERE iditempedido = ?";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados()) {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmAtualizaItemPedido = connection.prepareStatement(atualizaItemPedido)) {

                pstmAtualizaItemPedido.setInt(1, itemPedidoAtualizado.getProdutoAtributo().getIdProdutoAtributo());
                pstmAtualizaItemPedido.setDouble(2, itemPedidoAtualizado.getQuantidadeAtributo());
                pstmAtualizaItemPedido.setInt(3, itemPedido.getIdItemPedidoAtributo());


                int linhasAfetadas = pstmAtualizaItemPedido.executeUpdate();

                if (linhasAfetadas > 0) {
                    connection.commit();
                    atualizado = true;
                } else {
                    connection.rollback();
                }

            } catch (SQLException erro) {
                connection.rollback();
                erro.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return atualizado;
    }


}
