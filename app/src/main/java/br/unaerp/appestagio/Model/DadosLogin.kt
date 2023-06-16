package br.unaerp.appestagio.Model


open class DadosLogin{
    companion object{
        var id: Int = 0
        lateinit var nome: String
        lateinit var email: String
        var tipo: Boolean = false
    }
}
