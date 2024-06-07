package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.PedidoDAO
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SelecaoPedidoEItemController : AppCompatActivity() {

    lateinit var botaoCadastroPedido : Button
    lateinit var botaoCadastroItemPedido : Button
    lateinit var botaoVoltar : TextView
    lateinit var progressBarCadstroPedidoCliente: AlertDialog
    lateinit var dialog : AlertDialog;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_pedido)

        //Log.i("Erro", "O OBJETO E: $cliente")

        inicializaCamposEBotoes()

        var cliente = pegaClienteDaActivityAnterior(intent.extras) as Cliente

        var pedidoDAO = PedidoDAO()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoCadastroPedido.setOnClickListener()
        {
            caixaDeDialogoProgressBarCadastroPedido()

            lifecycleScope.launch {
                var pedidoCadastrado = withContext(Dispatchers.IO) {
                    pedidoDAO.inserePedidoNoBancoDeDados(cliente)
                }
                progressBarCadstroPedidoCliente.dismiss()

                if(pedidoCadastrado)
                {
                    caixaDeDialogoSucessoCadastroPedido()
                }
                else
                {
                    caixaDeDialogoImpossibilidadeCadastroPedido()
                }
            }
        }

        botaoCadastroItemPedido.setOnClickListener()
        {

        }

    }

    fun inicializaCamposEBotoes ()
    {
        botaoCadastroPedido = findViewById(R.id.botaoCadastroPedido)
        botaoCadastroItemPedido = findViewById(R.id.botaoAdicionarItemPedido)
        botaoVoltar = findViewById(R.id.botaoVoltarCadastroPedido)
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

    fun caixaDeDialogoProgressBarCadastroPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_cadastro_pedido, null)

        build.setView(view);

        progressBarCadstroPedidoCliente = build.create()
        progressBarCadstroPedidoCliente.show()

    }

    fun caixaDeDialogoSucessoCadastroPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_cadastro_pedido, null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOkCadastroPedido);
        botaoSucesso.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeCadastroPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_cadastro_pedido, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidadeCadastroPedido);
        botaoImpossibilidade.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

}