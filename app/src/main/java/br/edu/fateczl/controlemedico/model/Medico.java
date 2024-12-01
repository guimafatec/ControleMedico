package br.edu.fateczl.controlemedico.model;

import androidx.annotation.NonNull;

public class Medico {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private String crm;
    private String nome;
    private String especialidade;

    public Medico() {
        super();
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Dr./Dra. %s - %s - CRM: %s", nome, especialidade, crm);
    }
}
