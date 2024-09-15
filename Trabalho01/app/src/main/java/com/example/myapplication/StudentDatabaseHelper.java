package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class StudentDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "studentss.db"; // Nome do banco de dados
    private static final int DATABASE_VERSION = 4; // Versão do banco de dados (incrementada para mudanças)

    // Nome da tabela e colunas para alunos
    private static final String TABLE_STUDENTS = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CPF = "cpf";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_ACTIVE = "active";
    private static final String COLUMN_COURSE_TYPE = "course_type";

    // Nome da tabela e colunas para pagamentos
    private static final String TABLE_PAGAMENTOS = "pagamentos";
    private static final String COLUMN_PAGAMENTO_ID = "id";
    private static final String COLUMN_PAGAMENTO_ALUNO_ID = "aluno_id";
    private static final String COLUMN_PAGAMENTO_VALOR = "valor";
    private static final String COLUMN_PAGAMENTO_DATA = "data";
    private static final String COLUMN_PAGAMENTO_DESCRICAO = "descricao";

    public StudentDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela de alunos
        String createStudentsTable = "CREATE TABLE " + TABLE_STUDENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_CPF + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_ACTIVE + " INTEGER DEFAULT 1, " +
                COLUMN_COURSE_TYPE + " TEXT)";
        db.execSQL(createStudentsTable);

        // Criação da tabela de pagamentos com referência à tabela de alunos
        String createPagamentosTable = "CREATE TABLE " + TABLE_PAGAMENTOS + " (" +
                COLUMN_PAGAMENTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PAGAMENTO_ALUNO_ID + " INTEGER, " +
                COLUMN_PAGAMENTO_VALOR + " REAL, " +
                COLUMN_PAGAMENTO_DATA + " TEXT, " +
                COLUMN_PAGAMENTO_DESCRICAO + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_PAGAMENTO_ALUNO_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_ID + "))";
        db.execSQL(createPagamentosTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Método para atualizar o esquema do banco de dados quando a versão é incrementada
        if (oldVersion < 4) {
            try {
                // Verifica e adiciona a coluna descricao se não existir
                db.execSQL("ALTER TABLE " + TABLE_PAGAMENTOS + " ADD COLUMN " + COLUMN_PAGAMENTO_DESCRICAO + " TEXT");
            } catch (Exception e) {
                Log.e("DB_UPGRADE", "Erro ao atualizar a tabela pagamentos: " + e.getMessage());
            }
        }
    }

    // Método para adicionar um novo estudante ao banco de dados
    public void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, student.getName());
            values.put(COLUMN_CPF, student.getCpf());
            values.put(COLUMN_PHONE, student.getPhone());
            values.put(COLUMN_AGE, student.getAge());
            values.put(COLUMN_ACTIVE, student.isActive() ? 1 : 0);
            values.put(COLUMN_COURSE_TYPE, student.getCourseType());

            db.insertOrThrow(TABLE_STUDENTS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao adicionar estudante: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Método para adicionar um novo pagamento ao banco de dados
    public void addPagamento(Pagamento pagamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PAGAMENTO_ALUNO_ID, pagamento.getAlunoId());
            values.put(COLUMN_PAGAMENTO_VALOR, pagamento.getValor());
            values.put(COLUMN_PAGAMENTO_DATA, pagamento.getData());
            values.put(COLUMN_PAGAMENTO_DESCRICAO, pagamento.getDescricao());

            db.insertOrThrow(TABLE_PAGAMENTOS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao adicionar pagamento: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Método para deletar um estudante e seus pagamentos relacionados
    public void deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_PAGAMENTOS, COLUMN_PAGAMENTO_ALUNO_ID + " = ?", new String[]{String.valueOf(id)});
            db.delete(TABLE_STUDENTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao deletar estudante: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Método para deletar um pagamento específico
    public void deletePagamento(int pagamentoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_PAGAMENTOS, COLUMN_PAGAMENTO_ID + " = ?", new String[]{String.valueOf(pagamentoId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao deletar pagamento: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Método para atualizar as informações de um estudante
    public void updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, student.getName());
            values.put(COLUMN_CPF, student.getCpf());
            values.put(COLUMN_PHONE, student.getPhone());
            values.put(COLUMN_AGE, student.getAge());
            values.put(COLUMN_ACTIVE, student.isActive() ? 1 : 0);
            values.put(COLUMN_COURSE_TYPE, student.getCourseType());

            db.update(TABLE_STUDENTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(student.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao atualizar estudante: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Método para atualizar as informações de um pagamento
    public void updatePagamento(Pagamento pagamento) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PAGAMENTO_ALUNO_ID, pagamento.getAlunoId());
            values.put(COLUMN_PAGAMENTO_VALOR, pagamento.getValor());
            values.put(COLUMN_PAGAMENTO_DATA, pagamento.getData());
            values.put(COLUMN_PAGAMENTO_DESCRICAO, pagamento.getDescricao());

            db.update(TABLE_PAGAMENTOS, values, COLUMN_PAGAMENTO_ID + " = ?", new String[]{String.valueOf(pagamento.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao atualizar pagamento: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Método para obter um estudante pelo ID
    public Student getStudentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Student student = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_STUDENTS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                student = new Student(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CPF)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTIVE)) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TYPE))
                );
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao obter estudante: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return student;
    }

    // Método para obter todos os estudantes
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS, null);
            if (cursor.moveToFirst()) {
                do {
                    Student student = new Student();
                    student.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    student.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                    student.setCpf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CPF)));
                    student.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)));
                    student.setAge(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)));
                    student.setActive(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ACTIVE)) == 1);
                    student.setCourseType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TYPE)));
                    studentList.add(student);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao obter todos os estudantes: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return studentList;
    }

    // Método para obter todos os pagamentos de um aluno pelo ID do aluno
    public List<Pagamento> getPagamentosByAlunoId(int alunoId) {
        List<Pagamento> pagamentosList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_PAGAMENTOS, null, COLUMN_PAGAMENTO_ALUNO_ID + " = ?", new String[]{String.valueOf(alunoId)}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Pagamento pagamento = new Pagamento(
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAGAMENTO_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAGAMENTO_ALUNO_ID)),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PAGAMENTO_VALOR)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAGAMENTO_DATA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAGAMENTO_DESCRICAO))
                    );
                    pagamentosList.add(pagamento);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB_ERROR", "Erro ao obter pagamentos: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return pagamentosList;
    }
}
