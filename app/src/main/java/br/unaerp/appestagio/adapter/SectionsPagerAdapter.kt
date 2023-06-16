package br.unaerp.appestagio.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import br.unaerp.appestagio.R
import br.unaerp.appestagio.Controller.TelaAnunciosFragment
import br.unaerp.appestagio.Controller.PerfilAnuncianteFragment

import br.unaerp.appestagio.Controller.PerfilInteressadoFragment

private val tabTitles = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, private val check : Int)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        if (check == 0){
            return when (position) {
                0 -> PerfilInteressadoFragment()
                1 -> TelaAnunciosFragment()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }else{
            return when (position) {
                0 -> PerfilAnuncianteFragment()
                1 -> TelaAnunciosFragment()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitles[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}