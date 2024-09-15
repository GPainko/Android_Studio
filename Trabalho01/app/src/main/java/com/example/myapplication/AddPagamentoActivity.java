package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.method.DigitsKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddPagamentoActivity extends AppCompatActivity {

    private EditText editTextValue, editTextDate, editTextDescription;
    private Button buttonSaveInvoice;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        // Inicializando as views
        editTextValue = findViewById(R.id.edit_text_value);
        editTextDate = findViewById(R.id.edit_text_date);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonSaveInvoice = findViewById(R.id.button_save_invoice);

        // Obtendo o ID do estudante passado pela intent
        studentId = getIntent().getIntExtra("STUDENT_ID", -1);

        // Configura entrada de valor para permitir apenas números e vírgulas
        configureNumberInput(editTextValue);

        // Configura a formatação automática de data para o campo de data
        configureDateInput(editTextDate);

        // Define a ação do botão de salvar
        buttonSaveInvoice.setOnClickListener(v -> saveInvoice());
    }

    private void configureNumberInput(EditText editText) {
        // Configura o campo de entrada para permitir apenas números e vírgulas
        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789,"));

        // Limita o número máximo de caracteres para 15
        editText.setFilters(new InputFilter[]{new LengthFilter(15)});

        // Adiciona um TextWatcher para remover espaços adicionais
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove quaisquer espaços adicionais na entrada
                String input = s.toString().replaceAll("\\s+", "");
                if (!s.toString().equals(input)) {
                    editText.setText(input);
                    editText.setSelection(input.length());
                }
            }
        });
    }

    private void configureDateInput(EditText editText) {
        // Configura o campo de entrada para aplicar formatação automática de data
        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private final String ddmmyyyy = "DDMMYYYY";  // Formato padrão de data

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    // Remove caracteres não numéricos
                    String clean = s.toString().replaceAll("[^\\d]", "");

                    // Preenche com o formato de data padrão se necessário
                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        // Valida e corrige o dia, mês e ano
                        int day  = Integer.parseInt(clean.substring(0, 2));
                        int mon  = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = Math.max(1, Math.min(mon, 12)); // Garante que o mês esteja entre 1 e 12
                        day = Math.min(day, 31); // Garante que o dia não ultrapasse 31

                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    // Aplica a formatação no campo de data (dd/MM/yyyy)
                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    current = clean;
                    editText.setText(current);
                    editText.setSelection(Math.min(current.length(), clean.length()));
                }
            }
        });
    }

    private void saveInvoice() {
        // Coleta e valida os dados de entrada
        String valueString = editTextValue.getText().toString().replaceAll(",", ".").trim();
        String dateString = editTextDate.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        // Verifica se o campo de valor está vazio
        if (valueString.isEmpty()) {
            Toast.makeText(this, "Por favor, insira um valor válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        double value;
        try {
            value = Double.parseDouble(valueString); // Converte para um valor decimal
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido inserido.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica se a data inserida é válida
        if (!isValidDate(dateString)) {
            Toast.makeText(this, "Por favor, insira uma data válida no formato dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cria um objeto Pagamento com os dados fornecidos
        Pagamento pagamento = new Pagamento(studentId, value, dateString, description);

        // Salva o pagamento no banco de dados
        StudentDatabaseHelper dbHelper = new StudentDatabaseHelper(this);
        dbHelper.addPagamento(pagamento);

        // Exibe uma mensagem de confirmação e fecha a atividade
        Toast.makeText(this, "Fatura salva com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean isValidDate(String dateString) {
        // Método para validar a data no formato dd/MM/yyyy
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateFormat.setLenient(false);  // Define o analisador para modo não permissivo
            dateFormat.parse(dateString);
            return true;  // Data é válida
        } catch (ParseException e) {
            return false;  // Data é inválida
        }
    }
}
