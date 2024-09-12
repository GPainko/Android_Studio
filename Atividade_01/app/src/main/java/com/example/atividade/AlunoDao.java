package com.example.atividade;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AlunoDao {
    private Conexao conexao;
    private SQLiteDatabase banco;

    public AlunoDao(Context contex){
        conexao = new Conexao(contex);
        banco = conexao.getWritableDatabase();
    }

    public long inserir(Aluno aluno){
        ContentValues values = new ContentValues();
        values.put("nome",aluno.getNome());
        values.put("cpf",aluno.getCpf());
        values.put("telefone",aluno.getTelefone());
        return banco.insert("aluno",null,values);
    }
}
