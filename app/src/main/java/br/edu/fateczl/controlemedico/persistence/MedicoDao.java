package br.edu.fateczl.controlemedico.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fateczl.controlemedico.model.Medico;

public class MedicoDao implements IMedicoDao, ICRUDDao<Medico> {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase db;

    public MedicoDao (Context context) {
        this.context = context;
    }
    @Override
    public MedicoDao open() throws SQLException {
        gDao = new GenericDao(context);
        db = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        gDao.close();
    }

    @Override
    public void insert(Medico medico) throws SQLException {
        ContentValues contentValues = getContentValues(medico);
        db.insert("medico", null, contentValues);
    }

    @Override
    public int update(Medico medico) throws SQLException {
        ContentValues contentValues = getContentValues(medico);
        String where = String.format("crm = '%s'", medico.getCrm());
        return db.update("medico", contentValues, where, null);
    }

    @Override
    public void delete(Medico medico) throws SQLException {
        String where = String.format("crm = '%s'", medico.getCrm());
        db.delete("medico", where, null);
    }

    @SuppressLint("Range")
    @Override
    public Medico findOne(Medico medico) throws SQLException {
        String where = String.format("WHERE crm = '%s'", medico.getCrm());
        String sql = "SELECT crm, nome, especialidade FROM medico " + where;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (!cursor.isAfterLast()) {
            medico.setCrm(cursor.getString(cursor.getColumnIndex("crm")));
            medico.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            medico.setEspecialidade(cursor.getString(cursor.getColumnIndex("especialidade")));
        }
        cursor.close();
        return medico;
    }

    @SuppressLint("Range")
    @Override
    public List<Medico> findAll() throws SQLException {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT crm, nome, especialidade FROM medico";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            Medico medico = new Medico();
            medico.setCrm(cursor.getString(cursor.getColumnIndex("crm")));
            medico.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            medico.setEspecialidade(cursor.getString(cursor.getColumnIndex("especialidade")));

            medicos.add(medico);
            cursor.moveToNext();
        }
        cursor.close();
        return medicos;
    }

    @SuppressLint("Range")
    public List<String> allSpecialities() throws SQLException {
        List<String> especialidades = new ArrayList<>();
        String sql = "SELECT DISTINCT(especialidade) as especialidade FROM medico";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {

            String especialidade = cursor.getString(cursor.getColumnIndex("especialidade"));

            especialidades.add(especialidade);
            cursor.moveToNext();
        }
        cursor.close();
        return especialidades;
    }

    @SuppressLint("Range")
    public List<Medico> getDoctorListBySpeciality(String spec) throws SQLException {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT cQuery(sql, rm, nome, especialidade FROM medico WHERE especialidade = '" + spec + "'";
                Cursor cursor = db.rawnull);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            Medico medico = new Medico();
            medico.setCrm(cursor.getString(cursor.getColumnIndex("crm")));
            medico.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            medico.setEspecialidade(cursor.getString(cursor.getColumnIndex("especialidade")));

            medicos.add(medico);
            cursor.moveToNext();
        }
        cursor.close();
        return medicos;
    }
    private ContentValues getContentValues(Medico medico) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("crm", medico.getCrm());
        contentValues.put("nome", medico.getNome());
        contentValues.put("especialidade", medico.getEspecialidade());
        return contentValues;
    }
}
