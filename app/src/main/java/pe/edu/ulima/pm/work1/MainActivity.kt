package pe.edu.ulima.pm.work1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import pe.edu.ulima.pm.work1.views.Carta
import pe.edu.ulima.pm.work1.views.OnCardClickListener
import pe.edu.ulima.pm.work1.views2.Deck
import pe.edu.ulima.pm.work1.views2.Jugador


import kotlin.random.Random


class MainActivity : AppCompatActivity(), OnCardClickListener {

    /*
    *
    * INTEGRANTES:
    * Alessandra Nuñez Aguero - 20181311
    * Alejandro Sarmiento de la Cruz - 20181771
    *
    *
     */

    var cartaCentro : Carta? = null
    var deck : Deck? = null
    var jugador1: Jugador? = null
    var jugador2: Jugador? = null
    var jugador3: Jugador? = null
    var listOfPlayers = ArrayList<Jugador>()
    var currentPlayer : Jugador? = null
    var cartasutilizadas = ArrayList<Carta>()

    var contenedorJ1: LinearLayout? = null
    var contenedorJ2: LinearLayout? = null
    var contenedor: LinearLayout? = null
    var contenedorM : LinearLayout?=null
    var acumuladas = 0
    var salto = false

    var flagLanzaMasDe1 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide();

        deck = Deck(this)
        jugador1 = Jugador(this,1)
        jugador2 = Jugador(this,2)
        jugador3 = Jugador(this,3)
        listOfPlayers = arrayListOf(jugador1!!, jugador2!!, jugador3!!)
        currentPlayer = jugador1

        llenarMazo()
        repartir()

        contenedorJ1 = findViewById<LinearLayout>(R.id.mesa1)
        contenedorJ2 = findViewById<LinearLayout>(R.id.mesa3)
        contenedor = findViewById<LinearLayout>(R.id.baraja)
        contenedorM = findViewById<LinearLayout>(R.id.mesa2)

        contenedorJ1!!.addView(jugador3)
        contenedorJ2!!.addView(jugador2)

        for(i in currentPlayer!!.cards){
            i.setOnCardClickListener(this)
            contenedor!!.addView(i)
        }
        contenedorM!!.addView(cartaCentro)
        contenedorM!!.addView(deck)
    }

    fun llenarMazo(){
        for (i in 1..4){
            for (j in 1..13){
                when (i){
                    1 -> deck!!.cardsDeck.add(Carta(this,j,"corazon"))
                    2 -> deck!!.cardsDeck.add(Carta(this,j,"rombo"))
                    3 -> deck!!.cardsDeck.add(Carta(this,j,"espada"))
                    4 -> deck!!.cardsDeck.add(Carta(this,j,"trebol"))
                }
            }
        }
        //revolver mazo
        deck!!.cardsDeck= deck!!.cardsDeck.shuffled() as ArrayList<Carta>
    }

    fun addCartaAlAzar(p: Jugador){
        VerificarMazo()

        var rand = Random.nextInt(0,deck!!.cardsDeck.size)
        var crd = deck!!.cardsDeck.get(rand)
        p.cards.add(crd)
        deck!!.cardsDeck.remove(crd)
        actualizarLabels()
    }

    fun VerificarMazo(){
        if(deck!!.cardsDeck.isEmpty()){
            deck!!.cardsDeck = this.cartasutilizadas.shuffled() as ArrayList<Carta>
            this.cartasutilizadas.removeAll(this.cartasutilizadas)
        }
    }

    fun actualizarLabels(){
        findViewById<TextView>(R.id.txtAcum).setText("Cartas acumuladas: "+acumuladas)
        findViewById<TextView>(R.id.txtTotal).setText("Cartas en mano: "+currentPlayer!!.cards.size)
        findViewById<TextView>(R.id.txtTurno).setText("Turno del jugador "+currentPlayer!!.numP)
    }

    fun repartir(){
        // repartiendo por jugador
        for(p in listOfPlayers){
            for (i in  1..8){
                addCartaAlAzar(p)
            }
        }
        // poniendo una carta en el medio
        var rand = Random.nextInt(0,deck!!.cardsDeck.size)
        var crd = deck!!.cardsDeck.get(rand)
        cartaCentro = crd

        actualizarLabels()
        revisarSiPuedeLanzar2()
    }

    fun buscarCartaBaraja(mazo: ArrayList<Carta>,numero: Int, tipo:String):Int{
        var lugar = 0
        if(tipo==""){
            for(i in mazo) {
                if(i.numero==numero) return lugar
                else lugar +=1
            }
        } else {
            for(i in mazo){
                if(i.numero==numero && i.tipo==tipo) return lugar
                else lugar+=1
            }
        }
        return -1
    }


    fun quitarCarta(player: Jugador, posicion: Int){
        if(player.cards.size>1){
            //anhadiendo a monton de cartas utilizadas
            this.cartasutilizadas.add(cartaCentro!!)
            //actualizando cartas central
            cartaCentro = player.cards.get(posicion)
            player.cards.removeAt(posicion)
        }
        else   Victoria() //Toast.makeText(this,"¡Ganaste!",Toast.LENGTH_LONG).show()
        if(player.cards.size == 1){
            Toast.makeText(this,"Jugador "+player.numP+" va por una",Toast.LENGTH_LONG).show()
        }
    }
//
    fun lanzarCarta(numero: Int, tipo: String, player: Jugador): Boolean{
        // primer lanzamiento
        if(!flagLanzaMasDe1){
            if(cartaCentro!!.numero==numero || cartaCentro!!.tipo==tipo) {
                quitarCarta(currentPlayer!!, buscarCartaBaraja(currentPlayer!!.cards, numero, tipo))
                if (numero == 13) {
                    acumuladas += 3
                } else if (numero == 11) {
                    salto = true
                }
                ReloadAgain()
                return true
            }else{
                Toast.makeText(this,"Carta inválida para lanzar",Toast.LENGTH_LONG).show()
                return false
            }
        }
        // más de 1 lanzamiento
        else{
            // se puede tirar si es el mismo numero
            if(numero == cartaCentro?.numero){
                quitarCarta(currentPlayer!!, buscarCartaBaraja(currentPlayer!!.cards, numero, tipo))
                if(numero == 13){
                    acumuladas += 3
                }
                ReloadAgain()
                return true
            }
            // no se pueden lanzar otros numeros del mismo simbolo
            else if (tipo == cartaCentro?.tipo) {
                Toast.makeText(this,"Solo puedes seguir lanzando cartas del mismo número",Toast.LENGTH_LONG).show()
                return false
            }
            else {
                Toast.makeText(this,"Carta inválida para lanzar",Toast.LENGTH_LONG).show()
                return false
            }
        }
    }

    fun revisarSiPuedeLanzar(): Boolean{
        for(i in currentPlayer!!.cards){
            if(i.tipo == cartaCentro!!.tipo || i.numero == cartaCentro!!.numero) return true
        }
        return false
    }

    fun verificarSiRecibeCartasAcumuladas(){
        if(acumuladas != 0){
            var pos = buscarCartaBaraja(currentPlayer!!.cards,13, "")
            if (pos == -1){
                Toast.makeText(this,"No tienes ningún 13. Añadiendo cartas acumuladas...",Toast.LENGTH_LONG).show()
                for (i in 1..acumuladas) addCartaAlAzar(currentPlayer!!)
                Handler().postDelayed({
                    contenedor!!.removeAllViews()
                    for(i in currentPlayer!!.cards){
                        i.setOnCardClickListener(this)
                        contenedor!!.addView(i)
                    }
                    actualizarLabels()
                }, 3500)
                acumuladas = 0
            } else {
                Toast.makeText(this,"¡Tienes un 13!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun revisarSiPuedeLanzar2(){
        if(!revisarSiPuedeLanzar()){
            Toast.makeText(this,"No tienes cartas para lanzar, añadiendo una...",Toast.LENGTH_LONG).show()
            addCartaAlAzar(currentPlayer!!)
            Handler().postDelayed({
                contenedor!!.removeAllViews()
                for(i in currentPlayer!!.cards){
                    i.setOnCardClickListener(this)
                    contenedor!!.addView(i)
                }
            },2000)
            Handler().postDelayed({
                if(!revisarSiPuedeLanzar()){
                    Toast.makeText(this,"No tienes cartas para lanzar, pasando al sgte. jugador...",Toast.LENGTH_LONG).show()
                    Handler().postDelayed({ reLoad() }, 6000)
                }
            }, 2000)
        }
    }

    fun reLoad(){
        contenedor!!.removeAllViews()
        contenedorM!!.removeAllViews()
        contenedorJ1!!.removeAllViews()
        contenedorJ2!!.removeAllViews()

        var numJugador = currentPlayer!!.numP
        if((numJugador==1 && !salto) || (numJugador == 3 && salto)){
            currentPlayer = jugador2
            contenedorJ1!!.addView(jugador1)
            contenedorJ2!!.addView(jugador3)
        }

        else if((numJugador==2 && !salto) || (numJugador == 1 && salto)){
            currentPlayer = jugador3
            contenedorJ1!!.addView(jugador2)
            contenedorJ2!!.addView(jugador1)
        }
        else if((numJugador==3 && !salto) || (numJugador == 2 && salto)){
            contenedorJ1!!.addView(jugador3)
            contenedorJ2!!.addView(jugador2)
            currentPlayer = jugador1
        }
        for(i in currentPlayer!!.cards){
            i.setOnCardClickListener(this)
            contenedor!!.addView(i)
        }
        contenedorM!!.addView(cartaCentro)
        contenedorM!!.addView(deck)
        if (salto) salto = false
        actualizarLabels()
        verificarSiRecibeCartasAcumuladas()
        revisarSiPuedeLanzar2()
    }

    fun VerificarOtroLanzamiento():Boolean {
        // no se permitirá lanzar más de una carta J
            for (i in currentPlayer!!.cards){
                if(this.cartaCentro!!.numero == i.numero) {
                    flagLanzaMasDe1 = true
                    return true
                }
            }
        flagLanzaMasDe1 = false
        return false
    }

    fun ReloadAgain(){
        contenedor!!.removeAllViews()
        contenedorM!!.removeAllViews()
        for(i in currentPlayer!!.cards){
            i.setOnCardClickListener(this)
            contenedor!!.addView(i)
        }
        contenedorM!!.addView(cartaCentro)
        contenedorM!!.addView(deck)
        actualizarLabels()
    }


    fun Victoria(){
        val intent: Intent =Intent()
        intent.setClass(this,Victoria::class.java)
        var bundle: Bundle = Bundle()
        bundle.putString("ganador",this.currentPlayer!!.numP.toString())
        intent.putExtra("data",bundle)
        startActivity(intent)
    }

    var i = 0
    override fun onClick(numero: Int,tipo :String) {
        i++
        Handler().postDelayed( Runnable {
            if(i==2){
                //dos clicks
                var flag = lanzarCarta(numero, tipo, currentPlayer!!)
                if (VerificarOtroLanzamiento()){
                    ReloadAgain()
                }
                else if ( flag && numero == 13){
                    Toast.makeText(this,"Siguiente jugador se lleva "+this.acumuladas.toString()+" cartas",Toast.LENGTH_LONG).show()
                    Handler().postDelayed({ reLoad() }, 2000)
                } else if ( flag && numero == 11){
                    Toast.makeText(this,"Saltando al siguiente jugador...",Toast.LENGTH_LONG).show()
                    Handler().postDelayed({ reLoad() }, 2000)
                } else if ( !flag){
                    // que no haga nada y se manda mensaje de que la carta no es valida
                }
                else reLoad()
            }
            i=0
        },500)
    }

}