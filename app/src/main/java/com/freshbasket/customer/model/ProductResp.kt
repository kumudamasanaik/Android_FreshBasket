package com.freshbasket.customer.model

data class ProductResp(
        val id_customer: String?,
        val product: ArrayList<Product>?,
        val summary: CartSummary?,
        val total_sku: Int? = 0
) : CommonRes() {

    fun getSortedProductData(): ArrayList<Product> {
        product?.apply {
            ArrayList(product)
                    .sortedWith(compareByDescending<Product> { it.selSku!!.selling_price }
                            .thenByDescending { it.selSku!!.selling_price })
        }

        return product!!
    }


}
