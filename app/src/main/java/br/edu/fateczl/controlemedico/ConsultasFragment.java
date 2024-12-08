package br.edu.fateczl.controlemedico;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import br.edu.fateczl.controlemedico.controller.ConsultaController;
import br.edu.fateczl.controlemedico.controller.MedicoController;
import br.edu.fateczl.controlemedico.controller.PacienteController;
import br.edu.fateczl.controlemedico.model.Consulta;
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
    private Button btnDataConsulta, btnBuscarPaciente2, btnAgendar, btnModificarConsultas, btnExcluirConsultas, btnListarConsulta;
    private DatePickerDialog datePickerDialog;
    private TextView tvListarConsultas, tvInfoConsulta;
    private Spinner spEspec, spMedico, spHorarios, spConsultas;
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
        btnDataConsulta = view.findViewById(R.id.btnDataConsulta);
        initDatePicker();
        // FIM DATE PICKER

        tvInfoConsulta = view.findViewById(R.id.tvInfoConsulta);
        tvListarConsultas = view.findViewById(R.id.tvListarConsultas);
        tvListarConsultas.setMovementMethod(new ScrollingMovementMethod());

        tvResultBusca = view.findViewById(R.id.tvResultBusca);

        etIdPaciente = view.findViewById(R.id.etIdPaciente);
        btnBuscarPaciente2 = view.findViewById(R.id.btnBuscarPaciente2);
        spEspec = view.findViewById(R.id.spEspec);
        spMedico = view.findViewById(R.id.spMedico);
        spHorarios = view.findViewById(R.id.spHorarios);
        spConsultas = view.findViewById(R.id.spConsultas);

        btnAgendar = view.findViewById(R.id.btnAgendar);
        btnModificarConsultas = view.findViewById(R.id.btnModificarConsulta);
        btnExcluirConsultas = view.findViewById(R.id.btnExcluirConsulta);
        btnListarConsulta = view.findViewById(R.id.btnListarConsulta);

        pCont = new PacienteController(new PacienteDao(view.getContext()));
        mCont = new MedicoController(new MedicoDao(view.getContext()));
        cCont = new ConsultaController(new ConsultaDao(view.getContext()));

        btnBuscarPaciente2.setOnClickListener(op -> buscarPaciente());
        spinnerEspec();
        spinnerHorarios();

        btnAgendar.setOnClickListener(op -> inserir());
        btnModificarConsultas.setOnClickListener(op -> modificar());
        btnExcluirConsultas.setOnClickListener(op -> excluir());
        btnListarConsulta.setOnClickListener(op -> listar());

        return view;
    }

    private void inserir() {
        try {
            boolean valid = validarPreenchimento();
            if (!valid) throw new Exception("Preencha TODOS os campos");
            valid = validarDataConsulta();
            if (!valid) throw new Exception("Data deve ser futura");
            Consulta consulta = montaConsulta();
            cCont.inserir(consulta);
            Toast.makeText(view.getContext(), "Consulta INSERIDA com sucesso", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void modificar() {
        try {
            Consulta consulta = montaConsulta();
            Consulta consultaSelecionada = (Consulta) spConsultas.getSelectedItem();
            if (consulta == null || consultaSelecionada == null) throw new Exception("Consulta NÃO existe");
            consulta.setCodigo(consultaSelecionada.getCodigo());
            cCont.modificar(consulta);
            Toast.makeText(view.getContext(), "Consulta ATUALIZADA com sucesso", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        atualizaInfoConsulta();
    }

    private void excluir() {
        try {
            Consulta consulta = montaConsulta();
            Consulta consultaSelecionada = (Consulta) spConsultas.getSelectedItem();
            if (consulta == null || consultaSelecionada == null)
                throw new Exception("Consulta NÃO existe");
            consulta.setCodigo(consultaSelecionada.getCodigo());
            cCont.deletar(consulta);
            Toast.makeText(view.getContext(), "Consulta EXCLUÍDA com sucesso", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.err.println("MEULOG: " + e.getMessage());
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        atualizaInfoConsulta();
    }

    private void listar() {
        try {
            List<Consulta> consultas = cCont.listar();
            StringBuffer buffer = new StringBuffer();
            for (Consulta t : consultas) {
                buffer.append(t.toString() + "\n");
            }
            tvListarConsultas.setText(buffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarPaciente() {
        try {
            String idPacienteStr = etIdPaciente.getText().toString();
            Paciente buscar = pCont.buscar(idPacienteStr);
            if (buscar == null) throw new Exception("Paciente NÃO encontrado");
            tvResultBusca.setText(buscar.toString());
            Toast.makeText(view.getContext(), "Paciente encontrado", Toast.LENGTH_SHORT).show();
            spinnerConsultas();
        } catch (Exception e) {
            spEspec.setSelection(0);
            spMedico.setSelection(0);
            tvInfoConsulta.setText("");
            tvResultBusca.setText("");
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void spinnerConsultas() {
        try {
            String idPacienteStr = etIdPaciente.getText().toString();
            Paciente paciente = pCont.buscar(idPacienteStr);
            if (paciente == null) throw new Exception("Paciente NÃO encontrado");
            List<Consulta> consultas = cCont.listarPorPaciente(paciente);
            ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, consultas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spConsultas.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        spConsultas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preencheCampos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void preencheCampos() {
        Consulta consulta = (Consulta) spConsultas.getSelectedItem();
        ArrayAdapter<String> especAdapter = (ArrayAdapter<String>) spEspec.getAdapter();
        int totalEspec = especAdapter.getCount();
        for (int i = 0; i < totalEspec; i++) {
            if (especAdapter.getItem(i).equals(consulta.getMedico().getEspecialidade())) {
                spEspec.setSelection(i);
                break;
            }
        }
        ArrayAdapter<Medico> medicoAdapter = (ArrayAdapter<Medico>) spMedico.getAdapter();
        int totalMedico = medicoAdapter.getCount();
        for (int i = 0; i < totalMedico; i++) {
            if (medicoAdapter.getItem(i).equals(consulta.getMedico())) {
                spMedico.setSelection(i);
                break;
            }
        }
        String dhConsulta = consulta.getFmtDhConsulta();
        String data = dhConsulta.split(" ")[0];
        String hora = dhConsulta.split(" ")[1];

        btnDataConsulta.setText(data);

        ArrayAdapter<String> horaAdapter = (ArrayAdapter<String>) spHorarios.getAdapter();
        int totalHoras = horaAdapter.getCount();
        for (int i = 0; i < totalHoras; i++) {
            if (horaAdapter.getItem(i).equals(hora)) {
                spHorarios.setSelection(i);
                break;
            }
        }
    }

    private void spinnerEspec() {

        try {
            List<String> especialidades = mCont.listarEspecialidades();
            especialidades.add(0, "Selecione uma especialidade");
            ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, especialidades);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spEspec.setAdapter(adapter);
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        spEspec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerMedicos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void spinnerMedicos() {
        try {
            List<Medico> medicos = mCont.listar(spEspec.getSelectedItem().toString());
            ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, medicos);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMedico.setAdapter(adapter);
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        spMedico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atualizaInfoConsulta();
                atualizaHorarios();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void atualizaHorarios() {
        try {
            Medico medico = (Medico) spMedico.getSelectedItem();
            if (medico == null) throw new Exception("Médico NÃO encontrado");
            String data = btnDataConsulta.getText().toString();
            List<String> agendasDoMedico = cCont.listarAgendasDoMedico(medico, data);
            List<String> horarios = new ArrayList<>();
            spinnerHorarios();
            ArrayAdapter<String> horariosAdapter = (ArrayAdapter<String>) spHorarios.getAdapter();
            int totalHorarios = horariosAdapter.getCount();
            System.out.print("MEULOG: Horários: ");
            for (int i = 0; i < totalHorarios; i++) {
                System.out.print(horariosAdapter.getItem(i) + " ");
                horarios.add(horariosAdapter.getItem(i));
            }
            totalHorarios = horarios.size();
            for (int i = 0; i < totalHorarios; i++) {
                for (String agenda: agendasDoMedico) {
                    String hora = agenda.split(" ")[1];
                    if (hora.equals(horarios.get(i))) {
                        System.out.println("MEULOG: Agenda do Médico: " + agenda);
                        horarios.remove(i);
                        totalHorarios--;
                        break;
                    }
                }
            }
            horariosAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, horarios);
            horariosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHorarios.setAdapter(horariosAdapter);
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizaInfoConsulta() {
        boolean valid = validarPreenchimento();
        try {
            if (!valid) throw new Exception("Preencha TODOS os campos");
            Consulta consulta = montaConsulta();
            tvInfoConsulta.setText(consulta.description());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void spinnerHorarios() {
        try {
            List<String> schedule = new ArrayList<>();
            for (LocalTime localTime = LocalTime.of(8, 0); !localTime.toString().equals("17:30"); localTime = localTime.plusMinutes(30)) {
                String timeFormatted = localTime.toString();
                schedule.add(timeFormatted);
            }
            ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, schedule);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spHorarios.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        spHorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atualizaInfoConsulta();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private boolean validarPreenchimento() {
        String idPaciente = etIdPaciente.getText().toString();
        String especSelected = (String) spEspec.getSelectedItem();
        Medico medicoSelected = (Medico) spMedico.getSelectedItem();
        boolean valid = (!idPaciente.isEmpty()) &&
                (!especSelected.equals("Selecione uma especialidade")) &&
                (medicoSelected != null);
        return valid;
    }

    private boolean validarDataConsulta() {
        String dataConsulta = btnDataConsulta.getText().toString();
        DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dtConsulta = LocalDate.parse(dataConsulta, dtFmt);
        LocalDate today = LocalDate.now();
        if (dtConsulta.isBefore(today) || dtConsulta.equals(today)) {
            return false;
        }
        return true;
    }

    private Consulta montaConsulta() {
        Consulta consulta = null;
        try {
            String idPacienteStr = etIdPaciente.getText().toString();
            Paciente paciente = pCont.buscar(idPacienteStr);
            Medico selectedMedico = (Medico) spMedico.getSelectedItem();
            if (paciente == null) throw new Exception("Paciente NÃO encontrado");
            if (selectedMedico == null) throw new Exception("Medico NÃO encontrado");
            Medico medico = mCont.buscar(selectedMedico);
            String dataConsulta = btnDataConsulta.getText().toString();
            String horaConsulta = (String) spHorarios.getSelectedItem();
            String dhConsulta = dataConsulta + " " + horaConsulta;
            consulta = new Consulta();
            consulta.setPaciente(paciente);
            consulta.setMedico(medico);
            consulta.setValor(medico.getValorConsulta());
            consulta.setDhConsulta(dhConsulta);
        } catch (Exception e) {
            tvResultBusca.setText("");
            System.err.println("MEULOG: " + e.getMessage());
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return consulta;
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
        btnDataConsulta.setText(getMinDate());
        // Configuração de restrições para a data mínima a partir do dia seguinte à utilização do sistema
        TimeZone timeZoneUTC3 = TimeZone.getTimeZone("America/Sao_Paulo");
        Calendar calendar = Calendar.getInstance();
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        calendar.set(tomorrow.getYear(), tomorrow.getMonthValue() - 1, tomorrow.getDayOfMonth());
        long minDate = calendar.getTimeInMillis();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setStart(minDate);


        btnDataConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> selectDate = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Data da Consulta")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .setSelection(calendar.getTimeInMillis())
                        .build();
                selectDate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        System.out.println("VARIÁVEL selection\t" + selection);
                        long selectionUTC_3 = selection - timeZoneUTC3.getOffset(selection);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        String formattedDate = simpleDateFormat.format(selectionUTC_3);
                        System.out.println("DATA SELECIONADA: " + formattedDate);
                        LocalDate date = LocalDate.parse(formattedDate, dtFmt);

                        if (date.isBefore(today) || date.isEqual(today)) {
                            System.out.println("DATA É MENOR OU IGUAL A HOJE");
                            date = today.plusDays(1);
                        }

                        btnDataConsulta.setText(date.format(dtFmt));
                        atualizaInfoConsulta();
                        atualizaHorarios();
                    }
                });
                selectDate.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });

    }
}