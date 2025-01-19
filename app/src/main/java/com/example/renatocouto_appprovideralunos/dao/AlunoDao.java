package com.example.renatocouto_appprovideralunos.dao;


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
    void atualizar(Aluno aluno);

    @Delete
    void deletar(Aluno aluno);

    @Query("SELECT * FROM alunos WHERE id = :id")
    Aluno buscarPorId(long id);

    @Query("SELECT * FROM alunos")
    List<Aluno> buscarTodos();
}
