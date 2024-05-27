package trabalhofinalpdmpedroaugustovieiradefreitas.main.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class Cliente (private var cpf : String, private var nome: String,
               private var telefone : String, private var endereco: String, private var instagram : String) : Parcelable
{
    fun setCpfAtributo(cpf : String) {
        this.cpf = cpf
    }

    fun getCpfAtributo(): String {
        return this.cpf
    }

    fun setNomeAtributo(nome : String) {
        this.nome = nome
    }

    fun getNomeAtributo(): String {
        return this.nome
    }

    fun setTelefoneAtributo(telefone : String) {
        this.telefone = telefone
    }

    fun getTelefoneAtributo(): String {
        return this.telefone
    }

    fun setEnderecoAtributo(endereco: String) {
        this.endereco = endereco
    }

    fun getEnderecoAtributo(): String {
        return this.endereco
    }

    fun setInstagramAtributo(instagram: String) {
        this.instagram = instagram
    }

    fun getInstagramAtributo(): String {
        return this.instagram
    }

    override fun toString(): String {

        return  "\n" + "Cliente: " + this.nome + "\n" +
                "Cpf: " + this.cpf + "\n" +
                "Telefone: " + this.telefone + "\n" +
                "Endereco: " + this.endereco + "\n" +
                "Instagram: " + this.instagram + "\n"
    }
}