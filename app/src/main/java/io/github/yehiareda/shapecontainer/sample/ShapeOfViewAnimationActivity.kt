package io.github.yehiareda.shapecontainer.sample

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.yehiareda.shapecontainer.shapes.RoundRectView

class ShapeOfViewAnimationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shape_of_view_activity_anim)

        val roundRect = findViewById<RoundRectView>(R.id.roundRect)
        if (roundRect != null) {
            ValueAnimator.ofFloat(0f, 200f, 0f).apply {
                addUpdateListener { animation ->
                    roundRect.bottomStartRadius = (animation.animatedValue as Float)
                }
//                duration = 800
//                repeatCount = ValueAnimator.INFINITE
//                repeatMode = ValueAnimator.REVERSE
            }.start()
        }
        roundRect.setOnClickListener {
            startActivity(Intent(this, ShapeOfViewAnimationActivity::class.java))
        }
    }
}
