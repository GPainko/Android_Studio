package com.example.atividade;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText nome;
    private EditText cpf;
    private EditText telefone;
    private AlunoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editCpf);
        telefone = findViewById(R.id.editTelefone);
        dao = new AlunoDao(this);
    }

    public void salvar(View view){
        Aluno a = new Aluno();
        a.setNome(nome.getText().toString());
        a.setCpf(cpf.getText().toString());
        a.setTelefone(telefone.getText().toString());

        long id = dao.inserir(a);
        Toast.makeText(this,"Aluno inserido com id: " + id,Toast.LENGTH_SHORT).show();
    }
}