package dev.belalkhan.taletree

import dev.belalkhan.taletree.auth.AuthViewModel
import dev.belalkhan.taletree.auth.data.AuthRepository
import dev.belalkhan.taletree.auth.data.AuthRepositoryImpl
import dev.belalkhan.taletree.firebase.auth.FirebaseAuthClient
import dev.belalkhan.taletree.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val taleTreeModule = module {
    single { FirebaseAuthClient() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { AppViewModel(get()) }
    viewModel { MainViewModel(get()) }
}