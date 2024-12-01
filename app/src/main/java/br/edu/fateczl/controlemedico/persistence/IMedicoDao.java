package br.edu.fateczl.controlemedico.persistence;

import java.sql.SQLException;

public interface IMedicoDao {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    public MedicoDao open() throws SQLException;
    public void close();
}
