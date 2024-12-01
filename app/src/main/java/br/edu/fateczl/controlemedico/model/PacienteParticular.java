package br.edu.fateczl.controlemedico.model;

import androidx.annotation.NonNull;

import java.time.LocalDate;

public class PacienteParticular extends Paciente {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    public PacienteParticular() {
        super();
        this.setTipo("P");
    }
}
