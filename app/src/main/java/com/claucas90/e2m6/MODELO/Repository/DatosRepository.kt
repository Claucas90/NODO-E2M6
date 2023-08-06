package com.claucas90.e2m6.MODELO.Repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.claucas90.e2m6.MODELO.Datos
import com.claucas90.e2m6.MODELO.DatosDao

class DatosRepository(private val datosDao: DatosDao) {

    val allDatos: LiveData<List<Datos>> = datosDao.getAllDatos()

    val precioTotal: LiveData<List<Datos>> = datosDao.getAllDatos()

    @WorkerThread
    suspend fun insert(datos: Datos) {
        datosDao.insert(datos)
    }
    @WorkerThread
    suspend fun deleteAll() {
        datosDao.deleteAll()
    }
}