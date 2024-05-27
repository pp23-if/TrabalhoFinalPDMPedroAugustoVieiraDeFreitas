package trabalhofinalpdmpedroaugustovieiradefreitas.main.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

import android.widget.TextView
import trabalhofinalpdmpedroaugustovieiradefreitas.main.R
import trabalhofinalpdmpedroaugustovieiradefreitas.main.model.Cliente

class ClienteAdapter(context: Context, clientes: List<Cliente>) :
    ArrayAdapter<Cliente>(context, 0, clientes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val cliente = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_item_lista, parent, false)

        val textViewItem = view.findViewById<TextView>(R.id.textViewItem)
        textViewItem.text = cliente?.toString() // Ajuste conforme a estrutura do seu modelo Cliente

        return view
    }
}
