package com.freshbasket.customer.screens.commonadapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.freshbasket.customer.MyApplication.Companion.TAG
import com.freshbasket.customer.R
import com.freshbasket.customer.api.NetworkStatus
import com.freshbasket.customer.constants.Constants
import com.freshbasket.customer.listner.IAdapterClickListener
import com.freshbasket.customer.listner.IDialogClickListener
import com.freshbasket.customer.model.Product
import com.freshbasket.customer.model.Sku
import com.freshbasket.customer.model.inputmodel.ModifyCartIp
import com.freshbasket.customer.util.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partialy_cart_list_item.*
import kotlinx.android.synthetic.main.product_quantity_control_layout.*
import kotlinx.android.synthetic.main.product_weight_layout.*


class ProductListAdapter(var context: Context, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) :
        RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    var list: ArrayList<Product>? = null
    var modifProd: Product? = null
    var temp_cart_value: Int? = null
    fun addList(list: ArrayList<Product>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListAdapter.ViewHolder {
        var view = parent.inflate(R.layout.partialy_cart_list_item)
        return ViewHolder(view, adapterClickListener, adapterType)
    }

    override fun getItemCount(): Int {
        return if (list != null && list!!.size > 0) list!!.size else 0
    }

    override fun onBindViewHolder(holder: ProductListAdapter.ViewHolder, position: Int) {
        holder.bind(context, list!![position], position)
    }

    inner class ViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?, var adapterType: String = "common")
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        @SuppressLint("StringFormatMatches")
        fun bind(context: Context, item: Any, pos: Int) {
            if (item is Product) {


                /* this is avilible only cart API  */
                if (!item.image.isNullOrEmpty())
                    ImageLoader.setImage(imageView = image_product, imageUrl = item.image!!)

                item.pic?.apply {
                    if (item.pic.isNotEmpty())
                        ImageLoader.setImage(imageView = image_product, imageUrl = item.pic[0].pic!!)
                }


                item.name?.apply {
                    product_name.text = item.name
                }

                item.selSku = item.getSelectedSku()
                updateSelectedSkuPrices(item)
                if (adapterType == Constants.TYPE_WISH_LIST_ADAPTER || adapterType == Constants.TYPE_CART_ADAPTER) {
                    image_wishhlist.visibility = View.GONE
                    image_delete.visibility = View.VISIBLE
                } else
                    image_wishhlist.visibility = View.VISIBLE


                if (adapterType == Constants.TYPE_CART_SUMMARY) {
                    image_wishhlist.visibility = View.GONE
                    image_delete.visibility = View.GONE
                    tv_changewiehgt.isEnabled = false
                }


                updateWishListImage(item.wishlist, image_wishhlist)

                weigh_control.setOnClickListener {
                    CommonUtils.showSkuDialog(context, item, dialogClickListener = IDialogClickListener { data -> updateSelectedSkuPrices(item) }) }

                itemView.setOnClickListener { adapterClickListener?.onclick(item = item, pos = pos, type = temp_cart_value.toString(), op = Constants.OP_PROD_DETAILS) }
                tv_increment.setOnClickListener { clickForModify(it, item) }
                tv_decrement.setOnClickListener { clickForModify(it, item) }
                layout_add_cart.setOnClickListener { clickForModify(it, item) }
                image_wishhlist.setOnClickListener { modifyWishList(item) }
                /* image_wishhlist.setOnClickListener { modifyWishList(item) }
                 image_wishhlist.setOnCheckStateChangeListener { view, checked -> Log.e(TAG, "click $checked") }*/
                image_delete.setOnClickListener {
                    if (adapterType == Constants.TYPE_CART_ADAPTER)
                        removeFromCart(item)
                    else
                        modifyWishList(item)

                }
            }
        }

        private fun modifyWishList(item: Product) {


            if (NetworkStatus.isOnline2(context)) {
                item.tempWishlist = if (item.wishlist == 1) 0 else 1
                updateWishListImage(item.tempWishlist, image_wishhlist)
                modifProd = item
                item.prodPos = adapterPosition
                adapterClickListener?.onclick(item = item, pos = adapterPosition, op = Constants.WISHLIST)

            } else
                Toast.makeText(context, R.string.error_no_internet, Toast.LENGTH_SHORT).show()


        }

        fun removeFromCart(item: Product) {
            item.selSku?.apply {
                if (NetworkStatus.isOnline2(context)) {
                    modifProd = item
                    item.modifyCartIp = ModifyCartIp(id_sku = _id, id_product = id_product, op = Constants.MODIFY, quantity = tempMyCart.toString(),
                            _session = CommonUtils.getSession(), _id = CommonUtils.getCustomerID())
                    item.prodPos = adapterPosition
                    adapterClickListener?.onclick(item = item, pos = adapterPosition, op = Constants.TYPE_REMOVE_CART)

                } else
                    Toast.makeText(context, R.string.error_no_internet, Toast.LENGTH_SHORT).show()
            }

        }

        private fun updateWishListImage(item: Int?, wishlist_image: AppCompatImageView?) {
            if (item == 1)
                wishlist_image?.setImageResource(R.drawable.ic_faveriet_selected)
            else
                wishlist_image?.setImageResource(R.drawable.ic_favorite_un_selected)

        }


        private fun updateWishListImage(item: Product, wishlist_image: AppCompatImageView) {

            if (item.tempWishlist != -1) {
                wishlist_image.setImageResource(if (item.tempWishlist == 1) R.drawable.ic_faveriet_selected else R.drawable.ic_favorite_un_selected)
            } else {
                wishlist_image.setImageResource(if (item.wishlist == 1) R.drawable.ic_faveriet_selected else R.drawable.ic_favorite_un_selected)
            }

        }

        private fun clickForModify(view: View, item: Product) {
            if (!Validation.isValidObject(item.getSelectedSku()) || item.getSelectedSku()?.tempMyCart != -1) {
                MyLogUtils.e(TAG, "getTempMyCart not -1:" + item.getSelectedSku()?._id)
                return
            }

            when (view.id) {

                R.id.tv_increment, R.id.layout_add_cart -> {
                    item.selSku?.apply {
                        if (item.getSelectedSku()?.stock!!.toFloat() <= item.getSelectedSku()?.mycart!!.toFloat()) {
                            Toast.makeText(context, R.string.cannot_add_items_out_of_stock, Toast.LENGTH_SHORT).show()
                            return
                        }

                        if (item.getSelectedSku()?.mycart!!.toInt() < item.getSelectedSku()?.min_quantity!!.toInt()) {
                            if (item.getSelectedSku()?.min_quantity!!.toInt() > 1)
                                Toast.makeText(context, context.getString(R.string.should_be_minimum_quantity) + "${item.getSelectedSku()?.min_quantity}", Toast.LENGTH_SHORT).show()
                            tempMyCart = mycart!!.toInt() + item.getSelectedSku()?.min_quantity!!.toInt()
                        } else
                            tempMyCart = mycart!!.toInt() + 1

                        temp_cart_value = tempMyCart
                        item.modifyCartIp = ModifyCartIp(id_sku = _id, id_product = id_product, op = Constants.MODIFY, quantity = tempMyCart.toString(),
                                _session = CommonUtils.getSession(), _id = CommonUtils.getCustomerID())
                        modifProd = item
                        item.prodPos = adapterPosition
                        adapterClickListener?.onclick(item = item, op = Constants.OP_MODIFY_CART, pos = adapterPosition)


                    }
                }

                R.id.tv_decrement -> {
                    item.selSku?.apply {
                        if (item.getSelectedSku()?.mycart!!.toInt() <= item.getSelectedSku()?.min_quantity!!.toInt()) {
                            if (item.getSelectedSku()?.min_quantity!!.toInt() > 1)
                                Toast.makeText(context, context.getString(R.string.should_be_minimum_quantity) + "${item.getSelectedSku()?.min_quantity}", Toast.LENGTH_SHORT).show()
                            item.getSelectedSku()?.tempMyCart = 0
                        } else
                            tempMyCart = mycart!!.toInt() - 1

                        temp_cart_value = tempMyCart

                        item.modifyCartIp = ModifyCartIp(id_sku = _id, id_product = id_product, op = Constants.MODIFY, quantity = tempMyCart.toString(), _session = CommonUtils.getSession(), _id = CommonUtils.getCustomerID())
                        modifProd = item
                        item.prodPos = adapterPosition
                        adapterClickListener?.onclick(item = item,
                                op = Constants.OP_MODIFY_CART, pos = adapterPosition)

                    }
                }
            }
        }


        /*UPDATE SKU PRICES*/
        private fun updateSelectedSkuPrices(item: Product) {
            val selSku = item.getSelectedSku()
            selSku?.apply {
                tv_sku.text = selSku?.size
                val sellingPrice = this!!.selling_price?.toFloat()
                val mrp = mrp?.toFloat()
                tempMyCart = -1
                setCartCount(this@ViewHolder, this)
                if (mrp != null && mrp > 0 && sellingPrice != null) {
                    val savings = (((mrp - sellingPrice) / mrp) * 100)
                    assignProdAmt(sellingPrice, mrp, savings)
                }
            }

        }

        private fun setCartCount(viewHoldr: ViewHolder, sku: Sku) {
            viewHoldr.apply {
                tv_sku_count.text = if (sku.tempMyCart != -1) sku.tempMyCart.toString() else sku.mycart
                if (tv_sku_count.text.toString().contentEquals("0")) {
                    layout_add_cart.visibility = View.VISIBLE
                    layout_prod_count.visibility = View.GONE
                } else {
                    layout_add_cart.visibility = View.GONE
                    layout_prod_count.visibility = View.VISIBLE
                }
            }
        }


        private fun assignProdAmt(sellingPrice: Float, mrp: Float, savings: Float) {
            tv_mrp_price.apply {
                visibility = if (mrp - sellingPrice > 0) View.VISIBLE else View.GONE
                text = context.getString(R.string.rs, "%.0f".format(mrp))
                strikeThr()
            }
            tv_selling_price.apply {
                visibility = if (sellingPrice > 0) View.VISIBLE else View.GONE
                text = context.getString(R.string.rs, "%.0f".format(sellingPrice))
            }

            if (savings > 0) {
                tv_big_save.apply {
                    visibility = View.VISIBLE
                    text = context.getString(R.string.big_save_price, "%.0f".format(savings) + " %")
                }
            }

            tv_big_save.visibility = View.GONE   //TODO() Need to remove once get Properly prices*/
        }


    }

    fun showModifiedRes(type: String) {
        modifProd?.apply {
            when (type) {
                Constants.RES_SUCCESS -> {
                    selSku!!.mycart = getSelectedSku()?.tempMyCart.toString()
                    selSku!!.tempMyCart = -1
                    notifyItemChanged(prodPos)
                    //  removeFromCart()

                }
                Constants.RES_FAILED -> {
                    selSku!!.tempMyCart = -1
                }
            }
        }
    }


    private fun getProductPostionId(modifyCartIp: ModifyCartIp?): Int {
        if (Validation.isValidObject(modifyCartIp)) {
            val idproduct = modifyCartIp!!.id_product
            val sku_id = modifyCartIp.id_sku

            list?.apply {

                for (item in list!!) {
                    if (item._id!!.contentEquals(idproduct!!) && sku_id!!.contentEquals(item.getSelectedSku()?._id!!))
                        return item.prodPos

                }
            }
        }
        return -1

    }


    fun showModifiedWishListRes(type: String) {
        modifProd?.apply {
            when (type) {
                Constants.RES_SUCCESS -> {
                    wishlist = tempWishlist
                    notifyItemChanged(prodPos)
                    tempWishlist = -1
                }

                Constants.RES_FAILED -> {
                    tempWishlist = -1
                    notifyItemChanged(prodPos)
                }

            }
        }
    }

}