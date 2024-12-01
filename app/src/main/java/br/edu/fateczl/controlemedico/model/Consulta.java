package br.edu.fateczl.controlemedico.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.time.LocalDate;

public class Consulta {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private int codigo;
    private String dataConsulta;
    private Medico medico;
    private Paciente paciente;

    public Consulta() {
        super();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(String dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("Nº%d\t%s\tPaciente: %s\tEspecialidade: %s\tDr./Dra.%s", codigo, dataConsulta, paciente.getNome(), medico.getEspecialidade(), medico.getNome());
    }
}
