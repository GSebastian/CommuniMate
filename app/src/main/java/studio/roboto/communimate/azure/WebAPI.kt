package studio.roboto.communimate.azure

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WebAPI {
    companion object {

        var sKeyPhrasesAPI: IKeyPhrasesAPI? = null

        var sSearchAPI: ISearchAPI? = null

        fun getKeyPhrasesAPI(): IKeyPhrasesAPI {
            if (sKeyPhrasesAPI == null) {
                initKeyPhrasesAPI()
            }
            return sKeyPhrasesAPI!!
        }

        fun getSearchAPI(): ISearchAPI {
            if (sSearchAPI == null) {
                initSearchAPI()
            }
            return sSearchAPI!!
        }

        private fun initSearchAPI() {
            val interceptor: Interceptor = Interceptor { chain ->
                var request: Request = chain.request()!!
                request = request.newBuilder()
                        .addHeader("Content-type", "application/json")
                        .addHeader("api-key", "***REMOVED***")
                        .build()
                chain.proceed(request)
            }
            sSearchAPI =
                    buildAPIAdapter("***REMOVED***",
                            interceptor).create(ISearchAPI::class.java)
        }

        private fun initKeyPhrasesAPI() {
            val interceptor: Interceptor = Interceptor { chain ->
                var request: Request = chain.request()!!
                request = request.newBuilder()
                        .addHeader("Content-type", "application/json")
                        .addHeader("Ocp-Apim-Subscription-Key", "***REMOVED***")
                        .build()
                chain.proceed(request)
            }
            sKeyPhrasesAPI =
                    buildAPIAdapter("***REMOVED***/",
                            interceptor).create(IKeyPhrasesAPI::class.java)
        }

        private fun buildAPIAdapter(baseUrl: String, interceptor: Interceptor): Retrofit {
            val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(interceptor)

            val loggingIntercept: HttpLoggingInterceptor = HttpLoggingInterceptor()
            loggingIntercept.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(loggingIntercept)

            val client: OkHttpClient = clientBuilder
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build()

            return Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
    }
}