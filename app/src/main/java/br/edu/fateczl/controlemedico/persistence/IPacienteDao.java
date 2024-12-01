package br.edu.fateczl.controlemedico.persistence;

import java.sql.SQLException;

public interface IPacienteDao {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    public PacienteDao open() throws SQLException;
    public void close();
}
