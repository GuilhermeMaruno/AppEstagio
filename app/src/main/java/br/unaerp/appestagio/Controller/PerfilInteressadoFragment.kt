package br.unaerp.appestagio.Controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.unaerp.appestagio.R
import br.unaerp.appestagio.Model.BD


class PerfilInteressadoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_perfil_interessado, container, false)
        val txtPerfilNome = view.findViewById<TextView>(R.id.txtAnuncianteNome)
        val txtPerfilEmail = view.findViewById<TextView>(R.id.txtAnuncianteEmail)
        val userLogado = BD.UserLogado.user

        txtPerfilNome.text = userLogado.nome
        txtPerfilEmail.text = userLogado.email

        return view
    }
}