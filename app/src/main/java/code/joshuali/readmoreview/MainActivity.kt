package code.joshuali.readmoreview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(view: View) {
        findViewById<ReadMoreView>(R.id.readmore).setText("Thank you for using GitHub! ")
    }

}
