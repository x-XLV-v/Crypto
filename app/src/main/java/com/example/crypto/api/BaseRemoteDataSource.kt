package com.example.crypto.api

import com.example.crypto.api.model.GenericResponse
import com.example.crypto.utils.Constants
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

//Базовый источник удалённых данных
abstract class BaseRemoteDataSource {
    //Данная функция используется для получения от запроса к серверу
    suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            //Вызов функции для получения ответа с сервера
            val response = call()
            //Если запрос выполнен успешно
            if (response.isSuccessful) {
                //Получаем тело ответа из самого ответа
                val body = response.body()
                //Если тело не равно null возвращаем класс с телом ответа
                if (body != null) return Result.Success(body)
            } else if (response.errorBody() != null) {  //Иначе если тело ошибки не пустое
                //Получаем тело ошибки и преобразуем его к объекту класса GenericResponse
                val errorBody = getErrorBody(response.errorBody())
                //Если сообщение не равно null возвращаем ошибку с сообщения из ответа,
                // в противном случае, возрващаем сообщение из нашей константы
                return error(errorBody?.message ?: Constants.GENERIC_ERROR)
            }
            return error(Constants.GENERIC_ERROR)
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }
    //Данная функция формирует класс содержащий в себе сообщение об ошибке
    private fun error(message: String): Result.Error = Result.Error(message)

    //ResponseBody это тело ответа, получаем его при помощи сетевого запроса
    //Данная функция получает информацию об ошибке из ответа с сервера
    private fun getErrorBody(responseBody: ResponseBody?): GenericResponse? =
        try {
            Gson().fromJson(responseBody?.charStream(), GenericResponse::class.java)
        } catch (e: Exception) {
            null
        }
}