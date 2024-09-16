// IssuesAdapter.java
package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_ussuesmanager.R;

import java.util.List;

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.IssuesViewHolder> {

    private List<String> issues;

    public IssuesAdapter(List<String> issues) {
        this.issues = issues;
    }

    @NonNull
    @Override
    public IssuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_issue, parent, false);
        return new IssuesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssuesViewHolder holder, int position) {
        String issue = issues.get(position);
        holder.tvIssueTitle.setText(issue);
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public static class IssuesViewHolder extends RecyclerView.ViewHolder {
        TextView tvIssueTitle;

        public IssuesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIssueTitle = itemView.findViewById(R.id.tvIssueTitle);
        }
    }
}
