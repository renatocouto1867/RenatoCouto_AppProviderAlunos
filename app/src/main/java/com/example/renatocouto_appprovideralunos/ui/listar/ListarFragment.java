package com.example.renatocouto_appprovideralunos.ui.listar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.renatocouto_appprovideralunos.R;
import com.example.renatocouto_appprovideralunos.auxiliar.Mensagens;
import com.example.renatocouto_appprovideralunos.dao.AlunoDao;
import com.example.renatocouto_appprovideralunos.dao.MyDataBase;
import com.example.renatocouto_appprovideralunos.entity.Aluno;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListarFragment extends Fragment {

    private AlunoDao alunoDao;
    private ItemListarAlunoAdapter itemListarAlunoAdapter;
    private RecyclerView recyclerViewAluno;
    private ProgressBar progressBar;
    private TextView textViewProgress;

    public ListarFragment() {
        // Construtor público obrigatório
    }

    public static ListarFragment newInstance() {
        return new ListarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDb();
    }

    private void inicializaDb() {
        MyDataBase db = Room.databaseBuilder(
                requireContext().getApplicationContext(),
                MyDataBase.class,
                "meu_banco"
        ).allowMainThreadQueries().build();
        alunoDao = db.alunoDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar, container, false);

        progressBar = view.findViewById(R.id.progress_circular);
        recyclerViewAluno = view.findViewById(R.id.recyclerViewAluno);
        textViewProgress = view.findViewById(R.id.tv_carregando);

        carregarAlunos();

        return view;
    }

    private void carregarAlunos() {
        List<Aluno> alunoList = alunoDao.buscarTodos();

        if (alunoList != null && !alunoList.isEmpty()) {
            configurarRecyclerView(alunoList);
            progressBar.setVisibility(View.GONE);
            textViewProgress.setVisibility(View.GONE);

        } else {
            Mensagens.showAlerta(requireView(), getString(R.string.sem_aluno_cadastrado));
        }
    }

    private void configurarRecyclerView(List<Aluno> alunoList) {
        recyclerViewAluno.setLayoutManager(new LinearLayoutManager(requireContext()));

        itemListarAlunoAdapter = new ItemListarAlunoAdapter(alunoList, new ItemListarAlunoAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Aluno aluno) {
                Toast.makeText(requireActivity(), "Implementar edit", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Aluno aluno) {
                Toast.makeText(requireActivity(), "Implementar excluir", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewAluno.setAdapter(itemListarAlunoAdapter);
    }
}
