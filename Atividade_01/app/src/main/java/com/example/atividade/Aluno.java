package com.example.atividade;

import java.io.Serializable;

public class Aluno implements Serializable {
    private Integer id;
    private String nome;
    private String cpf;
    private String telephone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString(){
        String msg = (nome + "\n" + cpf +"\n" + telephone);
        return msg;
    }
}
