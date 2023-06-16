package br.unaerp.appestagio.Controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.unaerp.appestagio.R
import br.unaerp.appestagio.Model.BD
import br.unaerp.appestagio.Model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CadastroActivity : AppCompatActivity(), BD.LerUsuarioCallback {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        var rootDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios")

        val bd = BD.BDManager.bd
        val usuario = BD.UserLogado
        val btnCadastroVoltar = findViewById<Button>(R.id.btnCadastroVoltar)
        val btnCriarContaLogin = findViewById<Button>(R.id.btnCriarContaLogin)
        val edtCriaNome = findViewById<EditText>(R.id.edtCriaNome)
        val edtCriaEmail = findViewById<EditText>(R.id.edtCriaEmail)
        val edtCriaSenha = findViewById<EditText>(R.id.edtCriaSenha)
        val btnCriaConta = findViewById<Button>(R.id.btnCriarConta)
        val spinner: Spinner = findViewById(R.id.txtTipo)
        var tipo: Boolean = true


        btnCriarContaLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCadastroVoltar.setOnClickListener {
            finish()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position) as String
                tipo = selectedItem != "Anunciante"
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        btnCriaConta.setOnClickListener{
            var email = edtCriaEmail.text.toString().trim()
            var nome = edtCriaNome.text.toString().trim()
            var senha = edtCriaSenha.text.toString().trim()

            if (nome != "" && email != "" && senha != "") {

                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            usuario.dbUser = auth.currentUser

                            var user = Usuario(nome,email,tipo)

                            val novoUsuarioRef = rootDbRef.push()

                            novoUsuarioRef.setValue(user)

                            bd.lerDadosUsuarioPorEmail(email, this)

                        } else {
                            Toast.makeText(
                                baseContext, "Falha no Banco de Dados", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            } else {
                Toast.makeText(this, "Complete o cadastro", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onUsuarioLido(usuario: Usuario) {
        BD.UserLogado.user = usuario

        val intent = Intent(this, TelaPrincipal::class.java)
        startActivity(intent)
        finish()
    }

    override fun onUsuarioNaoEncontrado() {
        Toast.makeText(this, "Nenhum usuário encontrado com o email informado", Toast.LENGTH_SHORT).show()
    }

    override fun onError(mensagem: String) {
        Toast.makeText(this, "Erro na leitura dos dados: $mensagem", Toast.LENGTH_SHORT).show()
    }
}
