package com.burak.eventgram.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.burak.eventgram.R
import com.burak.eventgram.databinding.FragmentQrBinding
import com.burak.eventgram.databinding.FragmentUserBinding
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult


class qrFragment : Fragment() {
    private var _binding: FragmentQrBinding? = null
    private val binding get() = _binding!!
    private var eventId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("eventPrefs", 0)
        eventId = sharedPreferences.getString("eventId", "") ?: ""
        println("eventid oncreate de şu ${eventId}")

        if (eventId!!.isNotEmpty()) {
            val action = qrFragmentDirections.actionQrFragmentToFeedFragment(eventId!!)
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventId = arguments?.getString("eventId") ?: ""
        saveEventId(eventId!!)
        binding.makeevent.setOnClickListener { makeevent(it) }
        binding.qrokut.setOnClickListener { goevent(it) }
        binding.kodButton.setOnClickListener { feedegec(it) }
    }

    private fun saveEventId(eventId: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("eventPrefs", 0)
        val editor = sharedPreferences.edit()
        editor.putString("eventId", eventId)
        editor.apply()
    }

    private fun feedegec(view: View) {
        if (binding.writeId.toString() == null){
            Toast.makeText(requireContext(), "lütfen etkinliğin kodunu giriniz", Toast.LENGTH_LONG).show()
        }else{
            try {
                eventId = binding.writeId.text.toString()
                Toast.makeText(requireContext(), "geçiş başarılı", Toast.LENGTH_SHORT).show()
                val action = qrFragmentDirections.actionQrFragmentToFeedFragment(eventId!!)
                Navigation.findNavController(view).navigate(action)
            }catch (e: WriterException) {
                Toast.makeText(requireContext(), "Kod yanlış veya hata var", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun goevent(view: View) {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setOrientationLocked(false)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.initiateScan()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result.contents != null) {
            // QR kodu başarıyla tarandı, event ID'si alındı
            eventId = result.contents.toString()
            val action = qrFragmentDirections.actionQrFragmentToFeedFragment(eventId!!)
            Navigation.findNavController(requireView()).navigate(action)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun makeevent(view: View) {
        val action = qrFragmentDirections.actionQrFragmentToQryap()
        Navigation.findNavController(view).navigate(action)
        println("buraya tıklandı")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
