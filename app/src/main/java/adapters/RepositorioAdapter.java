package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ussuesmanager.IssuesActivity;
import com.example.android_ussuesmanager.R;

import java.util.List;

import models.Repositorio;

public class RepositorioAdapter extends RecyclerView.Adapter<RepositorioAdapter.RepositoryViewHolder> {

    private List<Repositorio> repositorios;
    private Context context;

    public RepositorioAdapter(List<Repositorio> repositorios, Context context) {
        this.repositorios = repositorios;
        this.context = context;
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repository, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder holder, int position) {
        Repositorio repository = repositorios.get(position);
        holder.tvRepoName.setText(repository.getNome());
        holder.tvRepoDescription.setText(repository.getDescricao());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, IssuesActivity.class);
            intent.putExtra("repoName", repository.getNome());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return repositorios.size();
    }

    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvRepoName;
        TextView tvRepoDescription;

        public RepositoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRepoName = itemView.findViewById(R.id.tvRepoName);
            tvRepoDescription = itemView.findViewById(R.id.tvRepoDescription);
        }
    }
}
