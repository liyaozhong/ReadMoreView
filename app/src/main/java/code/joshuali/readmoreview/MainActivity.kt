package code.joshuali.readmoreview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    lateinit var readmoreView : ReadMoreView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readmoreView = findViewById(R.id.readmore)
        findViewById<SeekBar>(R.id.left_padding_seekbar).setOnSeekBarChangeListener(this)
        findViewById<SeekBar>(R.id.right_padding_seekbar).setOnSeekBarChangeListener(this)
        findViewById<SeekBar>(R.id.top_padding_seekbar).setOnSeekBarChangeListener(this)
        findViewById<SeekBar>(R.id.bottom_padding_seekbar).setOnSeekBarChangeListener(this)
    }

    fun onClick(view: View) {
        findViewById<ReadMoreView>(R.id.readmore).setText("Thank you for using GitHub! We're happy you're here. Please read this Terms of Service agreement carefully before accessing or using GitHub. Because it is such an important contract between us and our users, we have tried to make it as clear as possible. For your convenience, we have presented these terms in a short non-binding summary followed by the full legal terms.")
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.tag) {
            "left_padding" -> readmoreView.setPadding(seekBar.progress, readmoreView.paddingTop, readmoreView.paddingRight, readmoreView.paddingBottom)
            "right_padding" -> readmoreView.setPadding(readmoreView.paddingLeft, readmoreView.paddingTop, seekBar.progress, readmoreView.paddingBottom)
            "top_padding" -> readmoreView.setPadding(readmoreView.paddingLeft, seekBar.progress, readmoreView.paddingRight, readmoreView.paddingBottom)
            "bottom_padding" -> readmoreView.setPadding(readmoreView.paddingLeft, readmoreView.paddingTop, readmoreView.paddingRight, seekBar.progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

}
