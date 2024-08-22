package com.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_AMOUNT = "amount";

    public ExpenseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_AMOUNT + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY, expense.getCategory());
            values.put(COLUMN_DESCRIPTION, expense.getDescription());
            values.put(COLUMN_AMOUNT, expense.getAmount());
            db.insert(TABLE_EXPENSES, null, values);
        } catch (Exception e) {
            // Handle exception
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public void deleteExpense(int id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.delete(TABLE_EXPENSES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            // Handle exception
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public Expense getExpenseById(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_EXPENSES, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
                int categoryIndex = cursor.getColumnIndexOrThrow(COLUMN_CATEGORY);
                int descriptionIndex = cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION);
                int amountIndex = cursor.getColumnIndexOrThrow(COLUMN_AMOUNT);

                return new Expense(
                        cursor.getInt(idIndex),
                        cursor.getString(categoryIndex),
                        cursor.getString(descriptionIndex),
                        cursor.getDouble(amountIndex)
                );
            } else {
                // Log or handle the case when no expense is found
                Log.d("ExpenseDatabaseHelper", "Expense not found for ID: " + id);
            }
        } catch (Exception e) {
            // Log the exception
            Log.e("ExpenseDatabaseHelper", "Error getting expense by ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return null;
    }


    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPENSES, null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Expense expense = new Expense(
                            cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                            cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT))
                    );
                    expenses.add(expense);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return expenses;
    }

    public void updateExpense(Expense expense) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY, expense.getCategory());
            values.put(COLUMN_DESCRIPTION, expense.getDescription());
            values.put(COLUMN_AMOUNT, expense.getAmount());

            db.update(TABLE_EXPENSES, values, COLUMN_ID + "=?", new String[]{String.valueOf(expense.getId())});
        } catch (Exception e) {
            // Handle exception
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
}
