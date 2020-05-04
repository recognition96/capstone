package com.example.inhacsecapstone;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllMedicineList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllMedicineList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "type";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public AllMedicineList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type "ALLDRUG" OR "DAYDRUG" OR "RECOGRESULT".
     * @return A new instance of fragment MedicineList.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance(String type) {
        AllMedicineList fragment = new AllMedicineList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medicine_list, container, false);
        ListView listview ;


            AllDrugListAdapter adapter;
            adapter = new AllDrugListAdapter() ;
            listview = (ListView) view.findViewById(R.id.listview);
            listview.setAdapter(adapter);

            ArrayList<String> date1 = new ArrayList<String>();
            date1.add("2018.10.30 12:10:13");
            date1.add("2013.10.30 19:10:13");
            //DrugItem item1 = new DrugItem(ContextCompat.getDrawable(getActivity(), R.drawable.example1), "약품", 3,"desc test",date1, 1, 1);

            //adapter.addItem(item1);

        return view;
    }
}
