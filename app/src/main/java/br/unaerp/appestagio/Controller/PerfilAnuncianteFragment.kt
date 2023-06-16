package br.unaerp.appestagio.Controller


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.unaerp.appestagio.R
import br.unaerp.appestagio.Model.BD


class PerfilAnuncianteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_perfil_anunciante, container, false)

        val btnCriarAnuncio = view.findViewById<Button>(R.id.btnCriarAnuncio)
        val txtNome = view.findViewById<TextView>(R.id.txtAnuncianteNome)
        val txtEmail = view.findViewById<TextView>(R.id.txtAnuncianteEmail)
        val txtTipo = view.findViewById<TextView>(R.id.txtAnuncianteTipo)
        val userLogado = BD.UserLogado.user

        txtNome.text = userLogado.nome
        txtEmail.text = userLogado.email
        if(userLogado.isInteressado){
            txtTipo.text = "Interessado"
        }else txtTipo.text = "Anunciante"


        btnCriarAnuncio.setOnClickListener{
            val intent = Intent(requireContext(), CriarAnuncioActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}