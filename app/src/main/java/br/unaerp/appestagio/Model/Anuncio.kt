package br.unaerp.appestagio.Model

data class Anuncio(
    var id: String,
    var titulo: String,
    var area: String,
    var local: String,
    var email: String,
    var telefone: String,
    var remuneracao: Double,
    var anunciante: String,
    var dti: String,
    var dtf: String,
    var mostraAnunciante: Boolean,
    var descricao: String
){
    constructor() : this("","","","","","",0.0,"","","",true,"")
}