package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Produto
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ProdutoDAO

class CadastroProdutoController : AppCompatActivity() {

    lateinit var radioButtonTipoGraoArabicaCerrado : RadioButton
    lateinit var radioButtonTipoGraoCanilon : RadioButton
    lateinit var radioButtonPontoTorraMedia : RadioButton
    lateinit var radioButtonPontoTorraForte : RadioButton
    lateinit var radioButtonBlendSim : RadioButton
    lateinit var radioButtonBlendNao : RadioButton
    lateinit var botaoCadastrarProduto : Button
    lateinit var botaoVoltar : TextView
    lateinit var campoValor : EditText
    lateinit var progressBarCadastroProduto: AlertDialog
    lateinit var dialog : AlertDialog;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_produto)

        inicializaInterfaceGrafica()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoCadastrarProduto.setOnClickListener()
        {
          if(verificaEntradasVazias())
          {
              criarToastCustomizadoEntradaVazia()
          }
          else
          {
              var produto = montaProduto()

              var produtoDAO = ProdutoDAO()

             caixaDeDialogoProgressBarCadastroProduto()

              lifecycleScope.launch {
                  var produtoInserido = withContext(Dispatchers.IO) {
                      produtoDAO.insereProdutoNoBancoDeDados(produto)
                  }
                  progressBarCadastroProduto.dismiss()

                  if(produtoInserido)
                  {
                      limparCampos()
                      caixaDeDialogoSucessoCadastroProduto()
                  }
                  else
                  {
                      caixaDeDialogoImpossibilidadeCadastroProduto()
                  }
              }

          }
        }

    }

    fun inicializaInterfaceGrafica ()
    {
        radioButtonTipoGraoArabicaCerrado = findViewById(R.id.botaoArabicaDoCerrado)
        radioButtonTipoGraoCanilon = findViewById(R.id.botaoCanilon)
        radioButtonPontoTorraMedia = findViewById(R.id.botaoPontoMedia)
        radioButtonPontoTorraForte = findViewById(R.id.botaoPontoForte)
        radioButtonBlendSim = findViewById(R.id.botaoSim)
        radioButtonBlendNao = findViewById(R.id.botaoNao)
        campoValor = findViewById(R.id.editValorProduto)
        botaoCadastrarProduto = findViewById(R.id.botaoCadastrarProduto)
        botaoVoltar = findViewById(R.id.botaoVoltarCadastroProduto)

    }

    fun caixaDeDialogoProgressBarCadastroProduto ()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_cadastro_produto, null)

        build.setView(view);

        progressBarCadastroProduto = build.create()
        progressBarCadastroProduto.show()

    }

    fun caixaDeDialogoSucessoCadastroProduto()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_cadastro_produto, null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOkCadastroProduto);
        botaoSucesso.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeCadastroProduto()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_cadastro_produto, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidadeCadastroProduto);
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

    fun montaProduto(): Produto {

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

        return Produto(0, tipoGrao, pontoTorra, valor, blend)
    }

    fun verificaEntradasVazias () : Boolean
    {
        return (campoValor.text.toString().isNullOrBlank() || campoValor.text.toString().isNullOrEmpty())
    }

    fun limparCampos ()
    {
        campoValor.text.clear();

    }
}