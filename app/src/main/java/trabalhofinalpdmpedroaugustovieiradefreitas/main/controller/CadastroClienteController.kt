package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Cliente
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ClienteDAO

class CadastroClienteController : AppCompatActivity() {


    lateinit var campoCpf : EditText
    lateinit var campoNome : EditText
    lateinit var campoTelefone : EditText
    lateinit var campoEndereco : EditText
    lateinit var campoInstagram : EditText
    lateinit var botaoCadastroCliente : Button
    lateinit var botaoVoltar : TextView
    lateinit var dialog : AlertDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

        //Log.i("Erro", "O OBJETO E: $cliente")

        inicializaCamposEBotoes()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoCadastroCliente.setOnClickListener()
        {
            var cliente = montaCliente();

        }


    }

    fun inicializaCamposEBotoes ()
    {
        campoCpf = findViewById(R.id.editCpf)
        campoNome = findViewById(R.id.editNome)
        campoTelefone = findViewById(R.id.editTelefone)
        campoEndereco = findViewById(R.id.editEndereco)
        campoInstagram = findViewById(R.id.editInstagram)
        botaoCadastroCliente = findViewById(R.id.botaoCadastrarCliente)
        botaoVoltar = findViewById(R.id.botaoVoltarCadastroCliente)
    }

    fun montaCliente () : Cliente
    {
       var cliente = Cliente(campoCpf.text.toString(), campoNome.text.toString(),
                             campoTelefone.text.toString(), campoEndereco.text.toString(),
                             campoInstagram.text.toString())

        return cliente;
    }

    fun verificaCpfInformado (cpf : String, clienteDAO: ClienteDAO) : Boolean
    {
        var  cpfRecebido = cpf.trim();

        var cpfEncontrado = clienteDAO.verificaCpfNoBancoDeDados(cpfRecebido).trim()

        return cpfEncontrado.lowercase() == cpfRecebido || cpfEncontrado.uppercase() == cpfRecebido
    }

    fun caixaDeDialogoSucessoCadastroCliente()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_cadastro_cliente, null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOk);
        botaoSucesso.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeCadastroCliente()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_cadastro_cliente, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidade);
        botaoImpossibilidade.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun verificaEntradasVazias () : Boolean
    {
        return (campoCpf.text.toString().isNullOrBlank() || campoCpf.text.toString().isNullOrEmpty() ||
                campoNome.text.toString().isNullOrBlank() || campoNome.text.toString().isNullOrEmpty() ||
                campoTelefone.text.toString().isNullOrBlank() || campoTelefone.text.toString().isNullOrEmpty() ||
                campoEndereco.text.toString().isNullOrBlank() ||  campoEndereco.text.toString().isNullOrEmpty() ||
                campoInstagram.text.toString().isNullOrBlank() || campoInstagram.text.toString().isNullOrEmpty())
    }

    fun limparCampos ()
    {
        campoCpf.text.clear();
        campoNome.text.clear();
        campoTelefone.text.clear();
        campoEndereco.text.clear();
        campoInstagram.text.clear();
    }

}