package com.example.controlica.core.di

import com.example.controlica.data.repository.AuthRepositoryImpl
import com.example.controlica.data.repository.EmployeeRepositoryImpl
import com.example.controlica.domain.repository.AuthRepository
import com.example.controlica.domain.repository.EmployeeRepository
import com.example.controlica.domain.use_case.auth.GetAllEmployeesUseCase
import com.example.controlica.domain.use_case.auth.GetEmployeeByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient{
        return createSupabaseClient(
            supabaseUrl = com.example.controlica.BuildConfig.SUPABASE_URL,
            supabaseKey = com.example.controlica.BuildConfig.SUPABASE_ANON_KEY
        ){
            install(Postgrest)
            install(Auth){
                flowType = FlowType.PKCE
                scheme = "app"
                host = "supabase.com"
            }
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
        return client.postgrest
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(client: SupabaseClient): Auth {
        return client.auth
    }

    @Provides
    @Singleton
    fun provideSupabaseStorage(client: SupabaseClient): Storage {
        return client.storage
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: Auth): AuthRepository = AuthRepositoryImpl(auth)

    @Provides
    @Singleton
    fun provideEmployeeRepository(
        supabaseClient: SupabaseClient
    ): EmployeeRepository {
        return EmployeeRepositoryImpl(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideGetEmployeeByIdUseCase(
        employeeRepository: EmployeeRepository
    ): GetEmployeeByIdUseCase {
        return GetEmployeeByIdUseCase(employeeRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllEmployeesUseCase(
        employeeRepository: EmployeeRepository
    ): GetAllEmployeesUseCase {
        return GetAllEmployeesUseCase(employeeRepository)
    }

}