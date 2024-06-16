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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Pedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.PedidoDAO
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ExclusaoPedidoController : AppCompatActivity() {


    lateinit var botaoExclusaoPedido : Button
    lateinit var pedidoSelecionado : TextView
    lateinit var botaoVoltar : TextView
    lateinit var progressBarExclusaoPedido: AlertDialog
    lateinit var dialog : AlertDialog;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exclusao_pedido)

        inicializaCamposEBotoes()

        var pedido = pegaPedidoDaActivityAnterior(intent.extras) as Pedido

        pedidoSelecionado.text = pedido.toString()

        var pedidoDAO = PedidoDAO()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoExclusaoPedido.setOnClickListener()
        {

            caixaDeDialogoProgressBarExclusaoPedido()

            lifecycleScope.launch {
                var excluido = withContext(Dispatchers.IO) {
                    pedidoDAO.desabilitaPedidoNoBancoDeDados(pedido)
                }
                progressBarExclusaoPedido.dismiss()

                if (excluido) {
                    caixaDeDialogoSucessoExclusaoPedido()
                    setResult(Activity.RESULT_OK)
                } else {
                    caixaDeDialogoImpossibilidadeExclusaoPedido()
                }

            }
        }

    }


    fun inicializaCamposEBotoes ()
    {
        botaoExclusaoPedido  = findViewById(R.id.botaoExclusaoPedidoEfetivacao)
        pedidoSelecionado = findViewById(R.id.txtCorpoExclusaoPedidoEfetivacao)
        botaoVoltar = findViewById(R.id.botaoVoltarExclusaoPedidoEfetivacao)
    }


    fun pegaPedidoDaActivityAnterior(bundle: Bundle?): Pedido? {
        return bundle?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable("Pedido", Pedido::class.java)
            } else {
                getParcelable("Pedido")
            }
        }
    }

    fun caixaDeDialogoSucessoExclusaoPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_exclusao_pedido, null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOkExclusaoPedido);
        botaoSucesso.setOnClickListener{dialog.dismiss()
            this.finish()
        }
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeExclusaoPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_exclusao_pedido, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidadeExclusaoPedido);
        botaoImpossibilidade.setOnClickListener{dialog.dismiss()
            this.finish()
        }
        dialog = build.create()
        dialog.show()

    }


    fun caixaDeDialogoProgressBarExclusaoPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_exclusao_pedido, null)

        build.setView(view);

        progressBarExclusaoPedido = build.create()
        progressBarExclusaoPedido.show()

    }

}