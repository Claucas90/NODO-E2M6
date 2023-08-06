package com.claucas90.e2m6.VIEW

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.claucas90.e2m6.R
import com.claucas90.e2m6.databinding.ActivityMainBinding
import com.claucas90.e2m6.databinding.FragmentBlankBinding
import com.claucas90.e2m6.MODELO.Datos
import com.claucas90.e2m6.MODELO.DatosRoomDatabase
import com.claucas90.e2m6.MODELO.Repository.DatosRepository
import com.claucas90.e2m6.VIEW.BlankFragment
import com.claucas90.e2m6.VIEW.ListaFragment
import com.claucas90.e2m6.VIEW.ViewModel.DatosViewModel
import com.claucas90.e2m6.VIEW.ViewModel.DatosViewModelFactory

class MainActivity : AppCompatActivity(), BlankFragment.CarroButtonClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var datosViewModel: DatosViewModel
    internal lateinit var data: List<Datos>
    private lateinit var totalTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Asignar el TextView del layout al miembro totalTextView
        totalTextView = binding.textTotal

        //instanciar la bd
        val database = DatosRoomDatabase.getDatabase(applicationContext)
        val datosDao = database.datosDao()
        val datosRepository = DatosRepository(datosDao)
        val datosViewModelFactory = DatosViewModelFactory(datosRepository)
        datosViewModel =
            ViewModelProvider(this, datosViewModelFactory).get(DatosViewModel::class.java)

        datosViewModel.allDatos.observe(this, Observer { datosList ->
            this.data = datosList // Aquí cargamos la variable global data con lta lista de allDatos
            var total = 0.0
            // Puedes iterar sobre la lista y acceder a cada elemento individualmente
            for (data in datosList) {
                val ttotal = data.cantidad * data.precio
                total += ttotal
            }
            val totalImprimir = total.toString()
            // Asignar el valor del total al TextView
            totalTextView.text = totalImprimir
        })
        //Cargamos el fragmento para agregar apenas cargue la app
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<BlankFragment>(R.id.fragmentContainer)
        }
    }


    // Función para el botón carro
    override fun onCarroButtonClick() {
        try {
            // Verificar si la propiedad ids está inicializada
            if (!data.isEmpty()) {
                // val total = this.data.sumOf { it.precio }
                // Acceder a la propiedad ids
                val listaFragment = ListaFragment()
                val bundle = Bundle()
                //   bundle.putString("total", total.toString())
                listaFragment.arguments = bundle
                supportFragmentManager.commit {
                    replace(R.id.fragmentContainer, listaFragment)
                    addToBackStack(null)
                }
            } else {
                // La propiedad ids no ha sido inicializada
                // Realiza la lógica correspondiente en este caso
                Toast.makeText(this, "la lista está vacía", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Manejar la excepción en caso de que ocurra algún error
            Toast.makeText(this, "Error al acceder a los datos", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }


    // Función para el botón insertar
    override fun insertar() {
        val nombreEditText = findViewById<EditText>(R.id.nombre)
        val cantidadEditText = findViewById<EditText>(R.id.cantidad)
        val precioEditText = findViewById<EditText>(R.id.precio)

        val nombre = nombreEditText.text.toString()
        val cantidad = cantidadEditText.text.toString().toIntOrNull() ?: 0
        val precio = precioEditText.text.toString().toDoubleOrNull() ?: 0.0
        val datos = Datos(null, nombre, precio, cantidad)
        datosViewModel.insert(datos)
        Toast.makeText(this, "agregado correctamente", Toast.LENGTH_SHORT).show()
    }

    fun eliminar()
    {
        datosViewModel.deleteAll()
        data = emptyList()
        totalTextView.text = "0"
        val listaFragment = ListaFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, listaFragment)
            .commit()
    }
}