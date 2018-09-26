package kotlinserverless.framework.services

import kotlinserverless.framework.healthchecks.InvalidEndpoint

interface SOAServiceInterface<T> {
    fun execute(caller: Int?) : SOAResult<T> {
        throw InvalidEndpoint()
    }

    // GET/UPDATE a single result by id and filters
    fun execute(caller: Int?, id: Int?, params: Map<String, String>?) : SOAResult<T> {
        throw InvalidEndpoint()
    }

    // GET/UPDATE a single result by key and filters
    fun execute(caller: Int?, key: String?, params: Map<String, String>?) : SOAResult<T> {
        throw InvalidEndpoint()
    }

    // GET/UPDATE multiple results by filters
    fun execute(caller: Int?, params: Map<String, String>?) : SOAResult<List<T>> {
        throw InvalidEndpoint()
    }

    // CREATE/UPDATE a single object
    fun execute(caller: Int?, data: T?, params: Map<String, String>?) : SOAResult<T> {
        throw InvalidEndpoint()
    }

    // CREATE/UPDATE multiple objects
    fun execute(caller: Int?, dataList: List<T>?, params: Map<String, String>?) : SOAResult<List<T>> {
        throw InvalidEndpoint()
    }
}