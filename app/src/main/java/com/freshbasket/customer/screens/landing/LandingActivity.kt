package com.freshbasket.customer.screens.landing

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.freshbasket.customer.BuildConfig
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.model.CustomerRes
import com.freshbasket.customer.model.inputmodel.SocialSignInputModel
import com.freshbasket.customer.screens.addressselection.SelectAddressActivity
import com.freshbasket.customer.screens.base.SubBaseActivity
import com.freshbasket.customer.screens.home.HomeActivity
import com.freshbasket.customer.util.CommonUtils
import com.freshbasket.customer.util.MyLogUtils
import com.freshbasket.customer.util.Validation
import com.freshbasket.customer.util.showToastMsg
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.app_bar_home.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class LandingActivity : SubBaseActivity(), GoogleApiClient.OnConnectionFailedListener, LandingContract.View {

    private var socialType: String? = null
    private val GMAIL_REQUEST_CODE = 2
    private val FACEBBOK_REQUEST_CODE = 1
    private var callbackManager: CallbackManager? = null
    private val TAG = "LandingActivity"
    private var mAuth: FirebaseAuth? = null
    private lateinit var mContext: Context
    private var currentUser: FirebaseUser? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var source: String? = null
    private var socialSignInputModel: SocialSignInputModel? = null
    private lateinit var presenter: LandingPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_landing, fragment_layout)
        setToolBarTittle(getString(R.string.landing_toolbar))
        hideCartView()
        mContext = this
        callbackManager = CallbackManager.Factory.create()
        if (intent != null) {
            source = intent.getStringExtra(Constants.SOURCE)

        }
        init()
    }

    override fun init() {
        presenter = LandingPresenter(this)

        googleSignIn()
        facebookLogin()
        doSpecificNeededSocialLogin()
    }

    override fun callSocialSignInApi() {
        if (NetworkStatus.isOnline2(this)) {
            if (socialSignInputModel != null) {
                showLoader()
                presenter.doSocialsignUp(socialSignInputModel!!)
            }
        } else
            showToastMsg(getString(R.string.error_no_internet))
    }

    override fun socailLoginRes(res: CustomerRes) {
        if (Validation.isValidStatus(res)) {
            hideLoader()
            if (res.result.isNotEmpty()) {
                CommonUtils.setCustomerData(res.result[0])
                showMsg(res.message)
                navigateHomeScreen()
            } else
                showMsg(res.message ?: getString(R.string.Whoops_Something_went_wrong))
        } else if (res.message != null) {
            showMsg(res.message!!)
        }
    }




    override fun showMsg(msg: String?) {
        showToastMsg(if(msg!=null) msg else getString(R.string.Whoops_Something_went_wrong))
    }

    override fun showLoader() {
        CommonUtils.showLoading(this, true)
    }

    override fun hideLoader() {
      CommonUtils.hideLoading()
    }

    override fun showViewState(state: Int) {
        if (base_multistateview != null)
            base_multistateview.viewState = state
    }

    private fun googleSignIn() {
        mAuth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    private fun doSpecificNeededSocialLogin() {


        if(NetworkStatus.isOnline2(this)) {

            intent.apply {

                source = intent.getStringExtra(Constants.SOURCE)
                if (source == Constants.SOURCE_SIGN) {
                    socialType = intent.getStringExtra(Constants.SOCIALTYPE)
                    if (socialType!!.contentEquals(Constants.SOCIAL_TYPE_GOOGLE)) {
                        signIn(socialType)
                    }
                    if (socialType!!.contentEquals(Constants.SOCIAL_TYPE_FACEBOOK)) {
                        signIn(socialType)
                    }
                }

            }
        }
        else
        {
            showMsg(getString(R.string.error_no_internet))
            finish()
        }


    }

    private fun signIn(socialType: String?) {
        try {
            if (socialType!!.contentEquals(Constants.SOCIAL_TYPE_GOOGLE)) {
                val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
                CommonUtils.showLoading(this, true)
                startActivityForResult(signInIntent, GMAIL_REQUEST_CODE)
            } else {
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email,public_profile"))
            }
        } catch (exp: Exception) {
            exp.printStackTrace()
        }

    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth?.currentUser
    }

    override fun onConnectionFailed(p0: ConnectionResult) {


    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        // Customer returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GMAIL_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                finish()
                Log.w(TAG, "Google sign in failed", e)
            }
        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")
                val user: FirebaseUser = mAuth!!.currentUser!!
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                finish()
                Log.w(TAG, "signInWithCredential:failure", task.exception)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        signOut(socialType)

        if (user != null) {
            CommonUtils.hideLoading()
            if (socialType!!.contentEquals(Constants.SOCIAL_TYPE_GOOGLE)) {
                socialSignInputModel = SocialSignInputModel(first_name = user.displayName, last_name = "", email = user.email, mobile = user.phoneNumber, google_id = user.uid, facebook_id = null,pic = user.photoUrl.toString())
            } else {
                socialSignInputModel = SocialSignInputModel(first_name = user.displayName, last_name = " ", email = user.email, mobile = user.phoneNumber, google_id = null, facebook_id = user.uid,pic = user.photoUrl.toString())
            }
            callSocialSignInApi()
        } else
            showToastMsg(getString(R.string.woops_something_went_wrong))

    }


    private fun signOut(socialType: String?) {
        try {
            // Firebase sign out
            mAuth?.signOut()
            if (socialType.equals(Constants.SOCIAL_TYPE_GOOGLE, ignoreCase = true)) {
                // Google sign out
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback {
                    Log.e("ok", "SignOut  Done")
                }

            } else {
                LoginManager.getInstance().logOut()
            }
        } catch (exp: Exception) {
            Log.e(TAG, "$socialType:social logout error ")
        }
    }


    private fun facebookLogin() {
        //setAuthListener();
        printhashkey()
        callbackManager = CallbackManager.Factory.create()

        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)

        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true)
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
        }

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                MyLogUtils.d(TAG, "facebook:onSuccess:$loginResult")
                //handleFacebookAccessToken(loginResult.getAccessToken(), loginResult);
                handleFacebookAccessToken(loginResult.accessToken)

                printhashkey()
            }




            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
                //printhashkey()
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
                // ...
            }
        })


    }


    private fun handleFacebookAccessToken(token: AccessToken) {
        socialType = Constants.SOCIAL_TYPE_FACEBOOK
        MyLogUtils.d(TAG, "handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                MyLogUtils.d(TAG, "signInWithCredential:success")
                val user: FirebaseUser = mAuth?.currentUser!!
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                MyLogUtils.w(TAG, "signInWithCredential:failure", task.exception!!)
                updateUI(null)
            }
        }
    }

    private fun printhashkey() {
        try {
            val info = packageManager.getPackageInfo(
                    "com.freshbasket.customer",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("Exception:", "PackageManager.NameNotFoundException")
        } catch (e: NoSuchAlgorithmException) {
            Log.e("Exception:", "NoSuchAlgorithmException")
        }
    }
    private fun navigateHomeScreen() {
       //startActivity(Intent(this, HomeActivity::class.java))
        val intent = Intent(context, HomeActivity::class.java)
        intent.apply {
            putExtra(Constants.SOURCE, Constants.LANDING_SCREEN)
        }
        startActivity(intent)
        finish()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
