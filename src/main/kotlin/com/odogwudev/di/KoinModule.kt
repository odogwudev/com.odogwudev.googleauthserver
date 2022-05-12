package com.odogwudev.di

import com.odogwudev.data.repository.UserDataSourceImpl
import com.odogwudev.domain.repository.UserDataSource
import com.odogwudev.util.Constants.DATABASE
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val koinModule = module {// library for mongodb
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase(DATABASE)
    }
    single<UserDataSource> {//type of user data source
        UserDataSourceImpl(get())// get wwould fetch koin instance which is already provided amd already declared aboce
    }
}
