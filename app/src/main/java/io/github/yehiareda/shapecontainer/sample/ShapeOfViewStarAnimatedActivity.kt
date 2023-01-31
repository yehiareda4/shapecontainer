package io.github.yehiareda.shapecontainer.sample

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.yehiareda.shapecontainer.shapes.ArcView

class ShapeOfViewStarAnimatedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shape_of_view_star_wars_animated)

        val arcLayout = findViewById<ArcView>(R.id.arcLayout)
        if (arcLayout != null) {
            ValueAnimator.ofFloat(0f, -200f, 200f).apply {
                addUpdateListener { animation ->
                    arcLayout.arcHeight = (animation.animatedValue as Float)
                }
                duration = 5000
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE
            }.start()
        }
    }
}
