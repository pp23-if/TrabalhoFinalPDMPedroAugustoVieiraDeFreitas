package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import android.widget.TextView
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Cliente
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Pedido
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Produto

class PedidoAdapter (context: Context, pedidos: List<Pedido>) :
    ArrayAdapter<Pedido>(context, 0, pedidos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val pedido = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_item_lista, parent, false)

        val textViewItem = view.findViewById<TextView>(R.id.textViewItem)
        textViewItem.text = pedido?.toString()

        return view
    }
}
