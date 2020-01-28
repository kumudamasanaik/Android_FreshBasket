package com.freshbasket.customer.model.inputmodel

data class SearchInput(
        val _id: String,
        val _session: String,
        val count: Int,
        val lang: String,
        val search: String,
        val start: Int
)