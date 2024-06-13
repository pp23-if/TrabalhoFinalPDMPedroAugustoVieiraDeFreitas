package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R

class PedidoController : AppCompatActivity() {

    lateinit var botaoCadastroPedido : Button
    lateinit var botaoVisualizacaoPedido : Button
    lateinit var botaoAlteracaoPedido : Button
    lateinit var botaoExclusaoPedido : Button
    lateinit var botaoVoltar : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opcoes_pedido)

        inicializaBotoes()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoCadastroPedido.setOnClickListener()
        {
            var intent = Intent(this, SelecaoClientePedidoController::class.java);
            startActivity(intent);
        }

        botaoVisualizacaoPedido.setOnClickListener()
        {
            var intent = Intent(this, SelecaoVisualizacaoClientesController::class.java);
            startActivity(intent);
        }

        botaoAlteracaoPedido.setOnClickListener()
        {
            var intent = Intent(this, SelecaoClientePedidoAlteracaoController::class.java);
            startActivity(intent);
        }

        botaoExclusaoPedido.setOnClickListener()
        {

        }
    }

    fun inicializaBotoes ()
    {
        botaoCadastroPedido = findViewById(R.id.botaoCadastroPedido)
        botaoVisualizacaoPedido = findViewById(R.id.botaoVisualizacaoPedido)
        botaoAlteracaoPedido = findViewById(R.id.botaoAlteracaoPedido)
        botaoExclusaoPedido = findViewById(R.id.botaoExclusaoPedido)
        botaoVoltar = findViewById(R.id.botaoVoltarPedidos)
    }
}