package pl.accodash.coolchess.auth

import android.content.Context
import com.auth0.android.Auth0
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.callback.Callback
import com.auth0.android.authentication.AuthenticationException
import kotlinx.coroutines.suspendCancellableCoroutine
import pl.accodash.coolchess.BuildConfig
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SharedPreferencesStorage

class AuthManager(context: Context) {
    private val domain = BuildConfig.AUTH0_DOMAIN
    private val clientId = BuildConfig.AUTH0_CLIENT_ID
    private val audience = BuildConfig.AUTH0_AUDIENCE

    private val account = Auth0(clientId, domain)
    private val credentialsManager = CredentialsManager(
        com.auth0.android.authentication.AuthenticationAPIClient(account),
        SharedPreferencesStorage(context)
    )

    suspend fun login(context: Context): Credentials {
        return suspendCancellableCoroutine { cont ->
            WebAuthProvider.login(account)
                .withScheme("demo")
                .withAudience(audience)
                .withScope("openid profile email offline_access")
                .start(context, object : Callback<Credentials, AuthenticationException> {
                    override fun onSuccess(result: Credentials) {
                        credentialsManager.saveCredentials(result)
                        cont.resume(result)
                    }

                    override fun onFailure(error: AuthenticationException) {
                        cont.resumeWithException(error)
                    }
                })
        }
    }

    suspend fun getSavedCredentials(): Credentials? {
        return suspendCancellableCoroutine { cont ->
            credentialsManager.getCredentials(object : Callback<Credentials, CredentialsManagerException> {
                override fun onSuccess(result: Credentials) {
                    cont.resume(result)
                }

                override fun onFailure(error: CredentialsManagerException) {
                    cont.resume(null)
                }
            })
        }
    }

    fun hasSavedCredentials(): Boolean = credentialsManager.hasValidCredentials()
    fun clearCredentials() = credentialsManager.clearCredentials()
}

