package com.example.codelabapplication.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codelabapplication.R
import com.example.codelabapplication.adapter.CocktailAdapter
import com.example.codelabapplication.application.CocktailApplication
import com.example.codelabapplication.databinding.FragmentCocktailBinding
import com.example.codelabapplication.model.Drink
import com.example.codelabapplication.util.ApiCallNetworkResource
import com.example.codelabapplication.util.ConnectivityLiveData
import com.example.codelabapplication.util.observeNetworkConnection
import com.example.codelabapplication.viewmodel.CocktailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CocktailFragment : Fragment() {
    private lateinit var _binding: FragmentCocktailBinding
    private val binding get() = _binding
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var cocktailAdapter: CocktailAdapter
    private lateinit var cocktailRecyclerView: RecyclerView
    val listOfPosts: MutableList<Drink> = mutableListOf()
    private val vieModel: CocktailViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityLiveData = ConnectivityLiveData(CocktailApplication.application)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCocktailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeNetworkConnection(connectivityLiveData,viewLifecycleOwner,
            {doThisWhenNetworkIsAvailable()}, {doThisWhenNetworkIsLost()})

        cocktailRecyclerView = binding.cocktailRecyclerview
        initRecyclerView()
        cocktailDetailsObservers()
        vieModel.getCocktailDetails("a")

        val mgr: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager



        /** Filter post as user types in the search view **/
        binding.edtEditSearchBox.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                mgr.hideSoftInputFromWindow(binding.edtEditSearchBox.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listOfPosts.clear()
                val searchText = newText?.lowercase()?.trim()
                if (searchText != null) {
                    if (searchText.isNotEmpty()){
                        vieModel.getCocktailDetails(searchText)
                    }
                  }else{
                    cocktailAdapter.differ.submitList(listOfPosts)
                }
                return true
            }

        })


        onBackPress()
    }




    /** initialise recyclerview*/
    private fun initRecyclerView(){
        cocktailRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            cocktailAdapter = CocktailAdapter()
            cocktailRecyclerView.addItemDecoration(DividerItemDecoration(cocktailRecyclerView.context, DividerItemDecoration.VERTICAL))
            adapter = cocktailAdapter
        }
    }




    /** This function observe the post request and update the recycler view **/
    private fun cocktailDetailsObservers(){
        vieModel.cocktailResponseLiveData.observe(viewLifecycleOwner){ it ->
            when (it){
              is  ApiCallNetworkResource.Success -> {
                  it.data.let {
                      if (it != null) {
                          cocktailAdapter.differ.submitList(it.drinks)
                      }
                  }
              }

              is ApiCallNetworkResource.Error -> {
                  it.message.let {
                      Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                      Log.d("MQ", "An error occur: $it")
                  }
              }

              is ApiCallNetworkResource .Loading -> {
                  binding.connectionLostImage.visibility = View.INVISIBLE
                  binding.connectionLostText.visibility = View.INVISIBLE

              }
          }
        }
    }




    /** Observing network state **/
    private fun doThisWhenNetworkIsLost() {
        binding.connectionLostImage.visibility = View.VISIBLE
        binding.connectionLostText.visibility = View.VISIBLE
        binding.cocktailRecyclerview.visibility = View.INVISIBLE
    }
    private fun doThisWhenNetworkIsAvailable() {
        binding.cocktailRecyclerview.visibility = View.VISIBLE
        vieModel.getCocktailDetails("a")
    }



    /**Log out Alert Dialogue */
    private fun showLogOutAlert(){
        val dialogView = layoutInflater.inflate(R.layout.custom_logout_dialog, null)
        val customDialog = activity?.let {
            AlertDialog.Builder(it)
                .setView(dialogView)
                .show()
        }

        val btnProfileLogOutDialog = dialogView.findViewById<Button>(R.id.fragment_quit_btn)
        btnProfileLogOutDialog.setOnClickListener {
            customDialog?.dismiss()
            requireActivity().finish()

        }

        val btnProfileCancelDialog = dialogView.findViewById<Button>(R.id.fragment_cancel_btn)
        btnProfileCancelDialog.setOnClickListener {
            customDialog?.dismiss()
        }
    }




    /** to quit the app **/
    fun onBackPress(){
        val callBack = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                showLogOutAlert()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callBack)
    }



}