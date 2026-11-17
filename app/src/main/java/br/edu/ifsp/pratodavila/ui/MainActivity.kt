package br.edu.ifsp.pratodavila.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.edu.ifsp.pratodavila.data.AppDatabase
import br.edu.ifsp.pratodavila.data.Prato
import br.edu.ifsp.pratodavila.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate call")

        setupViews()
        //testarBanco()
    }

    private fun setupViews() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

    /*
    private fun testarBanco() {
        val tag = "TesteBanco"
        val dao = AppDatabase.getInstance(this).pratoDao()

        lifecycleScope.launch {
            Log.d(tag, "1) Pratos no início: ${dao.listarTodos().size}")
            dao.listarTodos().forEach { Log.d(tag, "   - ${it.id}: ${it.nome}") }
            Log.d(tag, "3) Pratos no fim: ${dao.listarTodos().size}")
        }
    }*/
}
