package br.edu.ifsp.pratodavila.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.edu.ifsp.pratodavila.R
import br.edu.ifsp.pratodavila.data.AppDatabase
import br.edu.ifsp.pratodavila.data.Prato
import br.edu.ifsp.pratodavila.databinding.ActivityDetalhePratoBinding
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class DetalhePratoActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DetalhePratoActivity"
        const val EXTRA_PRATO_ID = "extra_prato_id" // id do prato a exibir
    }

    private lateinit var binding: ActivityDetalhePratoBinding
    private var prato: Prato? = null
    private val formatoMoeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhePratoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate call")

        setupViews()
    }

    // onResume para mostrar os dados atualizados ao voltar da edição
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume call")
        carregarPrato()
    }

    private fun setupViews() {
        binding.btnEdit.setOnClickListener {
            val p = prato
            if (p != null) {
                val intent = Intent(this, CadastroActivity::class.java)
                intent.putExtra(CadastroActivity.EXTRA_PRATO_ID, p.id)
                startActivity(intent)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        binding.toolbar.setNavigationOnClickListener { finish() } // seta de voltar
        binding.btnShare.setOnClickListener { compartilhar() }
    }

    private fun carregarPrato() {
        val id = intent.getLongExtra(EXTRA_PRATO_ID, -1L)
        val dao = AppDatabase.getInstance(this).pratoDao()

        lifecycleScope.launch {
            val encontrado = dao.buscarPorId(id)
            if (encontrado != null) {
                prato = encontrado
                binding.tvNome.text = encontrado.nome
                binding.tvPreco.text = formatoMoeda.format(encontrado.preco)
                binding.tvDescricao.text = encontrado.descricao
                binding.ivPrato.setImageURI(Uri.parse(encontrado.imagemUri))
            } else {
                finish() // id inválido ou prato excluído: volta para a lista
            }
        }
    }

    // Intent implícita: o Android mostra os apps que aceitam texto
    private fun compartilhar() {
        val p = prato
        if (p != null) {
            val texto = getString(R.string.share_text, p.nome, formatoMoeda.format(p.preco), p.descricao)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, texto)
            startActivity(Intent.createChooser(intent, getString(R.string.share_chooser_title)))
        }
    }
}
