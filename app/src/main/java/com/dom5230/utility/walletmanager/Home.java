package com.dom5230.utility.walletmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

public class Home extends Fragment {

    // Objects
    MySqliteTaskHelper helper;
    TransactionHistoryAdapter historyAdapter;
    ArrayList<TransactionRecord> items;

    // UI Components
    FloatingActionButton fab;
    ListView lvLastFiveTransactions;
    GraphView barGraph;
    TextView SpendingsForToday;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        helper = MySqliteTaskHelper.getInstance(getContext());
        items = new ArrayList<>();


        fab = view.findViewById(R.id.FAB);
        SpendingsForToday = view.findViewById(R.id.SpendingsForToday);
        lvLastFiveTransactions = view.findViewById(R.id.LVLastFiveTransacctions);
        barGraph = view.findViewById(R.id.BarGraph);

        setBarGraph();
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

    public void setBarGraph(){

        ArrayList<String> days = new ArrayList<>();
        days.add("");
        days.add("Mon");
        days.add("Tue");
        days.add("Wed");
        days.add("Thu");
        days.add("Fri");
        days.add("Sat");
        days.add("Sun");

        DataPoint[] dataPoints = new DataPoint[]{
                new DataPoint(0,0),
                new DataPoint(1, 500),
                new DataPoint(2, 200),
                new DataPoint(3, 300),
                new DataPoint(4, 250),
                new DataPoint(5, 100),
                new DataPoint(6, 400),
                new DataPoint(7, 320),
        };

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);
        barGraph.addSeries(series);
        series.setSpacing(50);

        int countofDays = dataPoints.length;
        String[] ActiveDataDays = new String[countofDays];
        for(int i = 0; i<countofDays ; i++){
            ActiveDataDays[i] = days.get(i);
        }

        StaticLabelsFormatter formatter = new StaticLabelsFormatter(barGraph);
        formatter.setHorizontalLabels(ActiveDataDays);
        barGraph.getGridLabelRenderer().setLabelFormatter(formatter);

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        series.setValuesOnTopSize(30);
    }
}
