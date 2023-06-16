package br.unaerp.appestagio.Controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.unaerp.appestagio.R
import br.unaerp.appestagio.Model.BD
import br.unaerp.appestagio.Model.DadosLogin.Companion.email
import br.unaerp.appestagio.Model.Usuario
import com.google.firebase.auth.FirebaseAuth


class LoginActivity: AppCompatActivity(), BD.LerUsuarioCallback {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var esqEmail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val bd = BD.BDManager.bd
        val bdUser = BD.UserLogado
        val btnLogar = findViewById<Button>(R.id.btnLogar)
        val btnVoltar = findViewById<Button>(R.id.btnLoginVoltar)
        val btnCriarConta = findViewById<Button>(R.id.btnLoginCriarConta)
        val edtEmail = findViewById<EditText>(R.id.edtUsuario)
        val edtSenha = findViewById<EditText>(R.id.edtSenha)
        val btnEsqueci = findViewById<Button>(R.id.btnEsqueciSenha)

        btnVoltar.setOnClickListener {
            finish()
        }

        btnEsqueci.setOnClickListener {
            val intent = Intent(this, EsqueciSenhaActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnCriarConta.setOnClickListener{
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogar.setOnClickListener {
            var email = edtEmail.text.toString().trim()
            var senha = edtSenha.text.toString().trim()
            esqEmail = email

            auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        bd.lerDadosUsuarioPorEmail(email, this)

                    } else {
                        Toast.makeText(baseContext, "Login Inválido.", Toast.LENGTH_SHORT).show()
                    }
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
