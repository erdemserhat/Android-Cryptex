package com.erdemserhat.encryptext.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * If you want to expand this project with additional features,
 * add your dependencies here.
 *
 * The Hilt setup has already been completed. You just need to
 * declare your dependency providers (such as Retrofit, Repositories, etc.)
 * here and inject them into any class where they are required.
 */
@Module
@InstallIn(SingletonComponent::class)
object ExampleModule {
    // you can delete this module if you wish, I just wanted to demonstrate use of hilt to make you remember.


}
