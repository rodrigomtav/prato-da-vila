package br.edu.ifsp.pratodavila.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PratoDao {
    @Insert
    suspend fun inserir(prato: Prato): Long

    @Update
    suspend fun atualizar(prato: Prato)

    @Delete
    suspend fun excluir(prato: Prato)

    @Query("SELECT * FROM pratos ORDER BY nome ASC")
    suspend fun listarTodos(): List<Prato>

    @Query(value = "SELECT * FROM pratos WHERE id = :id")
    suspend fun buscarPorId(id: Long): Prato?
}