package com.example.myapplication.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.view.adapter.ListAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.data.AcronymsDataItem
import com.example.myapplication.util.ApiState
import com.example.myapplication.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: ListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setupObserver()
    }

    private fun setupObserver() {
        mainViewModel.acronymsData.observe(this) {
            when (it) {
                is ApiState.Loading -> {
                    println("Statusxxxxxxxxxxx : Loading")
                    binding.progressBar.show()
                    binding.recyclerview.gone()
                }
                is ApiState.Success -> {
                    binding.progressBar.gone()
                    binding.recyclerview.show()
                    val acronymsDataItem = it.data as AcronymsDataItem
                    println("Status : Success : ${acronymsDataItem.sf}")
                    listAdapter.setData(acronymsDataItem.lfs)
                }
                is ApiState.Failure -> {
                    binding.progressBar.gone()
                    binding.recyclerview.gone()
                    println("Status : Failure : ${it.msg}")
                }
                is ApiState.Exception -> {
                    binding.progressBar.gone()
                    binding.recyclerview.gone()
                    println("Status : Exception :  ${it.code} ${it.msg}")
                    if (it.code == 503) {
                        showToast("Please Check the Internet Connection")
                    }
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    println("Status : Error : ")
                }
            }
        }
    }

    private fun initViews() {
        binding.findBtn.setOnClickListener {
            binding.searchBox.isFocused.let { view ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow((it as View).windowToken, 0)
            }
            val acronym = binding.searchBox.text?.toString() ?: ""
            val length = acronym.length
            when {
                (acronym.isEmpty().not() && length >= 3) -> {
                    mainViewModel.fetchAcronymsData(acronym)
                }
                acronym.isEmpty() -> {
                    showToast("Please enter the Acronym")
                }
                (acronym.isEmpty().not() && length < 3) -> {
                    showToast("Please enter the Minimum 3 Char")
                }
            }
        }
        listAdapter = ListAdapter()
        binding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listAdapter
        }
    }

    private fun Context.showToast(msg: CharSequence) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun View.show() {
        this.visibility = View.VISIBLE
    }

    private fun View.gone() {
        this.visibility = View.GONE
    }
}