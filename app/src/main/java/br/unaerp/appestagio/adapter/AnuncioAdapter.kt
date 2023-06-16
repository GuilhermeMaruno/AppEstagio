import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.unaerp.appestagio.R
import br.unaerp.appestagio.Model.Anuncio
import br.unaerp.appestagio.Model.BD
import java.text.SimpleDateFormat
import java.util.Locale

class AnuncioAdapter(
    private val context: Context,
    private val anuncioList: ArrayList<Anuncio>
) :
    RecyclerView.Adapter<AnuncioAdapter.AnuncioViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    inner class AnuncioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setInfo(info: Anuncio) {
            val bd = BD.BDManager.bd
            val titulo: TextView = itemView.findViewById(R.id.txtAnuncioTituloLayout)
            val area: TextView = itemView.findViewById(R.id.txtDetalhesArea)
            val descricao: TextView = itemView.findViewById(R.id.txtDescricao)
            val local: TextView = itemView.findViewById(R.id.txtDetalhesLocal)
            val remuneracao: TextView = itemView.findViewById(R.id.txtDetalhesRemuneracao)
            val anunciante: TextView = itemView.findViewById(R.id.txtDetalhesAnunciante)
            val dti: TextView = itemView.findViewById(R.id.txtDetalhesDtI)
            val dtf: TextView = itemView.findViewById(R.id.txtDetalhesDtF)
            val anuncianteLbl = itemView.findViewById<TextView>(R.id.txtLblAnunciante)

            val mostraAnunciante: Boolean = info.mostraAnunciante

            titulo.text = info.titulo
            descricao.text = info.descricao
            local.text = info.local
            remuneracao.text = info.remuneracao.toString()
            area.text = info.area
            if (mostraAnunciante){
                anunciante.text = info.anunciante
            } else {
                anunciante.visibility = View.GONE
                anuncianteLbl.visibility = View.GONE
            }

            dti.text = info.dti
            dtf.text = info.dtf
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnuncioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_anuncio, parent, false)
        return AnuncioViewHolder(view)
    }

    override fun getItemCount(): Int {
        return anuncioList.size
    }

    override fun onBindViewHolder(holder: AnuncioViewHolder, position: Int) {
        val anuncio: ConstraintLayout = holder.itemView.findViewById(R.id.anuncio)
        holder.setInfo(anuncioList[position])

        val btnDetalhes: Button = holder.itemView.findViewById(R.id.btnDetalhes)
        btnDetalhes.setOnClickListener {
            val anuncio = anuncioList[position]
            onItemClickListener?.onItemClick(anuncio)
        }
    }

    fun comparaData(data1: String, data2: String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date1 = dateFormat.parse(data1)
        val date2 = dateFormat.parse(data2)

        return date1.compareTo(date2)
    }

    interface OnItemClickListener {
        fun onItemClick(anuncio: Anuncio)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.onItemClickListener = listener
    }
}
