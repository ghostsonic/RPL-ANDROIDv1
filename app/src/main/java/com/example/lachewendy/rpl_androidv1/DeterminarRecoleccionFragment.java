package com.example.lachewendy.rpl_androidv1;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeterminarRecoleccionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeterminarRecoleccionFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RelativeLayout relativeOpciones;
    RelativeLayout relativeEscaner;
    private Button btnServicio;
    private Button btnEscanear;
    private Button btnCancelar;
    private Button btnConfirmar;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeterminarRecoleccionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeterminarRecoleccionFragment newInstance(String param1, String param2) {
        DeterminarRecoleccionFragment fragment = new DeterminarRecoleccionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DeterminarRecoleccionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_determinar_recoleccion, container, false);

        relativeOpciones = (RelativeLayout) view.findViewById(R.id.relativeOpciones);
        relativeEscaner = (RelativeLayout) view.findViewById(R.id.relativeEscanear);
        btnServicio = (Button)view.findViewById(R.id.btnServicio);
        btnEscanear = (Button) view.findViewById(R.id.btnEscanear);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnConfirmar = (Button) view.findViewById(R.id.btnConfirmar);

        btnEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeOpciones.setVisibility(View.GONE);
                relativeEscaner.setVisibility(View.VISIBLE);
            }
        });

        btnServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeOpciones.setVisibility(View.VISIBLE);
                relativeEscaner.setVisibility(View.GONE);
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }


}
