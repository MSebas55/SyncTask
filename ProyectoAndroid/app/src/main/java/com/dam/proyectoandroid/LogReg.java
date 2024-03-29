package com.dam.proyectoandroid;

import static com.dam.proyectoandroid.LoginFragment.getCampos;
import static com.dam.proyectoandroid.RegisterFragment.crearUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dam.proyectoandroid.Controlador.PageController;
import com.dam.proyectoandroid.Database.Constants;
import com.dam.proyectoandroid.Database.Interfaces.UserInterface;
import com.dam.proyectoandroid.Database.model.Usuario;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogReg extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tab1,tab2;
    PageController pagerAdapter;
    UserInterface userInterface;
    static Usuario usuario;
    Button registerbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        tab1 = findViewById(R.id.tablog);
        tab2 = findViewById(R.id.tabreg);


        registerbutton = findViewById(R.id.registerbutton);
        pagerAdapter = new PageController(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> campos = getCampos();
                loginUser(campos.get(0), campos.get(1),v);

            }
        });
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition()==0) {
                    pagerAdapter.notifyDataSetChanged();
                    registerbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            List<String> campos = getCampos();
                            loginUser(campos.get(0), campos.get(1),v);

                        }
                    });
                }
                if (tab.getPosition()==1) {
                    pagerAdapter.notifyDataSetChanged();
                    registerbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addUser(crearUser());
                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
    public void changeToInicio(View view) {
        Intent intent = new Intent(LogReg.this, Inicio.class);
        startActivity(intent);
    }
    public void addUser(Usuario usuario) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userInterface = retrofit.create(UserInterface.class);

        Call<Usuario> call = userInterface.create(usuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(LogReg.this, "error", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LogReg.this, "Usuario registrado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(LogReg.this, "error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void loginUser(String email,String password,View v) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userInterface = retrofit.create(UserInterface.class);

        Call<Usuario> call = userInterface.getUserByEmail(email);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(LogReg.this, "error", Toast.LENGTH_SHORT).show();
                    return;
                }
                usuario = response.body();
                if(usuario.getContra().equals(password)){
                    Toast.makeText(LogReg.this, "Sesion iniciada", Toast.LENGTH_SHORT).show();
                    changeToInicio(v);
                }


            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(LogReg.this, "error conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static Usuario getUsuario(){
        return usuario;
    }
}