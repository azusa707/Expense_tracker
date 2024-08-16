package com.expensetracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.expensetracker.R;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etAmount, etCategory, etDescription;
    private ExpenseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etAmount = findViewById(R.id.etAmount);
        etCategory = findViewById(R.id.etCategory);
        etDescription = findViewById(R.id.etDescription);
        dbHelper = new ExpenseDatabaseHelper(this);

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString();
            String category = etCategory.getText().toString();
            String description = etDescription.getText().toString();

            if (amountStr.isEmpty() || category.isEmpty() || description.isEmpty()) {
                // Handle empty fields (e.g., show a toast or error message)
                Toast.makeText(AddExpenseActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                Expense expense = new Expense(0, category, description, amount);
                dbHelper.addExpense(expense);

                // Optionally show a confirmation message
                Toast.makeText(AddExpenseActivity.this, "Expense added", Toast.LENGTH_SHORT).show();

                finish();  // Go back to MainActivity
            } catch (NumberFormatException e) {
                // Handle invalid number format
                Toast.makeText(AddExpenseActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
