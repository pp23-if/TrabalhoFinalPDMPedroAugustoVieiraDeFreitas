package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Produto
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ProdutoDAO

class SelecaoProdutosAlteracaoController : AppCompatActivity() {

    lateinit var listViewProdutosRecuperados : ListView
    lateinit var botaoDeVoltar : TextView
    lateinit var progressBarVisualizacaoProduto : AlertDialog
    var produtoDAO = ProdutoDAO()
    var adaptador: ProdutoAdapter? = null

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                caixaDeDialogoProgressBarBuscaDeProdutos()

                lifecycleScope.launch {
                    val listaDeProdutosAtualizada = withContext(Dispatchers.IO) {
                       produtoDAO.BuscaProdutosNoBancoDeDados()
                    }
                    progressBarVisualizacaoProduto.dismiss()

                    adaptador?.clear()
                    adaptador?.addAll(listaDeProdutosAtualizada)
                    adaptador?.notifyDataSetChanged()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_produtos)

        inicializaComponentesInterfaceGrafica()

        botaoDeVoltar.setOnClickListener {
            this.finish()
        }

        caixaDeDialogoProgressBarBuscaDeProdutos()

        lifecycleScope.launch {
            val listaDeProdutos = withContext(Dispatchers.IO) {
                produtoDAO.BuscaProdutosNoBancoDeDados()
            }
            progressBarVisualizacaoProduto.dismiss()

            if (listaDeProdutos.isNotEmpty()) {
                adaptador = ProdutoAdapter(this@SelecaoProdutosAlteracaoController, listaDeProdutos)
                listViewProdutosRecuperados.adapter = adaptador

                listViewProdutosRecuperados.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

                    val itemSelecionado = parent.getItemAtPosition(position) as Produto

                    val intent = Intent(this@SelecaoProdutosAlteracaoController, AtualizacaoProdutoController::class.java)
                    intent.putExtra("Produto", itemSelecionado)
                    startForResult.launch(intent)
                }

            } else {
                criarToastCustomizadoListaDeProdutosVazia()
            }
        }
    }

    fun caixaDeDialogoProgressBarBuscaDeProdutos() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_visualizacao_produto, null)

        build.setView(view)

        progressBarVisualizacaoProduto = build.create()
        progressBarVisualizacaoProduto.show()
    }

    fun criarToastCustomizadoListaDeProdutosVazia() {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_lista_produtos_vazia, null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun inicializaComponentesInterfaceGrafica() {
        listViewProdutosRecuperados = findViewById(R.id.listViewSelecaoProdutosCadastrados)
        botaoDeVoltar = findViewById(R.id.botaoVoltarSelecaoProdutos)
    }
}
