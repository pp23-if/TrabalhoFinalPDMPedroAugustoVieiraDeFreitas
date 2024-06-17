package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Cliente
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ClienteDAO
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CadastroClienteController : AppCompatActivity() {


    lateinit var campoCpf : EditText
    lateinit var campoNome : EditText
    lateinit var campoTelefone : EditText
    lateinit var campoEndereco : EditText
    lateinit var campoInstagram : EditText
    lateinit var botaoCadastroCliente : Button
    lateinit var botaoVoltar : TextView
    lateinit var progressBarValidacaoDados: AlertDialog
    lateinit var progressBarCadstroCliente: AlertDialog
    lateinit var progressBarEnvioSms: AlertDialog
    lateinit var dialog : AlertDialog;
    lateinit var dialogSms : AlertDialog;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

        inicializaCamposEBotoes()

        var clienteDAO = ClienteDAO();

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoCadastroCliente.setOnClickListener()
        {

            if(verificaEntradasVazias())
            {
                criarToastCustomizadoEntradaVazia()
            }
            else
            {
                var cliente = montaCliente();

                caixaDeDialogoProgressBarValidacaoDados()

                var cpfExiste = false;

                lifecycleScope.launch {
                    cpfExiste = withContext(Dispatchers.IO) {
                         verificaCpfInformado(cliente.getCpfAtributo(), clienteDAO)
                    }
                    progressBarValidacaoDados.dismiss()

                    if(cpfExiste) {
                        criarToastCustomizadoCpfValidado()
                    }

                    else
                    {
                        caixaDeDialogoProgressBarCadastroCliente()
                        var clienteCadastrado = false;

                        lifecycleScope.launch {
                            clienteCadastrado = withContext(Dispatchers.IO) {
                                clienteDAO.insereClienteNoBancoDeDados(cliente)
                            }
                            progressBarCadstroCliente.dismiss()

                            if(clienteCadastrado)
                            {
                                caixaDeDialogoSucessoCadastroCliente()
                            }
                            else
                            {
                                caixaDeDialogoImpossibilidadeCadastroCliente()
                            }
                        }

                    }
                }
            }

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

    fun verificaCpfInformado (cpf : String, clienteDAO: ClienteDAO) : Boolean
    {
        var  cpfRecebido = cpf.trim();

        var cpfEncontrado = clienteDAO.verificaCpfNoBancoDeDados(cpfRecebido).trim()

        return cpfEncontrado.lowercase() == cpfRecebido || cpfEncontrado.uppercase() == cpfRecebido
    }

    fun caixaDeDialogoSucessoCadastroCliente() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_cadastro_cliente, null)

        build.setView(view)

        val botaoSucesso = view.findViewById<Button>(R.id.botaoOk)
        botaoSucesso.setOnClickListener {
            dialog.dismiss()

            caixaDeDialogoProgressBarEnvioMensagemSms()

            lifecycleScope.launch {
                enviarMensagemDeTexto()
                progressBarEnvioSms.dismiss()
            }

            limparCampos()
        }

        dialog = build.create()
        dialog.show()
    }

    fun caixaDeDialogoImpossibilidadeCadastroCliente()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_cadastro_cliente, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidade);
        botaoImpossibilidade.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoProgressBarValidacaoDados()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading, null)

        build.setView(view);

        progressBarValidacaoDados = build.create()
        progressBarValidacaoDados.show()

    }

    fun caixaDeDialogoProgressBarCadastroCliente()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_cadastro_cliente, null)

        build.setView(view);

        progressBarCadstroCliente = build.create()
        progressBarCadstroCliente.show()

    }

    fun criarToastCustomizadoEntradaVazia ()
    {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_message,null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun criarToastCustomizadoCpfValidado ()
    {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_validacao_cpf,null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun verificaEntradasVazias () : Boolean
    {
        return (campoCpf.text.toString().isNullOrBlank() || campoCpf.text.toString().isNullOrEmpty() ||
                campoNome.text.toString().isNullOrBlank() || campoNome.text.toString().isNullOrEmpty() ||
                campoTelefone.text.toString().isNullOrBlank() || campoTelefone.text.toString().isNullOrEmpty() ||
                campoEndereco.text.toString().isNullOrBlank() ||  campoEndereco.text.toString().isNullOrEmpty() ||
                campoInstagram.text.toString().isNullOrBlank() || campoInstagram.text.toString().isNullOrEmpty())
    }

    fun limparCampos ()
    {
        campoCpf.text.clear();
        campoNome.text.clear();
        campoTelefone.text.clear();
        campoEndereco.text.clear();
        campoInstagram.text.clear();
    }

    fun enviarMensagemDeTexto() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.INTERNET), 111)
            return
        }

        try {
            val smsManager: SmsManager = this.getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage(campoTelefone.text.toString(), null, "Parabéns! o cadastro foi efetuado com sucesso.", null, null)
            caixaDeDialogoSucessoEnvioMensagemSms()
        } catch (e: Exception) {
            caixaDeDialogoImpossibilidadeEnvioMensagemSms()
        }
    }


    fun caixaDeDialogoProgressBarEnvioMensagemSms()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_loading_mandando_mensagem_sms, null)

        build.setView(view);

        progressBarEnvioSms = build.create()
        progressBarEnvioSms.show()

    }

    fun caixaDeDialogoSucessoEnvioMensagemSms()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_envio_mensagem_sms, null)

        build.setView(view);

        var botaoSucesso = view.findViewById<Button>(R.id.botaoOkEnvioSms);
        botaoSucesso.setOnClickListener{dialogSms.dismiss()}
        dialogSms = build.create()
        dialogSms.show()

    }

    fun caixaDeDialogoImpossibilidadeEnvioMensagemSms()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_envio_mensagem_sms, null)

        build.setView(view);

        var botaoImpossibilidade = view.findViewById<Button>(R.id.botaoImpossibilidadeEnvioSms);
        botaoImpossibilidade.setOnClickListener{dialogSms.dismiss()}
        dialogSms = build.create()
        dialogSms.show()

    }

}