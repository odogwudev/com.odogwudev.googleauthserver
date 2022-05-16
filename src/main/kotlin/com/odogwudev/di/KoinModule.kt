package com.odogwudev.di

import com.odogwudev.data.repository.UserDataSourceImpl
import com.odogwudev.domain.repository.UserDataSource
import com.odogwudev.util.Constants.DATABASE
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val koinModule = module {// library for mongodb
    single {
        KMongo.createClient(System.getenv("MONGODB_URI"))//using heroki i have to connect to atlas using a connection string or from heroku platform
            .coroutine
            .getDatabase(DATABASE)
    }
    single<UserDataSource> {//type of user data source
        UserDataSourceImpl(get())// get wwould fetch koin instance which is already provided amd already declared aboce
    }
}
