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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ClienteDAO
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedidoDAO
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Pedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Produto
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ProdutoDAO

class SelecaoProdutoAlteracaoItemPedido : AppCompatActivity() {

    lateinit var listViewProdutosPraSelecionar : ListView
    lateinit var botaoDeVoltar : TextView
    lateinit var progressBarVisualizacaoProdutoPraSelecionar : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_produto_item_pedido_alteracao)

        inicializaComponentesInterfaceGrafica()

        var itemPedido = pegaItemPedidoDaActivityAnterior(intent.extras) as ItemPedido

        botaoDeVoltar.setOnClickListener()
        {
            this.finish()
        }

        val produtoDAO = ProdutoDAO()

        caixaDeDialogoProgressBarBuscaDeProduto()

        lifecycleScope.launch {
            val listaDeProdutos = withContext(Dispatchers.IO) {
                produtoDAO.BuscaProdutosNoBancoDeDados()
            }
            progressBarVisualizacaoProdutoPraSelecionar.dismiss()

            if (listaDeProdutos.isNotEmpty()) {
                val adaptador = ProdutoAdapter(this@SelecaoProdutoAlteracaoItemPedido, listaDeProdutos)
                listViewProdutosPraSelecionar.adapter = adaptador

                listViewProdutosPraSelecionar.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

                    val itemSelecionado = parent.getItemAtPosition(position) as Produto

                    val intent = Intent(this@SelecaoProdutoAlteracaoItemPedido, AtualizacaoItemPedidoController::class.java)
                    intent.putExtra("Produto", itemSelecionado)
                    intent.putExtra("itemPedido", itemPedido)
                    startActivity(intent)
                }

            } else {
                criarToastCustomizadoListaDeProdutoVazia()
            }
        }
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

    fun caixaDeDialogoProgressBarBuscaDeProduto() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_visualizacao_produto, null)

        build.setView(view)

        progressBarVisualizacaoProdutoPraSelecionar = build.create()
        progressBarVisualizacaoProdutoPraSelecionar.show()
    }

    fun criarToastCustomizadoListaDeProdutoVazia() {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_lista_produtos_vazia, null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun inicializaComponentesInterfaceGrafica() {
        listViewProdutosPraSelecionar = findViewById(R.id.listViewSelecaoProdutoAlteracaoItemPedido)
        botaoDeVoltar = findViewById(R.id.botaoVoltarSelecaoProdutoAlteracaoItemPedido)
    }
}
