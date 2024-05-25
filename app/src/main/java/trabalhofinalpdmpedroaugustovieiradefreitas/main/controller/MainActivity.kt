package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R

class MainActivity : AppCompatActivity() {

    lateinit var botaoOpcoesCliente : Button
    lateinit var botaoOpcoesProduto : Button
    lateinit var botaoOpcoePedido : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializaBotoes()

        botaoOpcoesCliente.setOnClickListener()
        {
            var intent = Intent(this, ClienteController::class.java);
            startActivity(intent);
        }

        botaoOpcoesProduto.setOnClickListener()
        {
            var intent = Intent(this, ProdutoController::class.java);
            startActivity(intent);
        }

        botaoOpcoePedido.setOnClickListener()
        {
            var intent = Intent(this, PedidoController::class.java);
            startActivity(intent);
        }

    }

    fun inicializaBotoes ()
    {
        botaoOpcoesCliente = findViewById(R.id.botaoOpcoesCliente)
        botaoOpcoesProduto = findViewById(R.id.botaoOpcoesProduto)
        botaoOpcoePedido = findViewById(R.id.botaoOpcoesPedido)
    }
}