package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller


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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ClienteDAO
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ProdutoDAO

class VisualizacaoProdutoController : AppCompatActivity() {

    lateinit var listViewProdutosRecuperados : ListView
    lateinit var botaoDeVoltar : TextView
    lateinit var progressBarVisualizacaoProduto : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizacao_produto)

        inicializaComponentesInterfaceGrafica()

        botaoDeVoltar.setOnClickListener()
        {
            this.finish()
        }

        val produtoDAO = ProdutoDAO()

        caixaDeDialogoProgressBarBuscaDeProdutos()

        lifecycleScope.launch {
            val listaDeProdutos = withContext(Dispatchers.IO) {
                produtoDAO.BuscaProdutosNoBancoDeDados()
            }
            progressBarVisualizacaoProduto.dismiss()

            if (listaDeProdutos.isNotEmpty()) {
                val adaptador = ProdutoAdapter(this@VisualizacaoProdutoController, listaDeProdutos)
                listViewProdutosRecuperados.adapter = adaptador
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
        listViewProdutosRecuperados = findViewById(R.id.listViewProdutosCadastrados)
        botaoDeVoltar = findViewById(R.id.botaoVoltarVisualizacaoProduto)
    }
}
