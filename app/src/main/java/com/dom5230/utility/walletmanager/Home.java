package com.dom5230.utility.walletmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Home extends Fragment {

    // Objects
    MySqliteTaskHelper helper;
    TransactionHistoryAdapter historyAdapter;
    ArrayList<TransactionRecord> items;

    // UI Components
    FloatingActionButton fab;
    ListView lvLastFiveTransactions;
    TextView SpendingsForToday;
    PieChart pieChart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//      Objects
        helper = MySqliteTaskHelper.getInstance(getContext());
        items = new ArrayList<>();

//      UI Components
        fab = view.findViewById(R.id.FAB);
        SpendingsForToday = view.findViewById(R.id.SpendingsForToday);
        lvLastFiveTransactions = view.findViewById(R.id.LVLastFiveTransacctions);
        pieChart = view.findViewById(R.id.piechart);

//      Setting Pie Chart And Today's Expense Value
        setPieChart();
        updateTodaysExpense();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseAlertBox();
            }
        });

        populateListView();

        return view;
    }

    public void updateTodaysExpense(){
        SpendingsForToday.setText("₹ "+String.valueOf(helper.getExpensesForToday(getContext())));
    }

    public void expenseAlertBox(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.expense_input_dailogue,null);

        final EditText ETAmount = mview.findViewById(R.id.ETAmount);
        final Spinner spinner = mview.findViewById(R.id.SpinnerCategory);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                helper.insertRow(getContext(), ETAmount.getText().toString(),spinner.getSelectedItem().toString());
                populateListView();
                updateTodaysExpense();
            }
        });

        mBuilder.setNegativeButton("Cancel", null);
        mBuilder.setView(mview);
        mBuilder.setTitle("Expenditure Details");
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    public void populateListView(){
        items = helper.getRecentTwoRows(getContext());
        historyAdapter = new TransactionHistoryAdapter(getActivity(), items);
        lvLastFiveTransactions.setAdapter(historyAdapter);
    }

    public void setPieChart(){
        ArrayList<PieEntry> yvalues = new ArrayList<>();
        yvalues.add(new PieEntry(8f, "Food"));
        yvalues.add(new PieEntry(15f, "Bills"));
        yvalues.add(new PieEntry(12f, "Shopping"));
        yvalues.add(new PieEntry(25f, "Entertainment"));
        yvalues.add(new PieEntry(23f, "Travel"));

        PieDataSet dataSet = new PieDataSet(yvalues, "Category");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);

        Description description = new Description();
        description.setText("Categories");
        pieChart.setDescription(description);

        pieChart.setEntryLabelColor(Color.BLUE);
        pieChart.setData(data);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                setPieCenterText(e);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void setPieCenterText(Entry e){
        PieEntry pieEntry = (PieEntry)e;
        pieChart.setCenterText(pieEntry.getLabel()+": ₹"+(int)pieEntry.getValue());
        pieChart.setCenterTextSize(15f);
        pieChart.setCenterTextColor(Color.BLUE);
    }
}
