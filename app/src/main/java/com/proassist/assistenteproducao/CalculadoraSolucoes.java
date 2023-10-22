package com.proassist.assistenteproducao;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CalculadoraSolucoes extends AppCompatActivity {
    // Declarando variáveis
    int quantidadePorCarro;
    int minutosPorCarro;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_solucoes);

        // Receber as referências da interface gráfica
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        EditText editTextProgramada = findViewById(R.id.editTextA);
        EditText editTextProduzida = findViewById(R.id.editTextProduzida);
        Button buttonCalcular = findViewById(R.id.buttonCalcular);
        TextView textViewResultado = findViewById(R.id.textViewResultado);
        TextView textViewHorario = findViewById(R.id.textViewHorario);

        // Adiciona sombras aos textos
        textViewResultado.setShadowLayer(2, 2, 2, Color.BLACK);
        textViewHorario.setShadowLayer(2, 2, 2, Color.BLACK);


        // Extrai o código para um método separado para garantir que os valores sejam atribuídos
        setValoresPadrao(radioGroup);

        // Definindo as quantidades padrão de acordo com a opção selecionada pelo usuário
        radioGroup.setOnCheckedChangeListener((rg, checkedId) -> {
            switch (checkedId) {
                case R.id.radioButton100:
                    setQuantidadePadrao(1904, 29);
                    break;

                case R.id.radioButton250:
                    setQuantidadePadrao(1372, 38);
                    break;

                case R.id.radioButton500:
                    setQuantidadePadrao(936, 19);
                    break;

                case R.id.radioButton1000:
                    setQuantidadePadrao(550, 18);
                    break;
            }
        });

        // Adicionando um listener ao botão de calcular
        buttonCalcular.setOnClickListener(view -> {
            animarBotao(view);

            // Convertendo os valores das referências para String
            String qtdProgramadaStr = editTextProgramada.getText().toString();
            String qtdProduzidaStr = editTextProduzida.getText().toString();

            String resultadoTempo;
            String resultadoHorario;

            // Verificando se os campos de quantidade estão vazios
            if (qtdProgramadaStr.isEmpty() || qtdProduzidaStr.isEmpty()) {
                resultadoTempo = "Os campos de quantidade não podem estar vazios.";
                textViewResultado.setText(resultadoTempo);
                textViewHorario.setText("");
                return;
            }

            try {
                // Convertendo as Strings para int
                int quantidadeProgramada = Integer.parseInt(qtdProgramadaStr);
                int quantidadeProduzida = Integer.parseInt(qtdProduzidaStr);

                // Calcular a quantidade restantes e os minutos restantes
                MinutosRestantes minutosRestantes = calcularMinutosRestantes(quantidadeProgramada, quantidadeProduzida);
                int quantidadeRestante = minutosRestantes.getQuantidadeRestante();
                int minutosRestantesInt = minutosRestantes.getMinutosRestantes();

                if (quantidadeRestante < 0) {
                    resultadoTempo = "A quantidade produzida não pode ser maior do que a quantidade programada.";
                    textViewResultado.setText(resultadoTempo);
                    textViewHorario.setText("");
                    return;
                }

                // Trabalhando com as datas
                // Calcular a hora prevista para o termino
                Date dataTermino = calcularDataHora(minutosRestantesInt);

                // Calcular tempo restante
                TempoRestante tempoRestante = calcularTempoRestante(dataTermino);
                int horas = tempoRestante.getHoras();
                int minutos = tempoRestante.getMinutos();

                // Formatar as datas
                String dataTerminoStr = formatarData(dataTermino);

                if (horas > 0) {
                    resultadoTempo = String.format(Locale.getDefault(), "Tempo restante aproximado:\n%d horas e %d minutos.", horas, minutos);
                } else {
                    resultadoTempo = String.format(Locale.getDefault(), "Tempo restante aproximado: %d minutos.", minutos);
                }
                resultadoHorario = String.format(Locale.getDefault(), "Final previsto: %s", dataTerminoStr);
            } catch (Exception e) {
                // Exibir o resultado
                resultadoTempo = "Houve um erro! Tente novamente.";
                resultadoHorario = "Erro: " + e.getMessage();
                e.printStackTrace();
            }
            textViewResultado.setText(resultadoTempo);
            textViewHorario.setText(resultadoHorario);
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void setValoresPadrao(RadioGroup radioGroup) {

        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButton100:
                setQuantidadePadrao(1904, 29);
                break;

            case R.id.radioButton250:
                setQuantidadePadrao(1372, 38);
                break;

            case R.id.radioButton500:
                setQuantidadePadrao(936, 19);
                break;

            case R.id.radioButton1000:
                setQuantidadePadrao(550, 18);
                break;
        }
    }

    private void setQuantidadePadrao(int valor1, int valor2) {
        quantidadePorCarro = valor1;
        minutosPorCarro = valor2;
    }

    private void animarBotao(View view) {
        // Criando um objeto da classe Animation a partir do arquivo de animacao
        Animation anim = AnimationUtils.loadAnimation(CalculadoraSolucoes.this, R.anim.button_scale);
        // Adicionando um listener de animação ao botão para iniciar a animacao quando ele for clicado
        view.startAnimation(anim);
    }

    public static class MinutosRestantes {
        private final int quantidadeRestante;
        private final int minutosRestantes;

        public MinutosRestantes(int quantidadeRestante, int minutosRestantes) {
            this.quantidadeRestante = quantidadeRestante;
            this.minutosRestantes = minutosRestantes;
        }

        public int getQuantidadeRestante() {
            return quantidadeRestante;
        }

        public int getMinutosRestantes() {
            return minutosRestantes;
        }
    }

    private MinutosRestantes calcularMinutosRestantes(int quantidadeProgramada, int quantidadeProduzida) {
        int quantidadeRestante = quantidadeProgramada - quantidadeProduzida;
        int carrosRestantes = quantidadeRestante / quantidadePorCarro;
        int minutosRestantes = carrosRestantes * minutosPorCarro;

        return new MinutosRestantes(quantidadeRestante, minutosRestantes);
    }

    private Date calcularDataHora(int minutosRestantes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, minutosRestantes);

        return cal.getTime();
    }

    private String formatarData(Date dataTermino) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm'h'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());

        return sdf.format(dataTermino);
    }

    public static class TempoRestante {
        private final int horas;
        private final int minutos;

        public TempoRestante(int horas, int minutos) {
            this.horas = horas;
            this.minutos = minutos;
        }

        public int getHoras() {
            return horas;
        }

        public int getMinutos() {
            return minutos;
        }
    }

    public TempoRestante calcularTempoRestante(Date dataTermino) {
        long tempoRestanteMili = dataTermino.getTime() - new Date().getTime();
        long tempoRestanteMin = TimeUnit.MINUTES.convert(tempoRestanteMili, TimeUnit.MILLISECONDS);
        int horas = (int) (tempoRestanteMin / 60);
        int minutos = (int) (tempoRestanteMin % 60);

        return new TempoRestante(horas, minutos);
    }



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