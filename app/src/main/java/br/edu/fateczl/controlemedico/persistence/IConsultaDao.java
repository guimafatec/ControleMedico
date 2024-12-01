package br.edu.fateczl.controlemedico.persistence;

import java.sql.SQLException;

public interface IConsultaDao {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    public ConsultaDao open() throws SQLException;
    public void close();
}
