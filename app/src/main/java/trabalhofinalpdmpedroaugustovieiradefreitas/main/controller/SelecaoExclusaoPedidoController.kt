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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Pedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.PedidoDAO

class SelecaoExclusaoPedidoController : AppCompatActivity() {

    lateinit var listViewPedidosRecuperados : ListView
    lateinit var botaoDeVoltar : TextView
    lateinit var progressBarVisualizacaoPedido : AlertDialog
    lateinit var clienteRecebido : Cliente

    var pedidoDAO = PedidoDAO()
    var adaptador: PedidoAdapter? = null

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                caixaDeDialogoProgressBarBuscaDePedidos()

                lifecycleScope.launch {
                    val listaDePedidosAtualizada = withContext(Dispatchers.IO) {
                        pedidoDAO.BuscaPedidosNoBancoDeDados(clienteRecebido)
                    }
                    progressBarVisualizacaoPedido.dismiss()

                    adaptador?.apply {
                        clear()
                        addAll(listaDePedidosAtualizada)
                        notifyDataSetChanged()
                    } ?: run {
                        adaptador = PedidoAdapter(this@SelecaoExclusaoPedidoController, listaDePedidosAtualizada)
                        listViewPedidosRecuperados.adapter = adaptador
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao_pedido_exclusao)

        inicializaComponentesInterfaceGrafica()

        val cliente = pegaClienteDaActivityAnterior(intent.extras) as Cliente
        clienteRecebido = cliente

        botaoDeVoltar.setOnClickListener {
            this.finish()
        }

        caixaDeDialogoProgressBarBuscaDePedidos()

        lifecycleScope.launch {
            val listaDePedidos = withContext(Dispatchers.IO) {
                pedidoDAO.BuscaPedidosNoBancoDeDados(cliente)
            }
            progressBarVisualizacaoPedido.dismiss()

            if (listaDePedidos.isNotEmpty()) {
                adaptador = PedidoAdapter(this@SelecaoExclusaoPedidoController, listaDePedidos)
                listViewPedidosRecuperados.adapter = adaptador

                listViewPedidosRecuperados.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    val itemSelecionado = parent.getItemAtPosition(position) as Pedido

                    val intent = Intent(this@SelecaoExclusaoPedidoController, ExclusaoPedidoController::class.java)
                    intent.putExtra("Pedido", itemSelecionado)
                    startForResult.launch(intent)
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
        listViewPedidosRecuperados = findViewById(R.id.listViewExclusaoPedido)
        botaoDeVoltar = findViewById(R.id.botaoVoltarExclusaoPedido)
    }
}