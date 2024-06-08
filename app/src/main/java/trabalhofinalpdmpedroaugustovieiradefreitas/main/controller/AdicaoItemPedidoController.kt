package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedidoDAO
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Pedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Produto
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ProdutoDAO

class AdicaoItemPedidoController : AppCompatActivity() {

    lateinit var corpoTextoItemPedido: TextView
    lateinit var botaoVoltar : TextView
    lateinit var campoQuantidade : EditText
    lateinit var botaoAdicionar : Button
    lateinit var progressBarCadastroItemPedido: AlertDialog
    lateinit var dialog : AlertDialog;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicao_item_pedido)

        inicializaInterfaceGrafica()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        var pedido = pegaPedidoDaActivityAnterior(intent.extras) as Pedido

        var produto = pegaProdutoDaActivityAnterior(intent.extras) as Produto

        corpoTextoItemPedido.text = produto.toString()

        botaoAdicionar.setOnClickListener()
        {
            if(verificaEntradasVazias())
            {
                criarToastCustomizadoEntradaVazia()
            }
            else
            {
                var itemPedidoDAO = ItemPedidoDAO()

                var itemPedido = montaItemPedido(pedido, produto)

                caixaDeDialogoProgressBarAdicaoItemPedido()

                lifecycleScope.launch {
                    var itemPedidoInserido = withContext(Dispatchers.IO) {
                        itemPedidoDAO.insereItemPedidoNoBancoDeDados(itemPedido)
                    }
                    progressBarCadastroItemPedido.dismiss()

                    if(itemPedidoInserido)
                    {
                        limparCampos()
                        caixaDeDialogoSucessoAdicaoItemPedido()
                    }
                    else
                    {
                        caixaDeDialogoImpossibilidadeAdicaoItemPedido()
                    }
                }

            }
        }

    }

    fun inicializaInterfaceGrafica ()
    {

        corpoTextoItemPedido = findViewById(R.id.txtCorpoAdicaoItemPedido)
        campoQuantidade = findViewById(R.id.editQuantidadeItemPedido)
        botaoAdicionar = findViewById(R.id.botaoAdicaoItemPedido)
        botaoVoltar = findViewById(R.id.botaoVoltarAdicaoItemPedido)

    }

    fun pegaPedidoDaActivityAnterior(bundle: Bundle?): Pedido? {
        return bundle?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable("Pedido", Pedido::class.java)
            } else {
                getParcelable("Pedido")
            }
        }
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

    fun montaItemPedido (pedido: Pedido, produto: Produto) : ItemPedido
    {
       var quantidade = campoQuantidade.text.toString().toDouble();

       var itemPedido = ItemPedido(0, pedido, produto, quantidade);

       return itemPedido
    }

    fun caixaDeDialogoProgressBarAdicaoItemPedido ()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_adicao_item_pedido, null)

        build.setView(view);

        progressBarCadastroItemPedido = build.create()
        progressBarCadastroItemPedido.show()

    }

    fun caixaDeDialogoSucessoAdicaoItemPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_adicao_item_pedido,null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOkAdicaoItemPedido);
        botaoSucesso.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeAdicaoItemPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_adicao_item_pedido, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidadeAdicaoItemPedido);
        botaoImpossibilidade.setOnClickListener{dialog.dismiss()
            this.finish()
        }
        dialog = build.create()
        dialog.show()

    }

    fun criarToastCustomizadoEntradaVazia ()
    {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_message,null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }


    fun verificaEntradasVazias () : Boolean
    {
        return (campoQuantidade.text.toString().isNullOrBlank() || campoQuantidade.text.toString().isNullOrEmpty())
    }

    fun limparCampos ()
    {
        campoQuantidade.text.clear();

    }
}