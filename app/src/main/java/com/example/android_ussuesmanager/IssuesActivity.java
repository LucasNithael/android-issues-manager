package com.example.android_ussuesmanager;

import android.os.Bundle;
import android.util.Log;
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
