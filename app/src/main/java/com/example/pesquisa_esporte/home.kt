package com.example.pesquisa_esporte

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class home : AppCompatActivity() {

    private lateinit var etPrimeiroNome: EditText
    private lateinit var etSobrenome: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAniversario: EditText
    private lateinit var spnEsporte: Spinner
    private lateinit var btnEnviar: Button
    private lateinit var txtResultados: TextView

    private val placeholder = "--SELECIONE--"
    private val esportes = listOf(
        placeholder, "Não pratica esporte", "Futebol", "Críquete", "Hóquei", "Vôlei",
        "Basquete", "Tênis", "Corrida", "Salto em altura", "Salto em distância",
        "Arremesso de dardo", "Arremesso de peso", "Handebol", "Boxe", "Tênis de mesa"
    )

    private val votos = esportes.associateWith { 0 }.toMutableMap()
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

        findViewById<ImageView>(R.id.imgVoltar).setOnClickListener {
            startActivity(Intent(this, sobre::class.java))
        }

        etPrimeiroNome = findViewById(R.id.etPrimeiroNome)
        etSobrenome = findViewById(R.id.etSobrenome)
        etEmail = findViewById(R.id.etEmail)
        etAniversario = findViewById(R.id.etAniversario)
        spnEsporte = findViewById(R.id.spnEsporte)
        btnEnviar = findViewById(R.id.btnEnviar)
        txtResultados = findViewById(R.id.txtResultados)

        spnEsporte.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            esportes
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        btnEnviar.setOnClickListener {
            if (!validarCampos()) return@setOnClickListener

            val esporteEscolhido = spnEsporte.selectedItem.toString()
            votos[esporteEscolhido] = (votos[esporteEscolhido] ?: 0) + 1
            totalVotos++

            atualizarResumo()
            limparCampos()
        }
        atualizarResumo()
    }

    private fun validarCampos(): Boolean {
        val nome = etPrimeiroNome.text.toString().trim()
        val sobrenome = etSobrenome.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val aniversario = etAniversario.text.toString().trim()
        val esporteEscolhido = spnEsporte.selectedItem.toString()

        return when {
            nome.isEmpty() || sobrenome.isEmpty() || email.isEmpty() || aniversario.isEmpty() -> {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                false
            }
            esporteEscolhido == placeholder -> {
                Toast.makeText(this, "Selecione um esporte válido!", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun atualizarResumo() {
        val total = if (totalVotos == 0) 1 else totalVotos

        val resumo = votos
            .filterKeys { it != placeholder }
            .map { (esporte, qtd) ->
                val perc = (qtd * 100) / total
                String.format(Locale.getDefault(), "%s: %d // %d%%", esporte, qtd, perc)
            }
            .joinToString("\n")

        txtResultados.text = "Total de votos: $totalVotos\n\n$resumo"
    }

    private fun limparCampos() {
        etPrimeiroNome.text.clear()
        etSobrenome.text.clear()
        etEmail.text.clear()
        etAniversario.text.clear()
        spnEsporte.setSelection(0)
    }
}
