package ca.nkrishnaswamy.picshare.network

import ca.nkrishnaswamy.picshare.BuildConfig
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*

const val baseUrl = BuildConfig.BASE_URL

interface APIService {

    companion object {
        operator fun invoke(): APIService {
            val retrofit = Retrofit.Builder().baseUrl(baseUrl).build()
            return retrofit.create(APIService::class.java)
        }
    }

    @POST("/api/users/{idToken}")
    suspend fun createUser(@Body requestBody: RequestBody, @Path("idToken") idToken: String): Response<ResponseBody>

    @DELETE("/api/users/{idToken}")
    suspend fun deleteUser(@Path("idToken") idToken: String): Response<ResponseBody>

    @PUT("/api/users/{idToken}")
    suspend fun updateUser(@Body requestBody: RequestBody, @Path("idToken") idToken: String): Response<ResponseBody>

}