package com.example.paperwalls.ui.walls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.paperwalls.databinding.FragmentWallsBinding

class WallsFragment : Fragment() {

    private var _binding: FragmentWallsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val wallsViewModel =
            ViewModelProvider(this).get(WallsViewModel::class.java)

        _binding = FragmentWallsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textWalls
        wallsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}