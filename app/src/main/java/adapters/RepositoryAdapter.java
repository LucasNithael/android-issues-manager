package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ussuesmanager.R;

import models.Repositorio;
import models.Repository;

import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {

    private List<Repositorio> repositorios;

    public RepositoryAdapter(List<Repositorio> repositorios) {
        this.repositorios = repositorios;
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
