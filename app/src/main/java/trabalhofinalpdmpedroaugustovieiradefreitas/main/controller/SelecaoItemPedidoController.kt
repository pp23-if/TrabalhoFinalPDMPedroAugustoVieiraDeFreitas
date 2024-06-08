package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Pedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.PedidoDAO
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Produto
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ProdutoDAO

class SelecaoItemPedidoController : AppCompatActivity() {

    lateinit var listViewItemPedidoRecuperados : ListView
    lateinit var botaoDeVoltar : TextView
    lateinit var progressBarVisualizacaoItemPedido : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_item_pedido_adicao)

        inicializaComponentesInterfaceGrafica()

        var pedido = pegaPedidoDaActivityAnterior(intent.extras) as Pedido

        botaoDeVoltar.setOnClickListener()
        {
            this.finish()
        }

        var produtoDAO = ProdutoDAO();

        caixaDeDialogoProgressBarBuscaItemsPedidos()

        lifecycleScope.launch {
            val listaDeItemPedido = withContext(Dispatchers.IO) {
                produtoDAO.BuscaProdutosNoBancoDeDados()
            }
            progressBarVisualizacaoItemPedido.dismiss()

            if (listaDeItemPedido.isNotEmpty()) {
                val adaptador = ProdutoAdapter(this@SelecaoItemPedidoController, listaDeItemPedido)
                listViewItemPedidoRecuperados.adapter = adaptador

                listViewItemPedidoRecuperados.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

                    val itemSelecionado = parent.getItemAtPosition(position) as Produto

                    val intent = Intent(this@SelecaoItemPedidoController, AdicaoItemPedidoController::class.java)
                    intent.putExtra("Pedido", pedido)
                    intent.putExtra("Produto", itemSelecionado)
                    startActivity(intent)
                }

            } else {
                criarToastCustomizadoListaDeItensPedidosVazia()
            }
        }
    }

    fun caixaDeDialogoProgressBarBuscaItemsPedidos() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_visualizacao_produto, null)

        build.setView(view)

        progressBarVisualizacaoItemPedido = build.create()
        progressBarVisualizacaoItemPedido.show()
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

    fun criarToastCustomizadoListaDeItensPedidosVazia() {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_lista_produtos_vazia, null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun inicializaComponentesInterfaceGrafica() {
        listViewItemPedidoRecuperados = findViewById(R.id.listViewSelecaoItemPedido)
        botaoDeVoltar = findViewById(R.id.botaoVoltarSelecaoItemPedido)
    }
}
