package br.edu.fateczl.controlemedico;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import br.edu.fateczl.controlemedico.controller.PacienteController;
import br.edu.fateczl.controlemedico.model.Paciente;
import br.edu.fateczl.controlemedico.model.PacienteConveniado;
import br.edu.fateczl.controlemedico.model.PacienteParticular;
import br.edu.fateczl.controlemedico.persistence.PacienteDao;


public class PacientesFragment extends Fragment {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */

    private View view;
    private Button btnDataNascimento, btnBuscarPaciente, btnModificarPaciente, btnExcluirPaciente, btnInserirPaciente, btnListarPacientes;
    private DatePickerDialog datePickerDialog;
    private TextView tvListarPacientes;
    private RadioGroup rg_pacientes;
    private RadioButton rb_particular, rb_conveniado;
    private EditText etCpfCarteirinha, etNomePaciente;
    private PacienteController pCont;

    public PacientesFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pacientes, container, false);
        initDatePicker();
        btnDataNascimento = view.findViewById(R.id.btnDataNascimento);
        btnDataNascimento.setText(getMaxDate());
        btnDataNascimento.setOnClickListener(op -> openDatePicker(view));


        rg_pacientes = view.findViewById(R.id.rg_pacientes);
        rb_particular = view.findViewById(R.id.rb_particular);
        rb_conveniado = view.findViewById(R.id.rb_conveniado);
        rb_particular.setChecked(true);
        etCpfCarteirinha = view.findViewById(R.id.etCpfCarteirinha);
        etNomePaciente = view.findViewById(R.id.etNomePaciente);
        btnInserirPaciente = view.findViewById(R.id.btnInserirPaciente);
        btnModificarPaciente = view.findViewById(R.id.btnModificarPaciente);
        btnBuscarPaciente = view.findViewById(R.id.btnBuscarPaciente);
        btnExcluirPaciente = view.findViewById(R.id.btnExcluirPaciente);
        btnListarPacientes = view.findViewById(R.id.btnListarPacientes);
        tvListarPacientes = view.findViewById(R.id.tvListarPacientes);
        tvListarPacientes.setMovementMethod(new ScrollingMovementMethod());


        pCont = new PacienteController(new PacienteDao(view.getContext()));

        btnInserirPaciente.setOnClickListener(op -> inserir());
        btnModificarPaciente.setOnClickListener(op -> modificar());
        btnExcluirPaciente.setOnClickListener(op -> excluir());
        btnBuscarPaciente.setOnClickListener(op -> buscar());
        btnListarPacientes.setOnClickListener(op -> listar());

        return view;
    }

    private void inserir() {
        Paciente paciente = montaPaciente();
        try {
            pCont.inserir(paciente);
            Toast.makeText(view.getContext(), "Paciente INSERIDO com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void modificar() {
        Paciente paciente = montaPaciente();
        try {
            pCont.modificar(paciente);
            Toast.makeText(view.getContext(), "Paciente ATUALIZADO com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void excluir() {
        Paciente paciente = montaPaciente();
        try {
            pCont.deletar(paciente);
            Toast.makeText(view.getContext(), "Paciente EXCLUÍDO com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void buscar() {
        Paciente paciente = montaPaciente();
        try {
            Paciente buscar = pCont.buscar(paciente);
            if (buscar.getNome() != null) {
                System.out.println(paciente.toString());
                preencheCampos(paciente);
            } else {
                Toast.makeText(view.getContext(), "Paciente NÃO encontrado", Toast.LENGTH_LONG).show();
                limpaCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void listar() {
        try {
            List<Paciente> pacientes = pCont.listar();
            StringBuffer buffer = new StringBuffer();
            for (Paciente t: pacientes) {
                buffer.append(t.toString() + "\n");
            }
            tvListarPacientes.setText(buffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Paciente montaPaciente() {
        Paciente paciente;
        if (rb_conveniado.isChecked()){
            paciente = new PacienteConveniado();
        } else {
            paciente = new PacienteParticular();
        }
        paciente.setId(etCpfCarteirinha.getText().toString());
        paciente.setNome(etNomePaciente.getText().toString());
        paciente.setDataNascimento(btnDataNascimento.getText().toString());

        return paciente;
    }

    private void preencheCampos(Paciente paciente) {
        etCpfCarteirinha.setText(paciente.getId());
        etNomePaciente.setText(paciente.getNome());
        btnDataNascimento.setText(paciente.getDataNascimento());
        if (paciente.getTipo().equals("C")) {
            rb_conveniado.setChecked(true);
        } else {
            rb_particular.setChecked(true);
        }
    }

    private void limpaCampos(){
        etCpfCarteirinha.setText("");
        etNomePaciente.setText("");
    }
    private String getMaxDate() {
        LocalDate today = LocalDate.now();
        today = today.minusYears(18);
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        return String.format("%02d/%02d/%04d", day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = String.format("%02d/%02d/%04d", dayOfMonth, month, year);
                btnDataNascimento.setText(date);
            }
        };

        LocalDateTime today = LocalDateTime.now();
        today = today.minusYears(18);
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        int style = 3;
        datePickerDialog = new DatePickerDialog(this.getContext(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(today.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

    }

    private void openDatePicker(View view) {
        datePickerDialog.show();
    }

}