package br.unaerp.appestagio.Model

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class BD {
    val listaAnuncios = mutableListOf<Anuncio>()

    object BDManager {
        val bd: BD by lazy { BD() }
    }

    object UserLogado{
        var dbUser: FirebaseUser? = null
        var user: Usuario = Usuario("","",false)
    }


    fun cadastraAnuncio(id:String ,titulo: String,area: String,local: String,email: String,telefone: String,
                        remuneracao: Double,anunciante: String,dti: String,dtf: String,
                        mostraAnunciante: Boolean,descricao: String): Anuncio {
        var anuncio = Anuncio(id,titulo,area,local,email,telefone,remuneracao,anunciante, dti, dtf, mostraAnunciante, descricao)
        return anuncio
    }


    fun addAnuncio(id:String, titulo: String,area: String,local: String,email: String,telefone: String,
                   remuneracao: Double,anunciante: String,dti: String,dtf: String,
                   mostraAnunciante: Boolean,descricao: String){
        var anuncio = cadastraAnuncio(id,titulo,area,local,email,telefone,remuneracao,anunciante,dti,dtf,mostraAnunciante,descricao)
        listaAnuncios.add(anuncio)
    }

    fun getAnuncios(email: String):List<Anuncio>{
        if(email==""){
            return listaAnuncios.sortedByDescending { it.dti }
        }else return listaAnuncios.sortedByDescending { it.dti }.filter { it.anunciante == email }
    }

    fun getAnuncioByTitulo(titulo: String): Anuncio{
        var defaultAnuncio = Anuncio("","","","","","",0.0,"","","",false,"")
        for (anuncio in listaAnuncios) {
            if (anuncio.titulo == titulo) defaultAnuncio = anuncio
        }
        return defaultAnuncio
    }

    fun filtraAnuncioArea(area: String):List<Anuncio>{
        var lista = mutableListOf<Anuncio>()
        for (anuncio in listaAnuncios) {
            if (anuncio.area == area) {
                lista.add(anuncio)
            }
        }
        return lista
    }

    fun filtraAnuncioCidade(local: String):List<Anuncio>{
        var lista = mutableListOf<Anuncio>()
        for (anuncio in listaAnuncios) {
            if (anuncio.local == local) {
                lista.add(anuncio)
            }
        }
        return lista
    }

    fun deletaAnuncio(titulo: String){
        val anuncioParaRemover = listaAnuncios.find { it.titulo == titulo }
        listaAnuncios.remove(anuncioParaRemover)
    }

    fun listAnuncios():List<Anuncio>{
        return listaAnuncios
    }

    fun formatarData(data: Date): String {
        val formatoSaida = SimpleDateFormat("dd/MM/yyyy")
        return formatoSaida.format(data)
    }

    interface LerUsuarioCallback {
        fun onUsuarioLido(usuario: Usuario)
        fun onUsuarioNaoEncontrado()
        fun onError(mensagem: String)
    }

    fun lerDadosUsuarioPorEmail(email: String, callback: LerUsuarioCallback) {
        val rootDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios")

        val query: Query = rootDbRef.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val usuario = childSnapshot.getValue(Usuario::class.java)
                        if (usuario != null) {
                            val usuarioEncontrado = Usuario(usuario.nome, email, usuario.isInteressado)
                            callback.onUsuarioLido(usuarioEncontrado)
                            return
                        }
                    }
                } else {
                    callback.onUsuarioNaoEncontrado()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback.onError("Erro na leitura dos dados: ${error.message}")
            }
        })
    }
}