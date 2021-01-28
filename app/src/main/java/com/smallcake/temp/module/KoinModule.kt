package com.smallcake.temp.module

import org.koin.dsl.module

interface HelloRepository {
    fun giveHello(): String
}

class HelloRepositoryImpl() :HelloRepository {
     override fun giveHello() = "Hello Koin"
}
class MySimplePresenter(val repo: HelloRepository) {

    fun sayHello() = "${repo.giveHello()} from $this"
}

val appModule = module {

    // single instance of HelloRepository
    single<HelloRepository> { HelloRepositoryImpl() }

    // Simple Presenter Factory
    single { MySimplePresenter(get()) }
}