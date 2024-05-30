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

class ExclusaoClienteController : AppCompatActivity() {


    lateinit var botaoExclusaoCliente : Button
    lateinit var clienteSelecionado : TextView
    lateinit var botaoVoltar : TextView
    lateinit var progressBarExclusaoCliente: AlertDialog
    lateinit var dialog : AlertDialog;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exclusao_cliente)

        //Log.i("Erro", "O OBJETO E: $cliente")

        inicializaCamposEBotoes()

        var cliente = pegaClienteDaActivityAnterior(intent.extras) as Cliente

        clienteSelecionado.text = cliente.toString()

        var clienteDAO = ClienteDAO();

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoExclusaoCliente.setOnClickListener()
        {

            caixaDeDialogoProgressBarExclusaoCliente()

            lifecycleScope.launch {
                var excluido = withContext(Dispatchers.IO) {
                    clienteDAO.desabilitaClienteNoBancoDeDados(cliente)
                }
                progressBarExclusaoCliente.dismiss()

                if (excluido) {
                    caixaDeDialogoSucessoExclusaoCliente()
                    setResult(Activity.RESULT_OK)
                } else {
                    caixaDeDialogoImpossibilidadeExclusaoCliente()
                }

            }
        }

    }


    fun inicializaCamposEBotoes ()
    {
        botaoExclusaoCliente  = findViewById(R.id.botaoExcluirCliente)
        clienteSelecionado = findViewById(R.id.txtCorpoExclusaoCliente)
        botaoVoltar = findViewById(R.id.botaoVoltarExclusaoCliente)
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


    fun caixaDeDialogoSucessoExclusaoCliente()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_exclusao_cliente, null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOkExclusaoCliente);
        botaoSucesso.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeExclusaoCliente()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_exclusao_cliente, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidadeExclusaoCliente);
        botaoImpossibilidade.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }


    fun caixaDeDialogoProgressBarExclusaoCliente()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_exclusao_cliente, null)

        build.setView(view);

        progressBarExclusaoCliente = build.create()
        progressBarExclusaoCliente.show()

    }

}