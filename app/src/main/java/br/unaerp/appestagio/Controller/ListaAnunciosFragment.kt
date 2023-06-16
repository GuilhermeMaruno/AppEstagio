package br.unaerp.appestagio.Controller

import AnuncioAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.unaerp.appestagio.R
import br.unaerp.appestagio.Model.Anuncio
import com.google.firebase.database.*

class ListaAnunciosFragment : Fragment(), AnuncioAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnuncioAdapter
    private lateinit var listAnuncios: ArrayList<Anuncio>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference().child("Anuncios")

        val view = inflater.inflate(R.layout.fragment_lista_anuncio, container, false)
        recyclerView = view.findViewById(R.id.rvAnuncios)
        recyclerView.layoutManager = LinearLayoutManager(context)
        listAnuncios = ArrayList()
        adapter = AnuncioAdapter(requireContext(), listAnuncios)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)

        rootDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listAnuncios.clear()
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val key = dataSnapshot.key
                    val anuncio = dataSnapshot.getValue(Anuncio::class.java)
                    if (key != null && anuncio != null) {
                        if(adapter.comparaData(anuncio.dti,anuncio.dtf)>=0){
                            anuncio.id = key
                            listAnuncios.add(anuncio)
                        }else{
                            snapshot.ref.removeValue()
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Ocorreu um erro na leitura dos dados
                // Trate o erro de acordo com sua necessidade
            }
        })
        return view
    }

    override fun onItemClick(anuncio: Anuncio) {
        val intent = Intent(requireContext(), AnuncioActivity::class.java)
        intent.putExtra("anuncioID", anuncio.id)
        startActivity(intent)
    }
}
