package com.qymoy.ShitterCommunicator

import android.content.Context
import com.qymoy.ShitterCommunicator.data.bluetooth.BluetoothControllerImpl
import com.qymoy.ShitterCommunicator.domain.BluetoothController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Singleton
    @Provides
    fun provideBleController(@ApplicationContext context: Context): BluetoothController =
        BluetoothControllerImpl(context)
}