package com.dom5230.utility.walletmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    SharedPreferences sharedPreferences;

    // UI Components
    FloatingActionButton fab;
    ListView lvLastFiveTransactions;
    TextView SpendingsForToday;
    TextView averageSpendings;
    PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = getActivity().getSharedPreferences("com.dom5230.utility.walletmanager", Context.MODE_PRIVATE);
        Boolean checkFirstRun = sharedPreferences.getBoolean("FirstRun", true);
        Log.i("First Run:",String.valueOf(checkFirstRun));

//      Objects
        helper = MySqliteTaskHelper.getInstance(getContext());
        items = new ArrayList<>();

//      UI Components
        averageSpendings = view.findViewById(R.id.tvAverageSpending);
        fab = view.findViewById(R.id.FAB);
        SpendingsForToday = view.findViewById(R.id.SpendingsForToday);
        lvLastFiveTransactions = view.findViewById(R.id.LVLastFiveTransacctions);
        pieChart = view.findViewById(R.id.piechart);

//      Executed when the app runs for the first time
        if(checkFirstRun){
            firstRun();
            helper.insertCategories(getContext());
        }

//      Setting Up the ui components
        updateUIData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseAlertBox();
            }
        });

        populateListView();

        return view;
    }

    public void firstRun(){
        sharedPreferences = getActivity().getSharedPreferences("com.dom5230.utility.walletmanager", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("FirstRun",false).commit();
    }

    public void expenseAlertBox(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.expense_input_dailogue,null);


        final EditText ETAmount = mview.findViewById(R.id.ETAmount);
        final Spinner spinner = mview.findViewById(R.id.SpinnerCategory);
        final EditText ETDescription = mview.findViewById(R.id.etDescription);

        ArrayList<String> categoriesList = helper.getCategoriesList(getContext());
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoriesList);
        spinner.setAdapter(adapter);

        mBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                helper.insertRecord(getContext(), ETAmount.getText().toString(),spinner.getSelectedItem().toString(), ETDescription.getText().toString());
                populateListView();
                updateUIData();
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
        recentTransactionAdapter recentTransactionAdapter = new recentTransactionAdapter(getActivity(), items);
        lvLastFiveTransactions.setAdapter(recentTransactionAdapter);
//        historyAdapter = new TransactionHistoryAdapter(getActivity(), items);
//        lvLastFiveTransactions.setAdapter(historyAdapter);
        if(!recentTransactionAdapter.isEmpty()){
            lvLastFiveTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    transactionClickListener();
                }
            });
        }
    }

    public void transactionClickListener(){
        final android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new TransactionHistory(), "Transaction History");
        ft.addToBackStack(null);
        ft.commit();
    }

    private  void updateUIData(){
        setPieChart();
        updateTodaysExpense();
        updateAverageSalary();
    }

    public void updateTodaysExpense(){
        float expsesForToday = 0;
        float dbValue = helper.getExpensesForToday(getContext());
        if(dbValue != 0){
            expsesForToday = dbValue;
        }
        SpendingsForToday.setText(getResources().getString(R.string.dollar)+" "+expsesForToday);
    }

    public void updateAverageSalary(){
        float average = 0;
        float total = 0;
        ArrayList<TransactionRecord> records = helper.getRowsAsArrayListObjects(getContext());
        for(int i = 0; i < records.size(); i++){
            TransactionRecord record = records.get(i);
            total = total + Float.valueOf(record.getAmouont());
        }

        if(total != 0) {
            average = total / records.size();
        }

        averageSpendings.setText(getResources().getString(R.string.dollar)+" "+average);
    }

    public void setPieChart(){
        MyPieChart pie = new MyPieChart(getContext());

        ArrayList<PieEntry> yvalues = pie.preparePieData();

        if(yvalues.size() != 0) {
            Log.i("YValues", String.valueOf(yvalues.size()));
            PieDataSet dataSet = new PieDataSet(yvalues, "Category");
            dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

            PieData data = new PieData(dataSet);
            data.setDrawValues(false);

            Description description = new Description();
            description.setText("Categories");
            pieChart.setDescription(description);

            pieChart.setEntryLabelColor(Color.BLUE);
            pieChart.setEntryLabelTextSize(10);
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
    }

    public void setPieCenterText(Entry e){
        PieEntry pieEntry = (PieEntry)e;
        pieChart.setCenterText(pieEntry.getLabel()+": "+getResources().getString(R.string.dollar)+" "+pieEntry.getValue());
        pieChart.setCenterTextSize(15f);
        pieChart.setCenterTextColor(Color.BLUE);
    }
}
