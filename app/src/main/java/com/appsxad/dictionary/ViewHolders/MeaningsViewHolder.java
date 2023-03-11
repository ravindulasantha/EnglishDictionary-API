package com.appsxad.dictionary.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsxad.dictionary.R;

public class MeaningsViewHolder extends RecyclerView.ViewHolder {
    public TextView textVew_partOfSpeech;
    public RecyclerView recycler_definitions;
    public MeaningsViewHolder(@NonNull View itemView) {
        super(itemView);

        textVew_partOfSpeech = itemView.findViewById(R.id.textVew_partOfSpeech);
        recycler_definitions = itemView.findViewById(R.id.recycler_definitions);


    }

}
