package com.example.rannaghor;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public class SearchFragment extends Fragment {
    //======================== DECLARING OBJECTS AND VARIABLES =========================
    SearchView svSEARCHVIEW;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search,container,false);
        // ==================== SET COLOR FOR STATUS BAR ==============================
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(this.getActivity(),R.color.white));

        // ====================== ATTACH COMPONENTS ===========================
        svSEARCHVIEW=(androidx.appcompat.widget.SearchView) view.findViewById(R.id.sv_search);

        //======================= SET HINT AND TEXT COLOR =====================
        return view;
    }
}
