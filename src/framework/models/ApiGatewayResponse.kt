package kotlinserverless.framework.models

import com.beust.klaxon.Klaxon
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import framework.models.BaseIntEntity
import kotlinserverless.framework.models.*
import org.apache.log4j.LogManager
import java.nio.charset.StandardCharsets
import java.util.*


/**
 * Instead of having a generic response for everything now the Response class is an interface
 * and we create an specific implementation of it
 */
class ApiGatewayResponse(
        val statusCode: Int = 200,
        var body: Any? = null,
        val headers: Map<String, String>? = Collections.emptyMap(),
        val isBase64Encoded: Boolean = false
): Response {

  companion object {
    inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    val LOG = LogManager.getLogger(this::class.java) //TODO: figure out how to user the correct class name.
  }
	
  override fun toString(): String {
	  var objectMapper: ObjectMapper = ObjectMapper()
	  return objectMapper.writeValueAsString(this)
  }

  /**
   * Uses the Builder pattern to create the response
   */
  class Builder {
    var objectMapper: ObjectMapper = ObjectMapper()

    var statusCode: Int = 200
    var rawBody: Any? = null
    var headers: Map<String, String>? = Collections.emptyMap()
    var objectBody: BaseIntEntity? = null
    var listBody: List<Any>? = null
    var binaryBody: ByteArray? = null
    var base64Encoded: Boolean = false

    fun build(): ApiGatewayResponse {
      //port these changes to Kotlin Serverless codebase
      var body: Any? = null
      body = body ?: if(rawBody != null) rawBody else null
      body = body ?: if(objectBody != null) objectBody?.toMap() else null
      body = body ?: if(listBody != null) listBody else null
      body = body ?: String(Base64.getEncoder().encode(binaryBody), StandardCharsets.UTF_8)

      return ApiGatewayResponse(statusCode, body, headers, base64Encoded)
    }
  }
}