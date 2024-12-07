package br.edu.fateczl.controlemedico.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consulta {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private int codigo;
    private LocalDateTime dhConsulta;
    private Medico medico;
    private Paciente paciente;
    private float valor;

    public Consulta() {
        super();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getDhConsulta() {
        return dhConsulta;
    }

    public String getFmtDhConsulta() {
        DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.dhConsulta.format(dtFmt);
    }

    public void setDhConsulta(LocalDateTime dhConsulta) {
        this.dhConsulta = dhConsulta;
    }

    public void setDhConsulta(String dhConsulta) {
        DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dhParse = LocalDateTime.parse(dhConsulta, dtFmt);
        this.dhConsulta = dhParse;
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

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        float finalValue = valor;
        if (paciente.getTipo().equals("C")) {
            PacienteConveniado conveniado = (PacienteConveniado) paciente;
            finalValue = valor * (1 - conveniado.getDesconto());
            // Arredondar o valor
            finalValue = Math.round(finalValue * 100) / 100f;
        }

        this.valor = finalValue;
    }

    public String description(){
        return String.format("%s\t%s\t\tR$%.2f", getFmtDhConsulta(), medico.getEspecialidade(), getValor());
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("%s\t%s\t%s\tDr./Dra.%s", getFmtDhConsulta(), paciente.getId(), medico.getEspecialidade(), medico.getNome());
    }


}
