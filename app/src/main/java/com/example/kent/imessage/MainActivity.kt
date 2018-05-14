package com.example.kent.imessage

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.*
import com.twitter.sdk.android.core.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val RC_SIGN_IN = 100
    }

    private var mAuth : FirebaseAuth?= null
    private var mGoogleSignInClient : GoogleSignInClient?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Twitter.initialize(this)
        setContentView(R.layout.activity_main)

            val gso =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

            mAuth = FirebaseAuth.getInstance()
            mGoogleSignInClient = GoogleSignIn.getClient(this,gso)

        twitterlogin.callback = object : Callback <TwitterSession>(){
            override fun success (result : Result<TwitterSession>?){
                Toast.makeText(this@MainActivity,"Authentication sucess." , Toast.LENGTH_SHORT).show()
                handleTwitteSession(result?.data)
            }
            override fun failure (exception: TwitterException?){
                Toast.makeText(this@MainActivity,"Authentication failed." , Toast.LENGTH_SHORT).show()
                updateUI(null,null)
            }
        }


btn_signin.setOnClickListener {

    val intent = Intent(this,bottom_navigation::class.java)
    startActivity(intent)
}
        googlesignin.setOnClickListener{
            when (it.id){
                R.id.googlesignin -> signIn()
            }
        }


    }

    private fun signIn() {
        val signIn = mGoogleSignInClient?.signInIntent
        startActivityForResult(signIn, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
        else twitterlogin.onActivityResult(requestCode,resultCode,data)

    }

    private fun handleTwitteSession (session : TwitterSession?){
        val credential = TwitterAuthProvider.getCredential(session!!.authToken.token,session.authToken.secret)

        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this){
            if (it.isSuccessful){
                val user = mAuth?.currentUser
                updateUI(user,null)
                toast ("Authentication Succeeded.")
            }

            else
                toast ("Authentication Failed.")

        }
    }


    private fun handleSignInResult (completedTask : Task<GoogleSignInAccount>){
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthwithGoogle(account)
        } catch (e:ApiException){
            updateUI(null,null)
        }
    }

    private fun firebaseAuthwithGoogle(account: GoogleSignInAccount?){
        val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this){
            if (it.isSuccessful){
                val user  = mAuth?.currentUser
                updateUI (user,null)
            }
            else {
                Toast.makeText(this@MainActivity,"Authentication failed." , Toast.LENGTH_SHORT).show()
                updateUI (null,null)
            }
        }
    }


    private fun updateUI(User:FirebaseUser?,Account: GoogleSignInAccount?){}
    private fun toast (text:String){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

}
