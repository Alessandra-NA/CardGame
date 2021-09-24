package pe.edu.ulima.pm.work1.views2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import pe.edu.ulima.pm.work1.R
import pe.edu.ulima.pm.work1.views.Carta


class Jugador : View {
    private val caint: Paint = Paint()
    private var size :Int=0
    var numP: Int = 0
    var cards: ArrayList<Carta> = ArrayList()

    constructor(context: Context, attrs: AttributeSet):super (context,attrs){
        val a = context!!.theme.obtainStyledAttributes(attrs, R.styleable.Jugador,0,0)
        //aqui define tus cartas para probar
    }

    constructor(context:Context,numP:Int):super(context){
        this.numP = numP

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        size = Math.min(width,height)
        setMeasuredDimension(500,400)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCasilla(canvas)
    }

    private fun drawCasilla(canvas:Canvas?){
        var imageT : Bitmap? = null
        if(this.numP == 1){
            imageT = BitmapFactory.decodeResource(getResources(), R.drawable.joystickgreen)
        } else if(this.numP == 2){
            imageT = BitmapFactory.decodeResource(getResources(), R.drawable.joystickblue)
        } else {
            imageT = BitmapFactory.decodeResource(getResources(), R.drawable.joystickpurple)
        }
        val resizedBitmap = Bitmap.createScaledBitmap(imageT!!, 200, 200, false);
        canvas?.drawBitmap(resizedBitmap, 150f,110f,null)

        caint.setTextSize(70f)
        caint.setStyle(Paint.Style.FILL)

        caint.setColor(Color.parseColor("#ced6ce"))
        var texto = "Jugador "+numP
        canvas?.drawText(texto,90f,100f, caint)
        caint.setColor(Color.parseColor("#192b1e"))
        caint.setTextSize(45f)
        texto = "Cantidad de cartas: "+ cards.size
        canvas?.drawText(texto,size/10f,370f,caint)

    }

}