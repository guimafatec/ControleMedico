package br.edu.fateczl.controlemedico.model;

import androidx.annotation.NonNull;

public class PacienteConveniado extends Paciente {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private static float desconto = 0.07f;
    public PacienteConveniado() {
        super();
        this.setTipo("C");
    }

    public float getDesconto() {
        return this.desconto;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s - %.2f%%", getId(), getNome(), getTipo(), getDataNascimento(), getDesconto() * 100);
    }
}
