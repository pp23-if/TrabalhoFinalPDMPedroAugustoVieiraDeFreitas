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
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Produto
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ProdutoDAO

class AtualizacaoProdutoController : AppCompatActivity() {

    lateinit var radioButtonTipoGraoArabicaCerrado : RadioButton
    lateinit var radioButtonTipoGraoCanilon : RadioButton
    lateinit var radioButtonPontoTorraMedia : RadioButton
    lateinit var radioButtonPontoTorraForte : RadioButton
    lateinit var radioButtonBlendSim : RadioButton
    lateinit var radioButtonBlendNao : RadioButton
    lateinit var botaoAlterarProduto : Button
    lateinit var botaoVoltar : TextView
    lateinit var campoValor : EditText
    lateinit var progressBarAlteracaoProduto: AlertDialog
    lateinit var dialog : AlertDialog;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alteracao_produto)

        //Log.i("Erro", "O OBJETO E: $cliente")

        inicializaInterfaceGrafica()

        var produto = pegaProdutoDaActivityAnterior(intent.extras) as Produto

        Log.i("Erro", "O PRODUTO E: $produto")

        insereDadosProdutoNaTela(produto)

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
                var produtoAtualizado = montaProduto(produto)

                var produtoDAO = ProdutoDAO()

                Log.i("Erro", "O PRODUTO ANTES BACNO E: $produtoAtualizado")

                caixaDeDialogoProgressBarAlteracaoProduto()

                lifecycleScope.launch {
                    var atualizouProduto = withContext(Dispatchers.IO) {
                        produtoDAO.AtualizaProdutoNoBancoDeDados(produtoAtualizado)
                    }
                    progressBarAlteracaoProduto.dismiss()

                    if(atualizouProduto)
                    {
                        caixaDeDialogoSucessoAlteracaoProduto()
                        setResult(Activity.RESULT_OK)
                    }
                    else
                    {
                        caixaDeDialogoImpossibilidadeAlteracaoProduto()
                    }
                }

            }
        }

    }

    fun inicializaInterfaceGrafica ()
    {
        radioButtonTipoGraoArabicaCerrado = findViewById(R.id.botaoArabicaDoCerradoAlteracao)
        radioButtonTipoGraoCanilon = findViewById(R.id.botaoCanilonAlteracao)
        radioButtonPontoTorraMedia = findViewById(R.id.botaoPontoMediaAlteracao)
        radioButtonPontoTorraForte = findViewById(R.id.botaoPontoForteAlteracao)
        radioButtonBlendSim = findViewById(R.id.botaoSimAlteracao)
        radioButtonBlendNao = findViewById(R.id.botaoNaoAlteracao)
        campoValor = findViewById(R.id.editValorProdutoAlteracao)
        botaoAlterarProduto = findViewById(R.id.botaoAlterarProduto)
        botaoVoltar = findViewById(R.id.botaoVoltarAlteracaoProduto)

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

    fun caixaDeDialogoProgressBarAlteracaoProduto ()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_alteracao_produto, null)

        build.setView(view);

        progressBarAlteracaoProduto = build.create()
        progressBarAlteracaoProduto.show()

    }

    fun caixaDeDialogoSucessoAlteracaoProduto()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_alteracao_produto, null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOkAlteracaoProduto);
        botaoSucesso.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeAlteracaoProduto()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_alteracao_produto, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidadeAlteracaoProduto);
        botaoImpossibilidade.setOnClickListener{dialog.dismiss()}
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

    fun insereDadosProdutoNaTela(produto: Produto) {


        if(produto.getTipoGraoAtributo() == "ArabicaDoCerrado")
        {
            radioButtonTipoGraoArabicaCerrado.isChecked = true
        }

        if(produto.getTipoGraoAtributo() == "Canilon")
        {
            radioButtonTipoGraoCanilon.isChecked = true
        }

        if(produto.getPontoTorraAtributo() == "media")
        {
            radioButtonPontoTorraMedia.isChecked = true
        }

        if(produto.getPontoTorraAtributo() == "forte")
        {
            radioButtonPontoTorraForte.isChecked = true
        }

        if(produto.getBlendAtributo())
        {
            radioButtonBlendSim.isChecked = true
        }
        else
        {
            radioButtonBlendNao.isChecked = true
        }

        campoValor.setText(produto.getValorAtributo().toString())

    }
    fun montaProduto(produto: Produto): Produto {

        var tipoGrao = ""

        var pontoTorra = ""

        var blend = false

        if (radioButtonTipoGraoArabicaCerrado.isChecked) {
            tipoGrao = "ArabicaDoCerrado"
        }

        if (radioButtonTipoGraoCanilon.isChecked) {
            tipoGrao = "Canilon"
        }

        if (radioButtonPontoTorraMedia.isChecked) {
            pontoTorra = "media"
        }

        if (radioButtonPontoTorraForte.isChecked) {
            pontoTorra = "forte"
        }

        if (radioButtonBlendSim.isChecked) {
            blend = true
        }

        if (radioButtonBlendNao.isChecked) {
            blend = false
        }

        var valor = campoValor.text.toString().toDouble()

        return Produto(produto.getIdProdutoAtributo(), tipoGrao, pontoTorra, valor, blend)
    }

    fun verificaEntradasVazias () : Boolean
    {
        return (campoValor.text.toString().isNullOrBlank() || campoValor.text.toString().isNullOrEmpty())
    }

}