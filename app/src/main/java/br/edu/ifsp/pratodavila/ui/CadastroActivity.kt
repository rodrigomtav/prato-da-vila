package br.edu.ifsp.pratodavila.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.edu.ifsp.pratodavila.R
import br.edu.ifsp.pratodavila.data.AppDatabase
import br.edu.ifsp.pratodavila.data.Prato
import br.edu.ifsp.pratodavila.databinding.ActivityCadastroBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.Locale

class CadastroActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CadastroActivity"
        const val EXTRA_PRATO_ID = "extra_prato_id" // manda o id do prato, sem id = novo prato
    }

    private lateinit var binding: ActivityCadastroBinding
    private var pratoId = -1L
    private var pratoEmEdicao: Prato? = null
    private var imagemUri = ""
    private val escolherFoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imagemUri = uri.toString()
            binding.ivPreview.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate call")

        setupViews()
        pratoId = intent.getLongExtra(EXTRA_PRATO_ID, -1L)

        if (pratoId == -1L) {
            binding.toolbar.title = getString(R.string.form_title_new)
        } else {
            binding.toolbar.title = getString(R.string.form_title_edit)
            carregarPrato()
        }
    }

    private fun setupViews() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val teclado = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(systemBars.left, 0, systemBars.right, maxOf(systemBars.bottom, teclado.bottom))
            binding.toolbar.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        binding.btnSelectImage.setOnClickListener {
            escolherFoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnSave.setOnClickListener { salvar() }
        binding.btnDelete.setOnClickListener { confirmarExclusao() }
    }


    private fun carregarPrato() {
        val dao = AppDatabase.getInstance(this).pratoDao()

        lifecycleScope.launch {
            val p = dao.buscarPorId(pratoId)
            if (p != null) {
                pratoEmEdicao = p
                imagemUri = p.imagemUri

                binding.etNome.setText(p.nome)
                binding.etDescricao.setText(p.descricao)
                binding.etPreco.setText(String.format(Locale.forLanguageTag("pt-BR"), "%.2f", p.preco))
                binding.ivPreview.setImageURI(Uri.parse(p.imagemUri))
                binding.btnDelete.visibility = View.VISIBLE

            } else {
                finish() // prato não existe mais: volta
            }
        }
    }

    private fun salvar() {
        val nome = binding.etNome.text.toString().trim()
        val descricao = binding.etDescricao.text.toString().trim()
        val preco = binding.etPreco.text.toString().replace(",", ".").toDoubleOrNull()

        // limpa erros de uma tentativa anterior
        binding.tilNome.error = null
        binding.tilDescricao.error = null
        binding.tilPreco.error = null

        var valido = true
        if (nome.isEmpty()) {
            binding.tilNome.error = getString(R.string.error_empty_name)
            valido = false
        }
        if (descricao.isEmpty()) {
            binding.tilDescricao.error = getString(R.string.error_empty_description)
            valido = false
        }
        if (preco == null || preco <= 0) {
            binding.tilPreco.error = getString(R.string.error_invalid_price)
            valido = false
        }
        if (imagemUri.isEmpty()) {
            Toast.makeText(this, R.string.error_no_image, Toast.LENGTH_SHORT).show()
            valido = false
        }
        if (!valido || preco == null) return

        val dao = AppDatabase.getInstance(this).pratoDao()
        lifecycleScope.launch {
            val original = pratoEmEdicao
            if (original == null) {
                dao.inserir(Prato(nome = nome, descricao = descricao, preco = preco, imagemUri = imagemUri))
                Toast.makeText(this@CadastroActivity, R.string.msg_dish_saved, Toast.LENGTH_SHORT).show()
            } else {
                dao.atualizar(original.copy(nome = nome, descricao = descricao, preco = preco, imagemUri = imagemUri))
                Toast.makeText(this@CadastroActivity, R.string.msg_dish_updated, Toast.LENGTH_SHORT).show()
            }
            finish()
        }


    }
    private fun confirmarExclusao() {
        val p = pratoEmEdicao
        if (p != null) {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(getString(R.string.dialog_delete_message, p.nome))
                .setNegativeButton(R.string.dialog_cancel, null)
                .setPositiveButton(R.string.dialog_confirm) { _, _ ->
                    excluir(p)
                }
                .show()
        }
    }

    private fun excluir(p: Prato) {
        val dao = AppDatabase.getInstance(this).pratoDao()
        lifecycleScope.launch {
            dao.excluir(p)
            Toast.makeText(this@CadastroActivity, R.string.msg_dish_deleted, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}