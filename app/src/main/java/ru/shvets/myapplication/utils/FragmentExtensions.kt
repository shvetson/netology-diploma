package ru.shvets.myapplication.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import ru.shvets.myapplication.R

fun Fragment.findTopNavController(): NavController {
    val topLevelHost = requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_container_main) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}