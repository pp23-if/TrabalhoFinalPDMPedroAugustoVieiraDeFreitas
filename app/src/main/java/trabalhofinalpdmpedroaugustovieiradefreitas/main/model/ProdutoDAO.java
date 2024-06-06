package trabalhofinalpdmpedroaugustovieiradefreitas.main.model;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
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
                Log.i("Erro ao inserir Produto", Objects.requireNonNull(erro.getMessage()));
            }

        } catch (SQLException erro) {
            Log.i("Erro na conexão", Objects.requireNonNull(erro.getMessage()));
        }

        return adicionado;
    }

    public List<Produto> BuscaProdutosNoBancoDeDados() {

        List<Produto> listaDeProdutos = new LinkedList<>();

        String buscaProduto = "select * from produto where habilitado = ?";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados();
             PreparedStatement pstm = connection.prepareStatement(buscaProduto)) {

            pstm.setBoolean(1, true);

            try (ResultSet rs = pstm.executeQuery()) {

                while (rs.next()) {

                    int idProduto = rs.getInt("idproduto");
                    String tipoGrao = rs.getString("tipograo");
                    String pontoTorra = rs.getString("pontotorra");
                    double valor = rs.getDouble("valor");
                    boolean blend = rs.getBoolean("blend");

                    Produto produto = new Produto(idProduto,tipoGrao,pontoTorra,valor,blend);

                    listaDeProdutos.add(produto);

                }
            } catch (SQLException erro) {
                Log.i("Erro na busca de Produtos", Objects.requireNonNull(erro.getMessage()));
            }

        } catch (SQLException erro) {
            Log.i("Erro na conexão", Objects.requireNonNull(erro.getMessage()));
        }

        return listaDeProdutos;
    }

    public boolean AtualizaProdutoNoBancoDeDados(Produto produto) {

        boolean atualizado = false;

        String atualizaProduto = "UPDATE produto SET tipograo = ?, pontotorra = ?, valor = ?, blend = ? WHERE idproduto = ?";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados()) {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmAtualizaProduto = connection.prepareStatement(atualizaProduto)) {

                pstmAtualizaProduto.setString(1, produto.getTipoGraoAtributo());
                pstmAtualizaProduto.setString(2, produto.getPontoTorraAtributo());
                pstmAtualizaProduto.setDouble(3, produto.getValorAtributo());
                pstmAtualizaProduto.setBoolean(4, produto.getBlendAtributo());
                pstmAtualizaProduto.setInt(5, produto.getIdProdutoAtributo());


                int linhasAfetadas = pstmAtualizaProduto.executeUpdate();

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

    public boolean desabilitaProdutoNoBancoDeDados(Produto produto) {

        boolean desabilitado = false;

        String atualizaProduto = "UPDATE produto SET habilitado = ? WHERE idproduto = ?";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados()) {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmAtualizaProduto = connection.prepareStatement(atualizaProduto)) {

                pstmAtualizaProduto.setBoolean(1, false);
                pstmAtualizaProduto.setInt(2, produto.getIdProdutoAtributo());


                int linhasAfetadas = pstmAtualizaProduto.executeUpdate();

                if (linhasAfetadas > 0) {
                    connection.commit();
                    desabilitado = true;
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

        return desabilitado;
    }
}

