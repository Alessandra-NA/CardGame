package com.example.cardgame_crazy8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlin.random.Random
import android.os.Handler;



class MainActivity : AppCompatActivity() {

    class Carta(val num: Int, val symbol: String)
    class Player(val numP: Int, var cards:ArrayList<Carta>)

    var deck = ArrayList<Carta>()
    var cartaCentro = Carta(0,"")
    var p1 = Player(1, arrayListOf<Carta>())
    var p2 = Player(2, arrayListOf<Carta>())
    var p3 = Player(3, arrayListOf<Carta>())
    var listOfPlayers = arrayListOf(p1, p2, p3)
    var cartasAumentadas = 0

    fun llenarDeck(){
        deck = ArrayList<Carta>()
        for (i in 1..4){
            for (j in 1..13){
                when (i){
                    1 -> {
                        deck.add(Carta(j,"C"))
                    }
                    2 -> deck.add(Carta(j,"D"))
                    3 -> deck.add(Carta(j,"P"))
                    4 -> deck.add(Carta(j,"P"))
                }
            }
        }
        for(x in 0..51){
            print(deck.elementAt(x).symbol)
            print(deck.elementAt(x).num)
            print(", ")
        }
    }

    fun addCartaAlAzar(player: Player){
        var rand = Random.nextInt(0,deck.size)
        var crd = deck.get(rand)
        player.cards.add(crd)
        deck.remove(crd)
    }

    fun repartirDeck(){
        // Get random number
        for(p in listOfPlayers){
            for(n in 1..8){
                addCartaAlAzar(p)
            }
        }
    }

    fun primeraCarta(){
        var rand = Random.nextInt(0,deck.size)
        var crd = deck.get(rand)
        cartaCentro = crd
    }

    // ESTA NO SIRVE, estoy buscando otra forma para la condición 2
    /*fun verificarCartaElegida(crd: Carta): Boolean {
        // con el jugador 1, esto debería usarse para verificar que la carta cumple con los criterios
        // si sale true, se debe tirar la carta al medio, eliminarla del mazo del player y pasar al sgte jugador
        // si sale false, no pasa nada
        return if(crd.num == cartaCentro.num || crd.symbol == cartaCentro.symbol){
            true
        } else {
            var toast = Toast.makeText(this, "Elija otra carta.", Toast.LENGTH_SHORT)
            false
        }
    }*/

    //obtiene las posibles cartas y devuelve un array
    //si elije una carta verifica que este en el array
    //  si está, verifica si hay otra carta con el mismo numero, si no hay pasa al siguiente jugador
    //si la primera carta elegida no está en el array, sale el toast
    fun verificarCartaElegida(player: Player, cartaElegida: Carta){
        var arr = ArrayList<Carta>()
        for(i in player.cards){
            if(i.num == cartaCentro.num || i.symbol == cartaCentro.symbol){
                arr.add(i)
            }
        }
        if(arr.contains(cartaElegida)){
            var flag = false
            for(i in arr){
                if(i.num == cartaElegida.num) {
                    flag = true
                }
            }
            ponerCarta(player, cartaElegida)
            if(cartaElegida.num == 13) cartasAumentadas += 3
            if(flag && player.numP != 1) {
                var cartaIgualNumBot = buscarNumeroIgual(player)
                while ( cartaIgualNumBot != null){
                    ponerCarta(player,cartaIgualNumBot)
                    cartaIgualNumBot = buscarNumeroIgual(player)
                }
                flag = false
            }
            if(!flag) sgteJugador(player, cartaCentro.num, true)
        } else {
            var toast = Toast.makeText(this, "Elija otra carta.", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
    fun buscarNumeroIgual(player: Player): Carta?{
        for(i in player.cards) if(i.num == cartaCentro.num) return i
        return null
    }
    fun ponerCarta(player: Player, carta: Carta){
        player.cards.remove(carta)
        if(player.cards.size == 1) {
            var toast = Toast.makeText(this, "Jugador "+player.numP+" va por una.", Toast.LENGTH_SHORT)
            toast.show()
        }
        // CÓDIGO PARA MOSTRAR LA CARTA EN EL CENTRO DE LA PANTALLA
    }
    fun sgteJugador(player: Player, nroCarta: Int, aplicaSalto:Boolean){
        val handler = Handler()
        handler.postDelayed({
            when (player.numP){
                // considerando que 1 es el usuario
                1 -> {
                    if(nroCarta==11 && aplicaSalto) botJuega(p3)
                    else botJuega(p2)
                }
                2 -> {
                    if(nroCarta!=11 || (nroCarta == 11 && !aplicaSalto)) botJuega(p3)
                }
                3 -> {
                    if(nroCarta==11 && aplicaSalto) botJuega(p2)
                }
            }
        }, 1000)
    }
    fun buscarCarta(player: Player): Carta? {
        // busca 13 mismo palo
        for(i in player.cards) if(i.symbol == cartaCentro.symbol && i.num == 13) return i
        // busca 11 mismo palo
        for(i in player.cards) if(i.symbol == cartaCentro.symbol && i.num == 11) return i
        // busca mismo numero
        for(i in player.cards) if(i.num == cartaCentro.num) return i
        // busca mismo palo
        for(i in player.cards) if(i.symbol == cartaCentro.symbol) return i
        return null
    }
    fun botJuega(player: Player){
        var cartaElegida = buscarCarta(player)
        if(cartasAumentadas != 0){
            // si no tiene 13 o no tiene cartas, coge las cartas
            if((cartaElegida != null && cartaElegida.num != 13) || cartaElegida == null){
                for(x in 1..cartasAumentadas){
                    addCartaAlAzar(player)
                }
                cartasAumentadas = 0
                // vuelve a buscar la mejor carta
                cartaElegida = buscarCarta(player)
            }
            // si tiene 13
            else {
                verificarCartaElegida(player,cartaElegida)
            }
        }
        // si no hay 13 en el centro
        else{
            // si no tiene ninguna carta
            if(cartaElegida == null) {
                addCartaAlAzar(player)
                cartaElegida = buscarCarta(player)
                if(cartaElegida == null) sgteJugador(player,cartaCentro.num, false )
                else verificarCartaElegida(player,cartaElegida)
            }
            // si tiene cartas para poner
            else verificarCartaElegida(player,cartaElegida)
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