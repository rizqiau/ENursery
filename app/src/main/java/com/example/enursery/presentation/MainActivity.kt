package com.example.enursery.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
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
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isScannerInstalled = false
    private lateinit var scanner: GmsBarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val seeder = Injection.provideSeeder(this@MainActivity)
            seeder.seedIfNeeded()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setLogo(R.drawable.logooo)
            setDisplayUseLogoEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.main)

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
            Log.d("MainActivity", "Scanner module installed")
        }.addOnFailureListener {
            isScannerInstalled = false
            Log.e("MainActivity", "Failed to install scanner: ${it.message}")
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

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as? NavHostFragment
        if (navHostFragment == null) {
            Log.e("MainActivity", "NavHostFragment tidak ditemukan!")
            return
        }

        val navController: NavController = navHostFragment.navController
        Log.d("MainActivity", "NavController ditemukan, mengatur navigasi")

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mapPickerFragment,
                R.id.addEditPlotFragment,
                R.id.addEditVgmFragment-> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.bottomAppBar.visibility = View.GONE
                    binding.fab.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.bottomAppBar.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                }
            }
        }

        // Setup AppBar dengan NavController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.vgmFragment,
                R.id.plotFragment,
                R.id.profileFragment,
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        binding.fab.setOnClickListener {
            Log.d("MainActivity", "FAB diklik")
            if (isScannerInstalled) {
                startScanning()
            } else {
                Toast.makeText(this, "Google Scanner tidak terinstal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startScanning() {
        Log.d("MainActivity", "startScanning() dijalankan")
        scanner.startScan().addOnSuccessListener { barcode ->
            val scannedValue = barcode.rawValue
            scannedValue?.let {
                Log.d("QRScanner", "Scanned QR Code: $scannedValue")
                lifecycleScope.launch {
                    delay(100) // delay sedikit (optional)
                    val navController = findNavController(R.id.nav_host_fragment_activity_main)
                    val bundle = bundleOf("idBibit" to it)
                    navController.navigate(R.id.addEditVgmFragment, bundle)
                }
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