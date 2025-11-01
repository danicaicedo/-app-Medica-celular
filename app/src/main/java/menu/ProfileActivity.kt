package com.aristidevs.finalmente.menu
import android.view.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.aristidevs.finalmente.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnGoogleSignIn: Button
    private lateinit var btnLogout: Button
    private lateinit var txtUserInfo: TextView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileImage: ImageView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var btnEditProfile: Button

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        auth = FirebaseAuth.getInstance()
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn)
        btnLogout = findViewById(R.id.btnLogout)
        txtUserInfo = findViewById(R.id.txtUserInfo)
        profileName = findViewById(R.id.profileName)
        profileEmail = findViewById(R.id.profileEmail)
        profileImage = findViewById(R.id.profileImage)
        btnEditProfile = findViewById(R.id.btnEditProfile)

        setupGoogleSignInClient()
        updateUI(auth.currentUser)

        btnGoogleSignIn.setOnClickListener { signIn() }
        btnLogout.setOnClickListener { signOut() }
        btnEditProfile.setOnClickListener {
            Toast.makeText(this, "Función de editar perfil próximamente", Toast.LENGTH_SHORT).show()
        }

        // Barra de navegación inferior
        findViewById<LinearLayout>(R.id.homeButtonLayout).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }

          findViewById<LinearLayout>(R.id.homeButtonLayout).setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish() // Opcional, para cerrar la pantalla actual
        }

        findViewById<LinearLayout>(R.id.cardSearch).setOnClickListener {
            // Ya en la actividad de búsqueda, puedes dejarlo o mantenerlo para recargar
            startActivity(Intent(this, BusquedaActivity::class.java))
        }



        findViewById<LinearLayout>(R.id.profileButtonLayout).setOnClickListener {
            // Ajusta a tu actividad de perfil
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.searchButtonLayout).setOnClickListener {
            val intent = Intent(this, BusquedaActivity::class.java) // <--- el nombre correcto
            startActivity(intent)
            finish()
        }
        findViewById<LinearLayout>(R.id.emergencyButtonLayout).setOnClickListener {
            Toast.makeText(this, "Función de emergencias próximamente", Toast.LENGTH_SHORT).show()

        }

        findViewById<LinearLayout>(R.id.profileButtonLayout).setOnClickListener {
            Toast.makeText(this, "Ya estás en Perfil", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Error Google Sign-In: ${e.statusCode}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        if (account == null) {
            Toast.makeText(this, "Cuenta Google no encontrada", Toast.LENGTH_SHORT).show()
            return
        }
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                    Toast.makeText(this, "Has iniciado sesión correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error autenticando con Firebase", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: com.google.firebase.auth.FirebaseUser?) {
        if (user != null) {
            txtUserInfo.text = "Has iniciado sesión correctamente"
            profileName.text = user.displayName ?: "Nombre de Usuario"
            profileEmail.text = user.email ?: "correo@ejemplo.com"
            btnGoogleSignIn.visibility = View.GONE
            btnLogout.visibility = View.VISIBLE
        } else {
            txtUserInfo.text = "No has iniciado sesión aún"
            profileName.text = "Nombre de Usuario"
            profileEmail.text = "correo@ejemplo.com"
            btnGoogleSignIn.visibility = View.VISIBLE
            btnLogout.visibility = View.GONE
        }
    }

    private fun signOut() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            updateUI(null)
        }
    }
}
