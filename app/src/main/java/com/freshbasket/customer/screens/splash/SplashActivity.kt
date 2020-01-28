package com.freshbasket.customer.screens.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.freshbasket.customer.MyApplication.Companion.context
import com.freshbasket.customer.R
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.screens.home.HomeActivity
import com.freshbasket.customer.screens.login.LoginActivity
import com.freshbasket.customer.util.CommonUtils

class SplashActivity : AppCompatActivity() {
    private val TAG = "SplashActivity"
    private var mContext: Context? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext = this@SplashActivity
    }


    override fun onResume() {
        super.onResume()
        navigateScreen()


    }

    private fun navigateScreen() {

        Thread(Runnable {
            Thread.sleep(3000)
            this@SplashActivity.runOnUiThread {
                if (CommonUtils.isUserLogin())
                    navigateToHomeScreen()
                else
                    navigateToLoginScreen()
            }
        }).start()
    }

    fun navigateToLoginScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


    fun navigateToHomeScreen() {
        //startActivity(Intent(this, HomeActivity::class.java))
        val intent = Intent(context, HomeActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.SPLASH_SCREEN)
        }
        startActivity(intent)
        finish()
    }

}
