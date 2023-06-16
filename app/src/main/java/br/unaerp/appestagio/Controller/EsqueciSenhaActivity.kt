package br.unaerp.appestagio.Controller


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.unaerp.appestagio.R
import com.google.firebase.auth.FirebaseAuth


class EsqueciSenhaActivity: AppCompatActivity() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueci_senha)


        val btnReset = findViewById<Button>(R.id.btnReset)
        val btnVoltar = findViewById<Button>(R.id.btnEsqVoltar)
        val edtEmail = findViewById<EditText>(R.id.edtEsqUsuario)

        btnVoltar.setOnClickListener {
            finish()
        }

        btnReset.setOnClickListener {
            auth.sendPasswordResetEmail(edtEmail.text.toString().trim()).addOnSuccessListener {
                Toast.makeText(
                    baseContext,
                    "Link para resetar a senha foi enviado para seu email",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnSuccessListener {
                Toast.makeText(
                    baseContext,
                    "Link para resetar a senha foi enviado para seu email",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener{
                Toast.makeText(
                    baseContext,
                    "Falha ao encontrar cadastro",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

}
