package com.example.android_ussuesmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import services.GitHubService;

public class CadastrarIssueActivity extends AppCompatActivity {

    private EditText etTituloIssue, etDescricaoIssue;
    private Button btnCriarIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_issue);

        etTituloIssue = findViewById(R.id.etTituloIssue);
        etDescricaoIssue = findViewById(R.id.etDescricaoIssue);
        btnCriarIssue = findViewById(R.id.btnCriarIssue);

        // Obtém o nome do repositório passado pela Intent
        String repoName = getIntent().getStringExtra("repoName");

        btnCriarIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = etTituloIssue.getText().toString();
                String descricao = etDescricaoIssue.getText().toString();

                // Validação dos campos
                if (titulo.isEmpty()) {
                    Toast.makeText(CadastrarIssueActivity.this, "O título é obrigatório", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (descricao.isEmpty()) {
                    Toast.makeText(CadastrarIssueActivity.this, "A descrição é obrigatória", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Criar a issue usando a API do GitHub
                new GitHubService().criarIssue(repoName, titulo, descricao, new GitHubService.IssueCreateCallback() {
                    @Override
                    public void onSuccess(String issueUrl) {
                        Toast.makeText(CadastrarIssueActivity.this, "Issue criada com sucesso: " + issueUrl, Toast.LENGTH_LONG).show();
                        finish(); // Fecha a activity após a criação
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(CadastrarIssueActivity.this, "Erro ao criar issue: " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
