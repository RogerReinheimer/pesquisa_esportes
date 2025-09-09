package com.example.pesquisa_esporte

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class home : AppCompatActivity() {

    private lateinit var etPrimeiroNome: EditText
    private lateinit var etSobrenome: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAniversario: EditText
    private lateinit var spnEsporte: Spinner
    private lateinit var btnEnviar: Button
    private lateinit var txtResultados: TextView

    private val esportes = listOf("--SELECIONE--", "Não pratica esporte", "Futebol", "Críquete", "Hóquei", "Vôlei", "Basquete", "Tênis", "Corrida", "Salto em altura", "Salto em distância", "Arremesso de dardo", "Arremesso de peso", "Handebol", "Boxe", "Tênis de mesa")

    private val votos = mutableMapOf<String, Int>().apply {
        esportes.forEach { put(it, 0) }
    }

    private var totalVotos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imgVoltar = findViewById<ImageView>(R.id.imgVoltar)
        imgVoltar.setOnClickListener {
            val intent = Intent(this, sobre::class.java)
            startActivity(intent)
        }

        etPrimeiroNome = findViewById(R.id.etPrimeiroNome)
        etSobrenome = findViewById(R.id.etSobrenome)
        etEmail = findViewById(R.id.etEmail)
        etAniversario = findViewById(R.id.etAniversario)
        spnEsporte = findViewById(R.id.spnEsporte)
        btnEnviar = findViewById(R.id.btnEnviar)
        txtResultados = findViewById(R.id.txtResultados)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            esportes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnEsporte.adapter = adapter

        btnEnviar.setOnClickListener {
            val esporteEscolhido = spnEsporte.selectedItem.toString()

            if (esporteEscolhido == esportes[0]) {
                return@setOnClickListener
            }

            votos[esporteEscolhido] = (votos[esporteEscolhido] ?: 0) + 1
            totalVotos++

            atualizarResumo()

            limparCampos()
        }

        atualizarResumo()

    }// fim oncreate

    private fun atualizarResumo() {
        val total = if (totalVotos == 0) 1 else totalVotos

        val builder = StringBuilder()
        builder.append("Total de votos: $totalVotos\n\n")

        votos.forEach { (esporte, qtd) ->
            if (esporte != esportes[0]) {
                val perc = (qtd * 100) / total
                builder.append("$esporte: $qtd // $perc%\n")
            }
        }

        txtResultados.text = builder.toString()
    }

    private fun limparCampos() {
        etPrimeiroNome.text.clear()
        etSobrenome.text.clear()
        etEmail.text.clear()
        etAniversario.text.clear()
        spnEsporte.setSelection(0)
    }

}// fim codigo