package trabalhofinalpdmpedroaugustovieiradefreitas.main.model;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.sql.ResultSet;
public class ClienteDAO {

    public boolean insereClienteNoBancoDeDados(Cliente cliente) {

        boolean adicionado = false;

        String insereCliente = "INSERT INTO cliente (cpf, nome, telefone, endereco, instagram, habilitado) \n"
                + "VALUES (?,?,?,?,?,?)";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados()) {

            connection.setAutoCommit(false);

            try (PreparedStatement pstmInsereCliente = connection.prepareStatement(insereCliente)) {

                pstmInsereCliente.setString(1, cliente.getCpfAtributo());
                pstmInsereCliente.setString(2, cliente.getNomeAtributo());
                pstmInsereCliente.setString(3, cliente.getTelefoneAtributo());
                pstmInsereCliente.setString(4, cliente.getEnderecoAtributo());
                pstmInsereCliente.setString(5, cliente.getInstagramAtributo());
                pstmInsereCliente.setBoolean(6, true);

                int linhasAfetadas = pstmInsereCliente.executeUpdate();

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

    public List <Cliente> BuscaClientesNoBancoDeDados() {

        List <Cliente> listaDeClientes = new LinkedList<>();

        String buscaCliente = "select * from cliente where habilitado = ?";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados();
             PreparedStatement pstm = connection.prepareStatement(buscaCliente)) {

            pstm.setBoolean(1, true);

            try (ResultSet rs = pstm.executeQuery()) {

                while (rs.next()) {

                    String cpf = rs.getString("cpf");
                    String nome = rs.getString("nome");
                    String telefone = rs.getString("telefone");
                    String endereco = rs.getString("endereco");
                    String instagram = rs.getString("instagram");

                    Cliente cliente = new Cliente(cpf, nome, telefone, endereco, instagram);

                    listaDeClientes.add(cliente);

                }
            }
            catch (SQLException erro)
            {
                Log.i("Erro na busca de CLIENTES", Objects.requireNonNull(erro.getMessage()));
            }

        } catch (SQLException erro) {
            Log.i("Erro na conexão", Objects.requireNonNull(erro.getMessage()));
        }

        return listaDeClientes;
    }

    public String verificaCpfNoBancoDeDados(String cpf) {

        String cpfEncontrado = "";

        String buscaCliente = "select * from cliente where cpf = ? or cpf = ?";

        try (Connection connection = new ConexaoBancoDeDados().ConectaBancoDeDados();
             PreparedStatement pstm = connection.prepareStatement(buscaCliente)) {

            pstm.setString(1, cpf.toUpperCase());
            pstm.setString(2, cpf.toLowerCase());

            try (ResultSet rs = pstm.executeQuery()) {

                while (rs.next()) {
                    cpfEncontrado = rs.getString("cpf");
                }

            } catch (SQLException erro) {
                Log.i("Erro na consulta", Objects.requireNonNull(erro.getMessage()));
            }
        } catch (SQLException erro) {
            Log.i("Erro na conexão", Objects.requireNonNull(erro.getMessage()));
        }

        return cpfEncontrado;
    }



}
