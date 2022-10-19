package com.example.tipsy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var seekTipsy: SeekBar
    private lateinit var tipsyPercentage: TextView
    private lateinit var tipsyTipAmount: TextView
    private lateinit var totalTipsyAmount: TextView
    private lateinit var tipsyEmoji: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBaseAmount = findViewById(R.id.etBase)
        seekTipsy = findViewById(R.id.seekTipsy)
        tipsyPercentage = findViewById(R.id.tipsyPercentage)
        tipsyTipAmount = findViewById(R.id.tipsyTipAmount)
        totalTipsyAmount = findViewById(R.id.totalTipsyAmount)
        tipsyEmoji = findViewById(R.id.tipsyEmoji)

        seekTipsy.progress = INITIAL_TIP_PERCENT
        tipsyPercentage.text = "$INITIAL_TIP_PERCENT%"
        updateTipEmoji(INITIAL_TIP_PERCENT)
        seekTipsy.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(SeekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tipsyPercentage.text = "$progress%"
                computeTipAndTotal()
                updateTipEmoji(progress)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        etBaseAmount.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeTipAndTotal()
            }
        })
    }

    private fun updateTipEmoji(tipPercent: Int) {
        val tipsyDescription = when(tipPercent){
            // Emojis available from: https://emojipedia.org/
            in 0..9 -> "\uD83D\uDE44"
            in 10..14 -> "\uD83D\uDE1F"
            in 15..25 -> "\uD83D\uDE0A"
            else -> "\uD83E\uDD29"
        }
        tipsyEmoji.text = tipsyDescription
    }

    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()){
            tipsyTipAmount.text = ""
            totalTipsyAmount.text = ""
            return
        }
        // 1. Get the value of the Base and Tip Percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekTipsy.progress
        // 2. Compute Tip and Total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        // 3. Update the UI to show the value
        tipsyTipAmount.text = "%.2f".format(tipAmount)
        totalTipsyAmount.text = "%.2f".format(totalAmount)
    }
}