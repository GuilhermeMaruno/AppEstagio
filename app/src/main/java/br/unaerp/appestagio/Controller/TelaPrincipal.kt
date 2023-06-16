package br.unaerp.appestagio.Controller

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import br.unaerp.appestagio.adapter.SectionsPagerAdapter
import br.unaerp.appestagio.Model.BD
import br.unaerp.appestagio.databinding.ActivityPrincipalBinding

class TelaPrincipal: AppCompatActivity() {

    companion object {
        var recarregado = false
    }

    private lateinit var binding: ActivityPrincipalBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        var tipo = 0
        val tipoUser = BD.UserLogado.user.isInteressado
        if(tipoUser){
            tipo = 0
        }else
            tipo = 1


        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager,tipo)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)


    }

    override fun onResume() {
        super.onResume()

        if (!recarregado) {

            val viewPager: ViewPager = binding.viewPager
            val adapter = viewPager.adapter
            viewPager.adapter = adapter

            recarregado = true
        }
    }
}
