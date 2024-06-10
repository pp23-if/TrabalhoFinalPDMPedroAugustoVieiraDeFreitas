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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Pedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.PedidoDAO

class SelecaoVisualizacaoPedidoClienteSelecionadoController : AppCompatActivity() {

    lateinit var listViewPedidosRecuperados : ListView
    lateinit var botaoDeVoltar : TextView
    lateinit var progressBarVisualizacaoPedido : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selcao_visualizacao_pedido_cliente_selecionado)

        inicializaComponentesInterfaceGrafica()

        var cliente = pegaClienteDaActivityAnterior(intent.extras) as Cliente

        botaoDeVoltar.setOnClickListener()
        {
            this.finish()
        }

        var pedidoDAO = PedidoDAO()

        caixaDeDialogoProgressBarBuscaDePedidos()

        lifecycleScope.launch {
            val listaDePedidos = withContext(Dispatchers.IO) {
                pedidoDAO.BuscaPedidosNoBancoDeDados(cliente);
            }
            progressBarVisualizacaoPedido.dismiss()

            if (listaDePedidos.isNotEmpty()) {
                val adaptador = PedidoAdapter(this@SelecaoVisualizacaoPedidoClienteSelecionadoController, listaDePedidos)
                listViewPedidosRecuperados.adapter = adaptador

                listViewPedidosRecuperados.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

                    val itemSelecionado = parent.getItemAtPosition(position) as Pedido

                    val intent = Intent(this@SelecaoVisualizacaoPedidoClienteSelecionadoController, VisualizacaoItemPedidoController::class.java)
                    intent.putExtra("Pedido", itemSelecionado)
                    startActivity(intent)
                }

            } else {
                criarToastCustomizadoListaDeProdutosVazia()
            }
        }
    }

    fun caixaDeDialogoProgressBarBuscaDePedidos() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_visualizacao_pedido, null)

        build.setView(view)

        progressBarVisualizacaoPedido = build.create()
        progressBarVisualizacaoPedido.show()
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

    fun criarToastCustomizadoListaDeProdutosVazia() {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_lista_pedidos_vazia, null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun inicializaComponentesInterfaceGrafica() {
        listViewPedidosRecuperados = findViewById(R.id.listViewVisualizacaoSelecaoPedidosClientesSelecionado)
        botaoDeVoltar = findViewById(R.id.botaoVoltarVisualizacaoSelecaoPedidosClientesSelecionado)
    }
}
