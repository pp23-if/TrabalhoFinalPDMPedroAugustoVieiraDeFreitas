package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R

class ClienteController : AppCompatActivity() {

    lateinit var botaoCadastroCliente : Button
    lateinit var botaoVisualizacaoCliente : Button
    lateinit var botaoAlteracaoCliente : Button
    lateinit var botaoExclusaoCliente : Button
    lateinit var botaoVoltar : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opcoes_cliente)

        inicializaBotoes()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }
    }

    fun inicializaBotoes ()
    {
        botaoCadastroCliente = findViewById(R.id.botaoCadastroCliente)
        botaoVisualizacaoCliente = findViewById(R.id.botaoVisualizacaoCliente)
        botaoAlteracaoCliente = findViewById(R.id.botaoAlteracaoCliente)
        botaoExclusaoCliente = findViewById(R.id.botaoExclusaoCliente)
        botaoVoltar = findViewById(R.id.botaoVoltarClientes)
    }
}