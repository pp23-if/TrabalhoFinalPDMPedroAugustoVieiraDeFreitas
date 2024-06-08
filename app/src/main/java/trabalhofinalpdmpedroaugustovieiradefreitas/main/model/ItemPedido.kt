package trabalhofinalpdmpedroaugustovieiradefreitas.main.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime


@Parcelize
class ItemPedido (private var idItemPedido: Int, private var pedido : Pedido,
              private var produto : Produto, private var quantidade : Double) : Parcelable
{
    fun setIdItemPedidoAtributo(idItemPedido: Int) {
        this.idItemPedido = idItemPedido
    }

    fun getIdPedidoAtributo(): Int {
        return this.idItemPedido
    }

    fun setPedidoAtributo(pedido: Pedido) {
        this.pedido = pedido
    }

    fun getPedidoAtributo(): Pedido {
        return this.pedido
    }

    fun setProdutoAtributo(produto: Produto) {
        this.produto = produto
    }

    fun getProdutoAtributo(): Produto {
        return this.produto
    }

    fun setQuantidadeAtributo(quantidade: Double) {
        this.quantidade = quantidade
    }

    fun getQuantidadeAtributo(): Double {
        return this.quantidade
    }

    override fun toString(): String {

        return  "Pedido: " + this.pedido.getIdPedidoAtributo() + "\n" +
                "Data: " + this.pedido.getDataAtributo() + "\n" +
                "ItemPedido: " + this.produto + "\n" +
                "Quantidade: " +  this.quantidade + "\n" +
                "Cliente: " + "\n" +
                "Cpf: " + this.pedido.getClienteAtributo().getCpfAtributo() + "\n" +
                "Nome: " + this.pedido.getClienteAtributo().getNomeAtributo() + "\n"
    }
}