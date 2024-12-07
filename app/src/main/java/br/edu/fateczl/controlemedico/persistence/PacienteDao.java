package br.edu.fateczl.controlemedico.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fateczl.controlemedico.model.Paciente;
import br.edu.fateczl.controlemedico.model.PacienteConveniado;
import br.edu.fateczl.controlemedico.model.PacienteParticular;

public class PacienteDao implements IPacienteDao, ICRUDDao<Paciente> {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase db;

    public PacienteDao(Context context) {
        this.context = context;
    }

    @Override
    public PacienteDao open() throws SQLException {
        gDao = new GenericDao(context);
        db = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        gDao.close();
    }

    @Override
    public void insert(Paciente paciente) throws SQLException {
        ContentValues contentValues = getContentValues(paciente);
        db.insert("paciente", null, contentValues);
    }

    @Override
    public int update(Paciente paciente) throws SQLException {
        ContentValues contentValues = getContentValues(paciente);
        return db.update("paciente", contentValues, "id = '" + paciente.getId() + "'", null);
    }

    @Override
    public void delete(Paciente paciente) throws SQLException {
        db.delete("paciente", "id = '" + paciente.getId() + "'", null);
    }

    @SuppressLint("Range")
    @Override
    public Paciente findOne(Paciente paciente) throws SQLException {
        String sql = "SELECT id, tipo, nome, data_nascimento FROM paciente WHERE id = '" + paciente.getId() + "'";
        Cursor cursor = db.rawQuery(sql, null);
        paciente = null;
        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (!cursor.isAfterLast()) {
            String tipo = cursor.getString(cursor.getColumnIndex("tipo"));
            if (tipo.equals("P")) {
                paciente = new PacienteParticular();
            }
            else {
                paciente = new PacienteConveniado();
            }
            paciente.setTipo(tipo);
            paciente.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
            paciente.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            String dataNascimento = cursor.getString(cursor.getColumnIndex("data_nascimento"));
            paciente.setDataNascimento(dataNascimento);
        }
        cursor.close();
        return paciente;

    }

    @SuppressLint("Range")
    public Paciente findOne(String id) {
        String sql = "SELECT id, tipo, nome, data_nascimento FROM paciente WHERE id = '" + id + "'";
        Cursor cursor = db.rawQuery(sql, null);
        Paciente paciente = null;
        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (!cursor.isAfterLast()) {
            String tipo = cursor.getString(cursor.getColumnIndex("tipo"));

            if (tipo.equals("P")) {
                paciente = new PacienteParticular();
            }
            else {
                paciente = new PacienteConveniado();
            }
            paciente.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
            paciente.setTipo(tipo);
            paciente.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            String dataNascimento = cursor.getString(cursor.getColumnIndex("data_nascimento"));
            paciente.setDataNascimento(dataNascimento);
        }
        cursor.close();
        return paciente;
    }

    @SuppressLint("Range")
    @Override
    public List<Paciente> findAll() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT id, tipo, nome, data_nascimento FROM paciente";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            String tipo = cursor.getString(cursor.getColumnIndex("tipo"));
            Paciente paciente;

            if (tipo.equals("P")) {
                paciente = new PacienteParticular();
            }
            else {
                paciente = new PacienteConveniado();
            }
            paciente.setId(cursor.getString(cursor.getColumnIndex("id")));
            paciente.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            String dataNascimento = cursor.getString(cursor.getColumnIndex("data_nascimento"));
            paciente.setDataNascimento(dataNascimento);

            pacientes.add(paciente);
            cursor.moveToNext();
        }
        cursor.close();
        return pacientes;
    }

    private ContentValues getContentValues(Paciente paciente) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", paciente.getId());
        contentValues.put("nome", paciente.getNome());
        contentValues.put("tipo", paciente.getTipo());
        contentValues.put("data_nascimento", paciente.getDataNascimento().toString());
        return contentValues;
    }


}
