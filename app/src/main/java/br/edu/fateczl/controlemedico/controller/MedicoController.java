package br.edu.fateczl.controlemedico.controller;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.controlemedico.model.Medico;
import br.edu.fateczl.controlemedico.persistence.MedicoDao;

public class MedicoController implements IController<Medico> {
    private final MedicoDao mDao;

    public MedicoController(MedicoDao pDao) {
        this.mDao = pDao;

    }

    @Override
    public void inserir(Medico medico) throws SQLException {
        if (mDao.open() == null) {
            mDao.open();
        }
        mDao.insert(medico);
        mDao.close();
    }

    @Override
    public void modificar(Medico medico) throws SQLException {
        if (mDao.open() == null) {
            mDao.open();
        }
        mDao.update(medico);
        mDao.close();
    }

    @Override
    public void deletar(Medico medico) throws SQLException {
        if (mDao.open() == null) {
            mDao.open();
        }
        mDao.delete(medico);
        mDao.close();
    }

    @Override
    public Medico buscar(Medico medico) throws SQLException {
        if (mDao.open() == null) {
            mDao.open();
        }
        return mDao.findOne(medico);
    }

    @Override
    public List<Medico> listar() throws SQLException {
        if (mDao.open() == null) {
            mDao.open();
        }
        return mDao.findAll();
    }

    public List<Medico> listar(String especialidade) throws SQLException {
        if (mDao.open() == null) {
            mDao.open();
        }
        return mDao.getDoctorListBySpeciality(especialidade);
    }
    public List<String> listarEspecialidades() throws SQLException {
        if (mDao.open() == null) {
            mDao.open();
        }
        return mDao.allSpecialities();
    }


}