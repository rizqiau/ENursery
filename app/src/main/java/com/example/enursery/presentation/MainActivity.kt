package com.example.enursery.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.enursery.R
import com.example.enursery.core.di.Injection
import com.example.enursery.databinding.ActivityMainBinding
import com.example.enursery.presentation.input.InputActivity
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isScannerInstalled = false
    private lateinit var scanner: GmsBarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val seeder = Injection.provideSeeder(this@MainActivity)
            seeder.seedIfNeeded()
        }

        Log.d("MainActivity", "onCreate dijalankan")
        initMainActivity() // Pindahkan inisialisasi navigasi ke metode ini
        installGoogleScanner()
        initScanner()
    }

    private fun installGoogleScanner() {
        val moduleInstall = ModuleInstall.getClient(this)
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(GmsBarcodeScanning.getClient(this))
            .build()

        moduleInstall.installModules(moduleInstallRequest).addOnSuccessListener {
            isScannerInstalled = true
        }.addOnFailureListener {
            isScannerInstalled = false
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initScanner() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom()
            .build()

        scanner = GmsBarcodeScanning.getClient(this, options)
    }

    private fun initMainActivity() {
        Log.d("MainActivity", "initMainActivity() dijalankan")

        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

        // Ambil NavController dengan cara yang benar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as? NavHostFragment
        if (navHostFragment == null) {
            Log.e("MainActivity", "NavHostFragment tidak ditemukan!")
            return
        }

        val navController: NavController = navHostFragment.navController
        Log.d("MainActivity", "NavController ditemukan, mengatur navigasi")

        // Setup AppBar dengan NavController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_vgm,
                R.id.navigation_plot,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        binding.fab.setOnClickListener {
            if (isScannerInstalled) {
                startScanning()
            } else {
                Toast.makeText(this, "Google Scanner tidak terinstal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startScanning() {
        scanner.startScan().addOnSuccessListener { barcode ->
            val scannedValue = barcode.rawValue
            scannedValue?.let {
                Log.d("QRScanner", "Scanned QR Code: $it")

                // ✅ Navigasi ke InputActivity dengan bibitId hasil scan
                val intent = Intent(this, InputActivity::class.java).apply {
                    putExtra("EXTRA_BIBIT_ID", it) // Kirim hasil scan ke InputActivity
                }
                startActivity(intent)
            }
        }.addOnCanceledListener {
            Toast.makeText(this, "Scanning dibatalkan", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}