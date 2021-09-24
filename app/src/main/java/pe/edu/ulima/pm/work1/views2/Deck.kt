package pe.edu.ulima.pm.work1.views2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import pe.edu.ulima.pm.work1.R
import pe.edu.ulima.pm.work1.views.Carta

class Deck : View {
    private val caint: Paint = Paint()
    var imageT : Bitmap? =null
    private var size :Int=0
    var cardsDeck: ArrayList<Carta> = ArrayList()

    constructor(context: Context, attrs: AttributeSet):super (context,attrs){
        val a = context!!.theme.obtainStyledAttributes(attrs, R.styleable.Deck,0,0)
        //aqui define tus cartas para probar
    }

    constructor(context:Context):super(context){
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(300,400)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        imageT = BitmapFactory.decodeResource(getResources(), R.drawable.espalda)
        val resizedBitmap = Bitmap.createScaledBitmap(imageT!!, 300, 400, false);
        canvas?.drawBitmap(resizedBitmap, 10f,0f,null)
    }
}