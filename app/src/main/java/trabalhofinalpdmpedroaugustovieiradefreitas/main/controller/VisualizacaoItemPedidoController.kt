package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Cliente
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ClienteDAO
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedidoDAO
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Pedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ProdutoDAO

class VisualizacaoItemPedidoController : AppCompatActivity() {

    lateinit var listViewItemPedidoRecuperados : ListView
    lateinit var botaoDeVoltar : TextView
    lateinit var progressBarVisualizacaoItemPedido : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizacao_item_pedido)

        inicializaComponentesInterfaceGrafica()

        var pedido = pegaPedidoDaActivityAnterior(intent.extras) as Pedido

        botaoDeVoltar.setOnClickListener()
        {
            this.finish()
        }

        val itemPedidoDAO = ItemPedidoDAO()

        caixaDeDialogoProgressBarBuscaDeItemPedido()

        lifecycleScope.launch {
            val listaDeItemPedido = withContext(Dispatchers.IO) {
               itemPedidoDAO.BuscaItemPedidoNoBancoDeDados(pedido)
            }
            progressBarVisualizacaoItemPedido.dismiss()

            if (listaDeItemPedido.isNotEmpty()) {
                val adaptador = ItemPedidoAdapter(this@VisualizacaoItemPedidoController, listaDeItemPedido)
                listViewItemPedidoRecuperados.adapter = adaptador
            } else {
                criarToastCustomizadoListaDeItemPedidoVazia()
            }
        }
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

    fun caixaDeDialogoProgressBarBuscaDeItemPedido() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_visualizacao_item_pedido, null)

        build.setView(view)

        progressBarVisualizacaoItemPedido = build.create()
        progressBarVisualizacaoItemPedido.show()
    }

    fun criarToastCustomizadoListaDeItemPedidoVazia() {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_lista_item_pedido_vazia, null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun inicializaComponentesInterfaceGrafica() {
        listViewItemPedidoRecuperados = findViewById(R.id.listViewVisualizacaoItemPedido)
        botaoDeVoltar = findViewById(R.id.botaoVoltarVisualizacaoItemPedido)
    }
}
