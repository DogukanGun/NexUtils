package com.dag.nexutils.functions

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.dag.nexutils.base.extensions.safeLet
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.text.Text

fun openUrlInBrowser(context: Context, url: String?) {
    try {
        if (url == null){
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Error opening URL: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun saveContact(context: Context, name: String?, phone: String?, email: String?) {
    val intent = Intent(Intent.ACTION_INSERT)
    intent.type = ContactsContract.Contacts.CONTENT_TYPE
    intent.putExtra(ContactsContract.Intents.Insert.NAME, name)
    intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone)
    intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email)
    context.startActivity(intent)
}

fun makePhoneCall(context: Context, phone: String?) {
    try {
        if (phone == null){
            return
        }
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phone")
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Error making phone call: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun sendEmail(context: Context, email: String?, subject: String?, body: String?) {
    try {
        if (email == null){
            return
        }
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Error sending email: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun sendSms(context: Context, phone: String?, message: String?) {
    try {
        if (phone == null){
            return
        }
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("sms:$phone")
            putExtra("sms_body", message)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Error sending SMS: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun openLocationInMap(context: Context, latitude: Double?, longitude: Double?) {
    try {
        if (latitude == null || longitude == null){
            return
        }
        val uri = Uri.parse("geo:$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Error opening map: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun saveCalendarEvent(
    context: Context,
    title: String?,
    description: String?,
    location: String?,
    start: String?,
    end: String?
) {
    try {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.Events.DESCRIPTION, description)
            putExtra(CalendarContract.Events.EVENT_LOCATION, location)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start?.toLong())
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end?.toLong())
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Error saving calendar event: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun handlePlainText(context: Context, text: String?) {
    println("Handling plain text: $text")
    // Example: Copy text to clipboard
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText("Scanned Text", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
}

fun connectToWifi(context: Context, ssid: String?, password: String?) {
    safeLet(ssid,password){ _ssid,_password ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectToWifiWithNetworkSpecifier(context, _ssid, _password)
        } else {
            connectToWifiWithWifiManager(context, _ssid, _password)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun connectToWifiWithNetworkSpecifier(context: Context, ssid: String, password: String) {
    val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
        .setSsid(ssid)
        .setWpa2Passphrase(password)
        .build()

    val networkRequest = android.net.NetworkRequest.Builder()
        .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)
        .setNetworkSpecifier(wifiNetworkSpecifier)
        .build()

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    connectivityManager.requestNetwork(networkRequest, object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: android.net.Network) {
            super.onAvailable(network)
            Toast.makeText(context, "Connected to $ssid", Toast.LENGTH_SHORT).show()
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Toast.makeText(context, "Failed to connect to $ssid", Toast.LENGTH_SHORT).show()
        }
    })
}

private fun connectToWifiWithWifiManager(context: Context, ssid: String, password: String) {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiConfig = WifiConfiguration().apply {
        SSID = "\"$ssid\""
        preSharedKey = "\"$password\""
    }

    val networkId = wifiManager.addNetwork(wifiConfig)
    if (networkId != -1) {
        wifiManager.disconnect()
        wifiManager.enableNetwork(networkId, true)
        wifiManager.reconnect()
        Toast.makeText(context, "Connected to $ssid", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(context, "Failed to connect to $ssid", Toast.LENGTH_SHORT).show()
    }
}

fun extractText(task:Task<Text>):String{
    val allText = StringBuilder()
    for (block in task.result.textBlocks) {
        allText.append(block.text).append("\n") // Add block text
        for (line in block.lines) {
            allText.append(line.text).append("\n") // Add line text
            for (element in line.elements) {
                allText.append(element.text).append(" ") // Add element text
            }
        }
    }
    return allText.toString()
}


fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}