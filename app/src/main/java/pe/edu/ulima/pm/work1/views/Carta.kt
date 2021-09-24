package pe.edu.ulima.pm.work1.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import pe.edu.ulima.pm.work1.R

interface OnCardClickListener{
    //para que cada carta tenga datos especificos
    fun onClick(number: Int,type: String)
}

class Carta  : View {
    private val naint: Paint = Paint()
    var tipo : String? = "corazon"
    private var imageT:Bitmap? = null
    var numero : Int? = 1
    private var listener: OnCardClickListener? = null

    constructor(context: Context, attrs: AttributeSet):super (context,attrs){
            val a = context!!.theme.obtainStyledAttributes(attrs, R.styleable.Carta,0,0)
            tipo = a.getString(R.styleable.Carta_tipoA)
            numero = a.getInt(R.styleable.Carta_numeroA,0)
            //aqui define tus cartas para probar

    }

    constructor(context:Context,numeroT:Int,tipoT:String):super(context){
        numero = numeroT
        tipo = tipoT
    }

    fun ElegirImagen(){
        if(tipo.equals("rombo")){
            imageT = BitmapFactory.decodeResource(getResources(), R.drawable.rombo)
        }
        else if(tipo.equals("espada")){
            imageT = BitmapFactory.decodeResource(getResources(), R.drawable.espada)
        }
        else if(tipo.equals("trebol")){
            imageT = BitmapFactory.decodeResource(getResources(), R.drawable.trebol)
        }
        else{
            imageT = BitmapFactory.decodeResource(getResources(), R.drawable.corazon)
        }
    }


    fun setOnCardClickListener(listener: OnCardClickListener){
        this.listener=listener
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        listener?.onClick(numero!!,tipo!!)
        invalidate()
        return super.onTouchEvent(event)
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(300,400)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        ElegirImagen()
        drawCorazon(canvas)
    }

    private fun drawCorazon(canvas: Canvas?) {
        naint.color = Color.parseColor("#2f6133")
        naint.style = Paint.Style.FILL
        val fondo = RectF(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
        )
        canvas?.drawRect(fondo,naint)
        naint.color = Color.BLACK
        val part = RectF(
            10f,
            0f,
            width.toFloat(),
            height.toFloat(),
        )
        canvas?.drawRect(part,naint)
        naint.color = Color.WHITE
        val marco = RectF(
            20f,
            10f,
            width.toFloat()-20f,
            height.toFloat()-20f,
        )
        canvas?.drawRect(marco,naint)

        naint.setTextSize(60f)
        naint.setStyle(Paint.Style.FILL)

//decision de color de escritura
        if(tipo.equals("corazon") || tipo.equals("rombo")){
            naint.setColor(Color.RED)
        }
        else{
            naint.setColor(Color.BLACK)
        }

//escritura de numero
        if(numero!!<=10){
            canvas?.drawText(numero.toString(),width.toFloat()-90,80f, naint)
        }
        else if(numero==11){
            canvas?.drawText("J",width.toFloat()-70f,80f, naint)
        }
        else if(numero==12){
            canvas?.drawText("Q",width.toFloat()-70f,80f, naint)
        }
        else{
            canvas?.drawText("K",width.toFloat()-70f,80f, naint)
        }

//decision de imagen
        val resizedBitmap = Bitmap.createScaledBitmap(imageT!!, 100, 100, false);
        canvas?.drawBitmap(resizedBitmap, (width-100)/2f,(height-100)/2f,null)
    }

}