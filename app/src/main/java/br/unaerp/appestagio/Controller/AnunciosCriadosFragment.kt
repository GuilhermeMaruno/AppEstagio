package br.unaerp.appestagio.Controller

import AnuncioAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.unaerp.appestagio.R
import br.unaerp.appestagio.Model.Anuncio
import br.unaerp.appestagio.Model.BD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AnunciosCriadosFragment : Fragment(), AnuncioAdapter.OnItemClickListener {

    var user = BD.UserLogado.user
    private lateinit var listAnuncios: ArrayList<Anuncio>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnuncioAdapter
    private lateinit var databaseRef: DatabaseReference
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listAnuncios = ArrayList()
        val auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid ?: ""

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Anuncios")

        val view = inflater.inflate(R.layout.fragment_lista_anuncio, container, false)
        recyclerView = view.findViewById(R.id.rvAnuncios)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AnuncioAdapter(requireContext(), listAnuncios)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)

        return view
    }

    override fun onStart() {
        super.onStart()
        val query: Query = databaseRef.orderByChild("anunciante").equalTo(user.email)
        query.addValueEventListener(object : ValueEventListener {
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
                // Handle database error if needed
            }
        })
    }

    override fun onItemClick(anuncio: Anuncio) {
        val intent = Intent(requireContext(), AnuncioActivity::class.java)
        intent.putExtra("anuncioID", anuncio.id)
        startActivity(intent)
    }
}
