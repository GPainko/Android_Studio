package com.example.atividade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
        values.put("telefone",aluno.getTelephone());
        return banco.insert("aluno",null,values);
    }

    public List<Aluno> obterTodos() {
        List<Aluno> alunos = new ArrayList<>();
        Cursor cursor = banco.query("aluno", new String[]{"id", "nome", "cpf", "telefone"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Aluno a = new Aluno();
            a.setId(cursor.getInt(0));
            a.setNome(cursor.getString(1));
            a.setCpf(cursor.getString(2));
            a.setTelephone(cursor.getString(3));
            alunos.add(a);
        }
        return alunos;
    }
}

