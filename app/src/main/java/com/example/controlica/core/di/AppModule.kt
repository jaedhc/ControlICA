package com.example.controlica.core.di

import com.example.controlica.data.repository.AuthRepositoryImpl
import com.example.controlica.data.repository.EmployeeRepositoryImpl
import com.example.controlica.data.repository.ProductRepositoryImpl
import com.example.controlica.domain.repository.AuthRepository
import com.example.controlica.domain.repository.EmployeeRepository
import com.example.controlica.domain.repository.ProductRepository
import com.example.controlica.domain.use_case.employees.GetAllEmployeesUseCase
import com.example.controlica.domain.use_case.employees.GetEmployeeByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.minimalSettings
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    @Named("supabase_public_client")
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
    @Named("supabase_admin_client")
    fun provideAdminSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = com.example.controlica.BuildConfig.SUPABASE_URL,
            supabaseKey = com.example.controlica.BuildConfig.SUPABASE_SERVICE_ROLE_KEY// 👈 Aquí debes agregar en tus BuildConfig tu service_key
        ) {
            install(Postgrest)
            install(Auth){
                minimalSettings()
            }
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseDatabase(@Named("supabase_public_client")client: SupabaseClient): Postgrest {
        return client.postgrest
    }

    @Provides
    @Singleton
    fun provideSupabaseAuth(@Named("supabase_public_client") client: SupabaseClient): Auth {
        return client.auth
    }

    @Provides
    @Singleton
    fun provideSupabaseStorage(@Named("supabase_public_client") client: SupabaseClient): Storage {
        return client.storage
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: Auth): AuthRepository = AuthRepositoryImpl(auth)

    @Provides
    @Singleton
    fun provideEmployeeRepository(
        @Named("supabase_public_client") supabaseClient: SupabaseClient,
        @Named("supabase_admin_client") supabaseAdminClient: SupabaseClient,
        auth: Auth
    ): EmployeeRepository {
        return EmployeeRepositoryImpl(supabaseClient, supabaseAdminClient, auth)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        @Named("supabase_public_client") supabaseClient: SupabaseClient,
    ): ProductRepository {
        return ProductRepositoryImpl(supabaseClient)
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