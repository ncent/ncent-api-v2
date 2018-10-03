package kotlinserverless.framework.dispatchers

import kotlinserverless.framework.models.*
import java.io.File
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import kotlinserverless.framework.controllers.DefaultController
import kotlinserverless.framework.healthchecks.models.Healthcheck
import kotlinserverless.main.users.models.User
import kotlinserverless.framework.controllers.DefaultRestController
import main.users.controllers.UserController
import kotlin.reflect.full.createInstance

/**
 * Request Dispatcher implementation
 */
open class RequestDispatcher: Dispatcher<ApiGatewayRequest, Any> {

    @Throws(RouterException::class)
    override fun locate(request: ApiGatewayRequest): Any? {
        val path = request.input["path"]
        for ((regex, model, controller) in ROUTER.routes) {
			if (!Regex(regex).matches(path as CharSequence)) {
				continue
			}

            val modelClass = Class.forName(model).kotlin
            val controllerClass = Class.forName(controller).kotlin
            val controllerInstance = controllerClass.createInstance()

            val func = controllerClass.members.find { it.name == "defaultRouting" }

            return func?.call(
                    controllerInstance,
                    modelClass::class.java,
                    request!!,
                    controllerInstance
            )
        }
		
		throw RouterException(path as? String ?: "")
    }

    /**
     * Singleton that loads the routes once and keep them on memory
     */
    companion object BackendRouter {
		// this is not ideal and should use get resources, but having issues getting
		// maven to load them properly
        private val FILE = File("src/main/resources/yml/routes.yml")
        val ROUTER: Routes = ObjectMapper(YAMLFactory()).readValue(FILE, Routes::class.java)
    }
}