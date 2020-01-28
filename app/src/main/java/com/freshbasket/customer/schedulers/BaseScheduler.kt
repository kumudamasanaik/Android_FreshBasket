package com.freshbasket.customer.schedulers

import io.reactivex.Scheduler

interface BaseScheduler {
    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}