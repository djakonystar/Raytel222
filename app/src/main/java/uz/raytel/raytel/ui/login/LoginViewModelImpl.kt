package uz.raytel.raytel.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.raytel.raytel.data.remote.ResultData
import uz.raytel.raytel.data.remote.auth.AuthResponse
import uz.raytel.raytel.data.remote.auth.SignInDeviceId
import uz.raytel.raytel.data.remote.auth.SignInPhone
import uz.raytel.raytel.domain.repository.Repository
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val repository: Repository
): LoginViewModel, ViewModel() {
    override val signInDeviceIdFlow = MutableSharedFlow<AuthResponse>()
    override val signInPhoneFlow = MutableSharedFlow<AuthResponse>()
    override val loadingFlow = MutableSharedFlow<Boolean>()
    override val messageFlow = MutableSharedFlow<String>()
    override val errorFlow = MutableSharedFlow<Throwable>()

    override suspend fun signInDeviceId(signInDeviceId: SignInDeviceId) {
        repository.signInWithDeviceId(signInDeviceId).onEach {
            when (it) {
                is ResultData.Loading -> {
                    loadingFlow.emit(true)
                }
                is ResultData.Success -> {
                    loadingFlow.emit(false)
                    signInDeviceIdFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    loadingFlow.emit(false)
                    messageFlow.emit(it.message)
                }
                is ResultData.Error -> {
                    loadingFlow.emit(false)
                    errorFlow.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }

    override suspend fun signInPhone(signInPhone: SignInPhone) {
        repository.signInWithPhoneNumber(signInPhone).onEach {
            when (it) {
                is ResultData.Loading -> {
                    loadingFlow.emit(true)
                }
                is ResultData.Success -> {
                    loadingFlow.emit(false)
                    signInPhoneFlow.emit(it.data)
                }
                is ResultData.Message -> {
                    loadingFlow.emit(false)
                    messageFlow.emit(it.message)
                }
                is ResultData.Error -> {
                    loadingFlow.emit(false)
                    errorFlow.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }
}