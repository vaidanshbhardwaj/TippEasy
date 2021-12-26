package com.example.tippeasy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmt: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipAmt: TextView
    private lateinit var tvTotalAmt: TextView
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmt = findViewById(R.id.etBaseAmt)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipAmt = findViewById(R.id.tvTipAmt)
        tvTotalAmt = findViewById(R.id.tvTotalAmt)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                tvTipPercentLabel.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        etBaseAmt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeTipAndTotal()
            }

        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent){
            in 0..9 -> "Poor"
            in 10..14 ->"Acceptable"
            in 15..19 ->"Good"
            in 20..24 ->"Great"
            else -> "Splendid"
        }
        tvTipDescription.text = tipDescription
//        Updating the color based on the tipPercent
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat()/seekBarTip.max,
            ContextCompat.getColor(this,R.color.color_worst_tip),
            ContextCompat.getColor(this,R.color.color_best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if(etBaseAmt.text.isEmpty()){
            tvTipAmt.text = ""
            tvTotalAmt.text = ""
            return
        }
//        1. Getting the value of the base and tip percent
        val baseAmt = etBaseAmt.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
//        2. Computing the tip and total
        val tipAmt =  baseAmt * tipPercent / 100
        val totalAmt = tipAmt + baseAmt
//        3. Updating the UI
        tvTipAmt.text = "%.2f".format(tipAmt)
        tvTotalAmt.text = "%.2f".format(totalAmt)
    }
}
