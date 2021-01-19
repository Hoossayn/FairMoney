package com.example.fairmoney.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.fairmoney.data.Model.entities.Data
import com.example.fairmoney.databinding.ItemCharacterBinding

class MainActivityAdapter(private val listener: CharacterItemListener): RecyclerView.Adapter<MainActivityViewHolder>()  {

    interface CharacterItemListener {
        fun onClickedCharacter(characterId: String)
    }

    private val items = ArrayList<Data>()

    fun setItems(items: List<Data>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityViewHolder {
        val binding: ItemCharacterBinding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainActivityViewHolder(binding, listener)    }

    override fun onBindViewHolder(holder: MainActivityViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

}

class MainActivityViewHolder(private val itemBinding: ItemCharacterBinding, private val listener: MainActivityAdapter.CharacterItemListener) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener{

    private lateinit var character: Data

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: Data) {
        this.character = item
        itemBinding.name.text = "${item.firstName} ${item.lastName}"
        itemBinding.speciesAndStatus.text = """${item.email}"""
        Glide.with(itemBinding.root)
            .load(item.picture)
            .transform(CircleCrop())
            .into(itemBinding.image)
    }

    override fun onClick(v: View?) {
        listener.onClickedCharacter(character.id)
    }

}
