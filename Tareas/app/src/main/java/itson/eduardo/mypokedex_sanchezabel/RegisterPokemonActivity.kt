package itson.eduardo.mypokedex_sanchezabel

import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class RegisterPokemonActivity : AppCompatActivity() {

    val ClOUD_NAME = "donaolihv"
    val REQUEST_IMAGE_GET = 1
    val UPLOAD_PRESET = "pokemon-upload"

    //Esto es la Uri que representa la imagen
    var imageUri: Uri? = null

    private val userRef = FirebaseDatabase.getInstance().getReference("Pokemones")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_pokemon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initCloudinary()


        val name: EditText = findViewById(R.id.et_pokemonName)
        val number: EditText = findViewById(R.id.et_pokemonNumber)
        val upload: Button = findViewById(R.id.btn_selectImage)
        val save: Button = findViewById(R.id.btn_savePokemon)

        upload.setOnClickListener {
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }

        save.setOnClickListener {
            saveMarFromForm()
        }

    }

    fun savePokemon(callback: (String) -> Unit) {
        var url: String = ""
        if (imageUri != null) {
        MediaManager.get().upload(imageUri).unsigned(UPLOAD_PRESET)
            .callback(object : UploadCallback {
                override fun onStart(requesId: String) {
                    Log.d("Cloudinary Quickstart", "Upload start")

                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    Log.d("Cloudinary Quickstart", "Upload progress")
                }

                override fun onSuccess(requesId: String, resultData: Map<*, *>) {
                    Log.d("Cloudinary Quickstart", "Upload success")
                    //Aquí se busca encontrar la url de la imagen una vez se ha subido a cloudinary
                    url = resultData["secure_url"] as String? ?: ""
                    Log.d("URL}", url)
                    callback(url)
                }

                override fun onError(requistId: String, error: ErrorInfo) {
                    Log.d("Cloudinary Quickstart", "Upload failed")
                    callback("")
                }

                override fun onReschedule(requestId: String, error: ErrorInfo?) {

                }

            }).dispatch()
        } else {
            callback("") // Si no hay imagen, devolver cadena vacía
        }
    }


    private fun initCloudinary() {
        val config: MutableMap<String, String> = HashMap<String, String>()
        config["cloud_name"] = ClOUD_NAME
        MediaManager.init(this, config)
    }


    private fun saveMarFromForm() {
        val namep: EditText = findViewById(R.id.et_pokemonName)
        val pNumber: EditText = findViewById(R.id.et_pokemonNumber)


        savePokemon { thumbnail ->
            val pokemon = Pokemon(
                namep.text.toString(),
                pNumber.text.toString().toInt(),
                thumbnail
            )

            userRef.push().setValue(pokemon)
        }
    }

    private fun writeMark(mark: Pokemon) {
        var listV: TextView = findViewById(R.id.tv_pokemonList) as TextView
        var text = listV.text.toString() + mark.toString() + "\n"
        listV.text = text

    }

    //si
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val fullImageUri: Uri? = data?.data

            if (fullImageUri != null)
                changeImage(fullImageUri)
            imageUri = fullImageUri


        }
    }

    //si
    fun changeImage(uri: Uri) {
        val thumbnail: ImageView = findViewById(R.id.iv_thumbnail)

        try {
            thumbnail.setImageURI(uri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}