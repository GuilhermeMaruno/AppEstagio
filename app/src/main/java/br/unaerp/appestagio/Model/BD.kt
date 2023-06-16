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

    object BDManager {
        val bd: BD by lazy { BD() }
    }

    object UserLogado{
        var dbUser: FirebaseUser? = null
        var user: Usuario = Usuario("","",false)
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