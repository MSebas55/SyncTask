package com.dam.proyectoandroid;


import static com.dam.proyectoandroid.LogReg.getUsuario;
import static com.dam.proyectoandroid.LogReg.usuario;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.proyectoandroid.Database.Constants;
import com.dam.proyectoandroid.Database.Interfaces.UserInterface;
import com.dam.proyectoandroid.Database.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    UserInterface userInterface;
    EditText editTextNombre,editTextApellido,editTextEmail,editTextContra;
    TextView textName, textEmail;


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view  = inflater.inflate(R.layout.fragment_profile, container, false);
        Button cerrarSesion = view.findViewById(R.id.buttonOption3);
        Button eliminarCuenta = view.findViewById(R.id.buttonOption2);
        Button editarCuenta = view.findViewById(R.id.buttonOption1);
        textName = view.findViewById(R.id.textViewUsername);
        textEmail = view.findViewById(R.id.textViewMail);
        textName.setText(usuario.getNombre() + " " + usuario.getApellido());
        textEmail.setText(usuario.getEmail());

        // Asignar una función al botón usando un OnClickListener
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica que se ejecutará cuando se haga clic en el botón
                onMyButtonClick();
            }
        });
        eliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica que se ejecutará cuando se haga clic en el botón
                deleteUser(getUsuario().getId());
                onMyButtonClick();
            }
        });
        editarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica que se ejecutará cuando se haga clic en el botón
                showAlertDialog();

            }
        });
        return view;
    }
    @SuppressLint("MissingInflatedId")
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // Establecer el título del AlertDialog y su color
        builder.setTitle(Html.fromHtml("<font color='#0F3800'>Editar Perfil</font>"));

        // Inflar el layout personalizado
        View dialogView = getLayoutInflater().inflate(R.layout.alertdialog_editprof, null);

        builder.setView(dialogView);


        // Referenciar los campos del layout

        editTextNombre = dialogView.findViewById(R.id.inputname);
        editTextApellido = dialogView.findViewById(R.id.inputlastname);
        editTextEmail = dialogView.findViewById(R.id.inputemail);
        editTextContra = dialogView.findViewById(R.id.inputpassword);
        editTextNombre.setText(usuario.getNombre());
        editTextApellido.setText(usuario.getApellido());
        editTextEmail.setText(usuario.getEmail());
        editTextContra.setText(usuario.getContra());

        // Configurar los campos según tus necesidades
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                usuario = getUsuario();
                usuario.setNombre(editTextNombre.getText().toString());
                usuario.setApellido(editTextApellido.getText().toString());
                usuario.setEmail(editTextEmail.getText().toString());
                usuario.setContra(editTextContra.getText().toString());
                updateUser(usuario,usuario.getId());

                Intent nIntent = new Intent(getActivity(), Inicio.class);
                startActivity(nIntent);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public void updateUser(Usuario usuario,Integer id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userInterface = retrofit.create(UserInterface.class);

        Call<Usuario> call = userInterface.update(usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                Toast.makeText(getContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
            }
        });
    }

    public void deleteUser(Integer id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userInterface = retrofit.create(UserInterface.class);

        Call<Boolean> call = userInterface.delete(id);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                Toast.makeText(getContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
            }
        });
    }

    public void onMyButtonClick() {
        Toast.makeText(getActivity(), "Cerrando Sesión", Toast.LENGTH_SHORT).show();
        Intent nIntent = new Intent(getActivity(), LogReg.class);
        startActivity(nIntent);
    }
}