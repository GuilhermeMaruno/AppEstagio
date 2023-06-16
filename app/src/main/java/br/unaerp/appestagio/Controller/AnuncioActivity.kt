package br.unaerp.appestagio.Controller

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.unaerp.appestagio.R
import br.unaerp.appestagio.Model.Anuncio
import br.unaerp.appestagio.Model.BD
import com.google.firebase.database.*

class AnuncioActivity : AppCompatActivity() {
    companion object {
        const val anuncioId = "anuncioID"
    }

    private lateinit var query: Query
    private lateinit var idAnuncio: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anuncio)

        idAnuncio = intent.getStringExtra(anuncioId).toString()

        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Anuncios")
        query = databaseRef.orderByKey().equalTo(idAnuncio).limitToFirst(1)

        val btnDeleta = findViewById<Button>(R.id.btnDeletaAnuncio)
        val btnVoltar = findViewById<Button>(R.id.btnAnuncioVoltar)
        val txtTitulo = findViewById<TextView>(R.id.txtAnuncioTitulo)
        val txtAnunciante = findViewById<TextView>(R.id.txtAnuncioAnunciante)
        val txtArea = findViewById<TextView>(R.id.txtAnuncioArea)
        val txtEmail = findViewById<TextView>(R.id.txtAnuncioEmail)
        val txtTelefone = findViewById<TextView>(R.id.txtAnuncioTelefone)
        val txtCidade = findViewById<TextView>(R.id.txtAnuncioLocal)
        val txtRemuneracao = findViewById<TextView>(R.id.txtAnuncioRemuneracao)
        val txtDti = findViewById<TextView>(R.id.txtAnuncioDti)
        val txtDtf = findViewById<TextView>(R.id.txtAnuncioDtf)
        val txtDescricao = findViewById<TextView>(R.id.txtAnuncioDescricao)


        txtTelefone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            var phoneNumber = txtTelefone.text.toString()
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }

        btnVoltar.setOnClickListener {
            finish()
        }

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val anuncio = snapshot.getValue(Anuncio::class.java)
                if (anuncio != null) {
                    txtTitulo.text = anuncio.titulo
                    txtAnunciante.text = anuncio.anunciante
                    txtArea.text = anuncio.area
                    txtEmail.text = anuncio.email
                    txtTelefone.text = anuncio.telefone
                    txtCidade.text = anuncio.local
                    txtRemuneracao.text = anuncio.remuneracao.toString()
                    txtDti.text = anuncio.dti
                    txtDtf.text = anuncio.dtf
                    txtDescricao.text = anuncio.descricao

                    if (anuncio.anunciante == BD.UserLogado.user.email) {
                        btnDeleta.visibility = View.VISIBLE
                    } else {
                        btnDeleta.visibility = View.GONE
                    }

                    btnDeleta.setOnClickListener {
                        snapshot.ref.removeValue()

                        finish()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }

        query.addChildEventListener(childEventListener)
    }

    override fun onPause() {
        super.onPause()
        TelaPrincipal.recarregado = false
    }
}
