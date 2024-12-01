package br.edu.fateczl.controlemedico;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.controlemedico.controller.MedicoController;
import br.edu.fateczl.controlemedico.controller.PacienteController;
import br.edu.fateczl.controlemedico.model.Medico;
import br.edu.fateczl.controlemedico.model.Paciente;
import br.edu.fateczl.controlemedico.persistence.MedicoDao;
import br.edu.fateczl.controlemedico.persistence.PacienteDao;


public class MedicosFragment extends Fragment {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private View view;
    private TextView tvListarMedicos;
    private EditText etCrmMedico, etNomeMedico, etEspecMedico;
    private Button btnBuscarMedico, btnInserirMedico, btnModificarMedico, btnListarMedicos, btnExcluirMedico;
    private MedicoController mCont;
    public MedicosFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_medicos, container, false);
        tvListarMedicos = view.findViewById(R.id.tvListarMedicos);
        tvListarMedicos.setMovementMethod(new ScrollingMovementMethod());

        etCrmMedico = view.findViewById(R.id.etCrmMedico);
        etNomeMedico = view.findViewById(R.id.etNomeMedico);
        etEspecMedico = view.findViewById(R.id.etEspecMedico);
        btnBuscarMedico = view.findViewById(R.id.btnBuscarMedico);
        btnInserirMedico = view.findViewById(R.id.btnInserirMedico);
        btnExcluirMedico = view.findViewById(R.id.btnExcluirMedico);
        btnModificarMedico = view.findViewById(R.id.btnModificarMedico);
        btnListarMedicos = view.findViewById(R.id.btnListarMedicos);
        mCont = new MedicoController(new MedicoDao(view.getContext()));

        btnInserirMedico.setOnClickListener(op -> inserir());
        btnModificarMedico.setOnClickListener(op -> modificar());
        btnExcluirMedico.setOnClickListener(op -> excluir());
        btnBuscarMedico.setOnClickListener(op -> buscar());
        btnListarMedicos.setOnClickListener(op -> listar());

        return view;
    }

    private void inserir() {
        Medico medico = montaMedico();
        try {
            mCont.inserir(medico);
            Toast.makeText(view.getContext(), "Médico INSERIDO com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void modificar() {
        Medico medico = montaMedico();
        try {
            mCont.modificar(medico);
            Toast.makeText(view.getContext(), "Médico MODIFICADO com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void excluir() {
        Medico medico = montaMedico();
        try {
            mCont.deletar(medico);
            Toast.makeText(view.getContext(), "Médico EXCLUÍDO com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void buscar() {
        Medico medico = montaMedico();
        try {
            Medico buscar = mCont.buscar(medico);
            if (buscar.getNome() != null) {
                System.out.println(medico.toString());
                preencheCampos(medico);
            } else {
                Toast.makeText(view.getContext(), "Médico NÃO encontrado", Toast.LENGTH_LONG).show();
                limpaCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void listar() {
        try {
            List<Medico> medicos = mCont.listar();
            StringBuffer buffer = new StringBuffer();
            for (Medico m: medicos) {
                buffer.append(m.toString() + "\n");
            }
            tvListarMedicos.setText(buffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Medico montaMedico() {
        Medico medico = new Medico();
        String crm = etCrmMedico.getText().toString();
        crm = crm.isBlank() ? " " : crm;
        medico.setCrm(crm);
        medico.setNome(etNomeMedico.getText().toString());
        medico.setEspecialidade(etEspecMedico.getText().toString());
        return medico;
    }
    private void limpaCampos() {
        etCrmMedico.setText("");
        etNomeMedico.setText("");
        etEspecMedico.setText("");
    }

    private void preencheCampos(Medico medico){
        etCrmMedico.setText(medico.getCrm());
        etNomeMedico.setText(medico.getNome());
        etEspecMedico.setText(medico.getEspecialidade());
    }
}