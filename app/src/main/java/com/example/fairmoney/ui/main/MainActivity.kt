package com.example.fairmoney.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fairmoney.databinding.ActivityMainBinding
import com.example.fairmoney.ui.details.DetailsActivity
import com.example.fairmoney.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),MainActivityAdapter.CharacterItemListener {
    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    private lateinit var adapter: MainActivityAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupObservers()
    }


    private fun setupRecyclerView() {
        adapter = MainActivityAdapter(this)
        binding.charactersRv.layoutManager = LinearLayoutManager(this)
        binding.charactersRv.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.usersList.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE

                    it.data?.data?.let { list->
                        adapter.setItems(list)

                    }
                }
                Resource.Status.ERROR ->
                    it.message?.let {
                        showError(it)
                        binding.progressBar.visibility = View.GONE

                    }

                Resource.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        })
    }


    override fun onClickedCharacter(characterId: String) {

        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRAS_MOVIE_ID, characterId)
        startActivity(intent)

    }

    private fun showError(msg: String) {
        Snackbar.make(vparent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

}