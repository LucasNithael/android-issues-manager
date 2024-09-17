package com.example.android_ussuesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import adapters.IssuesAdapter;
import services.GitHubService;

public class IssuesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IssuesAdapter issuesAdapter;
    private List<String> issuesLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

        recyclerView = findViewById(R.id.recyclerViewIssues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adicionando o botão para cadastrar uma nova issue
        Button btnAddIssue = findViewById(R.id.btnAddIssue);
        btnAddIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia a Activity de cadastro de issue
                Intent intent = new Intent(IssuesActivity.this, CadastrarIssueActivity.class);
                intent.putExtra("repoName", getIntent().getStringExtra("repoName")); // Passa o nome do repositório
                startActivity(intent);
            }
        });

        String repoName = getIntent().getStringExtra("repoName");

        // Obtendo as issues do repositório
        new GitHubService().obterIssues(repoName, new GitHubService.IssuesCallback() {
            @Override
            public void onSuccess(List<String> issues) {
                issuesLista = issues;
                Log.d("IssuesActivity", "Número de issues obtidas: " + issuesLista.size());
                issuesAdapter = new IssuesAdapter(issuesLista);
                recyclerView.setAdapter(issuesAdapter);
            }

            @Override
            public void onError(String error) {
                Log.d("IssuesActivity", "Erro ao obter issues: " + error);
            }
        });
    }
}
