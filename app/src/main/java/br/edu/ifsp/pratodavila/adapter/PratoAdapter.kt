package br.edu.ifsp.pratodavila.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.pratodavila.data.Prato
import br.edu.ifsp.pratodavila.databinding.ItemPratoBinding
import java.text.NumberFormat
import java.util.Locale

class PratoAdapter(
    private var pratos: List<Prato>,
    private val onItemClick: (Prato) -> Unit //parâmetro do tipo função, ele é uma função que recebe um prato e não retorna nada, só serve pra avisar qual prato foi clicado
) : RecyclerView.Adapter<PratoAdapter.PratoViewHolder>() {

    //define a moeda que vai ser vendida, criado fora do bind como propriedade pois não tem necessidade de criar a cada linha rolada
    private val formatoMoeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

    //Recebe o ItemPratoBinding, que vem do xml do item_prato e guarda como val para o bind poder acessar
    class PratoViewHolder(val binding: ItemPratoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PratoViewHolder { //cria uma linha vazia
        //o adapter não é Activity e não tem um inflater, então ele pega um a partir do do context do pai (RecyclerView)
        val binding = ItemPratoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PratoViewHolder(binding)
    }

    // preenche a linha
    override fun onBindViewHolder(holder: PratoViewHolder, position: Int) {
        val prato = pratos[position] //pega o prato da posição que a RecyclerView pediu
        holder.binding.tvNome.text = prato.nome //preenche valores
        holder.binding.tvPreco.text = formatoMoeda.format(prato.preco) //preenche valores
        holder.binding.ivPrato.setImageURI(Uri.parse(prato.imagemUri)) //o parse converte a String do banco para Uri
        holder.itemView.setOnClickListener { onItemClick(prato) } //passa o prato clicado para a função que a activity entrega
    }

    override fun getItemCount(): Int = pratos.size

    //função para chamar o notifyDataSetChanged, que manda a ReciclerView redesenhar tudo. Não é o mais eficiente, mas para um app pequeno não tem problema.
    fun atualizar(novaLista: List<Prato>) {
        pratos = novaLista
        notifyDataSetChanged()
    }
}