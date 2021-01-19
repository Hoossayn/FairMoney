package com.example.fairmoney.ui.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.fairmoney.data.Model.entities.User
import com.example.fairmoney.databinding.ActivityDetailsBinding
import com.example.fairmoney.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var userId:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        intent?.getStringExtra(EXTRAS_MOVIE_ID)?.let { id ->
            viewModel.getMovieDetail(id)
            setupObservers()
        } ?: showError("Unknown Movie")


    }

    private fun setupObservers() {
        viewModel.user.observe(this, Observer {
            when (it?.status) {
                Resource.Status.SUCCESS -> {
                    if (it.data != null)
                        bindCharacter(it.data)
                    binding.progressBar.visibility = View.GONE
                    binding.characterCl.visibility = View.VISIBLE
                }

                Resource.Status.ERROR ->
                    it.message?.let {
                        showError(it)
                        binding.progressBar.visibility = View.GONE

                    }

                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.characterCl.visibility = View.GONE
                }
            }
        })
    }

    private fun bindCharacter(user: User) {
        binding.name.text = "${user.firstName} ${user.lastName}"
        binding.emailAddress.text = user.email
        binding.phoneNumber.text = user.phone
        Glide.with(binding.root)
            .load(user.picture)
            .transform(CircleCrop())
            .into(binding.image)
    }

    private fun showError(msg: String) {
        Snackbar.make(vDetails, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    companion object {
        const val EXTRAS_MOVIE_ID = "movie_id"
    }
}