package com.example.myapplication;

public class Pagamento {
    private int id; // Identificador único do pagamento
    private int alunoId; // ID do aluno associado ao pagamento
    private double valor; // Valor do pagamento
    private String data; // Data do pagamento no formato dd/MM/yyyy
    private String descricao; // Descrição do pagamento
    private String motivo; // Motivo ou justificativa do pagamento

    // Construtor completo com todos os campos
    public Pagamento(int id, int alunoId, double valor, String data, String descricao) {
        this.id = id;
        this.alunoId = alunoId;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
    }

    // Construtor sem ID (para inserção no banco de dados onde o ID é gerado automaticamente)
    public Pagamento(int alunoId, double valor, String data, String descricao) {
        this.alunoId = alunoId;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
    }

    // Getters e Setters para cada campo
    // Permitem o acesso e a modificação dos atributos privados da classe

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(int alunoId) {
        this.alunoId = alunoId;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
