package com.proassist.assistenteproducao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class CalculadoraEficiencia extends AppCompatActivity {
    EditText editTextA;
    EditText editTextB;
    EditText editTextC;
    EditText editTextD;
    TextView textViewEficiencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_eficiencia);

        editTextA = findViewById(R.id.editTextA);
        editTextB = findViewById(R.id.editTextB);
        editTextC = findViewById(R.id.editTextC);
        editTextD = findViewById(R.id.editTextD);
        textViewEficiencia = findViewById(R.id.textViewEficiencia);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                calcularEficiencia();
            }
        };

        editTextA.addTextChangedListener(watcher);
        editTextB.addTextChangedListener(watcher);
        editTextC.addTextChangedListener(watcher);
        editTextD.addTextChangedListener(watcher);
    }

    private void calcularEficiencia() {
        String valorAStr = editTextA.getText().toString();
        String valorBStr = editTextB.getText().toString();
        String valorCStr = editTextC.getText().toString();
        String valorDStr = editTextD.getText().toString();

        if (valorAStr.isEmpty() || valorBStr.isEmpty() || valorCStr.isEmpty() || valorDStr.isEmpty()){
            textViewEficiencia.setText(CalculadoraEficiencia.this.getString(R.string.textEficiencia));
        } else {
            double valorA = Double.parseDouble(valorAStr);
            double valorB = Double.parseDouble(valorBStr);
            double valorC = Double.parseDouble(valorCStr);
            double valorD = Double.parseDouble(valorDStr);

            double resultado = calculos(valorA, valorB, valorC, valorD);
            textViewEficiencia.setText(CalculadoraEficiencia.this.getString(R.string.resultadoEficiencia, formatarResultado(resultado)));
        }
    }

    private double calculos(double a, double b, double c, double d) {
        return ((b + c + d) / a) * 100;
    }

    private String formatarResultado(double resultado) {
        return String.format(Locale.getDefault(), "%.2f", resultado);
    }

    // Método para ocultar teclando em caso de clique fora do campo de texto
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}