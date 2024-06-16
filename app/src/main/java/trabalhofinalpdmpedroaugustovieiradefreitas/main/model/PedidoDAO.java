package trabalhofinalpdmpedroaugustovieiradefreitas.main.model;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class PedidoDAO {

    public boolean inserePedidoNoBancoDeDados(Cliente cliente) {

        boolean adicionado = false;

        DateTimeFormatter fd = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String inserePedido = "INSERT INTO pedido (datapedido, cpf, habilitado) \n"
                + "VALUES (?,?,?)";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados()) {

            connection.setAutoCommit(false);

            try (PreparedStatement pstmInserePedido = connection.prepareStatement(inserePedido)) {

                Log.i("Erro", "A DATA EH: " + fd.format(LocalDateTime.now()));

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

    public List<Pedido> BuscaPedidosNoBancoDeDados(Cliente cliente) {

        List<Pedido> listaDePedidos = new LinkedList<>();

        DateTimeFormatter fd = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String buscaPedidos = "select * from pedido where cpf = ? and habilitado = ?";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados();
             PreparedStatement pstm = connection.prepareStatement(buscaPedidos)) {

            pstm.setString(1, cliente.getCpfAtributo());
            pstm.setBoolean(2, true);

            try (ResultSet rs = pstm.executeQuery()) {

                while (rs.next()) {

                    int idPedido = rs.getInt("idpedido");
                    Timestamp dataPedido = rs.getTimestamp("datapedido");

                    String dataRecebida  = dataPedido.toString();

                    String dataSemMilissegundos = dataRecebida.substring(0,19);

                    LocalDateTime dataPedidoRecebido = LocalDateTime.parse(dataSemMilissegundos, fd);

                    Pedido pedido = new Pedido(idPedido, dataPedidoRecebido, cliente);

                    listaDePedidos.add(pedido);

                }
            } catch (SQLException erro) {
                Log.i("Erro na busca de Pedidos", Objects.requireNonNull(erro.getMessage()));
            }

        } catch (SQLException erro) {
            Log.i("Erro na conexão", Objects.requireNonNull(erro.getMessage()));
        }

        return listaDePedidos;
    }

    public boolean desabilitaPedidoNoBancoDeDados(Pedido pedido) {

        boolean desabilitado = false;

        String atualizaPedido = "UPDATE pedido SET habilitado = ? WHERE idpedido = ?";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados()) {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmAtualizaPedido = connection.prepareStatement(atualizaPedido)) {

                pstmAtualizaPedido.setBoolean(1, false);
                pstmAtualizaPedido.setInt(2, pedido.getIdPedidoAtributo());


                int linhasAfetadas = pstmAtualizaPedido.executeUpdate();

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
