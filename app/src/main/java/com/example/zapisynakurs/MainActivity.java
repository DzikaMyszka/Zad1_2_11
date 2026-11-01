package com.example.zapisynakurs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.materialswitch.MaterialSwitch;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etEmail;
    private Button btnBirthDate, btnSubmit;
    private Spinner spCourse;
    private RadioGroup rgMode;
    private SeekBar sbLevel;
    private TextView tvLevelValue, tvSummary;
    private MaterialSwitch swNewsletter;
    private CheckBox cbTerms;

    private String birthDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnBirthDate = findViewById(R.id.btnBirthDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        spCourse = findViewById(R.id.spCourse);
        rgMode = findViewById(R.id.rgMode);
        sbLevel = findViewById(R.id.sbLevel);
        tvLevelValue = findViewById(R.id.tvLevelValue);
        tvSummary = findViewById(R.id.tvSummary);
        swNewsletter = findViewById(R.id.swNewsletter);
        cbTerms = findViewById(R.id.cbTerms);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.courses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourse.setAdapter(adapter);


        tvLevelValue.setText(String.valueOf(sbLevel.getProgress()));

        cbTerms.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnSubmit.setEnabled(isChecked));

        sbLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLevelValue.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        btnSubmit.setOnClickListener(v -> {
            if (validateForm()) {
                new AlertDialog.Builder(this)
                        .setTitle("Wysłać zgłoszenie?")
                        .setMessage("Sprawdź dane przed wysłaniem")
                        .setPositiveButton("Tak", (dialog, which) -> {
                            String name = etName.getText().toString().trim();
                            String email = etEmail.getText().toString().trim();
                            String course = spCourse.getSelectedItem().toString();
                            String mode = rgMode.getCheckedRadioButtonId() == R.id.rbOnsite ? "Stacjonarny" : "Zdalny";
                            String newsletter = swNewsletter.isChecked() ? "tak" : "nie";
                            int level = sbLevel.getProgress();

                            String summary = name + "\n" + email + "\n" + birthDate + "\n" + course + "\n" + mode + "\n" + level + "/10\n" + newsletter;
                            tvSummary.setText(summary);

                            Snackbar.make(findViewById(R.id.main), "Zgłoszenie wysłane", Snackbar.LENGTH_LONG)
                                    .setAction("Cofnij", v2 -> tvSummary.setText(""))
                                    .show();
                        })
                        .setNegativeButton("Nie", null)
                        .show();
            }
        });

        }
    private boolean validateForm() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            etName.setError("Podaj imię i nazwisko");
            etName.requestFocus();
            return false;
        }

        String email = etEmail.getText().toString().trim();
        if (email.isEmpty() || !email.contains("@")) {
            etEmail.setError("Niepoprawny adres e-mail");
            etEmail.requestFocus();
            return false;
        }

        if (spCourse.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Wybierz kurs", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (rgMode.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Zaznacz tryb zajęć", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}