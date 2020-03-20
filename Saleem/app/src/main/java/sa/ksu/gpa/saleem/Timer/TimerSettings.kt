package sa.ksu.gpa.saleem.Timer

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import sa.ksu.gpa.saleem.R

class TimerSettings : AppCompatActivity() {
    private lateinit var prepare: EditText
    private lateinit var active: EditText
    private lateinit var rest: EditText
    private lateinit var round: EditText
    private lateinit var restRound: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_settings)
        prepare=findViewById(R.id.timer_prepaer)
        active= findViewById(R.id.timer_active)
        rest=findViewById(R.id.timer_rest)
        round=findViewById(R.id.timer_round)
        restRound=findViewById(R.id.timer_rest_round)
    }
}
