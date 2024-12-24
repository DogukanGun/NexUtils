package com.dag.nexutils.features.home.presentation.components

import android.content.Context
import com.dag.nexutils.R
import com.dag.nexutils.base.navigation.Destination
import com.dag.nexutils.functions.connectToWifi
import com.dag.nexutils.functions.handlePlainText
import com.dag.nexutils.functions.makePhoneCall
import com.dag.nexutils.functions.openUrlInBrowser
import com.dag.nexutils.functions.saveCalendarEvent
import com.dag.nexutils.functions.saveContact
import com.dag.nexutils.functions.sendEmail
import com.dag.nexutils.functions.sendSms
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

enum class ClickType {
    Navigation,
    Custom
}

data class HomeCardProps(
    val text: String,
    val animationRes: Int,
    val clickType: ClickType,
    val destination: Destination? = null,
    val onClick: ((context: Context, transmitter: (String) -> Unit) -> Unit)? = null
)

val features = listOf(
    HomeCardProps(
        "QR Reader",
        R.raw.qr_animation,
        ClickType.Custom,
        onClick = { context, transmitter ->
            val options = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE,
                    Barcode.FORMAT_AZTEC
                )
                .enableAutoZoom()
                .build()
            val scanner = GmsBarcodeScanning.getClient(context, options)
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    when (barcode.valueType) {
                        Barcode.TYPE_WIFI -> {
                            barcode.wifi?.let {
                                connectToWifi(context, it.ssid, it.password)
                            }
                        }

                        Barcode.TYPE_URL -> {
                            barcode.url?.let {
                                openUrlInBrowser(context, it.url)
                            }
                        }

                        Barcode.TYPE_TEXT -> {
                            barcode.rawValue?.let {
                                handlePlainText(context, it)
                            }
                        }

                        Barcode.TYPE_CONTACT_INFO -> {
                            val contact = barcode.contactInfo!!
                            saveContact(
                                context, contact.name!!.formattedName,
                                contact.phones[0].number!!, contact.emails[0].address!!
                            )
                        }

                        Barcode.TYPE_PHONE -> {
                            barcode.phone?.let {
                                makePhoneCall(context, it.number)
                            }
                        }

                        Barcode.TYPE_EMAIL -> {
                            barcode.email?.let {
                                sendEmail(context, it.address, it.subject, it.body)
                            }
                        }

                        Barcode.TYPE_SMS -> {
                            barcode.sms?.let {
                                sendSms(context, it.phoneNumber!!, it.message!!)
                            }
                        }

                        Barcode.TYPE_CALENDAR_EVENT -> {
                            barcode.calendarEvent?.let {
                                saveCalendarEvent(
                                    context,
                                    it.summary,
                                    it.description,
                                    it.location,
                                    it.start!!.rawValue,
                                    it.end!!.rawValue
                                )
                            }

                        }
                    }
                }
                .addOnCanceledListener {
                    // Task canceled
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                }
        }
    ),
    HomeCardProps(
        "Document Scanner",
        R.raw.scan_document,
        ClickType.Navigation,
        Destination.ScannerScreen
    ),
    HomeCardProps(
        "Text Extraction",
        R.raw.doc_extraction,
        ClickType.Navigation,
        Destination.TextExtractionScreen
    )
)