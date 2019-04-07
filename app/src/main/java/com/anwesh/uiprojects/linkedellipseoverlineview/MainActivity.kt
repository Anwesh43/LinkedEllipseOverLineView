package com.anwesh.uiprojects.linkedellipseoverlineview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.ellipseoverlineview.EllipseOverLineView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EllipseOverLineView.create(this)
    }
}
