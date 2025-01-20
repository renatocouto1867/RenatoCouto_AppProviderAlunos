package com.example.renatocouto_appprovideralunos.ui.listar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.renatocouto_appprovideralunos.MainActivity;
import com.example.renatocouto_appprovideralunos.R;
import com.example.renatocouto_appprovideralunos.dao.AlunoDao;
import com.example.renatocouto_appprovideralunos.dao.MyDataBase;
import com.example.renatocouto_appprovideralunos.entity.Aluno;
import com.example.renatocouto_appprovideralunos.ui.cadastra.CadastrarFragment;

import java.util.List;

public class ListarFragment extends Fragment {

    private AlunoDao alunoDao;
    private ItemListarAlunoAdapter itemListarAlunoAdapter;
    private RecyclerView recyclerViewAluno;
    private ProgressBar progressBar;
    private TextView textViewProgress;

    public ListarFragment() {

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
            progressBar.setVisibility(View.VISIBLE);
            configurarRecyclerView(alunoList);
            progressBar.setVisibility(View.GONE);
            textViewProgress.setVisibility(View.GONE);

        } else {
            progressBar.setVisibility(View.GONE);
            textViewProgress.setVisibility(View.VISIBLE);
            configurarRecyclerView(alunoList);
            textViewProgress.setText(R.string.sem_aluno_cadastrado);
        }
    }

    private void configurarRecyclerView(List<Aluno> alunoList) {
        recyclerViewAluno.setLayoutManager(new LinearLayoutManager(requireContext()));

        itemListarAlunoAdapter = new ItemListarAlunoAdapter(alunoList, new ItemListarAlunoAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Aluno aluno) {
                editaAluno(aluno);
            }

            @Override
            public void onDeleteClick(Aluno aluno) {
                deleteAluno(aluno);
            }

        });

        recyclerViewAluno.setAdapter(itemListarAlunoAdapter);
    }

    private void editaAluno(Aluno aluno) {
        Bundle result = new Bundle();
        result.putSerializable("aluno", aluno);

        Fragment fragment = CadastrarFragment.newInstance();
        fragment.setArguments(result);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.iniciarFragment(fragment,R.string.editar_cadastro);
        }

    }

    private void deleteAluno(Aluno aluno) {
        new AlertDialog.Builder(getContext()).setTitle(R.string.confirmar_exclusao)
                .setMessage(getString(R.string.realmente_deseja_deletar) + aluno.getNome() + "?")
                .setPositiveButton(getString(R.string.deletar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //sim
                        alunoDao.deletar(aluno);
                        carregarAlunos();
                    }
                }).setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //não
                        dialog.dismiss();
                    }
                }).create().show();

    }

}
