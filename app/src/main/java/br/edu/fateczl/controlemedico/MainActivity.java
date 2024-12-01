package br.edu.fateczl.controlemedico;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Bundle bundle = getIntent().getExtras();
        carregaFragment(bundle);
    }

    private void carregaFragment(Bundle bundle) {
        fragment = new InicioFragment();
        if (bundle != null) {
            String tipo = bundle.getString("tipo");
            if (tipo.equals("pacientes")) {
                fragment = new PacientesFragment();
            }
            if (tipo.equals("medicos")) {
                fragment = new MedicosFragment();
            }
            if(tipo.equals("consultas")) {
                fragment = new ConsultasFragment();
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, MainActivity.class);

        if (id == R.id.item_pacientes) {
            return setupOptions(bundle, intent, "tipo", "pacientes");
        }
        if (id == R.id.item_consultas) {
            return setupOptions(bundle, intent, "tipo", "consultas");
        }
        if (id == R.id.item_medicos) {
            return setupOptions(bundle, intent, "tipo", "medicos");
        }
        return true;
    }

    private boolean setupOptions(Bundle bundle, Intent intent, String key, String value) {
        bundle.putString(key, value);
        intent.putExtras(bundle);
        this.startActivity(intent);
        this.finish();
        return true;
    }
}