package trabalhofinalpdmpedroaugustovieiradefreitas.main.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Parcelize
class Produto (private var idProduto: Int, private var tipoGrao : String,
               private var pontoTorra : String,  private var valor : Double,
               private var blend : Boolean) : Parcelable
{
    fun setIdProdutoAtributo(idProduto : Int) {
        this.idProduto = idProduto
    }

    fun getIdProdutoAtributo(): Int {
        return this.idProduto
    }

    fun setTipoGrao(tipoGrao : String) {
        this.tipoGrao = tipoGrao
    }

    fun getTipoGraoAtributo(): String {
        return this.tipoGrao
    }

    fun setPontoTorraAtributo(pontoTorra: String) {
        this.pontoTorra = pontoTorra
    }

    fun getPontoTorraAtributo(): String {
        return this.pontoTorra
    }

    fun setValorAtributo(valor: Double) {
        this.valor = valor
    }

    fun getValorAtributo(): Double {
        return this.valor
    }

    fun setBlendAtributo(blend: Boolean) {
        this.blend = blend
    }

    fun getBlendAtributo(): Boolean {
        return this.blend
    }

    override fun toString(): String {

        if(this.tipoGrao == "ArabicaDoCerrado")
        {
            this.tipoGrao = "Arábica do cerrado"
        }

        if(this.pontoTorra == "media")
        {
            this.pontoTorra = "média"
        }


        if(this.blend)
        {
            return  "Id Produto: " + this.idProduto + "\n" +
                    "Tipo Grão: " + this.tipoGrao + "\n" +
                    "Ponto Torra: " + this.pontoTorra + "\n" +
                    "Valor: " + this.valor + "\n" +
                    "Blend: " + "sim" + "\n"
        }
        else
        {
            return  "Id Produto: " + this.idProduto + "\n" +
                    "Tipo Grão: " + this.tipoGrao + "\n" +
                    "Ponto Torra: " + this.pontoTorra + "\n" +
                    "Valor: " + this.valor + "\n" +
                    "Blend: " + "não" + "\n"
        }


    }
}