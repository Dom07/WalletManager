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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Home extends Fragment {

    // Objects
    MySqliteTaskHelper helper;
   ArrayList<TransactionRecord> items;
    SharedPreferences sharedPreferences;

    // UI Components
    FloatingActionButton fab;
    ListView lvLastFiveTransactions;
    TextView SpendingsForToday;
    TextView averageSpendings;
    PieChart pieChart;
    Spinner spinnerGraphCategory;
    String currency  = MainActivity.getCurrency();
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = getActivity().getSharedPreferences("com.dom5230.utility.walletmanager", Context.MODE_PRIVATE);
        Boolean checkFirstRun = sharedPreferences.getBoolean("FirstRun", true);

//      Objects
        helper = MySqliteTaskHelper.getInstance(getContext());
        items = new ArrayList<>();

//      UI Components
        averageSpendings = view.findViewById(R.id.tvAverageSpending);
        fab = view.findViewById(R.id.FAB);
        SpendingsForToday = view.findViewById(R.id.SpendingsForToday);
        lvLastFiveTransactions = view.findViewById(R.id.LVLastFiveTransacctions);
        pieChart = view.findViewById(R.id.piechart);
        spinnerGraphCategory = view.findViewById(R.id.spinnerGraphCategory);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.GraphCategory, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGraphCategory.setAdapter(adapter);

        spinnerGraphCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String timeLine = spinnerGraphCategory.getSelectedItem().toString();
                pieChart.clear();
                setPieChart(timeLine);
                pieChart.notifyDataSetChanged();
                pieChart.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        MainActivity.setCurrency();
    }

    public void expenseAlertBox(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mview = getLayoutInflater().inflate(R.layout.expense_input_dailogue,null);

        final EditText ETAmount = mview.findViewById(R.id.ETAmount);
        ETAmount.setHint(currency+"0.00");
        final Spinner spinner = mview.findViewById(R.id.SpinnerCategory);
        final EditText ETDescription = mview.findViewById(R.id.etDescription);
        TextView tvDescriptionCounter = mview.findViewById(R.id.tvDescriptionCount);


        updateDescriptionCount(ETDescription, tvDescriptionCounter);

        ArrayList<String> categoriesList = helper.getCategoriesList(getContext());
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoriesList);
        spinner.setAdapter(adapter);

        mBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                float floatAmount;
                String amount = String.valueOf(ETAmount.getText());

                if(amount.equals("")){
                    floatAmount = 0;
                }else{
                    floatAmount = Float.valueOf(amount);
                }

                if(floatAmount == 0){
                    Toast.makeText(getContext(), "Please enter a valid amount.", Toast.LENGTH_SHORT).show();
                }else{
                    helper.insertRecord(getContext(), ETAmount.getText().toString(),spinner.getSelectedItem().toString(), ETDescription.getText().toString());
                }
                populateListView();
                updateUIData();
            }
        });

        mBuilder.setNegativeButton("Cancel", null);
        mBuilder.setView(mview);
        mBuilder.setTitle("Enter Details");
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    public void populateListView(){
        items = helper.getRecentTwoRows(getContext());
        recentTransactionAdapter recentTransactionAdapter = new recentTransactionAdapter(getActivity(), items);
        lvLastFiveTransactions.setAdapter(recentTransactionAdapter);
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
        highlightTransactionHistoryItemInBottomMenu();
    }

    private  void updateUIData(){
        setPieChart("Today");
        updateTodaysExpense();
        updateAverageSalary();
    }

    public void updateTodaysExpense(){
        float expsesForToday = 0;
        float dbValue = helper.getExpensesForToday(getContext());
        if(dbValue != 0){
            expsesForToday = dbValue;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        expsesForToday = Float.parseFloat(df.format(expsesForToday));
        SpendingsForToday.setText(currency+" "+expsesForToday);
    }

    public void updateAverageSalary(){
        double average = 0;
        double total = 0;
        ArrayList<TransactionRecord> records = helper.getRowsAsArrayListObjects(getContext());
        for(int i = 0; i < records.size(); i++){
            TransactionRecord record = records.get(i);
            total = total + Double.valueOf(record.getAmouont());
        }

        if(total != 0) {
            average = total / records.size();
            DecimalFormat df = new DecimalFormat("0.00");
            average = Double.parseDouble(df.format(average));
        }
        averageSpendings.setText(currency+" "+average);
    }

    public void setPieChart(String timeLine){
        MyPieChart pie = new MyPieChart(getContext());

        ArrayList<PieEntry> yvalues = pie.preparePieData(timeLine);

        if(yvalues.size() != 0) {
            PieDataSet dataSet = new PieDataSet(yvalues, "Category");
            dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

            PieData data = new PieData(dataSet);
            data.setDrawValues(false);

            Description description = new Description();
            description.setText("");
            pieChart.setDescription(description);

            pieChart.setEntryLabelColor(Color.BLUE);
            pieChart.setEntryLabelTextSize(10);
            pieChart.setData(data);

            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    Float getValue = e.getY();
                    getValue = Float.valueOf(df.format(getValue));
                    e.setY(getValue);
                    setPieCenterText(e);
                }

                @Override
                public void onNothingSelected() {
                    pieChart.setCenterText(" ");
                }
            });
        }
    }

    public void setPieCenterText(Entry e){
        PieEntry pieEntry = (PieEntry)e;
        pieChart.setCenterText(pieEntry.getLabel()+":"+currency+""+pieEntry.getValue());
        pieChart.setCenterTextSize(15f);
        pieChart.setCenterTextColor(Color.BLUE);
    }

    public void updateDescriptionCount(EditText editText, final TextView textView){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textView.setText(String.valueOf(charSequence.length())+"/30");
                if(charSequence.length() == 30){
                    Toast.makeText(getContext(), "You cannot enter anymore characters.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        editText.addTextChangedListener(textWatcher);
    }

    public void highlightTransactionHistoryItemInBottomMenu(){
        ImageView ivTransactionHistory = getActivity().findViewById(R.id.ivRecentTransaction);
        ImageView ivHome = getActivity().findViewById(R.id.ivHome);
        ivTransactionHistory.setImageResource(R.drawable.ic_history_highlighted_24dp);
        ivHome.setImageResource(R.drawable.ic_home_black_24dp);
    }
}
