package com.expensetracker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private ArrayList<Expense> expenseList;
    private Context context;

    // Constructor with context
    public ExpenseAdapter(Context context, ArrayList<Expense> expenseList){
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.expenseNameTextView.setText(expense.getCategory());
        holder.expenseAmountTextView.setText(String.format("$%.2f", expense.getAmount()));
        holder.expenseDescriptionTextView.setText(expense.getDescription());

        holder.btnEdit.setOnClickListener(v -> {
            if (context != null) {
                Intent intent = new Intent(context, EditExpenseActivity.class);
                intent.putExtra("EXPENSE_ID", expense.getId()); // Pass the expense ID
                context.startActivity(intent);
            } else {
                Log.e("ExpenseAdapter", "Context is null");
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }



    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseNameTextView;
        TextView expenseAmountTextView;
        TextView expenseDescriptionTextView;
        Button btnEdit;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseNameTextView = itemView.findViewById(R.id.expenseName);
            expenseAmountTextView = itemView.findViewById(R.id.expenseAmount);
            expenseDescriptionTextView = itemView.findViewById(R.id.expenseDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
