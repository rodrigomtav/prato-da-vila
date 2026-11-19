package br.edu.ifsp.pratodavila.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database (entities = [Prato::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun pratoDao() : PratoDao


    companion object {

        // lugar onde o banco fica guardado. Começa vazio.
        private var INSTANCE: AppDatabase? = null

        // quem quiser o banco chama essa função
        fun getInstance(context: Context): AppDatabase {

            // Se o banco ainda NÃO foi criado
            if (INSTANCE == null) {
                //cria agora e guarda na variável
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "prato_da_vila.db"
                ).addCallback(object : RoomDatabase.Callback() { //criei o callback para popular o bd com os pratos iniciais
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // insere direto com SQL (sem coroutine) para os pratos já existirem na primeira consulta
                        val sql = "INSERT INTO pratos (nome, descricao, preco, imagemUri) VALUES (?, ?, ?, ?)"

                        db.execSQL(sql, arrayOf("Açaí completo",
                            "Açaí cremoso com banana, granola e leite condensado, aprox 800Ml",
                            21.9,
                            "android.resource://br.edu.ifsp.pratodavila/drawable/img_prato_acai"))

                        db.execSQL(sql, arrayOf("Prato do dia",
                            "Prato do dia: Arroz com açafrão, creme de carne seca com requeijão",
                            29.99,
                            "android.resource://br.edu.ifsp.pratodavila/drawable/img_prato_prato_do_dia"))

                        db.execSQL(sql, arrayOf("Tutu de feijão",
                            "Tutu de feijão com calabresa, torresmo e couve.",
                            18.99,
                            "android.resource://br.edu.ifsp.pratodavila/drawable/img_prato_tutu_de_feijao"))

                        db.execSQL(sql, arrayOf("Filé de peixe empanado",
                            "Porção de filé de peixe empanado, acompanha molho tártaro. Aprox 400g",
                            20.00,
                            "android.resource://br.edu.ifsp.pratodavila/drawable/img_prato_file_de_peixe_empanado"))
                    }
                }).build()
            }

            return INSTANCE!!
        }
    }
}