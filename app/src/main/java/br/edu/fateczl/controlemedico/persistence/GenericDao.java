package br.edu.fateczl.controlemedico.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenericDao extends SQLiteOpenHelper {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private static final String DATABASE = "CONSULTORIO";
    private static final int DATABASE_VER = 1;
    private final String CREATE_TABLE_PACIENTE =
            "CREATE TABLE paciente ( " +
                "id TEXT NOT NULL PRIMARY KEY, " +
                "tipo TEXT NOT NULL, " +
                "nome TEXT NOT NULL, " +
                "data_nascimento TEXT NOT NULL " +
            ")";
    private final String CREATE_TABLE_MEDICO =
            "CREATE TABLE medico ( " +
                "crm TEXT NOT NULL PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "especialidade TEXT NOT NULL " +
            ")";
    private final String CREATE_TABLE_CONSULTA =
            "CREATE TABLE consulta ( " +
                "codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "data TEXT NOT NULL, " +
                "crm_medico TEXT NOT NULL, " +
                "id_paciente TEXT NOT NULL, " +
                "FOREIGN KEY (crm_medico) REFERENCES medico(crm), " +
                "FOREIGN KEY (id_paciente) REFERENCES paciente(id) " +
            ")";
    public GenericDao(Context context) {
        super(context, DATABASE, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PACIENTE);
        db.execSQL(CREATE_TABLE_MEDICO);
        db.execSQL(CREATE_TABLE_CONSULTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS consulta");
            db.execSQL("DROP TABLE IF EXISTS medico");
            db.execSQL("DROP TABLE IF EXISTS paciente");
            onCreate(db);
        }
    }
}
