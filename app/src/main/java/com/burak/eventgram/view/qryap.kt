package com.burak.eventgram.view

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.burak.eventgram.databinding.FragmentQryapBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter


class qryap : Fragment() {

    private var _binding: FragmentQryapBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var qrImageView: ImageView
    private var eventId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQryapBinding.inflate(inflater, container, false)
        val view = binding.root
        firestore = FirebaseFirestore.getInstance()
        qrImageView = binding.imageView2

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.qrolustur.setOnClickListener { qryap(it) }
        binding.feedegit.setOnClickListener {
            eventId?.let { id ->
                gofeed(it, id)
            } ?: run {
                Toast.makeText(requireContext(), "Etkinlik henüz oluşturulmadı!", Toast.LENGTH_LONG).show()
            }
        }
        binding.qrindir.setOnClickListener {
            qrImageView.drawable?.let { drawable ->
                val bitmap = (drawable as BitmapDrawable).bitmap
                downloadqr(bitmap)
            }
        }
    }

    private fun gofeed(view: View, eventId: String) {
        val action = qryapDirections.actionQryapToFeedFragment(eventId)
        Navigation.findNavController(view).navigate(action)
    }

    private fun downloadqr(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "qr_code_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/EventGram")
        }

        val uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            requireContext().contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                Toast.makeText(requireContext(), "QR kodu galeriye kaydedildi!", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(requireContext(), "QR kodu kaydedilemedi!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun qryap(view: View) {
        val eventName = binding.eventname.text.toString()
        if (eventName.isEmpty()) {
            Toast.makeText(requireContext(), "Etkinlik adı boş olamaz", Toast.LENGTH_LONG).show()
            return
        }

        eventId = firestore.collection("events").document().id
        val event = hashMapOf(
            "name" to eventName,
            "createdAt" to FieldValue.serverTimestamp()
        )

        firestore.collection("events").document(eventId!!).set(event)
            .addOnSuccessListener {
                generateQrCode(eventId!!)
                binding.eventIdtext.setText(eventId)
                setSharedPreference(requireContext(), "eventId", eventId)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Etkinlik oluşturulamadı!", Toast.LENGTH_LONG).show()
            }
    }

    fun setSharedPreference(context: Context, key: String?, value: String?) {
        val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        edit.putString(key , value)
        edit.commit()
    }

    private fun generateQrCode(eventId: String) {
        val qrCodeSize = 500
        val qrCodeWriter = QRCodeWriter()

        try {
            val bitMatrix = qrCodeWriter.encode(eventId, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize)
            val bitmap = Bitmap.createBitmap(qrCodeSize, qrCodeSize, Bitmap.Config.RGB_565)

            for (x in 0 until qrCodeSize) {
                for (y in 0 until qrCodeSize) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }

            qrImageView.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
