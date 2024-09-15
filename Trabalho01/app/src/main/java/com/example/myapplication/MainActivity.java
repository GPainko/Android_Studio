package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD_STUDENT = 1;
    private static final int REQUEST_CODE_EDIT_STUDENT = 2;

    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private List<Student> studentList;
    private FloatingActionButton fabAdd, fabDelete, fabEdit;
    private StudentDatabaseHelper studentDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializando o helper do banco de dados
        studentDatabaseHelper = new StudentDatabaseHelper(this);

        // Inicializando as views
        recyclerView = findViewById(R.id.recycler_view);
        fabAdd = findViewById(R.id.fab);
        fabDelete = findViewById(R.id.fab_delete);
        fabEdit = findViewById(R.id.fab_refresh);

        // Configurando o RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentList = loadStudentsFromDatabase(); // Carrega a lista de estudantes do banco de dados
        studentAdapter = new StudentAdapter(this, studentList); // Inicializa o adapter com a lista de estudantes
        recyclerView.setAdapter(studentAdapter); // Associa o adapter ao RecyclerView

        // Listener para o botão de adicionar estudante
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEstudanteActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_STUDENT);
        });

        // Listener para o botão de deletar estudante(s)
        fabDelete.setOnClickListener(v -> {
            List<Student> selectedStudents = studentAdapter.getSelectedStudents(); // Obtém a lista de estudantes selecionados
            if (selectedStudents.isEmpty()) {
                Toast.makeText(this, "Nenhum estudante selecionado", Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Confirmação")
                        .setMessage("Deseja realmente deletar " + selectedStudents.size() + " estudantes?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            deleteSelectedStudents(selectedStudents); // Deleta os estudantes selecionados
                            refreshStudentList(); // Atualiza a lista de estudantes
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

        // Listener para o botão de editar estudante
        fabEdit.setOnClickListener(v -> {
            List<Student> selectedStudents = studentAdapter.getSelectedStudents(); // Obtém a lista de estudantes selecionados
            if (selectedStudents.size() != 1) {
                Toast.makeText(this, "Selecione exatamente um estudante para editar", Toast.LENGTH_SHORT).show();
            } else {
                Student studentToEdit = selectedStudents.get(0);
                Intent intent = new Intent(MainActivity.this, AddEstudanteActivity.class);
                intent.putExtra("student_id", studentToEdit.getId()); // Passa o ID do estudante a ser editado
                startActivityForResult(intent, REQUEST_CODE_EDIT_STUDENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            refreshStudentList(); // Atualiza a lista de estudantes após uma ação de adicionar ou editar
        }
    }

    private List<Student> loadStudentsFromDatabase() {
        // Carregar estudantes do banco de dados
        return studentDatabaseHelper.getAllStudents();
    }

    private void deleteSelectedStudents(List<Student> selectedStudents) {
        // Deleta os estudantes selecionados do banco de dados
        for (Student student : selectedStudents) {
            studentDatabaseHelper.deleteStudent(student.getId());
        }
        Toast.makeText(this, "Estudantes deletados com sucesso", Toast.LENGTH_SHORT).show();
    }

    private void refreshStudentList() {
        // Atualiza a lista de estudantes exibida no RecyclerView
        studentList = loadStudentsFromDatabase(); // Recarregar a lista do banco de dados
        studentAdapter.setStudentList(studentList); // Atualizar a lista no adapter
        studentAdapter.notifyDataSetChanged(); // Notificar o adapter para atualizar a interface
    }
}
