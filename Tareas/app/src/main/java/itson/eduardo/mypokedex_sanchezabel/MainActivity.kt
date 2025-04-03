package itson.eduardo.mypokedex_sanchezabel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnresgisterPokemon: Button = findViewById(R.id.btn_addPokemon)
        btnresgisterPokemon.setOnClickListener {
            val intent: Intent = Intent(this, RegisterPokemonActivity::class.java)

            startActivity(intent)
        }
    }


}

class JuegoAdapter: BaseAdapter {
    var context: Context? = null
    var pokemones = ArrayList<Pokemon>()

    constructor(context: Context, juegos: ArrayList<Pokemon>) {
        this.context = context
        this.pokemones = juegos
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var pokemon = pokemones[position]
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var vista = inflator.inflate(R.layout.pokemon_item, null)

        var imagen = vista.findViewById(R.id.im_imagenPokemon) as ImageView
        var nombre = vista.findViewById(R.id.tv_nombrePokemon) as TextView
        var numero = vista.findViewById(R.id.tv_numeroPokemon) as TextView

        if (pokemon.thumbnail.isNotEmpty()) {
            imagen.setImageURI(Uri.parse(pokemon.thumbnail))
        } else {
            imagen.setImageResource(R.drawable.pikachu)
        }

        nombre.setText(pokemon.name)
        numero.setText(pokemon.number)

        imagen.setOnClickListener {
            var intent: Intent = Intent(context, MainActivity::class.java)
            intent.putExtra("nombre", pokemon.name)
            intent.putExtra("numero", pokemon.number)
            intent.putExtra("imagen", pokemon.thumbnail)
            context!!.startActivity(intent)
        }

        return vista
    }

    override fun getCount(): Int {
        return pokemones.size
    }

    override fun getItem(position: Int): Any {
        return pokemones[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}