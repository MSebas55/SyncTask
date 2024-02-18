package com.dam.proyectoandroid;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.proyectoandroid.Database.Constants;
import com.dam.proyectoandroid.Database.Interfaces.CRUDInterface;
import com.dam.proyectoandroid.Database.adapters.ProyectsAdapter;
import com.dam.proyectoandroid.Database.model.Proyecto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoricEventRVAdapter extends RecyclerView.Adapter<HistoricEventRVAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> eventNames;
    private ArrayList<String> eventLocations;
    private ArrayList<String> textColors;

    List<Proyecto> proyectos;
    CRUDInterface crudInterface;
    ListView listView;

    public HistoricEventRVAdapter(Context context, ArrayList<String> eventNames, ArrayList<String> eventLocations, ArrayList<String> textColors) {
        this.context = context;
        this.eventNames = eventNames;
        this.eventLocations = eventLocations;
        this.textColors = textColors;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvEventName.setText(eventNames.get(position));
        holder.tvEventLocation.setText(eventLocations.get(position));

        // Obtener el color de texto para la posición actual
        String colorString = textColors.get(position);

        // Verificar si el colorString no es null y no está vacío antes de intentar parsearlo
        if (colorString != null && !colorString.isEmpty()) {
            // Parsear el color desde el String
            int color = Color.parseColor(colorString);

            // Establecer el color de texto para cada TextView
            holder.tvEventName.setTextColor(color);
            holder.tvEventLocation.setTextColor(color);
        }
    }


    @Override
    public int getItemCount() {
        return eventNames.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName, tvEventLocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventLocation = itemView.findViewById(R.id.tvEventLocation);
        }
    }
    private void getAll(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        crudInterface = retrofit.create(CRUDInterface.class);
        Call<List<Proyecto>> call = crudInterface.getAll();
        call.enqueue(new Callback<List<Proyecto>>() {
            @Override
            public void onResponse(Call<List<Proyecto>> call, Response<List<Proyecto>> response) {
                if(!response.isSuccessful()){
                    Log.e("Response err: ",response.message());
                    return;
                }
                proyectos = response.body();
                ProyectsAdapter proyectsAdapter = new ProyectsAdapter(proyectos, context.getApplicationContext());
                listView.setAdapter(proyectsAdapter);
                proyectos.forEach(p -> Log.i("Proyectos: ",p.getNombre()));
            }

            @Override
            public void onFailure(Call<List<Proyecto>> call, Throwable t) {
                Log.e("Trow err: ",t.getMessage());
            }
        });
    }
}
