package com.proassist.assistenteproducao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button botaoSolucoes = findViewById(R.id.botaoSolucoes);
        Button botaoEficiencia = findViewById(R.id.botaoEficiencia);

        botaoSolucoes.setOnClickListener(this);
        botaoEficiencia.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.botaoSolucoes) {
            Intent intentSolucoes = new Intent(MainActivity.this, CalculadoraSolucoes.class);
            startActivity(intentSolucoes);

        } else if (view.getId() == R.id.botaoEficiencia) {
            Intent intentSolucoes = new Intent(MainActivity.this, CalculadoraEficiencia.class);
            startActivity(intentSolucoes);
        }
    }
}