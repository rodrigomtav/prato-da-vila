package br.edu.ifsp.pratodavila.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database (entities = [Prato::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun pratoDao() : PratoDao


    companion object {

        // lugar onde o banco fica guardado. Começa vazio.
        private var INSTANCE: AppDatabase? = null

        // quem quiser o banco chama essa função
        fun getInstance(context: Context): AppDatabase {

            // Se o banco ainda NÃO foi criado...
            if (INSTANCE == null) {
                // ...cria agora e guarda na variável
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "prato_da_vila.db"
                ).addCallback(object : RoomDatabase.Callback() { //criei o callback para popular o bd com os pratos iniciais
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch{ //como as funções do DAO são suspend, precisei usar esse coroutine para executá-las, já que aqui não tem activity
                            val dao = getInstance(context).pratoDao()
                            dao.inserir(Prato(nome = "Açaí completo",
                                descricao = "Açaí cremoso com banana, granola e leite condensado, aprox 800Ml",
                                preco = 21.9,
                                imagemUri = "android.resource://br.edu.ifsp.pratodavila/drawable/img_prato_acai"))

                            dao.inserir(Prato(nome = "Prato do dia",
                                descricao = "Prato do dia: Arroz com açafrão, creme de carne seca com requeijão",
                                preco = 29.99,
                                imagemUri = "android.resource://br.edu.ifsp.pratodavila/drawable/img_prato_prato_do_dia"))

                            dao.inserir(Prato(nome = "Tutu de feijão",
                                descricao = "Tutu de feijão com calabresa, torresmo e couve.",
                                preco = 18.99,
                                imagemUri = "android.resource://br.edu.ifsp.pratodavila/drawable/img_prato_tutu_de_feijao"))

                            dao.inserir(Prato(nome = "Filé de peixe empanado",
                                descricao = "Porção de filé de peixe empanado, acompanha molho tártaro. Aprox 400g",
                                preco = 20.00,
                                imagemUri = "android.resource://br.edu.ifsp.pratodavila/drawable/img_prato_file_de_peixe_empanado"))
                        }
                    }
                }).build()
            }

            return INSTANCE!!
        }
    }
}