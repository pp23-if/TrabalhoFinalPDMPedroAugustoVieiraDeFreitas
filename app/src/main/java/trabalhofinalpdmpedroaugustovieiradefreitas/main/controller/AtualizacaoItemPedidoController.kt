package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.app.Activity
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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Cliente
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedidoDAO
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Produto
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ProdutoDAO

class AtualizacaoItemPedidoController : AppCompatActivity() {


    lateinit var botaoAlterarProduto : Button
    lateinit var botaoVoltar : TextView
    lateinit var corpoTextoItemPedido : TextView
    lateinit var corpoTextoProduto : TextView
    lateinit var campoQuantidade : EditText
    lateinit var progressBarAlteracaoItemPedido: AlertDialog
    lateinit var dialog : AlertDialog;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alteracao_item_pedido)

        //Log.i("Erro", "O OBJETO E: $cliente")

        inicializaInterfaceGrafica()

        var produto = pegaProdutoDaActivityAnterior(intent.extras) as Produto

        var itemPedido = pegaItemPedidoDaActivityAnterior(intent.extras) as ItemPedido

        corpoTextoItemPedido.text = itemPedido.toString()
        corpoTextoProduto.text = produto.toString()


        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoAlterarProduto.setOnClickListener()
        {
            if(verificaEntradasVazias())
            {
                criarToastCustomizadoEntradaVazia()
            }
            else
            {
                var itemPedidoAtualizado = ItemPedido(0, itemPedido.getPedidoAtributo(), produto, campoQuantidade.text.toString().toDouble());

                var itemPedidoDAO = ItemPedidoDAO()

                caixaDeDialogoProgressBarAlteracaoItemPedido()

                lifecycleScope.launch {
                    var atualizouItemPedido = withContext(Dispatchers.IO) {
                        itemPedidoDAO.AtualizaItemPedidoNoBancoDeDados(itemPedido, itemPedidoAtualizado)
                    }
                    progressBarAlteracaoItemPedido.dismiss()

                    if(atualizouItemPedido)
                    {
                        caixaDeDialogoSucessoAlteracaoItemPedido()
                    }
                    else
                    {
                        caixaDeDialogoImpossibilidadeAlteracaoItemPedido()
                    }
                }

            }
        }

    }

    fun inicializaInterfaceGrafica ()
    {
        corpoTextoItemPedido = findViewById(R.id.txtCorpoAlteracaoItempedido)
        corpoTextoProduto = findViewById(R.id.txtCorpoAlteracaoItempedido2)
        campoQuantidade = findViewById(R.id.editQuantidadeItemPedidoAlteracao)
        botaoAlterarProduto = findViewById(R.id.botaoAlteracaoItempedido)
        botaoVoltar = findViewById(R.id.botaoVoltarAlteracaoItempedido)

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

    fun pegaItemPedidoDaActivityAnterior(bundle: Bundle?): ItemPedido? {
        return bundle?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable("itemPedido", ItemPedido::class.java)
            } else {
                getParcelable("itemPedido")
            }
        }
    }

    fun caixaDeDialogoProgressBarAlteracaoItemPedido ()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_alteracao_item_pedido, null)

        build.setView(view);

        progressBarAlteracaoItemPedido = build.create()
        progressBarAlteracaoItemPedido.show()

    }

    fun caixaDeDialogoSucessoAlteracaoItemPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_alteracao_item_pedido, null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOkAlteracaoItemPedido);
        botaoSucesso.setOnClickListener{dialog.dismiss()
            this.finish()
        }
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeAlteracaoItemPedido()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_alteracao_item_pedido, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidadeAlteracaoItemPedido);
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

}