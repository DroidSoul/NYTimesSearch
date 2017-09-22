package com.droidsoul.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.droidsoul.nytimessearch.R;
import com.droidsoul.nytimessearch.models.Query;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.lang.invoke.MethodType;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.parceler.Parcels.unwrap;

/**
 * Created by bear&bear on 9/17/2017.
 */
public class FilterFragment extends DialogFragment implements AdapterView.OnItemSelectedListener, DatePickerFragment.DateListener {

    onFragmentResult activityCommander;
    Query query;
    ImageButton ibtnStartDate;
    TextView tvStartDate;
    Spinner spinner;
    CheckBox cbArts;
    CheckBox cbFashion;
    CheckBox cbSports;
    Button btnSave;
    String sortOrder;
    String beginDate;
    String newsdeskFilter;
    String[] choices = {"oldest", "newest"};
    // get user selected value from spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sortOrder = choices[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDateSet(Calendar c, String tag) {
        if (tag.equals("begin_date")) {
            beginDate = convertDate(c);
            tvStartDate.setText(beginDate);
        }
    }

    public String convertDate(Calendar date) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(date.getTime());
        return formatted;
    }

    public interface onFragmentResult {
        void returnData(Query query);
    }

    public FilterFragment() {

    }

    public static FilterFragment newInstance(Query query) {
        FilterFragment frag = new FilterFragment();
        Bundle args = new Bundle();
        args.putParcelable("query", Parcels.wrap(query));
        frag.setArguments(args);
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ibtnStartDate = view.findViewById(R.id.ibtnStartDate);
        tvStartDate = view.findViewById(R.id.tvStartDate);
        spinner = view.findViewById(R.id.spinner);
        cbArts = view.findViewById(R.id.cbArts);
        cbFashion = view.findViewById(R.id.cbFashion);
        cbSports = view.findViewById(R.id.cbSports);
        btnSave = view.findViewById(R.id.btnSave);
        query = unwrap(getArguments().getParcelable("query"));
        sortOrder = query.getSortOrder();
        beginDate = query.getBeginDate();
        newsdeskFilter = query.getNewsdeskFilter();

        if (beginDate != null) {
            tvStartDate.setText(beginDate);
        }
        ibtnStartDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment df = new DatePickerFragment();
                df.setTargetFragment(FilterFragment.this, 700);
                Bundle args = new Bundle();
                args.putString("begin_date", beginDate);
                df.setArguments(args);
                df.show(fm, "begin_date");
            }
        });
        setSpinner();
        setCheckBoxer();
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                activityCommander = (onFragmentResult) getActivity();
                query = new Query(query.getQueryStr(), 0, sortOrder, newsdeskFilter, beginDate);
                activityCommander.returnData(query);
                dismiss();
            }
        });
    }
    public void setSpinner() {
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,choices);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        if (sortOrder != null) {
            if (sortOrder.equals("oldest")) {
                spinner.setSelection(0);
            }
            else {
                spinner.setSelection(1);
            }
        }
    }

    public void setCheckBoxer() {

        if (newsdeskFilter != null) {
            if (newsdeskFilter.contains("Arts")) {
                cbArts.setChecked(true);
            }
            if (newsdeskFilter.contains("Fashion")) {
                cbFashion.setChecked(true);
            }
            if (newsdeskFilter.contains("Sports")) {
                cbSports.setChecked(true);
            }
        }

        newsdeskFilter = null;
        boolean isArtsChecked = cbArts.isChecked();
        boolean isFashionChecked = cbFashion.isChecked();
        boolean isSportsChecked = cbSports.isChecked();
        if (isArtsChecked || isFashionChecked || isSportsChecked) {
            newsdeskFilter = "";
            boolean isOneItem = false;
            if (isArtsChecked) {
                newsdeskFilter += " Arts";
            }
            if (isFashionChecked) {
                newsdeskFilter += " Fashion%20%26%20Style";
            }
            if (isSportsChecked) {
                newsdeskFilter += " Sports";
            }
            query.setNewsdeskFilter(newsdeskFilter);
        }

        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean checked) {
                // compoundButton is the checkbox
                // boolean is whether or not checkbox is checked
                // Check which checkbox was clicked
                newsdeskFilter = null;
                boolean isArtsChecked = cbArts.isChecked();
                boolean isFashionChecked = cbFashion.isChecked();
                boolean isSportsChecked = cbSports.isChecked();
                boolean isOneItem = false;
                if (isArtsChecked) {
                    newsdeskFilter += " Arts";
                }
                if (isFashionChecked) {
                    newsdeskFilter += " Fashion%20%26%20Style";
                }
                if (isSportsChecked) {
                    newsdeskFilter += " Sports";
                }
                query.setNewsdeskFilter(newsdeskFilter);
                switch(view.getId()) {
                    case R.id.cbArts:
                        if (checked) {
                            // Put some meat on the sandwich
                        } else {
                            // Remove the meat
                        }
                        break;
                    case R.id.cbFashion:
                        if (checked) {
                            // Cheese me
                        } else {
                            // I'm lactose intolerant
                        }
                        break;
                    case R.id.cbSports:
                        if (checked) {

                        } else {

                        }
                        break;
                }
            }
        };
        cbArts.setOnCheckedChangeListener(checkListener);
        cbFashion.setOnCheckedChangeListener(checkListener);
        cbSports.setOnCheckedChangeListener(checkListener);
    }



}
