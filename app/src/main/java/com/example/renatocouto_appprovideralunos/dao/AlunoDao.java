package com.example.renatocouto_appprovideralunos.dao;


import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.renatocouto_appprovideralunos.entity.Aluno;

import java.util.List;

@Dao
public interface AlunoDao {

    @Insert
    long inserir(Aluno aluno);

    @Update
    int atualizar(Aluno aluno);

    @Delete
    void deletar(Aluno aluno);

    @Query("DELETE FROM alunos WHERE id = :id")
    int deletarPorId(long id);



    @Query("SELECT * FROM alunos WHERE id = :id")
    Aluno buscarPorId(long id);

    @Query("SELECT * FROM alunos")
    List<Aluno> buscarTodos();

    @Query("SELECT * FROM alunos")
    Cursor buscarTosdosCursor();
}
