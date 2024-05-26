package ru.gunferzs.pingproject

import android.Manifest
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.gunferzs.pingproject.ui.theme.PingProjectTheme


class MainActivity : ComponentActivity() {

    var cm: ConnectivityManager? = null

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            // Handle Permission granted/rejected
            if (isGranted) {
                Toast.makeText(this, "получен", Toast.LENGTH_SHORT)
            } else {
                Toast.makeText(this, "не получен", Toast.LENGTH_SHORT)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PingProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        activityResultLauncher.launch(Manifest.permission.QUERY_ALL_PACKAGES)

        val packageManager = packageManager
        val intent = Intent("android.net.VpnService", null)
        val result = packageManager.queryIntentActivities(intent, 0)
        packageManager
            .getInstalledPackages(0)
            .find {
                it.packageName.contains("openvpn")
            }
            .let {
                packageManager
                    .checkPermission(
                        Manifest.permission.BIND_VPN_SERVICE,
                        it?.packageName.orEmpty()
                    )
            }
        cm = this.getSystemService(ConnectivityManager::class.java)
        cm?.registerDefaultNetworkCallback(
            object: ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Toast.makeText(this@MainActivity, "получен", Toast.LENGTH_SHORT).show()
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    Toast.makeText(this@MainActivity, "получен", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PingProjectTheme {
        Greeting("Android")
    }
}