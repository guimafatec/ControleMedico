package br.edu.fateczl.controlemedico.controller;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.controlemedico.model.Paciente;
import br.edu.fateczl.controlemedico.persistence.PacienteDao;

public class PacienteController implements IController<Paciente>{
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private final PacienteDao pDao;

    public PacienteController(PacienteDao pDao) {
        this.pDao = pDao;

    }

    @Override
    public void inserir(Paciente paciente) throws SQLException {
        if (pDao.open() == null) {
            pDao.open();
        }
        pDao.insert(paciente);
        pDao.close();
    }

    @Override
    public void modificar(Paciente paciente) throws SQLException {
        if (pDao.open() == null) {
            pDao.open();
        }
        pDao.update(paciente);
        pDao.close();
    }

    @Override
    public void deletar(Paciente paciente) throws SQLException {
        if (pDao.open() == null) {
            pDao.open();
        }
        pDao.delete(paciente);
        pDao.close();
    }

    @Override
    public Paciente buscar(Paciente paciente) throws SQLException {
        if (pDao.open() == null) {
            pDao.open();
        }
        return pDao.findOne(paciente);
    }

    @Override
    public List<Paciente> listar() throws SQLException {
        if (pDao.open() == null) {
            pDao.open();
        }
        return pDao.findAll();
    }

    public Paciente buscar(String id) throws SQLException {
        if (pDao.open() == null) {
            pDao.open();
        }
        return pDao.findOne(id);
    }
}
