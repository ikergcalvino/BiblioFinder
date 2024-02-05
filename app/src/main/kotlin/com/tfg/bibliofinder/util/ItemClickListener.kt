package com.tfg.bibliofinder.util

interface ItemClickListener<T> {
    fun onItemClick(item: T)
    fun onInfoButtonClick(item: T)
    fun onBookButtonClick(item: T)
}