package com.tahirdev.kyberchat.ui.onboard

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tahirdev.kyberchat.R
import com.tahirdev.kyberchat.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    // Declare the binding object
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): OnboardingFragment {
            val fragment = OnboardingFragment()
            val bundle = Bundle().apply {
                putInt(ARG_POSITION, position)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using ViewBinding
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)

     //   binding.onBoarding.setBackgroundResource(R.drawable.ob_bg1_night)

        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                // Night mode is active, set the night background
                binding.onBoarding.setBackgroundResource(R.drawable.ob_bg1_night)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                // Day mode is active, set the day background
                binding.onBoarding.setBackgroundResource(R.drawable.ob_bg)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                // Default mode or undefined, you can set a default background here
                binding.onBoarding.setBackgroundResource(R.drawable.ob_bg)  // or whatever default you prefer
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the position passed in arguments and set content accordingly
        val position = arguments?.getInt(ARG_POSITION) ?: 0

        binding.onboardingImage.setImageResource(R.drawable.ob2)

        // Use position to change the content for each onboarding screen
        when (position) {
            0 -> {
                binding.onboardingImage.setImageResource(R.drawable.ob2)
                binding.onboardingText.text = getString(R.string.ob1Txt)
            }
            1 -> {
                binding.onboardingImage.setImageResource(R.drawable.ob2)
                binding.onboardingText.text = getString(R.string.ob2Txt)
            }
            2 -> {
                binding.onboardingImage.setImageResource(R.drawable.ob2)
                binding.onboardingText.text = getString(R.string.ob3Txt)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
