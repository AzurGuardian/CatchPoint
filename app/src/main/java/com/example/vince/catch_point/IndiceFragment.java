package com.example.vince.catch_point;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class IndiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAMPOINT = "paramPoint";
    private static final String ARG_PARAMSCORE = "paramScore";
    // TODO: Rename and change types of parameters
    private Point[] points;
    private int noPoint = 1;
    private int noInd = 0;
    Score score;
    LinearLayout llO;
    LinearLayout llF;

    private OnFragmentInteractionListener mListener;





    public IndiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment IndiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndiceFragment newInstance(Point[] param1,Score score) {

        IndiceFragment fragment = new IndiceFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(ARG_PARAMPOINT, param1);
        Log.e("TabPointFrag",""+param1.length);
        args.putParcelable(ARG_PARAMSCORE,score);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            points = (Point[])getArguments().getParcelableArray(ARG_PARAMPOINT);
            score = (Score)getArguments().getParcelable(ARG_PARAMSCORE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_indice, container, false);
        llO = (LinearLayout) view.findViewById(R.id.llO);
        llF = (LinearLayout) view.findViewById(R.id.llF);
        affIndice();
        Button validIndice = (Button) view.findViewById(R.id.validIndice);

        validIndice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Êtes-vous sûr de vouloir récuperer un indice supplémentaire ?\nVous perdrez du score si vous acceptez.")
                        .setTitle("Indice supplémentaire")
                        .setCancelable(true)
                        .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //faire manip score + indice
                                affIndiceF();

                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                AlertDialog dialog = builder.create();

                dialog.show();

            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void nextPoint(){
        llF.removeAllViews();
        llO.removeAllViews();
        noPoint++;
        affIndice();
        noInd = 0;
    }

    public void affIndice(){
        String indices = "";
        Log.e("PointInd", "affIndice: "+noPoint );
        if (noPoint < points.length && points[noPoint].getIndicesO() != null){
            for (Indice indice:
                    points[noPoint].getIndicesO()) {
                        if(indice.getType().equals("file")){
                            ImageView indOiv = new ImageView(this.getContext());
                            indOiv.setImageBitmap(indice.getBmImg());
                            llO.addView(indOiv);
                        }else if(indice.getType().equals("text")){
                            TextView indOtv = new TextView(this.getContext());
                            indOtv.setText(indice.getTextIndice());
                            llO.addView(indOtv);
                        }
            }

        }
    }

    public void affIndiceF(){
        ArrayList<Indice> arrayInd = points[noPoint].getIndicesF() ;
        if(noInd < arrayInd.size()){
            Indice indF = arrayInd.get(noInd);
            if( indF.getType().equals("file")){
                ImageView indFiv = new ImageView(this.getContext());
                indFiv.setImageBitmap(indF.getBmImg());
                llF.addView(indFiv);
            }else if (indF.getType().equals("text")){
                TextView indFtv = new TextView(this.getContext());
                indFtv.setText(indF.getTextIndice());
                llF.addView(indFtv);
            }
            noInd++;
            score.updateScore(-100);
        }else {
            Toast.makeText(this.getActivity(),"Vous avez déjà tout les indices pour ce point",Toast.LENGTH_SHORT).show();
        }

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }







}
