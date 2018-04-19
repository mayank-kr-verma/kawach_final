package nitp.navi.kawach_final

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.util.Log


class Login : AppCompatActivity() {
    var mAuth: FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth= FirebaseAuth.getInstance()
        signInAnonymously()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    fun signInAnonymously(){
        mAuth!!.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        val user = mAuth!!.getCurrentUser()

                    } else {


                    }
                }

    }
    fun buRegisterEvent(view: View){

        if (isOnline()){
            if (etPhoneNumber.text.toString().replace("[^0-9]".toRegex(),"").length == 10){
                val userData=UserData(this)
                userData.savePhone(etPhoneNumber.text.toString())
                //userData.saveName(etName.text.toString())

                val df =SimpleDateFormat("yyyy/MMM/dd HH:MM:ss")
                val date =Date()

                val mDatabase = FirebaseDatabase.getInstance().reference
                mDatabase.child("Users").child(etPhoneNumber.text.toString()).child("name").setValue(etName.text.toString())
                mDatabase.child("Users").child(etPhoneNumber.text.toString()).child("request").setValue(df.format(date).toString())
                mDatabase.child("Users").child(etPhoneNumber.text.toString()).child("Finders").setValue(df.format(date).toString())


                finish()
            }
            else{
                Toast.makeText(this,"Enter valid phone number",Toast.LENGTH_SHORT).show()
            }

        }
        else {
            Toast.makeText(this, "No internet detected", Toast.LENGTH_SHORT).show()
        }



    }

    private fun isOnline(): Boolean {

        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            Log.e("exitvalue",exitValue.toString())
            return (exitValue == 0)

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }
}
