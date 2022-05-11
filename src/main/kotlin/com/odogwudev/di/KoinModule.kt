package com.odogwudev.di

import com.odogwudev.util.Constants.DATABASE
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val koinModule = module {
    single {
        KMongo.createClient()// library for mongodb
            .coroutine
            .getDatabase(DATABASE)
    }
}