package com.example.myapplication;

public class Student {
    private int id; // Identificador único do estudante
    private String name; // Nome do estudante
    private String cpf; // CPF do estudante (formato 000.000.000-00)
    private String phone; // Telefone do estudante (formato (00) 00000-0000)
    private int age; // Idade do estudante
    private boolean active; // Indica se o estudante está ativo (true = ativo, false = inativo)
    private String courseType; // Tipo de curso do estudante (e.g., Graduação ou Pós-graduação)

    // Construtor completo, usado para criar instâncias de Student quando todos os dados são conhecidos
    public Student(int id, String name, String cpf, String phone, int age, boolean active, String courseType) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.age = age;
        this.active = active;
        this.courseType = courseType;
    }

    // Construtor sem ID, usado para criar instâncias de Student quando o ID ainda não é conhecido (novo estudante)
    public Student(String name, String cpf, String phone, int age, boolean active, String courseType) {
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.age = age;
        this.active = active;
        this.courseType = courseType;
    }

    // Construtor padrão vazio, usado quando não há informações para inicializar imediatamente
    public Student() {

    }

    // Getters e Setters para cada campo
    // Permitem o acesso e a modificação dos atributos privados da classe

    public int getId() {
        return id; // Retorna o ID do estudante
    }

    public void setId(int id) {
        this.id = id; // Define o ID do estudante
    }

    public String getName() {
        return name; // Retorna o nome do estudante
    }

    public void setName(String name) {
        this.name = name; // Define o nome do estudante
    }

    public String getCpf() {
        return cpf; // Retorna o CPF do estudante
    }

    public void setCpf(String cpf) {
        this.cpf = cpf; // Define o CPF do estudante
    }

    public String getPhone() {
        return phone; // Retorna o telefone do estudante
    }

    public void setPhone(String phone) {
        this.phone = phone; // Define o telefone do estudante
    }

    public int getAge() {
        return age; // Retorna a idade do estudante
    }

    public void setAge(int age) {
        this.age = age; // Define a idade do estudante
    }

    public boolean isActive() {
        return active; // Retorna se o estudante está ativo
    }

    public void setActive(boolean active) {
        this.active = active; // Define se o estudante está ativo
    }

    public String getCourseType() {
        return courseType; // Retorna o tipo de curso do estudante
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType; // Define o tipo de curso do estudante
    }

    @Override
    public String toString() {
        // Retorna uma representação em string do objeto Student
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", phone='" + phone + '\'' +
                ", age=" + age +
                ", active=" + active +
                ", courseType='" + courseType + '\'' +
                '}';
    }
}
