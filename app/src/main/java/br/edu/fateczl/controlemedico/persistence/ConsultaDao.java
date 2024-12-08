package br.edu.fateczl.controlemedico.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.fateczl.controlemedico.model.Consulta;
import br.edu.fateczl.controlemedico.model.Medico;
import br.edu.fateczl.controlemedico.model.Paciente;
import br.edu.fateczl.controlemedico.model.PacienteConveniado;
import br.edu.fateczl.controlemedico.model.PacienteParticular;

public class ConsultaDao implements IConsultaDao, ICRUDDao<Consulta> {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase db;

    public ConsultaDao(Context context) {
        this.context = context;
    }

    @Override
    public ConsultaDao open() throws SQLException {
        gDao = new GenericDao(context);
        db = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        gDao.close();
    }

    @Override
    public void insert(Consulta consulta) throws SQLException {
        ContentValues contentValues = getContentValues(consulta);
        db.insert("consulta", null, contentValues);
    }

    @Override
    public int update(Consulta consulta) throws SQLException {
        ContentValues contentValues = getContentValues(consulta);
        String where = String.format("codigo = %d", consulta.getCodigo());
        return db.update("consulta", contentValues, where, null);
    }

    @Override
    public void delete(Consulta consulta) throws SQLException {
        String where = String.format("codigo = %d", consulta.getCodigo());
        db.delete("consulta", where, null);
    }

    @SuppressLint("Range")
    @Override
    public Consulta findOne(Consulta consulta) throws SQLException {
        String sql = "SELECT " +
                "a.codigo, a.data, a.crm_medico, a.id_paciente, " +
                "b.id, b.tipo, b.nome AS nm_paciente, b.data_nascimento, " +
                "c.crm, c.nome AS nm_medico, c.especialidade " +
                "FROM consulta a " +
                "INNER JOIN paciente b " +
                "ON a.id_paciente = b.id " +
                "INNER JOIN medico c " +
                "ON a.crm_medico = c.crm " +
                "WHERE b.id = " + consulta.getPaciente().getId();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (!cursor.isAfterLast()) {
            String tipo = cursor.getString(cursor.getColumnIndex("tipo"));
            Paciente paciente = tipo.equals("P") ? new PacienteParticular() : new PacienteConveniado();
            paciente.setId(cursor.getString(cursor.getColumnIndex("id")));
            paciente.setNome(cursor.getString(cursor.getColumnIndex("nm_paciente")));
            String dataNascimento = cursor.getString(cursor.getColumnIndex("data_nascimento"));
            paciente.setDataNascimento(dataNascimento);

            Medico medico = new Medico();
            medico.setCrm(cursor.getString(cursor.getColumnIndex("crm")));
            medico.setNome(cursor.getString(cursor.getColumnIndex("nm_medico")));
            medico.setEspecialidade(cursor.getString(cursor.getColumnIndex("especialidade")));

            consulta.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            String dataString = cursor.getString(cursor.getColumnIndex("data"));

            consulta.setDhConsulta(dataString);
            consulta.setPaciente(paciente);
            consulta.setMedico(medico);

        }
        cursor.close();
        return consulta;
    }

    @SuppressLint("Range")
    @Override
    public List<Consulta> findAll() throws SQLException {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT " +
                "a.codigo, a.data, a.crm_medico, a.id_paciente, a.valor, " +
                "b.id, b.tipo, b.nome AS nm_paciente, b.data_nascimento, " +
                "c.crm, c.nome AS nm_medico, c.especialidade, c.valor_consulta " +
                "FROM consulta a " +
                "INNER JOIN paciente b " +
                "ON a.id_paciente = b.id " +
                "INNER JOIN medico c " +
                "ON a.crm_medico = c.crm ";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            Consulta consulta = new Consulta();
            String tipo = cursor.getString(cursor.getColumnIndex("tipo"));
            Paciente paciente = tipo.equals("P") ? new PacienteParticular() : new PacienteConveniado();
            paciente.setId(cursor.getString(cursor.getColumnIndex("id")));
            paciente.setNome(cursor.getString(cursor.getColumnIndex("nm_paciente")));
            String dataNascimento = cursor.getString(cursor.getColumnIndex("data_nascimento"));
            paciente.setDataNascimento(dataNascimento);

            Medico medico = new Medico();
            medico.setCrm(cursor.getString(cursor.getColumnIndex("crm")));
            medico.setNome(cursor.getString(cursor.getColumnIndex("nm_medico")));
            medico.setEspecialidade(cursor.getString(cursor.getColumnIndex("especialidade")));
            medico.setValorConsulta(cursor.getFloat(cursor.getColumnIndex("valor_consulta")));

            consulta.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            String dataString = cursor.getString(cursor.getColumnIndex("data"));
            float valor = cursor.getFloat(cursor.getColumnIndex("valor"));
            consulta.setDhConsulta(dataString);
            consulta.setPaciente(paciente);
            consulta.setMedico(medico);
            consulta.setValor(valor);

            consultas.add(consulta);
            cursor.moveToNext();
        }
        cursor.close();
        return consultas;
    }

    @SuppressLint("Range")
    public List<Consulta> findByPatient(Paciente buscPaciente) throws SQLException {
        List<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT " +
                "a.codigo, a.data, a.crm_medico, a.id_paciente, a.valor, " +
                "b.id, b.tipo, b.nome AS nm_paciente, b.data_nascimento, " +
                "c.crm, c.nome AS nm_medico, c.especialidade, c.valor_consulta " +
                "FROM consulta a " +
                "INNER JOIN paciente b " +
                "ON a.id_paciente = b.id " +
                "INNER JOIN medico c " +
                "ON a.crm_medico = c.crm " +
                "WHERE a.id_paciente = '" + buscPaciente.getId() + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            Consulta consulta = new Consulta();
            String tipo = cursor.getString(cursor.getColumnIndex("tipo"));
            Paciente paciente = tipo.equals("P") ? new PacienteParticular() : new PacienteConveniado();
            paciente.setId(cursor.getString(cursor.getColumnIndex("id")));
            paciente.setNome(cursor.getString(cursor.getColumnIndex("nm_paciente")));
            String dataNascimento = cursor.getString(cursor.getColumnIndex("data_nascimento"));
            paciente.setDataNascimento(dataNascimento);

            Medico medico = new Medico();
            medico.setCrm(cursor.getString(cursor.getColumnIndex("crm")));
            medico.setNome(cursor.getString(cursor.getColumnIndex("nm_medico")));
            medico.setEspecialidade(cursor.getString(cursor.getColumnIndex("especialidade")));
            medico.setValorConsulta(cursor.getFloat(cursor.getColumnIndex("valor_consulta")));

            consulta.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            String dataString = cursor.getString(cursor.getColumnIndex("data"));
            float valor = cursor.getFloat(cursor.getColumnIndex("valor"));
            consulta.setDhConsulta(dataString);
            consulta.setPaciente(paciente);
            consulta.setMedico(medico);
            consulta.setValor(valor);

            consultas.add(consulta);
            cursor.moveToNext();
        }
        cursor.close();
        return consultas;
    }

    @SuppressLint("Range")
    public List<String> findSchedules(Medico medico, String data) {
        List<String> agendas = new ArrayList<>();
        String sql = "SELECT " +
                "a.data, a.crm_medico, c.crm, c.nome as nm_medico " +
                "FROM consulta a " +
                "INNER JOIN medico c " +
                "ON a.crm_medico = c.crm " +
                "WHERE c.crm = '" + medico.getCrm() + "' " +
                "AND SUBSTR(a.data, 1, 10) = '" + data + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            String dataCol = cursor.getString(cursor.getColumnIndex("data"));
            agendas.add(dataCol);
            cursor.moveToNext();
        }
        cursor.close();
        return agendas;
    }

    private ContentValues getContentValues(Consulta consulta) {
        ContentValues contentValues = new ContentValues();
//        contentValues.put("codigo", consulta.getCodigo());
        contentValues.put("data", consulta.getFmtDhConsulta());
        contentValues.put("valor", consulta.getValor());
        contentValues.put("crm_medico", consulta.getMedico().getCrm());
        contentValues.put("id_paciente", consulta.getPaciente().getId());
        return contentValues;
    }

}
