package com.pg.homeworknetwork

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import coil.load
import coil.transform.RoundedCornersTransformation
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieDetailFragment : Fragment(R.layout.fragment_movie_preview) {
    lateinit var poster: ImageView
    lateinit var originalTitle: TextView
    lateinit var overview: TextView
    lateinit var popularity: TextView
    lateinit var releaseDate: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            poster = findViewById(R.id.poster)
            originalTitle = findViewById(R.id.originalTitle)
            overview = findViewById(R.id.overview)
            popularity = findViewById(R.id.popularity)
            releaseDate = findViewById(R.id.releaseDate)
        }

        val movieId = arguments?.getInt(ARG_ID) ?: 550

        ApiObj.getApi().getMovie(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { movie ->
                    println("MOVIE OBJECT --> $movie")
                    poster.load("${BuildConfig.API_IMAGE_BASE_URL}${movie.posterPath}") {
                        transformations(RoundedCornersTransformation(16f))
                    }
                    originalTitle.text = movie.originalTitle
                    overview.text = movie.overview
                    popularity.text = movie.popularity.toString()
                    releaseDate.text = movie.releaseDate
                },
                {
                    println(it.message)
                }
        )

        activity?.onBackPressedDispatcher?.addCallback(this.viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val manager: FragmentManager = parentFragmentManager
                val transaction: FragmentTransaction = manager.beginTransaction()
                transaction.replace(R.id.mainFragment, MovieListFragment())
                transaction.commit()
            }
        })
    }

    companion object {
        const val TAG = "MovieDetailFragment"
        const val ARG_ID = "MovieDetailFragment_Arguments_Movie_Id"
    }
}