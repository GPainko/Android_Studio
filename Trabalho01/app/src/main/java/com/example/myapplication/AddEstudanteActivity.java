package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEstudanteActivity extends AppCompatActivity {
    private EditText editTextName, editTextCpf, editTextPhone, editTextAge;
    private CheckBox checkBoxActive;
    private RadioGroup radioGroupCourseType;
    private Button buttonSave;
    private StudentDatabaseHelper databaseHelper;
    private int studentId = -1;  // -1 indica que estamos adicionando um novo estudante

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_student);

        // Inicializando as views do layout
        editTextName = findViewById(R.id.edit_text_name);
        editTextCpf = findViewById(R.id.edit_text_cpf);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextAge = findViewById(R.id.edit_text_age);
        checkBoxActive = findViewById(R.id.checkbox_active);
        radioGroupCourseType = findViewById(R.id.radio_group_course_type);
        buttonSave = findViewById(R.id.button_save);
        databaseHelper = new StudentDatabaseHelper(this);

        // Adicionar TextWatcher para formatar o campo de CPF em tempo real
        editTextCpf.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;  // Flag para evitar loop ao atualizar texto

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Método não utilizado
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Método não utilizado
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) {  // Verifica se já está atualizando para evitar loop
                    isUpdating = false;
                    return;
                }

                String str = s.toString();
                // Remove qualquer caractere que não seja número
                str = str.replaceAll("[^\\d]", "");

                // Limita o comprimento do CPF a 11 dígitos
                if (str.length() > 11) {
                    str = str.substring(0, 11);
                }

                // Aplica a formatação do CPF
                StringBuilder formatted = new StringBuilder();
                if (str.length() > 3) {
                    formatted.append(str.substring(0, 3)).append(".");
                    if (str.length() > 6) {
                        formatted.append(str.substring(3, 6)).append(".");
                        if (str.length() > 9) {
                            formatted.append(str.substring(6, 9)).append("-");
                            formatted.append(str.substring(9));
                        } else {
                            formatted.append(str.substring(6));
                        }
                    } else {
                        formatted.append(str.substring(3));
                    }
                } else {
                    formatted.append(str);
                }

                // Atualiza o campo de CPF com o formato aplicado
                isUpdating = true;
                editTextCpf.setText(formatted.toString());
                editTextCpf.setSelection(formatted.length());
            }
        });

        // Adicionar TextWatcher para formatar o campo de Telefone em tempo real
        editTextPhone.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;  // Flag para evitar loop ao atualizar texto

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Método não utilizado
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Método não utilizado
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdating) {  // Verifica se já está atualizando para evitar loop
                    isUpdating = false;
                    return;
                }

                String str = s.toString();
                // Remove qualquer caractere que não seja número
                str = str.replaceAll("[^\\d]", "");

                // Limita o comprimento do telefone a 11 dígitos
                if (str.length() > 11) {
                    str = str.substring(0, 11);
                }

                // Aplica a formatação do Telefone
                StringBuilder formatted = new StringBuilder();
                if (str.length() > 0) {
                    formatted.append("(");
                    if (str.length() > 2) {
                        formatted.append(str.substring(0, 2)).append(") ");
                        if (str.length() > 7) {
                            formatted.append(str.substring(2, 7)).append("-");
                            formatted.append(str.substring(7));
                        } else {
                            formatted.append(str.substring(2));
                        }
                    } else {
                        formatted.append(str);
                    }
                } else {
                    formatted.append(str);
                }

                // Atualiza o campo de Telefone com o formato aplicado
                isUpdating = true;
                editTextPhone.setText(formatted.toString());
                editTextPhone.setSelection(formatted.length());
            }
        });

        // Verifica se estamos editando um estudante existente
        if (getIntent().hasExtra("student_id")) {
            studentId = getIntent().getIntExtra("student_id", -1);
            loadStudentData(studentId);  // Carrega dados do estudante existente
        }

        // Configuração do botão de salvar
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {  // Valida os campos de entrada
                    // Coleta os dados dos campos de entrada
                    String name = editTextName.getText().toString().trim();
                    String cpf = editTextCpf.getText().toString().trim();
                    String phone = editTextPhone.getText().toString().trim();
                    int age = Integer.parseInt(editTextAge.getText().toString().trim());
                    boolean isActive = checkBoxActive.isChecked();
                    String courseType = ((RadioButton) findViewById(radioGroupCourseType.getCheckedRadioButtonId())).getText().toString();

                    if (studentId == -1) {
                        // Adiciona um novo estudante
                        Student student = new Student(0, name, cpf, phone, age, isActive, courseType);
                        databaseHelper.addStudent(student);
                        Toast.makeText(AddEstudanteActivity.this, "Estudante adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Atualiza o estudante existente
                        Student student = new Student(studentId, name, cpf, phone, age, isActive, courseType);
                        databaseHelper.updateStudent(student);
                        Toast.makeText(AddEstudanteActivity.this, "Estudante atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                    }

                    setResult(RESULT_OK);  // Define o resultado como OK
                    finish();  // Fecha a atividade
                }
            }
        });
    }

    // Carrega os dados do estudante a partir do banco de dados
    private void loadStudentData(int studentId) {
        Student student = databaseHelper.getStudentById(studentId);
        if (student != null) {
            // Preenche os campos com os dados do estudante
            editTextName.setText(student.getName());
            editTextCpf.setText(student.getCpf());
            editTextPhone.setText(student.getPhone());
            editTextAge.setText(String.valueOf(student.getAge()));
            checkBoxActive.setChecked(student.isActive());

            // Marca o tipo de curso selecionado
            if (student.getCourseType().equals("Graduação")) {
                ((RadioButton) findViewById(R.id.radio_undergraduate)).setChecked(true);
            } else if (student.getCourseType().equals("Pós-graduação")) {
                ((RadioButton) findViewById(R.id.radio_postgraduate)).setChecked(true);
            }
        }
    }

    // Validação dos campos de entrada
    private boolean validateInput() {
        String name = editTextName.getText().toString().trim();
        String cpf = editTextCpf.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String ageText = editTextAge.getText().toString().trim();

        // Verifica se o nome é válido (somente letras)
        if (name.isEmpty() || !name.matches("[a-zA-Z\\s]+")) {
            showToast("Por favor, insira um nome válido (somente letras)");
            return false;
        }

        // Verifica se o CPF é válido (formato 000.000.000-00)
        if (cpf.isEmpty() || !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            showToast("Por favor, insira um CPF válido no formato 000.000.000-00");
            return false;
        }

        // Verifica se o telefone é válido (formato (00) 00000-0000)
        if (phone.isEmpty() || !phone.matches("\\(\\d{2}\\) \\d{5}-\\d{4}")) {
            showToast("Por favor, insira um telefone válido no formato (00) 00000-0000");
            return false;
        }

        // Verifica se a idade foi informada e se é válida
        if (ageText.isEmpty()) {
            showToast("Por favor, insira a idade");
            return false;
        }

        try {
            int age = Integer.parseInt(ageText);
            if (age <= 0 || age > 120) {
                showToast("Por favor, insira uma idade válida");
                return false;
            }
        } catch (NumberFormatException e) {
            showToast("Por favor, insira uma idade válida");
            return false;
        }

        return true;  // Todos os campos são válidos
    }

    // Exibe uma mensagem de toast
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
