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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedidoDAO
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Pedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.PedidoDAO
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ExclusaoItemPedidoController : AppCompatActivity() {


    lateinit var botaoExclusaoItemPedido : Button
    lateinit var itemPedidoSelecionado: TextView
    lateinit var botaoVoltar : TextView
    lateinit var progressBarExclusaoItemPedido: AlertDialog
    lateinit var dialog : AlertDialog;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exclusao_item_pedido)

        inicializaCamposEBotoes()

        var itemPedido = pegaItemPedidoDaActivityAnterior(intent.extras) as ItemPedido

        itemPedidoSelecionado.text = itemPedido.toString()

        var itemPedidoDAO = ItemPedidoDAO()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoExclusaoItemPedido.setOnClickListener()
        {

            caixaDeDialogoProgressBarExclusaoItemPedido()

            lifecycleScope.launch {
                var excluido = withContext(Dispatchers.IO) {
                    itemPedidoDAO.desabilitaItemPedidoNoBancoDeDados(itemPedido)
                }
                progressBarExclusaoItemPedido.dismiss()

                if (excluido) {
                    caixaDeDialogoSucessoExclusaoItemPedido()
                    setResult(Activity.RESULT_OK)
                } else {
                    caixaDeDialogoImpossibilidadeExclusaoItemPedido()
                }

            }
        }

    }


    fun inicializaCamposEBotoes ()
    {
        botaoExclusaoItemPedido  = findViewById(R.id.botaoExclusaoItemPedido)
        itemPedidoSelecionado = findViewById(R.id.txtCorpoExclusaoItemPedido)
        botaoVoltar = findViewById(R.id.botaoVoltarExclusaoItemPedido)
    }


    fun pegaItemPedidoDaActivityAnterior(bundle: Bundle?): ItemPedido? {
        return bundle?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable("itemPedido", ItemPedido::class.java)
            } else {
                getParcelable("itemPedido")
            }
        }
    }

    fun caixaDeDialogoSucessoExclusaoItemPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_exclusao_item_pedido, null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOkExclusaoItemPedido);
        botaoSucesso.setOnClickListener{dialog.dismiss()
            this.finish()
        }
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeExclusaoItemPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_exclusao_item_pedido, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidadeExclusaoItemPedido);
        botaoImpossibilidade.setOnClickListener{dialog.dismiss()
            this.finish()
        }
        dialog = build.create()
        dialog.show()

    }


    fun caixaDeDialogoProgressBarExclusaoItemPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_exclusao_item_pedido, null)

        build.setView(view);

        progressBarExclusaoItemPedido = build.create()
        progressBarExclusaoItemPedido.show()

    }

}