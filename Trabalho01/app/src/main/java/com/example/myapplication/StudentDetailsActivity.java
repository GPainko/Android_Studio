package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class StudentDetailsActivity extends AppCompatActivity {

    // Declaração de views para exibir os detalhes do estudante
    private TextView textViewName, textViewCpf, textViewPhone, textViewAge, textViewActive, textViewCourseType, textViewInvoices;
    private LinearLayout invoicesContainer; // Container para exibir dinamicamente as faturas do estudante
    private int studentId; // ID do estudante
    private StudentDatabaseHelper dbHelper; // Helper para operações no banco de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        // Inicialização das views
        textViewName = findViewById(R.id.text_view_name);
        textViewCpf = findViewById(R.id.text_view_cpf);
        textViewPhone = findViewById(R.id.text_view_phone);
        textViewAge = findViewById(R.id.text_view_age);
        textViewActive = findViewById(R.id.text_view_active);
        textViewCourseType = findViewById(R.id.text_view_course_type);
        textViewInvoices = findViewById(R.id.text_view_invoices);
        invoicesContainer = findViewById(R.id.invoices_container); // Inicializar o container para faturas

        // Inicialização do helper do banco de dados
        dbHelper = new StudentDatabaseHelper(this);

        // Obtendo o ID do aluno da Intent passada para esta atividade
        studentId = getIntent().getIntExtra("STUDENT_ID", -1);

        // Carregar detalhes do aluno se o ID for válido
        if (studentId != -1) {
            loadStudentDetails(studentId);
        } else {
            // Caso o ID seja inválido, exibir uma mensagem de erro
            textViewName.setText("Aluno não encontrado");
        }
    }

    // Método para carregar os detalhes do estudante do banco de dados
    private void loadStudentDetails(int studentId) {
        try {
            Student student = dbHelper.getStudentById(studentId); // Busca o estudante pelo ID

            if (student != null) {
                // Atualiza as views com os dados do estudante
                textViewName.setText(student.getName());
                textViewCpf.setText(student.getCpf());
                textViewPhone.setText(student.getPhone());
                textViewAge.setText(String.valueOf(student.getAge()));
                textViewActive.setText(student.isActive() ? "Ativo" : "Inativo");
                textViewCourseType.setText(student.getCourseType());

                // Obter e exibir as faturas associadas ao estudante
                List<Pagamento> pagamentos = dbHelper.getPagamentosByAlunoId(studentId);

                if (pagamentos != null && !pagamentos.isEmpty()) {
                    textViewInvoices.setText("Faturas Abertas: " + pagamentos.size());

                    // Limpar o container de faturas antes de adicionar novas views dinamicamente
                    invoicesContainer.removeAllViews();

                    // Adicionar dinamicamente cada fatura ao container
                    for (Pagamento pagamento : pagamentos) {
                        TextView invoiceTextView = new TextView(this);
                        invoiceTextView.setText(String.format(Locale.getDefault(), "Valor: R$ %.2f, Data: %s, Motivo: %s",
                                pagamento.getValor(), pagamento.getData(), pagamento.getDescricao()));
                        invoicesContainer.addView(invoiceTextView); // Adiciona a view da fatura ao container
                    }
                } else {
                    // Nenhuma fatura encontrada para o estudante
                    textViewInvoices.setText("Nenhuma fatura aberta.");
                }
            } else {
                // Se o estudante não for encontrado, exibir uma mensagem de erro
                textViewName.setText("Dados do aluno não encontrados");
            }
        } catch (Exception e) {
            // Captura e exibe um log de qualquer erro ao carregar detalhes do aluno
            Log.e("StudentDetailsActivity", "Erro ao carregar detalhes do aluno", e);
            textViewName.setText("Erro ao carregar os dados.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fechar o banco de dados ao destruir a activity para liberar recursos
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
