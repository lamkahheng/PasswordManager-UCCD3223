package my.edu.utar.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.MyViewHolder> {
  private List<PasswordModel> passwordList;
  private Context context;

  public PasswordAdapter(List<PasswordModel> passwordList, Context context) {
    this.passwordList = passwordList;
    this.context = context;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.password_raw_item, parent, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    PasswordModel currentItem = passwordList.get(position);

    holder.tvRowSite.setText(currentItem.getSiteName());
    holder.tvRowUser.setText(currentItem.getUsername());

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int currentPos = holder.getBindingAdapterPosition();

        if (currentPos != RecyclerView.NO_POSITION) {
          PasswordModel currentItem = passwordList.get(currentPos);

          Intent intent = new Intent(context, AddEditActivity.class);

          intent.putExtra("ID", currentItem.getId());
          intent.putExtra("SITE", currentItem.getSiteName());
          intent.putExtra("USER", currentItem.getUsername());
          intent.putExtra("PASS", currentItem.getPassword());
          intent.putExtra("NOTES", currentItem.getNotes());

          context.startActivity(intent);
        }
      }
    });

    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        int currentPos = holder.getBindingAdapterPosition();

        if (currentPos != RecyclerView.NO_POSITION) {
          PasswordModel currentItem = passwordList.get(currentPos);

          AlertDialog.Builder builder = new AlertDialog.Builder(context);
          builder.setTitle("Delete Password");
          builder.setMessage("Are you sure you want to delete " + currentItem.getSiteName() + "?");

          builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              DatabaseHelper dbHelper = new DatabaseHelper(context);
              boolean success = dbHelper.deleteOne(currentItem.getId());

              if (success) {
                Toast.makeText(context, "Password deleted successfully.", Toast.LENGTH_SHORT).show();

                passwordList.remove(currentPos);
                notifyItemRemoved(currentPos);
                notifyItemRangeChanged(currentPos, passwordList.size());
              }
            }
          });

          builder.setNegativeButton("No", ((dialog, which) -> dialog.dismiss()));
          builder.show();
        }
        return true;
      }
    });
  }

  @Override
  public int getItemCount() {
    return passwordList.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tvRowSite, tvRowUser;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      tvRowSite = itemView.findViewById(R.id.tvRowSite);
      tvRowUser = itemView.findViewById(R.id.tvRowUser);
    }
  }
}
