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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Produto
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ProdutoDAO
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ExclusaoProdutoController : AppCompatActivity() {


    lateinit var botaoExclusaoProduto : Button
    lateinit var produtoSelecionado : TextView
    lateinit var botaoVoltar : TextView
    lateinit var progressBarExclusaoProduto: AlertDialog
    lateinit var dialog : AlertDialog;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exclusao_produto)

        //Log.i("Erro", "O OBJETO E: $cliente")

        inicializaCamposEBotoes()

        var produto = pegaProdutoDaActivityAnterior(intent.extras) as Produto

        produtoSelecionado.text = produto.toString()

        var produtoDAO = ProdutoDAO()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoExclusaoProduto.setOnClickListener()
        {

            caixaDeDialogoProgressBarExclusaoProduto()

            lifecycleScope.launch {
                var excluido = withContext(Dispatchers.IO) {
                    produtoDAO.desabilitaProdutoNoBancoDeDados(produto)
                }
                progressBarExclusaoProduto.dismiss()

                if (excluido) {
                    caixaDeDialogoSucessoExclusaoProduto()
                    setResult(Activity.RESULT_OK)
                } else {
                    caixaDeDialogoImpossibilidadeExclusaoProduto()
                }

            }
        }

    }


    fun inicializaCamposEBotoes ()
    {
        botaoExclusaoProduto  = findViewById(R.id.botaoExcluirProduto)
        produtoSelecionado = findViewById(R.id.txtCorpoExclusaoProduto)
        botaoVoltar = findViewById(R.id.botaoVoltarExclusaoProduto)
    }


    fun pegaProdutoDaActivityAnterior(bundle: Bundle?): Produto? {
        return bundle?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable("Produto", Produto::class.java)
            } else {
                getParcelable("Produto")
            }
        }
    }


    fun caixaDeDialogoSucessoExclusaoProduto()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_exclusao_produto, null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOkExclusaoProduto);
        botaoSucesso.setOnClickListener{dialog.dismiss()
            this.finish()
        }
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeExclusaoProduto()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_exclusao_produto, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidadeExclusaoProduto);
        botaoImpossibilidade.setOnClickListener{dialog.dismiss()
            this.finish()
        }
        dialog = build.create()
        dialog.show()

    }


    fun caixaDeDialogoProgressBarExclusaoProduto()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_exclusao_produto, null)

        build.setView(view);

        progressBarExclusaoProduto = build.create()
        progressBarExclusaoProduto.show()

    }

}