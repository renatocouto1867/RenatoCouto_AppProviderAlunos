package com.example.renatocouto_appprovideralunos;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.renatocouto_appprovideralunos.home.HomeFragment;
import com.example.renatocouto_appprovideralunos.ui.cadastra.CadastrarFragment;
import com.example.renatocouto_appprovideralunos.ui.listar.ListarFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ImageButton botaoHome;
    private ImageButton botaoSair;
    private BottomNavigationView bottomNavigationView;
    private TextView textTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        inicializarViews();
        configurarCliques();

        //https://developer.android.com/reference/com/google/android/material/navigation/NavigationBarView#setItemActiveIndicatorColor(android.content.res.ColorStateList)
        //muda a cor da seleção dos icones na barra de navegação
        //public void setItemActiveIndicatorColor (ColorStateList csl)
        bottomNavigationView.setItemActiveIndicatorColor(ColorStateList.valueOf(getColor(R.color.gray_400)));
    }

    private void inicializarViews() {
        botaoSair = findViewById(R.id.btn_sair);
        bottomNavigationView = findViewById(R.id.nav_view);
        textTitulo = findViewById(R.id.txt_titulo_toolbar);
        botaoHome = findViewById(R.id.btn_home);
    }

    private void configurarCliques() {
        configurarBotaoSair();
        configurarBottomNavigation();
        ouvinteCliqueMenuHome();
    }

    public void iniciarFragment(Fragment fragment, int titulo) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

        atualizarTitulo(titulo);
    }

    private void configurarBotaoSair() {
        botaoSair.setOnClickListener(view -> finish());
    }

    private boolean tratarCliqueMenu(@NonNull MenuItem itemMenu) {
        if (itemMenu.getItemId() == R.id.menu_cadastrar) {
            iniciarFragment(CadastrarFragment.newInstance(), R.string.cadastrar);
            return true;
        }
        if (itemMenu.getItemId() == R.id.menu_listar) {
            iniciarFragment(ListarFragment.newInstance(), R.string.listar);
            return true;
        }
        return false;
    }

    private void configurarBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> tratarCliqueMenu(item));
    }

    private void atualizarTitulo(int titulo) {
        if (textTitulo != null) {
            textTitulo.setText(titulo);
        }
    }
    private void ouvinteCliqueMenuHome() {
        botaoHome.setOnClickListener(view -> {
            iniciarFragment(HomeFragment.newInstance(), R.string.provedor_aluno);
        });
    }

}
