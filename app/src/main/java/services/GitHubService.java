package services;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import models.Repositorio;

public class GitHubService {

    private static final String GITHUB_TOKEN = "";
    private static final String BASE_URL = "https://api.github.com/users/lucasnithael";

    public interface RepositoryCallback {
        void onSuccess(List<Repositorio> repositorios);
        void onError(String error);
    }

    public interface IssuesCallback {
        void onSuccess(List<String> issues);
        void onError(String error);
    }

    public void obterRepositorios(RepositoryCallback callback) {
        new AsyncTask<Void, Void, List<Repositorio>>() {
            @Override
            protected List<Repositorio> doInBackground(Void... voids) {
                List<Repositorio> repositorios = new ArrayList<>();
                try {
                    URL url = new URL(BASE_URL + "/repos");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    conn.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);

                    Log.d("GitHubService", "Enviando requisição para: " + url.toString());

                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Erro: HTTP código " + conn.getResponseCode());
                    }

                    // Lendo a resposta da API
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        output.append(line);
                    }
                    conn.disconnect();

                    Log.d("GitHubService", "Resposta da API: " + output.toString());

                    // Convertendo a resposta para JSON
                    JSONArray jsonArray = new JSONArray(output.toString());

                    // Iterando sobre os repositórios
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject repoJson = jsonArray.getJSONObject(i);
                        String nome = repoJson.getString("name");
                        String descricao = repoJson.isNull("description") ? "Sem descrição" : repoJson.getString("description");

                        // Adicionando o repositório à lista
                        repositorios.add(new Repositorio(nome, descricao));
                    }
                } catch (Exception e) {
                    Log.e("GitHubService", "Erro ao obter repositórios", e);
                    return null;
                }
                return repositorios;
            }

            @Override
            protected void onPostExecute(List<Repositorio> repositorios) {
                if (repositorios != null) {
                    callback.onSuccess(repositorios);
                } else {
                    callback.onError("Erro ao obter repositórios");
                }
            }
        }.execute();
    }

    public void obterIssues(String repoName, IssuesCallback callback) {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<String> issues = new ArrayList<>();
                try {
                    URL url = new URL(BASE_URL + "/repos/" + repoName + "/issues");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
                    conn.setRequestProperty("Authorization", "token " + GITHUB_TOKEN);

                    Log.d("GitHubService", "Enviando requisição para: " + url.toString());

                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Erro: HTTP código " + conn.getResponseCode());
                    }

                    // Lendo a resposta da API
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        output.append(line);
                    }
                    conn.disconnect();

                    Log.d("GitHubService", "Resposta da API: " + output.toString());

                    // Convertendo a resposta para JSON
                    JSONArray jsonArray = new JSONArray(output.toString());

                    // Iterando sobre as issues
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject issueJson = jsonArray.getJSONObject(i);
                        String titulo = issueJson.getString("title");

                        // Adicionando a issue à lista
                        issues.add(titulo);
                    }
                } catch (Exception e) {
                    Log.e("GitHubService", "Erro ao obter issues", e);
                    return null;
                }
                return issues;
            }

            @Override
            protected void onPostExecute(List<String> issues) {
                if (issues != null) {
                    callback.onSuccess(issues);
                } else {
                    callback.onError("Erro ao obter issues");
                }
            }
        }.execute();
    }
}
