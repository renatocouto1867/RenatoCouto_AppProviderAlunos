package com.example.renatocouto_appprovideralunos.ui.cadastra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.renatocouto_appprovideralunos.MainActivity;
import com.example.renatocouto_appprovideralunos.R;
import com.example.renatocouto_appprovideralunos.auxiliar.Mensagens;
import com.example.renatocouto_appprovideralunos.dao.AlunoDao;
import com.example.renatocouto_appprovideralunos.dao.MyDataBase;
import com.example.renatocouto_appprovideralunos.entity.Aluno;
import com.example.renatocouto_appprovideralunos.ui.listar.ListarFragment;

import java.util.Objects;

/**
 * Fragment para cadastro de alunos.
 */
public class CadastrarFragment extends Fragment {
    private AlunoDao alunoDao;
    private boolean isEdicao = false;
    private Aluno aluno;
    private Button btnSalvar, btnLimpar;
    private EditText editTextNome, editTextIdade, editTextNota1, editTextNota2, editTextNota3;

    public CadastrarFragment() {
    }

    public static CadastrarFragment newInstance() {
        return new CadastrarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializaDb();
    }

    private void inicializaDb() {
        MyDataBase db = Room.databaseBuilder(
                Objects.requireNonNull(requireContext()),
                MyDataBase.class,
                "meu_banco"
        ).allowMainThreadQueries().build();
        alunoDao = db.alunoDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla o layout para o fragmento
        View view = inflater.inflate(R.layout.fragment_cadastrar, container, false);

        inicializarViews(view);
        configurarBotoes();
        inicializaArguments();
        return view;
    }

    private void inicializaArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            aluno = (Aluno) bundle.getSerializable("aluno");
            editTextNome.setText(aluno.getNome());
            editTextIdade.setText(String.valueOf(aluno.getIdade()));
            editTextNota1.setText(String.valueOf(aluno.getNota1()));
            editTextNota2.setText(String.valueOf(aluno.getNota2()));
            editTextNota3.setText(String.valueOf(aluno.getNota2()));
            isEdicao = true;
        } else {
            aluno = new Aluno();
            isEdicao = false;
        }
    }

    private void inicializarViews(View view) {
        editTextNome = view.findViewById(R.id.editNome);
        editTextIdade = view.findViewById(R.id.editIdade);
        editTextNota1 = view.findViewById(R.id.editNota1);
        editTextNota2 = view.findViewById(R.id.editNota2);
        editTextNota3 = view.findViewById(R.id.editNota3);
        btnSalvar = view.findViewById(R.id.button_salvar);
        btnLimpar = view.findViewById(R.id.button_limpar);
    }

    private void configurarBotoes() {
        btnSalvar.setOnClickListener(view -> salvarAluno());
        btnLimpar.setOnClickListener(view -> limparCampos());
    }

    private void salvarAluno() {
        String nome = editTextNome.getText().toString().trim();
        String idadeStr = editTextIdade.getText().toString().trim();
        String nota1Str = editTextNota1.getText().toString().trim();
        String nota2Str = editTextNota2.getText().toString().trim();
        String nota3Str = editTextNota3.getText().toString().trim();

        if (nome.isEmpty() || idadeStr.isEmpty() || nota1Str.isEmpty() || nota2Str.isEmpty() || nota3Str.isEmpty()) {
            Mensagens.showErro(requireView(), getString(R.string.por_favor_preencha_todos_os_campos));
            return;
        }

        int idade;
        double nota1, nota2, nota3;

        try {
            idade = Integer.parseInt(idadeStr);
            nota1 = Double.parseDouble(nota1Str);
            nota2 = Double.parseDouble(nota2Str);
            nota3 = Double.parseDouble(nota3Str);

            boolean isNotaValida = validaNota(nota1) && validaNota(nota2) && validaNota(nota3);
            if (!isNotaValida) {
                Mensagens.showErro(requireView(), getString(R.string.insira_valores_v_lidos_para_idade_e_notas));
                return;
            }

            aluno.setNome(nome);
            aluno.setIdade(idade);
            aluno.setNota1(nota1);
            aluno.setNota2(nota2);
            aluno.setNota3(nota3);

            if (isEdicao) {
                alunoDao.atualizar(aluno);
            } else alunoDao.inserir(aluno);
            Mensagens.showSucesso(requireView(), getString(R.string.aluno_salvo_com_sucesso));

        } catch (NumberFormatException e) {
            Mensagens.showErro(requireView(), getString(R.string.insira_valores_v_lidos_para_idade_e_notas));
            return;
        }

        iniciarFragamentList();

    }

    private void iniciarFragamentList() {
        Fragment fragment = ListarFragment.newInstance();
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.iniciarFragment(fragment,R.string.listar);
        }
    }

    private boolean validaNota(double nota) {
        return nota >= 0.0 && nota <= 10.0;
    }

    private void limparCampos() {
        editTextNome.setText("");
        editTextIdade.setText("");
        editTextNota1.setText("");
        editTextNota2.setText("");
        editTextNota3.setText("");
        editTextNome.requestFocus();
    }
}
