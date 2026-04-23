package com.example.infocdmx

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.infocdmx.core.FragmentCommunicator
import com.example.infocdmx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), FragmentCommunicator {

    // Cambiamos a lateinit para inicializarlo en onCreate
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inflar el binding antes de cualquier otra cosa
        binding = ActivityMainBinding.inflate(layoutInflater)

        // 2. Activar modo borde a borde
        enableEdgeToEdge()

        // 3. Establecer el contenido usando la raíz del binding
        setContentView(binding.root)

        // 4. Configurar los Insets (márgenes de barra de estado/navegación)
        // Usamos binding.root en lugar de findViewById(R.id.main) para evitar el error en rojo
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun manageLoader(isVisible: Boolean) {
        // Usamos binding para acceder al loaderView de forma segura
        // Si loaderView sigue en rojo, asegúrate de que en activity_main.xml
        // exista un View con el id: android:id="@+id/loaderView"
        binding.loaderView.isVisible = isVisible
    }
}