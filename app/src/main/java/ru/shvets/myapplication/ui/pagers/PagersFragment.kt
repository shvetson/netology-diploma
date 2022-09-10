package ru.shvets.myapplication.ui.pagers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import ru.shvets.myapplication.R
import ru.shvets.myapplication.databinding.FragmentPagersBinding

class PagersFragment : Fragment(R.layout.fragment_pagers) {
    private lateinit var binding: FragmentPagersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagersBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val navController = (childFragmentManager.findFragmentById(R.id.fragment_container_pagers) as NavHostFragment).navController
        val config = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, config)
        binding.bottomNavigationView.setupWithNavController(navController)

//        NavigationUI.setupActionBarWithNavController(this, navController)
//        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        return binding.root
    }
}