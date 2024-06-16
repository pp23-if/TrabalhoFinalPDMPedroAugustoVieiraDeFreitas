package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Cliente
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.PedidoDAO

class SelecaoPedidoItemExclusaoController: AppCompatActivity() {

    lateinit var botaoExclusaoPedido : Button
    lateinit var botaoExclusaoItemPedido : Button
    lateinit var botaoVoltar : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_exclusao_pedido_item_pedido)

        inicializaCamposEBotoes()

        var cliente = pegaClienteDaActivityAnterior(intent.extras) as Cliente

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoExclusaoPedido.setOnClickListener()
        {
            val intent = Intent(this, SelecaoExclusaoPedidoController::class.java)
            intent.putExtra("Cliente", cliente)
            startActivity(intent)
        }

        botaoExclusaoItemPedido.setOnClickListener()
        {
            val intent = Intent(this, SelecaoPedidoParaExclusaoItemPedidoController::class.java)
            intent.putExtra("Cliente", cliente)
            startActivity(intent)
        }

    }

    fun inicializaCamposEBotoes ()
    {
        botaoExclusaoPedido = findViewById(R.id.botaoOpcaoExclusaoPedido)
        botaoExclusaoItemPedido = findViewById(R.id.botaoOpcaoExclusaoItemPedido)
        botaoVoltar = findViewById(R.id.botaoVoltarOpcoesExclusaoPedidoItem)
    }

    fun pegaClienteDaActivityAnterior(bundle: Bundle?): Cliente? {
        return bundle?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable("Cliente", Cliente::class.java)
            } else {
                getParcelable("Cliente")
            }
        }
    }

}