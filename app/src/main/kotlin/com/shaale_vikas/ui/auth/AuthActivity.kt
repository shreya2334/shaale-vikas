package com.shaale_vikas.ui.auth
import com.shaale_vikas.utils.toast
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shaale_vikas.databinding.ActivityAuthBinding
import com.shaale_vikas.MainActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityAuthBinding

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityAuthBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        if (savedInstanceState == null) {

            supportFragmentManager
                .beginTransaction()
                .replace(
                    binding.authFragmentContainer.id,
                    LoginFragment()
                )
                .commit()
        }
    }

    fun goToRegister() {

        supportFragmentManager
            .beginTransaction()
            .replace(
                binding.authFragmentContainer.id,
                RegisterFragment()
            )
            .addToBackStack(null)
            .commit()
    }

    fun goToMain() {

        toast("Opening Main Screen")

        startActivity(
            Intent(
                this,
                com.shaale_vikas.MainActivity::class.java
            )
        )

        finish()
    }
}