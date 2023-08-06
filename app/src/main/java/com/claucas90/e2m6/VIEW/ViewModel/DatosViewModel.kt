package com.claucas90.e2m6.VIEW.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.claucas90.e2m6.MODELO.Datos
import com.claucas90.e2m6.MODELO.Repository.DatosRepository
import kotlinx.coroutines.launch

class DatosViewModel(private val repository: DatosRepository) : ViewModel() {

    // devuelve todos los datos de la bd
    val allDatos: LiveData<List<Datos>> = repository.allDatos

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(dato: Datos) = viewModelScope.launch {
        repository.insert(dato)
    }
    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class DatosViewModelFactory(private val repository: DatosRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DatosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}