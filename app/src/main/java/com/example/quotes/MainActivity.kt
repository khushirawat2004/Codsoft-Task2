package com.example.quotes

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.quotes.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        binding.nextBtn.setOnClickListener {
            getQuote()
        }

        // Load initial quote
        getQuote()
    }

    private fun getQuote() {
        setInProgress(true)
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.quoteApi.getRandomQuotes()
                runOnUiThread {
                    setInProgress(false)
                    response.body()?.first()?.let {
                        setUI(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // Log the exception
                runOnUiThread {
                    setInProgress(false)
                }
            }
        }
    }

    private fun setUI(quote: QuoteModel) {
        binding.quoteTv.text = quote.q
        binding.authorTv.text = quote.a
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.nextBtn.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.nextBtn.visibility = View.VISIBLE
        }
    }
}
