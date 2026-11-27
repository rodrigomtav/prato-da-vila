package br.edu.ifsp.pratodavila.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.pratodavila.adapter.PratoAdapter
import br.edu.ifsp.pratodavila.data.AppDatabase
import br.edu.ifsp.pratodavila.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PratoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate call")

        setupViews()
    }

    //tive que usar o onResume pra atualizar a tela automaticamente quando criar o prato em outra tela, o que não daria certo com o onCreate
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume call")
        carregarPratos()
    }

    private fun setupViews() {
        binding.fabAddDish.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            insets

        }


        // Intent explícita: abre o detalhe levando só o id do prato
        adapter = PratoAdapter(emptyList()) { prato ->
            val intent = Intent(this, DetalhePratoActivity::class.java)
            intent.putExtra(DetalhePratoActivity.EXTRA_PRATO_ID, prato.id)
            startActivity(intent)
        }

        binding.rvPratos.layoutManager = LinearLayoutManager(this) //define a lista vertical
        binding.rvPratos.adapter = adapter //liga os dados à tela
    }

    private fun carregarPratos() {
        val dao = AppDatabase.getInstance(this).pratoDao()

        //preciso usar o lifecycle por que a função do dao é suspend
        lifecycleScope.launch {
            val pratos = dao.listarTodos()
            adapter.atualizar(pratos)

            val vazio = if (pratos.isEmpty()) View.VISIBLE else View.GONE
            binding.ivEmpty.visibility = vazio
            binding.tvEmptyTitle.visibility = vazio
            binding.tvEmptyMessage.visibility = vazio
            binding.rvPratos.visibility = if (pratos.isEmpty()) View.GONE else View.VISIBLE
        }
    }
}
