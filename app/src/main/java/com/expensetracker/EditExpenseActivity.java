package com.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditExpenseActivity extends AppCompatActivity {

    private EditText categoryEditText;
    private EditText descriptionEditText;
    private EditText amountEditText;
    private Button saveButton;
    private ExpenseDatabaseHelper dbHelper;
    private int expenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        dbHelper = new ExpenseDatabaseHelper(this);

        categoryEditText = findViewById(R.id.categoryEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        amountEditText = findViewById(R.id.amountEditText);
        saveButton = findViewById(R.id.saveButton);

        // Get the expense ID from the intent
        expenseId = getIntent().getIntExtra("EXPENSE_ID", -1);

        if (expenseId != -1) {
            // Load the expense details into the form
            Expense expense = dbHelper.getExpenseById(expenseId);
            if (expense != null) {
                categoryEditText.setText(expense.getCategory());
                descriptionEditText.setText(expense.getDescription());
                amountEditText.setText(String.valueOf(expense.getAmount()));
            }
        }

        saveButton.setOnClickListener(v -> {
            String category = categoryEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String amountStr = amountEditText.getText().toString();

            if (category.isEmpty() || description.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);

                // Update the expense
                Expense expense = new Expense(expenseId, category, description, amount);
                dbHelper.updateExpense(expense);

                // Return to the main activity
                setResult(RESULT_OK);
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
