package br.edu.ifsp.pratodavila.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.pratodavila.databinding.ActivityMainBinding

/**
 * Tela inicial provisória (Etapa 0).
 * Segue a ordem recomendada na aula "Convenções de código":
 * constantes -> atributos -> ciclo de vida -> funções auxiliares -> utilitárias.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate chamado")
    }
}
