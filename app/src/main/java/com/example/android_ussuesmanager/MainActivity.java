package com.example.android_ussuesmanager;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import adapters.RepositorioAdapter;
import models.Repositorio;
import services.GitHubService;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RepositorioAdapter repositoryAdapter;
    private List<Repositorio> repositoriosLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewRepos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtendo os repositórios
        new GitHubService().obterRepositorios(new GitHubService.RepositoryCallback() {
            @Override
            public void onSuccess(List<Repositorio> repositorios) {
                repositoriosLista = repositorios;
                Log.d("MainActivity", "Número de repositórios obtidos: " + repositoriosLista.size());
                repositoryAdapter = new RepositorioAdapter(repositoriosLista, MainActivity.this);
                recyclerView.setAdapter(repositoryAdapter);
            }

            @Override
            public void onError(String error) {
                Log.d("MainActivity", "Erro ao obter repositórios: " + error);
            }
        });
    }
}