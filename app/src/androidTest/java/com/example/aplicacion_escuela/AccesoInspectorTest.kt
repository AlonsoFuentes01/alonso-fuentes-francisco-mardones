package com.example.aplicacion_escuela

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.aplicacion_escuela.data.UserRepository
import com.example.aplicacion_escuela.viewmodel.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccesoInspectorTest {

    @Test
    fun login_conservaLaCuentaLocalDelInspector() {
        val instrumentacion = InstrumentationRegistry.getInstrumentation()
        val aplicacion = instrumentacion.targetContext.applicationContext as Application
        val repositorio = UserRepository(aplicacion)
        val inspector = repositorio.getUsers().first { it.role == "ADMIN" }
        val sesionAnterior = repositorio.getSession()

        instrumentacion.runOnMainSync {
            try {
                val modelo = LoginViewModel(aplicacion)
                modelo.email = inspector.email
                modelo.password = inspector.password
                assertTrue(modelo.login())
                assertEquals(inspector.email, repositorio.getSession())
            } finally {
                repositorio.setSession(sesionAnterior)
            }
        }
    }
}
