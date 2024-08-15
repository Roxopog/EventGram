package com.burak.eventgram.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.burak.eventgram.R
import com.burak.eventgram.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class UserFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private var eventId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        eventId = getSharedPreference(requireContext(), "eventId").toString()
        println("eventid userde şu ${eventId}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.kayitButton.setOnClickListener { kayitol(it) }
        binding.girisButton.setOnClickListener { girisyap(it) }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            if(eventId != null && eventId!!.isNotEmpty()){
                val action = UserFragmentDirections.actionUserFragmentToFeedFragment(eventId!!)
                Navigation.findNavController(view).navigate(action)
            }
            else{
                val action = UserFragmentDirections.actionUserFragmentToQrFragment()
                Navigation.findNavController(view).navigate(action)
            }

        }
    }
    fun kayitol(view: View) {
        val email = binding.editTextTextEmailAddress.text.toString()
        val sifre = binding.editTextTextPassword.text.toString()
        if (email.equals("") || sifre.equals("")) {
            Toast.makeText(requireContext(), "şifre veya parola boş olamaz", Toast.LENGTH_LONG).show()
            return
        } else {
            auth.createUserWithEmailAndPassword(email, sifre)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val action = UserFragmentDirections.actionUserFragmentToQrFragment()
                        Navigation.findNavController(view).navigate(action)
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }


        }
    }

    fun getSharedPreference(context: Context, key: String?): String? {
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            .getString(key, null)
    }
    //kayıtlı kullanıcı giriş yapma
    fun girisyap(view: View) {
        val email = binding.editTextTextEmailAddress.text.toString()
        val sifre = binding.editTextTextPassword.text.toString()
        if (email.equals("") || sifre.equals("")) {
            Toast.makeText(requireContext(), "şifre veya parola boş olamaz", Toast.LENGTH_LONG).show()
            return
        }
        else{
            auth.signInWithEmailAndPassword(email, sifre)
                .addOnSuccessListener {
                    val action = UserFragmentDirections.actionUserFragmentToQrFragment()
                    Navigation.findNavController(view).navigate(action)

                }.addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}