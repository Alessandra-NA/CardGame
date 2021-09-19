package com.example.cardgame_crazy8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlin.random.Random



class MainActivity : AppCompatActivity() {

    class Carta(val num: Int, val symbol: String)
    class Player(val numP: Int, var cards:ArrayList<Carta>)

    var deck = ArrayList<Carta>()
    fun llenarDeck(){
        deck = ArrayList<Carta>()
        for (i in 1..4){
            for (j in 1..13){
                when (i){
                    1 -> deck.add(Carta(j,"C"))
                    2 -> deck.add(Carta(j,"D"))
                    3 -> deck.add(Carta(j,"P"))
                    4 -> deck.add(Carta(j,"P"))
                }
            }
        }
        /*for(x in 0..51){
            print(deck.elementAt(x).symbol)
            print(deck.elementAt(x).num)
            print(", ")
        }*/
    }
    var p1 = Player(1, arrayListOf<Carta>())
    var p2 = Player(2, arrayListOf<Carta>())
    var p3 = Player(3, arrayListOf<Carta>())
    var listOfPlayers = arrayListOf(p1, p2, p3)
    fun repartirDeck(){
        // Get random number
        for(p in listOfPlayers){
            for(n in 1..8){
                var rand = Random.nextInt(0,deck.size)
                var crd = deck.get(rand)
                p.cards.add(crd)
            }
        }
    }
    var cartaCentro = Carta(0,"")
    fun primeraCarta(){
        var rand = Random.nextInt(0,deck.size)
        var crd = deck.get(rand)
        cartaCentro = crd
    }

    // ESTA NO SIRVE, estoy buscando otra forma para la condición 2
    fun verificarCartaElegida(crd: Carta): Boolean {
        // con el jugador 1, esto debería usarse para verificar que la carta cumple con los criterios
        // si sale true, se debe tirar la carta al medio, eliminarla del mazo del player y pasar al sgte jugador
        // si sale false, no pasa nada
        return if(crd.num == cartaCentro.num || crd.symbol == cartaCentro.symbol){
            true
        } else {
            var toast = Toast.makeText(this, "Elija otra carta.", Toast.LENGTH_SHORT)
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botoncito = findViewById(R.id.botoncito) as Button
        botoncito.setOnClickListener{
            llenarDeck()
        }
    }
}