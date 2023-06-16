package br.unaerp.appestagio.Model

data class Usuario(
    var nome: String,
    var email: String,
    var isInteressado: Boolean,
){
    constructor() : this("", "", true)
}
