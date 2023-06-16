package br.unaerp.appestagio.Controller

import br.unaerp.appestagio.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import br.unaerp.appestagio.Model.Anuncio
import br.unaerp.appestagio.Model.BD
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CriarAnuncioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_anuncio)

        var rootDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Anuncios")

        val bd = BD.BDManager.bd
        val btnCriaAnuncioVoltar = findViewById<Button>(R.id.btnCriaAnuncioVoltar)
        val btnCriarAnuncio = findViewById<Button>(R.id.btnCriarAnuncio)
        val edtTitulo = findViewById<EditText>(R.id.edtTitulo)
        val edtAnuncioEmail = findViewById<EditText>(R.id.edtAnuncioEmail)
        val edtTelefone = findViewById<EditText>(R.id.edtTelefone)
        val edtCidade = findViewById<EditText>(R.id.edtCidade)
        val edtDtF = findViewById<EditText>(R.id.edtDtF)
        val edtRemuneracao = findViewById<EditText>(R.id.edtRemuneracao)
        val spinner: Spinner = findViewById(R.id.txtArea)
        val btnMostraAnunciante = findViewById<ToggleButton>(R.id.mostraAnunciante)
        val edtDescricao = findViewById<EditText>(R.id.edtDescricao)
        var area: String = ""

        btnCriaAnuncioVoltar.setOnClickListener {
            finish()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as String
                area = selectedItem
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        edtDtF.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private val dateFormat = "##/##/####"
            private val divider = "/"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) {
                    return
                }

                isFormatting = true

                val cleanText = s.toString().replace(divider, "")

                val formattedText = StringBuilder()
                var currentIndex = 0
                var dividerIndex = 0
                while (dividerIndex < dateFormat.length) {
                    if (currentIndex >= cleanText.length) {
                        break
                    }

                    if (dateFormat[dividerIndex] == '#') {
                        formattedText.append(cleanText[currentIndex])
                        currentIndex++
                    } else {
                        formattedText.append(dateFormat[dividerIndex])
                    }

                    dividerIndex++
                }

                edtDtF.setText(formattedText.toString())
                edtDtF.setSelection(formattedText.length)

                isFormatting = false
            }
        })

        edtTelefone.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false
            private val mask = "(##)#####-####"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) {
                    return
                }

                isFormatting = true

                val cleanText = s.toString().replace("[^\\d]".toRegex(), "")

                val formattedText = StringBuilder()
                var currentIndex = 0
                var maskIndex = 0

                while (maskIndex < mask.length && currentIndex < cleanText.length) {
                    if (mask[maskIndex] == '#') {
                        formattedText.append(cleanText[currentIndex])
                        currentIndex++
                    } else {
                        formattedText.append(mask[maskIndex])
                    }

                    maskIndex++
                }

                edtTelefone.setText(formattedText.toString())
                edtTelefone.setSelection(formattedText.length)

                isFormatting = false
            }
        })


        btnCriarAnuncio.setOnClickListener {

            val rem = edtRemuneracao.text.toString()
            val remuneracao = rem.toDouble()

            val dateI = bd.formatarData(Date())

            if (edtTitulo.text.toString() != "" && edtCidade.text.toString() != "" && edtAnuncioEmail.text.toString() != "" &&
                edtDescricao.text.toString() != "" && edtRemuneracao.text.toString() != "" && edtTelefone.text.toString() != ""
                && edtDtF.text.toString() != "" && comparaData(dateI, edtDtF.text.toString())<=0
            ) {

                var anuncio = Anuncio(
                    "",
                    edtTitulo.text.toString().trim(),
                    area,
                    edtCidade.text.toString().trim(),
                    edtAnuncioEmail.text.toString().trim(),
                    edtTelefone.text.toString().trim(),
                    remuneracao,
                    BD.UserLogado.user.email,
                    dateI,
                    edtDtF.text.toString().trim(),
                    btnMostraAnunciante.isChecked,
                    edtDescricao.text.toString().trim()
                )

                val novoAnuncioRef = rootDbRef.push()

                novoAnuncioRef.setValue(anuncio)
                finish()

            } else Toast.makeText(this, "Cadastro Inválido", Toast.LENGTH_LONG).show()
        }
    }

    fun comparaData(data1: String, data2: String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date1 = dateFormat.parse(data1)
        val date2 = dateFormat.parse(data2)

        return date1.compareTo(date2)
    }

    override fun onPause() {
        super.onPause()
        TelaPrincipal.recarregado = false
    }
}
