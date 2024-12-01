package br.edu.fateczl.controlemedico;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.edu.fateczl.controlemedico.controller.ConsultaController;
import br.edu.fateczl.controlemedico.controller.MedicoController;
import br.edu.fateczl.controlemedico.controller.PacienteController;
import br.edu.fateczl.controlemedico.model.Medico;
import br.edu.fateczl.controlemedico.model.Paciente;
import br.edu.fateczl.controlemedico.persistence.ConsultaDao;
import br.edu.fateczl.controlemedico.persistence.MedicoDao;
import br.edu.fateczl.controlemedico.persistence.PacienteDao;


public class ConsultasFragment extends Fragment {
    /*
     * @author: Gustavo Guimarães de Oliveira
     */
    private View view;
    private Button btnDataConsulta, btnBuscarPaciente2, btnAgendar, btnModificarConsultas, btnExcluirConsultas;
    private DatePickerDialog datePickerDialog;
    private TextView tvListarConsultas;
    private Spinner spEspec, spMedico, spHorarios;
    private EditText etIdPaciente;
    private PacienteController pCont;
    private MedicoController mCont;
    private ConsultaController cCont;
    private TextView tvResultBusca;

    public ConsultasFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consultas, container, false);

        // DATE PICKER
        initDatePicker();
        btnDataConsulta = view.findViewById(R.id.btnDataConsulta);
        btnDataConsulta.setText(getMinDate());
        btnDataConsulta.setOnClickListener(op -> openDatePicker(view));
        // FIM DATE PICKER

        tvListarConsultas = view.findViewById(R.id.tvListarConsultas);
        tvListarConsultas.setMovementMethod(new ScrollingMovementMethod());

        tvResultBusca = (TextView) view.findViewById(R.id.tvResultBusca);

        etIdPaciente = view.findViewById(R.id.etIdPaciente);
        btnBuscarPaciente2 = view.findViewById(R.id.btnBuscarPaciente2);
        spEspec = view.findViewById(R.id.spEspec);
        spMedico = view.findViewById(R.id.spMedico);
        spHorarios = view.findViewById(R.id.spHorarios);
        btnAgendar = view.findViewById(R.id.btnAgendar);
        btnModificarConsultas = view.findViewById(R.id.btnModificarConsulta);
        btnExcluirConsultas = view.findViewById(R.id.btnExcluirConsulta);

        pCont = new PacienteController(new PacienteDao(view.getContext()));
        mCont = new MedicoController(new MedicoDao(view.getContext()));
        cCont = new ConsultaController(new ConsultaDao(view.getContext()));

        btnBuscarPaciente2.setOnClickListener(op -> buscarPaciente());
        spinnerEspec();
        spinnerHorarios();

        return view;
    }

    private void spinnerEspec() {

        try {
            List<String> especialidades = mCont.listarEspecialidades();
            especialidades.add(0, "Selecione uma especialidade");
            ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, especialidades);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spEspec.setAdapter(adapter);
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        spEspec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerMedicos(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void spinnerMedicos(View view) {
        try {
            List<Medico> medicos = mCont.listar(spEspec.getSelectedItem().toString());
            ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, medicos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMedico.setAdapter(adapter);
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void spinnerHorarios() {
        try {
            LocalDate date = LocalDate.parse(btnDataConsulta.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            List<String> schedule = new ArrayList<>();
            for (LocalTime localTime = LocalTime.of(8, 0); !localTime.toString().equals("17:30"); localTime = localTime.plusMinutes(30)) {
                String timeFormatted = localTime.toString();
                schedule.add(timeFormatted);
            }
            ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, schedule);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHorarios.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void buscarPaciente() {
        Paciente paciente = new Paciente();
        paciente.setId(etIdPaciente.getText().toString());
        try {
            Paciente buscar = pCont.buscar(paciente);
            if (buscar.getNome() != null) {
                tvResultBusca.setText(paciente.toString());
                Toast.makeText(view.getContext(), "Paciente encontrado", Toast.LENGTH_SHORT).show();
            } else {
                tvResultBusca.setText("");
                Toast.makeText(view.getContext(), "Paciente NÃO encontrado", Toast.LENGTH_SHORT).show();
//                limpaCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private String getMinDate() {
        LocalDate today = LocalDate.now();
        today = today.plusDays(1);
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
                btnDataConsulta.setText(date);
            }
        };

        LocalDateTime today = LocalDateTime.now();

        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        int style = 3;
        datePickerDialog = new DatePickerDialog(this.getContext(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(today.plusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

    }

    private void openDatePicker(View view) {
        datePickerDialog.show();
    }

}