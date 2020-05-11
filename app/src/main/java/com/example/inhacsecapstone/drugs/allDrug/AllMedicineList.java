package com.example.inhacsecapstone.drugs.allDrug;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inhacsecapstone.Entity.MedicineEntity;
import com.example.inhacsecapstone.Entity.TakesEntity;
import com.example.inhacsecapstone.R;
import com.example.inhacsecapstone.drugs.ViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllMedicineList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllMedicineList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "type";
    private ViewModel mViewModel;




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

        RecyclerView recyclerView = view.findViewById(R.id.allMedicineView);
        final AllDrugListAdapter adapter = new AllDrugListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mViewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

        mViewModel.getAllDrugs().observe(requireActivity(), new Observer<List<MedicineEntity>>() {
            @Override
            public void onChanged(@Nullable final List<MedicineEntity> drugs) {
                // Update the cached copy of the words in the adapter.
                adapter.setDrugs(drugs);
            }
        });

        mViewModel.getAllTakes().observe(requireActivity(), new Observer<List<TakesEntity>>() {
            @Override
            public void onChanged(List<TakesEntity> takes) {
                adapter.setTakes(takes );
            }
        });

        return view;
    }
}
