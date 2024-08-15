package com.burak.eventgram.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.burak.eventgram.adapter.PostAdapter
import com.burak.eventgram.databinding.FragmentFeedBinding
import com.burak.eventgram.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventId: String
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    var postList : ArrayList<Post> = arrayListOf()
    private var adapter : PostAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener { goupload(it) }
        eventId = arguments?.getString("eventId") ?: ""
        println("feed eventid ${eventId}")
        binding.exitButton.setOnClickListener { exitfun(it) }
        binding.exiteventButton.setOnClickListener { exitevent(it) }
        fireStoreVerileriAl()
        adapter = PostAdapter(postList)
        binding.FeedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.FeedRecyclerView.adapter = adapter

    }
    private fun fireStoreVerileriAl(){
        db.collection(eventId).orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
            }else{
                if(value != null){
                    if(!value.isEmpty){
                        postList.clear()
                        val documents = value.documents
                        for (document in documents){

                            val comment = document.get("comment") as String
                            val email = document.get("userEmail") as String
                            val downloadUrl = document.get("downloadUrl") as String

                            val post = Post(downloadUrl,email,comment)
                            postList.add(post)
                        }
                        adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun exitevent(view: View) {
        clearSharedPreference(requireContext())
        val action = FeedFragmentDirections.actionFeedFragmentToQrFragment()
        Navigation.findNavController(view).navigate(action)

        Toast.makeText(requireContext(), "Etkinlikten çıkıldı", Toast.LENGTH_SHORT).show()
    }

    private fun exitfun(view: View) {
        auth.signOut()
        val action = FeedFragmentDirections.actionFeedFragmentToUserFragment()
        Navigation.findNavController(view).navigate(action)

        Toast.makeText(requireContext(), "Çıkış yapıldı", Toast.LENGTH_SHORT).show()
    }
    fun clearSharedPreference(context: Context) {
        val sharedPref = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        edit.clear()
        edit.commit()
    }

    private fun goupload(view: View) {
        val action = FeedFragmentDirections.actionFeedFragmentToUploadFragment(eventId)
        Navigation.findNavController(view).navigate(action)
    }
}
