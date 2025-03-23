package com.example.conecta4

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.conecta4.databinding.ActivityGameBinding
import com.google.android.material.button.MaterialButton

/**
 * Actividad principal del juego.
 * @author DianDev
 * @version 1.0
 * @date 2023-11-15
 */
class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var buttonResetScore: Button
    private lateinit var buttonResetGame: Button
    private lateinit var textViewScoreRed: TextView
    private lateinit var textViewScoreYellow: TextView
    // Lista de todos los botones
    private lateinit var botones: List<MaterialButton>
    private var turnoRojo = true // Porque la aprtida la comienza siempre la ROJA
    private var scoreRojo = 0
    private var scoreAmarillo = 0
    // Lista de las columnas de botones
    private val columnas = listOf(
        listOf(0, 7, 14, 21, 28, 35),// Columna 1: botones 1, 8, 15, 22, 29, 36
        listOf(1, 8, 15, 22, 29, 36),// Columna 2: botones 2, 9, 16, 23, 30, 37
        listOf(2, 9, 16, 23, 30, 37),// Columna 3: botones 3, 10, 17, 24, 31, 38
        listOf(3, 10, 17, 24, 31, 38),// Columna 4: botones 4, 11, 18, 25, 32, 39
        listOf(4, 11, 18, 25, 32, 39),// Columna 5: botones 5, 12, 19, 26, 33, 40
        listOf(5, 12, 19, 26, 33, 40),// Columna 6: botones 6, 13, 20, 27, 34, 41
        listOf(6, 13, 20, 27, 34, 41)// Columna 7: botones 7, 14, 21, 28, 35, 42
    )
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        botones = listOf(
            binding.button, binding.button2, binding.button3, binding.button4, binding.button5, binding.button6, binding.button7,
            binding.button8, binding.button9, binding.button10, binding.button11, binding.button12, binding.button13, binding.button14,
            binding.button15, binding.button16, binding.button17, binding.button18, binding.button19, binding.button20, binding.button21,
            binding.button22, binding.button23, binding.button24, binding.button25, binding.button26, binding.button27, binding.button28,
            binding.button29, binding.button30, binding.button31, binding.button32, binding.button33, binding.button34, binding.button35,
            binding.button36, binding.button37, binding.button38, binding.button39, binding.button40, binding.button41, binding.button42
        )

        buttonResetScore = binding.buttonResetScore
        buttonResetGame = binding.buttonRestartGame
        textViewScoreRed = binding.textScoreRed
        textViewScoreYellow = binding.textScoreYellow

        buttonResetScore.setOnClickListener {
            scoreRojo = 0
            scoreAmarillo = 0
            actualizarPuntaje()

        }
        buttonResetGame.setOnClickListener {
            reiniciarTablero()

        }
        for (button in botones) {
            button.setOnClickListener {
                val columna = tenerColumna(button)
                if (columna != -1) {
                    colorearBotones(columna)
                }
            }
        }
        reiniciarTablero()
    }

    /**
     * Colorea el botón correspondiente en la columna seleccionada.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun colorearBotones(columna: Int) {
        for (i in columnas[columna].indices.reversed()) {
            val boton = botones[columnas[columna][i]]
            // Para mí
            Log.d("Boton", "El boton es: $boton")
            if (boton.backgroundTintList == null) {
                boton.backgroundTintList = if (turnoRojo) {
                    Color.valueOf(Color.RED).toArgb()
                } else {
                    Color.valueOf(Color.YELLOW).toArgb()
                }.let { color -> android.content.res.ColorStateList.valueOf(color) }

                if (verificarGanador(columna, i)) {
                    if (turnoRojo) {
                        scoreRojo++
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Rojo gana")
                        builder.setPositiveButton("Aceptar") { dialog: DialogInterface, _: Int ->
                            dialog.dismiss()
                        }
                        val dialog = builder.create()
                        dialog.show()

                    } else {
                        scoreAmarillo++
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Amarillo gana")
                        builder.setPositiveButton("Aceptar") { dialog: DialogInterface, _: Int ->
                            dialog.dismiss()
                        }
                        val dialog = builder.create()
                        dialog.show()

                    }
                    actualizarPuntaje()
                    reiniciarTablero()
                } else {
                    turnoRojo = !turnoRojo
                }
                break
            }
        }
    }

    /**
     * Reinicia el tablero a su estado inicial.
     */
    private fun reiniciarTablero() {
        for (button in botones){
            button.backgroundTintList = null
        }
        turnoRojo = true
    }

    /**
     * Verifica si hay un ganador en la columna y fila dadas.
     */
    private fun verificarGanador(columna: Int, fila: Int): Boolean {
        val colorActual = botones[columnas[columna][fila]].backgroundTintList?.defaultColor

        return (verificarLinea(columna, fila, 1, 0, colorActual) || // Horizontal
                verificarLinea(columna, fila, 0, 1, colorActual) || // Vertical
                verificarLinea(columna, fila, 1, 1, colorActual) || // Diagonal derecha
                verificarLinea(columna, fila, 1, -1, colorActual))  // Diagonal izquierda
    }

    /**
     * Verifica si hay un ganador en una línea en una dirección específica.
     */
    private fun verificarLinea(columna: Int, fila: Int, dirCol: Int, dirFila: Int, color: Int?): Boolean {
        var contador = 1

        // Verificar en la dirección positiva
        var columnaActual = columna + dirCol
        var filaActual = fila + dirFila
        while (columnaActual in 0..6 && filaActual in 0..5 && botones[columnas[columnaActual][filaActual]].backgroundTintList?.defaultColor == color) {
            contador++
            columnaActual += dirCol
            filaActual += dirFila
            // Para mí
            Log.d("Contador", "El contador es: $contador")
            Log.d("Columna", "La columna actual es: $columnaActual")
            Log.d("Fila", "La fila actual es: $filaActual")
        }

        // Verificar en la dirección negativa
        columnaActual = columna - dirCol
        filaActual = fila - dirFila
        while (columnaActual in 0..6 && filaActual in 0..5 && botones[columnas[columnaActual][filaActual]].backgroundTintList?.defaultColor == color) {
            contador++
            columnaActual -= dirCol
            filaActual -= dirFila
            // Para mí
            Log.d("Contador", "El contador es: $contador")
            Log.d("Columna", "La columna actual es: $columnaActual")
            Log.d("Fila", "La fila actual es: $filaActual")
        }

        return contador >= 4
    }
    /**
     * Actualiza el puntaje en la interfaz de usuario.
     */
    private fun actualizarPuntaje() {
        textViewScoreRed.text = scoreRojo.toString()
        textViewScoreYellow.text = scoreAmarillo.toString()
    }

    /**
     * Devuelve la columna a la que pertenece el botón.
     */
    private fun tenerColumna(button: MaterialButton): Int {
        val indice = botones.indexOf(button)
        for (i in columnas.indices){
            if (indice in columnas[i]){
                // Para mí
                Log.d("Columna", "La columna es: $i")
                return i
            }
        }
        return -1
    }
}