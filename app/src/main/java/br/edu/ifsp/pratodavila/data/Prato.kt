package br.edu.ifsp.pratodavila.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "pratos")
data class Prato (

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String ="",
    val descricao: String ="",
    val preco: Double = 0.0,
    val imagemUri: String = ""
)