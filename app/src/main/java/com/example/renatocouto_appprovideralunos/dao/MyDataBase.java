package com.example.renatocouto_appprovideralunos.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.renatocouto_appprovideralunos.entity.Aluno;


@Database(entities = {Aluno.class}, version = 1)
public abstract class MyDataBase extends RoomDatabase {

    public abstract AlunoDao alunoDao();


}
