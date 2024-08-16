package com.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.expensetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewExpenses;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> expenseList;
    private ExpenseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewExpenses = findViewById(R.id.listViewExpenses);
        expenseList = new ArrayList<>();

        dbHelper = new ExpenseDatabaseHelper(this);
        loadExpensesFromDatabase();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenseList);
        listViewExpenses.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });
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
        for (Expense expense : expenses) {
            expenseList.add(expense.getCategory() + " - " + expense.getAmount() + " - " + expense.getDescription());
        }
    }
}

