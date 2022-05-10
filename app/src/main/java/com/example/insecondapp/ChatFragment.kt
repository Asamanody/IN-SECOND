package com.example.insecondapp


import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class ChatFragment : Fragment() {
    private val mViewModel: ChatViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chat_fragment, container, false)
    }
    companion object {
        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }
}