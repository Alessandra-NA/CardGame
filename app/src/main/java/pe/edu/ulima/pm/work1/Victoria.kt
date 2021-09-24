package pe.edu.ulima.pm.work1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Victoria:AppCompatActivity() {
    var ganador:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_victoria)
        getSupportActionBar()?.hide();

        ganador=intent.getBundleExtra("data")?.getString("ganador")
        findViewById<TextView>(R.id.textView).text="El ganador es el jugador "+ganador
    }
}