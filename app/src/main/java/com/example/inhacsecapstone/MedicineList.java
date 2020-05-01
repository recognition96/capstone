package com.example.inhacsecapstone;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicineList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicineList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "type";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public MedicineList() {
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
    public static MedicineList newInstance(String type) {
        MedicineList fragment = new MedicineList();
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


       if(true)
        {
            AllDrugListAdapter adapter;
            adapter = new AllDrugListAdapter() ;
            listview = (ListView) view.findViewById(R.id.listview);
            listview.setAdapter(adapter);


            adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.example1),
                    "약품", 3,1, "desc test", 1, 1, 1) ;
            // 두 번째 아이템 추가.
            adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.example3),
                    "테스트", 3,1, "desc test", 1, 1, 1) ;
        }
        else if(mParam1 == "DAYDRUG")
        {
            DayDrugListAdapter adapter;
            adapter = new DayDrugListAdapter() ;
            listview = (ListView) view.findViewById(R.id.listview);
            listview.setAdapter(adapter);

            adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.example1),
                    "Box", "Account Box Black 36dp") ;
            // 두 번째 아이템 추가.
            adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.example3),
                    "Circle", "Account Circle Black 36dp") ;
        }
        else if(mParam1 == "RECOGRESULT")
        {
            RecogResultListAdapter adapter;
            adapter = new RecogResultListAdapter() ;
            listview = (ListView) view.findViewById(R.id.listview);
            listview.setAdapter(adapter);

            adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.example1),
                    "Box", "Account Box Black 36dp") ;
            // 두 번째 아이템 추가.
            adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.example3),
                    "Circle", "Account Circle Black 36dp") ;
        }
        else
            System.out.println("type 파라미터가 잘못되었습니다.");
        return view;
    }
}
