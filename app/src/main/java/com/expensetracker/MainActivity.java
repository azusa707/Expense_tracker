package com.expensetracker;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewExpenses;
    private ExpenseAdapter adapter;
    private ArrayList<Expense> expenseList;
    private ExpenseDatabaseHelper dbHelper;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
        pieChart = findViewById(R.id.pieChart); // Initialize PieChart
        expenseList = new ArrayList<>();

        dbHelper = new ExpenseDatabaseHelper(this);
        loadExpensesFromDatabase();

        adapter = new ExpenseAdapter(expenseList);
        recyclerViewExpenses.setAdapter(adapter);
        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));

        updatePieChart(); // Set up PieChart with actual data

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

    private void updatePieChart() {
        List<PieEntry> entries = new ArrayList<>();

        // Calculate total expenses by category
        Map<String, Float> categoryTotals = new HashMap<>();
        for (Expense expense : expenseList) {
            String category = expense.getCategory();
            float amount = expense.getAmount();

            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0f) + amount);
        }

        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(dataSet);

        // Update PieChart
        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
    }

    private void loadExpensesFromDatabase() {
        expenseList.clear();
        List<Expense> expenses = dbHelper.getAllExpenses();
        expenseList.addAll(expenses);

        updatePieChart(); // Update PieChart with data
    }

    private void deleteExpense(int position) {
        Expense expense = expenseList.get(position);

        // Remove the item from the database
        dbHelper.deleteExpense(expense.getId());

        // Remove the item from the list
        expenseList.remove(position);

        // Notify the adapter about the removed item
        adapter.notifyItemRemoved(position);

        updatePieChart(); // Update PieChart after deletion
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpensesFromDatabase();
        adapter.notifyDataSetChanged();
        updatePieChart(); // Update PieChart when resuming
    }
}
