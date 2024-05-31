package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R

class ProdutoController : AppCompatActivity() {

    lateinit var botaoCadastroProduto : Button
    lateinit var botaoVisualizacaoProduto : Button
    lateinit var botaoAlteracaoProduto : Button
    lateinit var botaoExclusaoProduto : Button
    lateinit var botaoVoltar : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opcoes_produto)

        inicializaBotoes()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoCadastroProduto.setOnClickListener()
        {
            var intent = Intent(this, CadastroProdutoController::class.java);
            startActivity(intent);
        }

        botaoVisualizacaoProduto.setOnClickListener()
        {
            var intent = Intent(this, VisualizacaoProdutoController::class.java);
            startActivity(intent);
        }

        botaoAlteracaoProduto.setOnClickListener()
        {

        }

        botaoExclusaoProduto.setOnClickListener()
        {

        }
    }

    fun inicializaBotoes ()
    {
        botaoCadastroProduto = findViewById(R.id.botaoCadastroProduto)
        botaoVisualizacaoProduto = findViewById(R.id.botaoVisualizacaoProduto)
        botaoAlteracaoProduto = findViewById(R.id.botaoAlteracaoProduto)
        botaoExclusaoProduto = findViewById(R.id.botaoExclusaoProduto)
        botaoVoltar = findViewById(R.id.botaoVoltarProdutos)
    }
}