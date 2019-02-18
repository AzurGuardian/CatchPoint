package com.example.vince.catch_point;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ParcoursMenuAdapter extends RecyclerView.Adapter<ParcoursMenuAdapter.ViewHolder> {
        public ParcoursMenuActivty pmActivity;
        private ArrayList<Parcours> mDataset;

        public SharedPreferences sharedPreferences;


    ///////////////////////////////////////////////////////////////////////////
        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // each data item is just a string in this case
        public TextView tvNom;
        public TextView tvDistance;
        public TextView tvDifficulte;

        public int idParcours;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            Log.e("ConsVH", "ViewHolder: "+ ((ConstraintLayout)v) );
            tvNom = v.findViewById(R.id.tvNom);
            tvDistance = v.findViewById(R.id.tvDistance);
            tvDifficulte = v.findViewById(R.id.tvDifficulty);
        }


        @Override
        public void onClick(View view) {
            Log.e("screugneugneu", "onClick: " );
            // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Souhaitez-vous lancer le parcours " + this.tvNom.getText())
                    .setTitle("Lancer Parcours")
                    .setCancelable(true)
                    .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.cancel();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            Log.e("idParcours", "onClick: "+idParcours );
                            editor.putInt("idParcours",idParcours );
                            editor.commit();

                            Intent parcoursActivity = new Intent(pmActivity.getApplicationContext(), ParcoursActivity.class);
                            pmActivity.finish();
                            pmActivity.startActivity(parcoursActivity);



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


//
    }
    //            }
        // Provide a suitable constructor (depends on the kind of dataset)

    //
    public ParcoursMenuAdapter(ArrayList<Parcours> myDataset, ParcoursMenuActivty pma, SharedPreferences sp) {
            pmActivity=pma;
            Log.e("ADAPTERPARCOURS", "ParcoursMenuAdapter: "+ myDataset.size() );
            mDataset = myDataset;
            sharedPreferences = sp;


        }
    //            public void display(){
        // Create new views (invoked by the layout manager)

    @Override
        public ParcoursMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.parcours_row_layout, parent, false);


            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }
        // Replace the contents of a view (invoked by the layout manager)

    @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element

            NumberFormat nf = new DecimalFormat("0.###");
            Log.e("viewHolder", "onBindViewHolder: "+ holder.toString() );
            holder.idParcours = mDataset.get(position).getId();
            holder.tvNom.setText(mDataset.get(position).getNom());
            holder.tvDistance.setText(nf.format(mDataset.get(position).getDistance())+" km");
            holder.tvDifficulte.setText(mDataset.get(position).getDifficulte());


        }
        // Return the size of your dataset (invoked by the layout manager)

    @Override
        public int getItemCount() {
            return mDataset.size();
        }



}

