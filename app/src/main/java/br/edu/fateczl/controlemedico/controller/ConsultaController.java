package br.edu.fateczl.controlemedico.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import br.edu.fateczl.controlemedico.model.Consulta;
import br.edu.fateczl.controlemedico.persistence.ConsultaDao;

public class ConsultaController implements IController<Consulta> {
    private final ConsultaDao cDao;

    public ConsultaController(ConsultaDao cDao) {
        this.cDao = cDao;
    }

    @Override
    public void inserir(Consulta consulta) throws SQLException {
        if (cDao.open() == null) {
            cDao.open();
        }
        cDao.insert(consulta);
        cDao.close();
    }

    @Override
    public void modificar(Consulta consulta) throws SQLException {
        if (cDao.open() == null) {
            cDao.open();
        }
        cDao.update(consulta);
        cDao.close();
    }

    @Override
    public void deletar(Consulta consulta) throws SQLException {
        if (cDao.open() == null) {
            cDao.open();
        }
        cDao.delete(consulta);
        cDao.close();
    }

    @Override
    public Consulta buscar(Consulta consulta) throws SQLException {
        if (cDao.open() == null) {
            cDao.open();
        }
        return cDao.findOne(consulta);
    }

    @Override
    public List<Consulta> listar() throws SQLException {
        if (cDao.open() == null) {
            cDao.open();
        }
        return cDao.findAll();
    }
}
