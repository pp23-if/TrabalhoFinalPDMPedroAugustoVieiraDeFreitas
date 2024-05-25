package trabalhofinalpdmpedroaugustovieiradefreitas.main.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Parcelize
class Pedido (private var idPedido: Int, private var data :  LocalDateTime? = LocalDateTime.now(),
               private var cliente : Cliente) : Parcelable
{
    fun setIdPedidoAtributo(idPedido : Int) {
        this.idPedido = idPedido
    }

    fun getIdPedidoAtributo(): Int {
        return this.idPedido
    }

    fun setDataAtributo(data : LocalDateTime) {
        this.data = data
    }

    fun getDataAtributo(): LocalDateTime? {
        return this.data
    }

    fun setClienteAtributo(cliente: Cliente) {
        this.cliente = cliente
    }

    fun getClienteAtributo(): Cliente {
        return this.cliente
    }


    override fun toString(): String {

        val formatadorDia = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        return  "Pedido: " + this.idPedido + "\n" +
                "Data: " + this.data!!.format(formatadorDia) + "\n" +
                "Cliente: " + "\n" +
                "Cpf: " + this.cliente.getCpfAtributo() + "\n" +
                "Nome: " + this.cliente.getNomeAtributo() + "\n"
    }
}