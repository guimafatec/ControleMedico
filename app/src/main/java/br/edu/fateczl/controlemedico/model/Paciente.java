package br.edu.fateczl.controlemedico.model;

import androidx.annotation.NonNull;

import java.time.LocalDate;

public class Paciente {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private String id;
    private String nome;
    private String dataNascimento;
    private String tipo;
    public Paciente() {super();}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s - %s - %s", this.getId(), this.getNome(), this.getDataNascimento());
    }
}
