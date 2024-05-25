package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Cliente

class CadastroClienteController : AppCompatActivity() {


    lateinit var campoCpf : EditText
    lateinit var campoNome : EditText
    lateinit var campoTelefone : EditText
    lateinit var campoEndereco : EditText
    lateinit var campoInstagram : EditText
    lateinit var botaoCadastroCliente : Button
    lateinit var botaoVoltar : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

        inicializaCamposEBotoes()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoCadastroCliente.setOnClickListener()
        {

        }


    }

    fun inicializaCamposEBotoes ()
    {
        campoCpf = findViewById(R.id.editCpf)
        campoNome = findViewById(R.id.editNome)
        campoTelefone = findViewById(R.id.editTelefone)
        campoEndereco = findViewById(R.id.editEndereco)
        campoInstagram = findViewById(R.id.editInstagram)
        botaoCadastroCliente = findViewById(R.id.botaoCadastrarCliente)
        botaoVoltar = findViewById(R.id.botaoVoltarCadastroCliente)
    }

    fun montaCliente () : Cliente
    {
       var cliente = Cliente(campoCpf.text.toString(), campoNome.text.toString(),
                             campoTelefone.text.toString(), campoEndereco.text.toString(),
                             campoInstagram.text.toString())

        return cliente;
    }
}