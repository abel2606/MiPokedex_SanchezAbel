package itson.eduardo.mypokedex_sanchezabel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: JuegoAdapter
    private val pokemones = ArrayList<Pokemon>()
    private val userRef = FirebaseDatabase.getInstance().getReference("Pokemones")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById(R.id.lv_pokemonList)
        adapter = JuegoAdapter(this, pokemones)
        listView.adapter = adapter

        // Cargar Pokémon desde Firebase
        loadPokemonsFromFirebase()

        val btnresgisterPokemon: Button = findViewById(R.id.btn_addPokemon)
        btnresgisterPokemon.setOnClickListener {
            val intent: Intent = Intent(this, RegisterPokemonActivity::class.java)

            startActivity(intent)
        }
    }

    private fun loadPokemonsFromFirebase() {
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pokemones.clear()  // Limpiar la lista antes de agregar los nuevos Pokémon
                for (data in snapshot.children) {
                    val pokemon = data.getValue(Pokemon::class.java)
                    if (pokemon != null) {
                        pokemones.add(pokemon)
                    }
                }
                adapter.notifyDataSetChanged()  // Notificar al adaptador que los datos han cambiado
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al obtener datos: ${error.message}")
            }
        })
    }



}

class JuegoAdapter : BaseAdapter {
    var context: Context
    var pokemones = ArrayList<Pokemon>()

    constructor(context: Context, pokemones: ArrayList<Pokemon>) {
        this.context = context
        this.pokemones = pokemones
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val pokemon = pokemones[position]
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vista = inflator.inflate(R.layout.pokemon_item, null)

        val imagen = vista.findViewById<ImageView>(R.id.im_imagenPokemon)
        val nombre = vista.findViewById<TextView>(R.id.tv_nombrePokemon)
        val numero = vista.findViewById<TextView>(R.id.tv_numeroPokemon)

        if (pokemon.thumbnail.isNotEmpty()) {
            Glide.with(context).load(pokemon.thumbnail).into(imagen)
        } else {
            imagen.setImageResource(R.drawable.pikachu)
        }

        nombre.text = pokemon.name
        numero.text = pokemon.number.toString()

        return vista
    }

    override fun getCount(): Int = pokemones.size

    override fun getItem(position: Int): Any = pokemones[position]

    override fun getItemId(position: Int): Long = position.toLong()
}
