package com.shaale_vikas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shaale_vikas.databinding.ActivityMainBinding
import com.shaale_vikas.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityMainBinding

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityMainBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        if (savedInstanceState == null) {

            supportFragmentManager
                .beginTransaction()
                .replace(
                    binding.mainFragmentContainer.id,
                    HomeFragment()
                )
                .commit()
        }
    }
}