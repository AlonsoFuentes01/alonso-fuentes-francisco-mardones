package com.example.aplicacion_escuela

import android.app.Application
import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.aplicacion_escuela.viewmodel.NuevoTicketViewModel
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NuevoTicketFotoTest {
    @Test
    fun adjuntarFoto_preparaCapturaDeCamara() {
        val modelo = prepararCaptura()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            assertNotNull(modelo.foto)
            assertNull(modelo.errorFoto)
        }
    }

    @Test
    fun quitarFoto_limpiaElAdjunto() {
        val modelo = prepararCaptura()
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            modelo.quitarFoto()
            assertNull(modelo.foto)
        }
    }

    private fun prepararCaptura(): NuevoTicketViewModel {
        val instrumentacion = InstrumentationRegistry.getInstrumentation()
        val app = instrumentacion.targetContext.applicationContext as Application
        val modelo = NuevoTicketViewModel(app)
        val captura = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888)
        instrumentacion.runOnMainSync { modelo.adjuntarFoto(captura = captura) }

        repeat(100) {
            var cargando = false
            instrumentacion.runOnMainSync { cargando = modelo.cargandoFoto }
            if (!cargando) return modelo
            Thread.sleep(20)
        }
        error("La foto no terminó de procesarse")
    }
}
