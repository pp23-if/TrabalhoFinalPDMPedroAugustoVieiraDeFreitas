package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Cliente
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ClienteDAO
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AtualizacaoClienteController : AppCompatActivity() {


    lateinit var campoCpf : EditText
    lateinit var campoNome : EditText
    lateinit var campoTelefone : EditText
    lateinit var campoEndereco : EditText
    lateinit var campoInstagram : EditText
    lateinit var botaoAlteracaoCliente : Button
    lateinit var botaoVoltar : TextView
    lateinit var progressBarValidacaoDados: AlertDialog
    lateinit var progressBarAlteracaoCliente: AlertDialog
    lateinit var dialog : AlertDialog;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alteracao_cliente)

        inicializaCamposEBotoes()

        var cliente = pegaClienteDaActivityAnterior(intent.extras) as Cliente

        var clienteDAO = ClienteDAO();

        insereDadosClienteNaTela(cliente)

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoAlteracaoCliente.setOnClickListener()
        {

          var clienteAtualizado = montaClienteAtualizado()

          caixaDeDialogoProgressBarValidacaoDados()

            lifecycleScope.launch {
                var cpfExiste = withContext(Dispatchers.IO) {
                    verificaCpfInformado(clienteAtualizado.getCpfAtributo(), clienteDAO, cliente)
                }
                progressBarValidacaoDados.dismiss()

                if (cpfExiste) {
                    criarToastCustomizadoCpfValidado()
                }
                else
                {
                    caixaDeDialogoProgressBarAlteracaoCliente()

                    lifecycleScope.launch {
                        var alterado = withContext(Dispatchers.IO) {
                            clienteDAO.AtualizaClienteNoBancoDeDados(cliente, clienteAtualizado)
                        }
                        progressBarAlteracaoCliente.dismiss()

                        if (alterado) {

                            caixaDeDialogoSucessoAtualizacaoCliente()
                            setResult(Activity.RESULT_OK)
                        }
                        else
                        {
                            caixaDeDialogoImpossibilidadeAlteracaoCliente()
                        }

                    }

                }

            }

        }

    }

    fun inicializaCamposEBotoes ()
    {
        campoCpf = findViewById(R.id.editCpfAlteracaoCliente)
        campoNome = findViewById(R.id.editNomeAlteracaoCliente)
        campoTelefone = findViewById(R.id.editTelefoneAlteracaoCliente)
        campoEndereco = findViewById(R.id.editEnderecoAlteracaoCliente)
        campoInstagram = findViewById(R.id.editInstagramAlteracaoCliente)
        botaoAlteracaoCliente = findViewById(R.id.botaoAlterarCliente)
        botaoVoltar = findViewById(R.id.botaoVoltarAlteracaoCliente)
    }


    fun pegaClienteDaActivityAnterior(bundle: Bundle?): Cliente? {
        return bundle?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable("Cliente", Cliente::class.java)
            } else {
                getParcelable("Cliente")
            }
        }
    }

    fun insereDadosClienteNaTela (cliente: Cliente)
    {
        campoCpf.setText(cliente.getCpfAtributo()) ;
        campoNome.setText(cliente.getNomeAtributo());
        campoTelefone.setText(cliente.getTelefoneAtributo());
        campoEndereco.setText(cliente.getEnderecoAtributo());
        campoInstagram.setText(cliente.getInstagramAtributo());

    }

    fun montaClienteAtualizado() : Cliente
    {
        var cliente = Cliente(campoCpf.text.toString(), campoNome.text.toString(),
            campoTelefone.text.toString(), campoEndereco.text.toString(),
            campoInstagram.text.toString())


        return cliente
    }

    fun caixaDeDialogoSucessoAtualizacaoCliente()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_alteracao_cliente, null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOkAlteracao);
        botaoSucesso.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeAlteracaoCliente()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_alteracao_cliente, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidadeAlteracaoCliente);
        botaoImpossibilidade.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoProgressBarValidacaoDados()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading, null)

        build.setView(view);

        progressBarValidacaoDados = build.create()
        progressBarValidacaoDados.show()

    }

    fun caixaDeDialogoProgressBarAlteracaoCliente()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_alteracao_cliente, null)

        build.setView(view);

        progressBarAlteracaoCliente = build.create()
        progressBarAlteracaoCliente.show()

    }

    fun verificaCpfInformado (cpf : String, clienteDAO: ClienteDAO, cliente: Cliente) : Boolean
    {
        var  cpfRecebido = cpf.trim();

        var cpfEncontrado = clienteDAO.verificaCpfClienteEmAtualizacaoNoBancoDeDados(cpf, cliente).trim()


        return cpfEncontrado.lowercase() == cpfRecebido || cpfEncontrado.uppercase() == cpfRecebido
    }

    fun criarToastCustomizadoCpfValidado ()
    {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_validacao_cpf,null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

}