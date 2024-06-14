package com.example.urskin.data.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.urskin.R
import com.example.urskin.data.pref.ArticleModel
import com.example.urskin.view.article.ArticleActivity

class ArticleAdapter(
    private val listArticle: ArrayList<ArticleModel>
) : RecyclerView.Adapter<ArticleAdapter.ListViewHolder>(){

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_article,parent,false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listArticle.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, source, tittle, desc, img) = listArticle[position]
        holder.tvId.text = id
        holder.tvSource.text = source
        holder.tvTittle.text = tittle
        holder.tvDesc.text = desc
        holder.imgArticle.setImageResource(img)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,ArticleActivity::class.java)
            intent.putExtra("key_article",listArticle[holder.adapterPosition])
            holder.itemView.context.startActivity(intent)
        }
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ArticleModel)
    }


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tv_id)
        val tvSource: TextView = itemView.findViewById(R.id.tv_source)
        val tvTittle: TextView = itemView.findViewById(R.id.tv_tittle)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_desc)
        val imgArticle: ImageView = itemView.findViewById(R.id.img_article)
    }
}