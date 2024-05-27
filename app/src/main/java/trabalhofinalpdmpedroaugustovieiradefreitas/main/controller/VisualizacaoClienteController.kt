package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
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

class VisualizacaoClienteController : AppCompatActivity() {

    lateinit var listViewClientesRecuperados : ListView
    var adaptador : ArrayAdapter<Cliente>? = null
    lateinit var botaoDeVoltar : TextView
    lateinit var progressBarVisualizacaoCliente : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizacao_clientes)

        inicializaComponentesInterfaceGrafica()

        botaoDeVoltar.setOnClickListener()
        {
            this.finish()
        }

        val clienteDAO = ClienteDAO()

        caixaDeDialogoProgressBarBuscaDeClientes()

        lifecycleScope.launch {
            val listaDeClientes = withContext(Dispatchers.IO) {
                clienteDAO.BuscaClientesNoBancoDeDados()
            }
            progressBarVisualizacaoCliente.dismiss()

            if (listaDeClientes.isNotEmpty()) {
                adaptador = ArrayAdapter(this@VisualizacaoClienteController, android.R.layout.simple_list_item_1, listaDeClientes)
                listViewClientesRecuperados.adapter = adaptador
            } else {
                criarToastCustomizadoListaDeClientesVazia()
            }
        }
    }

    fun caixaDeDialogoProgressBarBuscaDeClientes() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_visualizacao_cliente, null)

        build.setView(view)

        progressBarVisualizacaoCliente = build.create()
        progressBarVisualizacaoCliente.show()
    }

    fun criarToastCustomizadoListaDeClientesVazia() {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_lista_clientes_vazia, null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun inicializaComponentesInterfaceGrafica() {
        listViewClientesRecuperados = findViewById(R.id.listViewClientesCadastrados)
        botaoDeVoltar = findViewById(R.id.botaoVoltarVisualizacaoCliente)
    }
}
