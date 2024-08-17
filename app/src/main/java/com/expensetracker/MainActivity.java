package com.expensetracker;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewExpenses;
    private ExpenseAdapter adapter;
    private ArrayList<Expense> expenseList;
    private ExpenseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
        expenseList = new ArrayList<>();

        dbHelper = new ExpenseDatabaseHelper(this);
        loadExpensesFromDatabase();

        adapter = new ExpenseAdapter(expenseList);
        recyclerViewExpenses.setAdapter(adapter);
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));

        // Set up swipe-to-delete

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false; // We are not moving items, just swipe.
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteExpense(position); // Call deleteExpense to handle removal from the list and database
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                // Optionally, you can add a background color or icon during the swipe
            }
        };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewExpenses);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });
    }
    private void deleteExpense(int position) {
        Expense expense = expenseList.get(position);

        // Remove the item from the database
        dbHelper.deleteExpense(expense.getId());

        // Remove the item from the list
        expenseList.remove(position);

        // Notify the adapter about the removed item
        adapter.notifyItemRemoved(position);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadExpensesFromDatabase();
        adapter.notifyDataSetChanged();
    }

    private void loadExpensesFromDatabase() {
        expenseList.clear();
        List<Expense> expenses = dbHelper.getAllExpenses();
        expenseList.addAll(expenses);
    }
}
