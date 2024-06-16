package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Cliente
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedidoDAO
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Pedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.PedidoDAO
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Produto
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ProdutoDAO

class SelecaoItemPedidoExclusaoController : AppCompatActivity() {

    private lateinit var listViewItemPedidoRecuperados: ListView
    private lateinit var botaoDeVoltar: TextView
    private lateinit var progressBarVisualizacaoItemPedido: AlertDialog
    private lateinit var pedidoRecebido: Pedido

    private val itemPedidoDAO = ItemPedidoDAO()
    private var adaptador: ItemPedidoAdapter? = null

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                // Exibe a barra de progresso enquanto a lista de itens é atualizada
                caixaDeDialogoProgressBarBuscaItemsPedidos()

                lifecycleScope.launch {
                    val listaDeItenPedidosAtualizada = withContext(Dispatchers.IO) {
                        itemPedidoDAO.BuscaItemPedidoNoBancoDeDados(pedidoRecebido)
                    }
                    progressBarVisualizacaoItemPedido.dismiss()

                    // Atualiza a lista de pedidos na interface gráfica
                    adaptador?.apply {
                        clear()
                        addAll(listaDeItenPedidosAtualizada)
                        notifyDataSetChanged()
                    } ?: run {
                        adaptador = ItemPedidoAdapter(this@SelecaoItemPedidoExclusaoController, listaDeItenPedidosAtualizada)
                        listViewItemPedidoRecuperados.adapter = adaptador
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_item_pedido_exclusao)

        inicializaComponentesInterfaceGrafica()

        val pedido = pegaPedidoDaActivityAnterior(intent.extras) as Pedido
        pedidoRecebido = pedido

        botaoDeVoltar.setOnClickListener {
            this.finish()
        }

        caixaDeDialogoProgressBarBuscaItemsPedidos()

        lifecycleScope.launch {
            val listaDeItemPedido = withContext(Dispatchers.IO) {
                itemPedidoDAO.BuscaItemPedidoNoBancoDeDados(pedido)
            }
            progressBarVisualizacaoItemPedido.dismiss()

            if (listaDeItemPedido.isNotEmpty()) {
                adaptador = ItemPedidoAdapter(this@SelecaoItemPedidoExclusaoController, listaDeItemPedido)
                listViewItemPedidoRecuperados.adapter = adaptador

                listViewItemPedidoRecuperados.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

                    val itemSelecionado = parent.getItemAtPosition(position) as ItemPedido

                    val intent = Intent(this@SelecaoItemPedidoExclusaoController, ExclusaoItemPedidoController::class.java)
                    intent.putExtra("itemPedido", itemSelecionado)
                    startForResult.launch(intent)
                }

            } else {
                criarToastCustomizadoListaDeItensPedidosVazia()
            }
        }
    }

    private fun caixaDeDialogoProgressBarBuscaItemsPedidos() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_visualizacao_item_pedido, null)

        build.setView(view)

        progressBarVisualizacaoItemPedido = build.create()
        progressBarVisualizacaoItemPedido.show()
    }

    private fun pegaPedidoDaActivityAnterior(bundle: Bundle?): Pedido? {
        return bundle?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable("Pedido", Pedido::class.java)
            } else {
                getParcelable("Pedido")
            }
        }
    }

    private fun criarToastCustomizadoListaDeItensPedidosVazia() {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_lista_item_pedido_vazia, null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    private fun inicializaComponentesInterfaceGrafica() {
        listViewItemPedidoRecuperados = findViewById(R.id.listViewSelecaoItemExclusao)
        botaoDeVoltar = findViewById(R.id.botaoVoltarSelecaoItemExclusao)
    }
}