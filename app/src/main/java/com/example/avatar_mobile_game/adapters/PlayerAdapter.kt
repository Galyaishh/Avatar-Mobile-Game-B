package com.example.avatar_mobile_game.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.avatar_mobile_game.DataManager.PlayerRecord
import com.example.avatar_mobile_game.databinding.PlayerItemBinding
import com.example.avatar_mobile_game.interfaces.PlayerCallback

class PlayerAdapter(private val players: List<PlayerRecord>) :
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    var onPlayerClickListener: PlayerCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = PlayerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int = players.size

    private fun getItem(position: Int): PlayerRecord = players[position]

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = getItem(position)

        with(holder.binding) {
            playerTXTRank.text = (position + 1).toString()
            playerTXTName.text = player.name
            playerTXTScore.text = "Score: ${player.score}"

            root.setOnClickListener {
                onPlayerClickListener?.let {
                    it.showLocationOnMap(player, position)
                }
            }
        }
    }

    inner class PlayerViewHolder(val binding: PlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
