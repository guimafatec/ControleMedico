package br.edu.fateczl.controlemedico;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
        btnDataNascimento = view.findViewById(R.id.btnDataNascimento);
        initDatePicker();


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
            if (paciente == null) throw new Exception("Preencher CPF/Carteirinha");
            pCont.inserir(paciente);
            Toast.makeText(view.getContext(), "Paciente INSERIDO com sucesso", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void modificar() {
        Paciente paciente = montaPaciente();
        try {
            if (paciente == null) throw new Exception("Preencher CPF/Carteirinha");
            pCont.modificar(paciente);
            Toast.makeText(view.getContext(), "Paciente ATUALIZADO com sucesso", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void excluir() {
        Paciente paciente = montaPaciente();
        try {
            if (paciente == null) throw new Exception("Preencher CPF/Carteirinha");
            pCont.deletar(paciente);
            Toast.makeText(view.getContext(), "Paciente EXCLUÍDO com sucesso", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void buscar() {
        Paciente paciente = montaPaciente();
        try {
            if (paciente == null) throw new Exception("Preencher CPF/Carteirinha");
            Paciente buscar = pCont.buscar(paciente);
            if (buscar != null) {
                System.out.println(paciente.toString());
                preencheCampos(buscar);
            } else {
                Toast.makeText(view.getContext(), "Paciente NÃO encontrado", Toast.LENGTH_LONG).show();
                limpaCampos();
            }
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void listar() {
        try {
            List<Paciente> pacientes = pCont.listar();
            StringBuffer buffer = new StringBuffer();
            for (Paciente t : pacientes) {
                buffer.append(t.toString() + "\n");
            }
            tvListarPacientes.setText(buffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Paciente montaPaciente() {
        Paciente paciente = null;
        String idPaciente = etCpfCarteirinha.getText().toString();
        if (!idPaciente.isBlank()) {
            if (rb_conveniado.isChecked()) {
                paciente = new PacienteConveniado();
            } else {
                paciente = new PacienteParticular();
            }
            paciente.setId(idPaciente);
            paciente.setNome(etNomePaciente.getText().toString());
            paciente.setDataNascimento(btnDataNascimento.getText().toString());
        }
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

    private void limpaCampos() {
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
        btnDataNascimento.setText(getMaxDate());
        // Configuração de restrições para a data (Data de nascimento com idade mínima de 18 anos)
        TimeZone timeZoneUTC3 = TimeZone.getTimeZone("America/Sao_Paulo");
        Calendar calendar = Calendar.getInstance();
        LocalDate today = LocalDate.now();
        LocalDate eighteenYearsAgo = today.minusYears(18);
        calendar.set(eighteenYearsAgo.getYear(), eighteenYearsAgo.getMonthValue() - 1, eighteenYearsAgo.getDayOfMonth());
        long maxDate = calendar.getTimeInMillis();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setEnd(maxDate);

        btnDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> selectDate = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Data de Nascimento")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .setSelection(calendar.getTimeInMillis())
                        .build();
                selectDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        long selectionUTC_3 = selection - timeZoneUTC3.getOffset(selection);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String formattedDate = simpleDateFormat.format(selectionUTC_3);
                        LocalDate date = LocalDate.parse(formattedDate, dtFmt);
                        if (date.isAfter(eighteenYearsAgo)) {
                            date = eighteenYearsAgo;
                        }

                        btnDataNascimento.setText(date.format(dtFmt));
                    }
                });
                selectDate.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });
    }

    private void openDatePicker(View view) {
        datePickerDialog.show();
    }

}