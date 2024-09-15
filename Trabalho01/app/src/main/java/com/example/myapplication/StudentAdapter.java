package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private Context context;
    private List<Student> studentList; // Lista de estudantes exibida no RecyclerView
    private List<Student> selectedStudents = new ArrayList<>(); // Lista de estudantes selecionados
    private StudentDatabaseHelper dbHelper; // Helper do banco de dados para acessar informações de estudantes e pagamentos

    // Construtor do adapter
    public StudentAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
        this.dbHelper = new StudentDatabaseHelper(context); // Inicializa o DB Helper para operações de banco de dados
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout de item de estudante e cria o ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        // Liga os dados de cada estudante à view correspondente
        Student student = studentList.get(position);
        holder.textViewName.setText(student.getName());
        holder.textViewCpf.setText(student.getCpf());
        holder.textViewPhone.setText(student.getPhone());
        holder.textViewAge.setText(String.valueOf(student.getAge()));

        // Obtém o número de faturas abertas para o aluno
        List<Pagamento> pagamentos = dbHelper.getPagamentosByAlunoId(student.getId());
        int openInvoicesCount = pagamentos.size();
        holder.textViewInvoices.setText("Faturas Abertas: " + openInvoicesCount);

        // Listener para seleção do item (alterna a seleção ao clicar)
        holder.itemView.setOnClickListener(v -> {
            if (selectedStudents.contains(student)) {
                selectedStudents.remove(student);
                holder.itemView.setBackgroundColor(Color.WHITE); // Desmarca a seleção
            } else {
                selectedStudents.add(student);
                holder.itemView.setBackgroundColor(Color.LTGRAY); // Marca a seleção
            }
        });

        // Listener para o botão de visualizar detalhes do aluno
        holder.buttonViewStudent.setOnClickListener(v -> {
            Intent intent = new Intent(context, StudentDetailsActivity.class);
            intent.putExtra("STUDENT_ID", student.getId()); // Passa o ID do estudante para a próxima atividade
            context.startActivity(intent);
        });

        // Listener para o botão de gerar fatura
        holder.fabGenerateInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddPagamentoActivity.class);
            intent.putExtra("STUDENT_ID", student.getId()); // Passa o ID do estudante para a próxima atividade
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Retorna o número total de itens na lista de estudantes
        return studentList.size();
    }

    public List<Student> getSelectedStudents() {
        // Retorna a lista de estudantes selecionados
        return selectedStudents;
    }

    public void clearSelection() {
        // Limpa a lista de seleção e notifica o adapter para atualizar a visualização
        selectedStudents.clear();
        notifyDataSetChanged();
    }

    public void setStudentList(List<Student> studentList) {
        // Atualiza a lista de estudantes no adapter e notifica o adapter para atualizar a visualização
        this.studentList = studentList;
        notifyDataSetChanged();
    }

    // Método para atualizar a lista de alunos e número de faturas abertas
    public void updateStudentData(List<Student> updatedStudentList) {
        // Atualiza a lista de estudantes e notifica o adapter para atualizar a visualização
        this.studentList = updatedStudentList;
        notifyDataSetChanged();
    }

    // Método para liberar recursos ao final
    public void releaseResources() {
        // Fecha a conexão com o banco de dados para liberar recursos
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    // ViewHolder para os itens de estudante no RecyclerView
    class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewCpf, textViewPhone, textViewAge, textViewInvoices; // TextViews para exibir detalhes do estudante
        Button buttonViewStudent; // Botão para visualizar detalhes do estudante
        FloatingActionButton fabGenerateInvoice; // Botão flutuante para gerar fatura

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializa as views do layout do item de estudante
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewCpf = itemView.findViewById(R.id.text_view_cpf);
            textViewPhone = itemView.findViewById(R.id.text_view_phone);
            textViewAge = itemView.findViewById(R.id.text_view_age);
            textViewInvoices = itemView.findViewById(R.id.text_view_invoices);
            buttonViewStudent = itemView.findViewById(R.id.button_view_student);
            fabGenerateInvoice = itemView.findViewById(R.id.fab_generate_invoice);
        }
    }
}
