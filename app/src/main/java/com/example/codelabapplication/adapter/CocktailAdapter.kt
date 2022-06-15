package com.example.codelabapplication.adapter

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.codelabapplication.R
import com.example.codelabapplication.databinding.CocktailLayoutBindingBinding
import com.example.codelabapplication.model.Drink


class CocktailAdapter():RecyclerView.Adapter<CocktailAdapter.CocktailViewHolder>(){


    val diffUtilCallBack = object : DiffUtil.ItemCallback<Drink>() {

        override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.strAlcoholic == newItem.strAlcoholic
        }

        override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.strAlcoholic == newItem.strAlcoholic && oldItem.dateModified == newItem.dateModified
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallBack)

    inner class CocktailViewHolder(binding: CocktailLayoutBindingBinding):RecyclerView.ViewHolder(binding.root){
        val cocktailCategory = binding.tvCocktailCategory
        val cocktailGlass = binding.tvCocktailGlass
        val cocktailImage = binding.ivCocktailImage
        val firstIngredient = binding.tvFirstIngredient
        val instruction = binding.tvInstruction
        val  strDrink = binding.tvStrDrink
        val alcohol = binding.tvAlcoholic

        @SuppressLint("SetTextI18n")
        fun bindView(cocktail: Drink){
            val tvCocktailCategory : CharSequence = Html.fromHtml("<b>${itemView.context.getString(R.string.cocktail_category)}</b>  ${ cocktail.strCategory}")
            cocktailCategory.setText(tvCocktailCategory)
            val tvGlassCategory : CharSequence = Html.fromHtml("<b>${itemView.context.getString(R.string.cocktail_glass)}</b>  ${ cocktail.strGlass}")
            cocktailGlass.setText(tvGlassCategory)
            Glide.with(itemView.context)
               .load(cocktail.strDrinkThumb)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions().placeholder(R.drawable.paugba))
                .into(cocktailImage)
            val ingredients : CharSequence = Html.fromHtml("<b>${itemView.context.getString(R.string.first_ingredient)}</b>  " +
                    "${cocktail.strIngredient1}, ${cocktail.strIngredient2}, ${cocktail.strIngredient3}")
            firstIngredient.setText(ingredients)
            val txt1 : CharSequence = Html.fromHtml("<b>${itemView.context.getString(R.string.instruction)}</b>  ${cocktail.strInstructions}")
            instruction.setText(txt1)
            val txt : CharSequence = Html.fromHtml("<b>${itemView.context.getString(R.string.strdrink)}</b>  ${ cocktail.strDrink}")
            strDrink.setText(txt)
            alcohol.text = itemView.context.getString(R.string.alcoholic, cocktail.strAlcoholic)
            if (cocktail.strAlcoholic == "Alcoholic"){
                val styledText: CharSequence = Html.fromHtml("<b>Alcoholic:</b>  Yes")
               alcohol.setText(styledText)
            }else{
                val styledText: CharSequence = Html.fromHtml("<b>Alcoholic:</b>  No")
                alcohol.setText(styledText)
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailAdapter.CocktailViewHolder {
       val binding = CocktailLayoutBindingBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return CocktailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CocktailAdapter.CocktailViewHolder, position: Int) {
        holder.bindView(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}