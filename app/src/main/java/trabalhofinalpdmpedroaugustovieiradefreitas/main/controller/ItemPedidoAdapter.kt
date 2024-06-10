package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import android.widget.TextView
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Cliente
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.ItemPedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Pedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Produto

class ItemPedidoAdapter (context: Context, itemPedidos: List<ItemPedido>) :
    ArrayAdapter<ItemPedido>(context, 0, itemPedidos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemPedido = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_item_lista, parent, false)

        val textViewItem = view.findViewById<TextView>(R.id.textViewItem)
        textViewItem.text = itemPedido?.toString()

        return view
    }
}
