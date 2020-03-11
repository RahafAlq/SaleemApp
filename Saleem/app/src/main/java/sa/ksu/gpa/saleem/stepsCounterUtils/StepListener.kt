package sa.ksu.gpa.saleem.stepsCounterUtils


// Will listen to step alerts
interface StepListener {
    fun step(timeNs: Long)
}