package com.freshbasket.customer.model

data class NewFilterResUpdated(val fud_pref: ArrayList<FudPref>?,
                               val refine: ArrayList<Refine>?,
                               val sort: ArrayList<Sort>?): CommonRes()