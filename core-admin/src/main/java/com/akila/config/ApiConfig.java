package com.akila.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import java.lang.reflect.Method;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Akila", version = "v1"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
public class ApiConfig implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * Map permission to description.
     */
    public static final Map<String, String> PERMISSIONS = new HashMap<>();

    /**
     * Map API group to description.
     */
    public static final Map<String, String> API_GROUPS = new HashMap<>();

    private final OpenAPI openAPI;

    public ApiConfig(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

        requestMappingHandlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            // Get the controller class
            Class<?> controllerClass = handlerMethod.getBeanType();

            // Check for @RequestMapping annotation at the class level
            RequestMapping controllerRequestMapping = controllerClass.getAnnotation(RequestMapping.class);
            if (controllerRequestMapping == null) {
                return; // Skip if no @RequestMapping annotation is present
            }

            // Get API groups from @RequestMapping
            String[] controllerApiGroups = controllerRequestMapping.value();
            if (controllerApiGroups == null || controllerApiGroups.length == 0) {
                return; // Skip if no API groups are defined
            }

            // Check for @Tag annotation at the class level
            Tag controllerTag = controllerClass.getAnnotation(Tag.class);
            String apiGroupDescription = controllerTag != null ? controllerTag.description() : "";

            // Get the handler method
            Method method = handlerMethod.getMethod();

            // Check for @PostMapping annotation at the method level
            PostMapping handlerPostMapping = method.getAnnotation(PostMapping.class);
            if (handlerPostMapping == null) {
                return; // Skip if no @PostMapping annotation is present
            }

            // Get API names from @PostMapping
            String[] apiNames = handlerPostMapping.value();
            if (apiNames == null || apiNames.length == 0) {
                return; // Skip if no API names are defined
            }

            // Check for @Operation annotation at the method level
            Operation handlerOperation = method.getAnnotation(Operation.class);
            String handlerDescription = handlerOperation != null ? handlerOperation.description() : "";

            // Process API groups and names
            for (String apiGroup : controllerApiGroups) {
                // Add API group description
                API_GROUPS.put(apiGroup, apiGroupDescription);

                // Add APIF permissions
                for (String apiName : apiNames) {
                    String apiPath = String.format("%s:%s", apiGroup, apiName);
                    PERMISSIONS.put(apiPath, handlerDescription);
                }
            }
        });
    }

//    private void extractPermissions(String path, PathItem pathItem) {
//        if (pathItem.getPost() != null) {
//            processOperation(pathItem.getPost(), path);
//        }
//    }
//
//    private void processOperation(Operation operation, String path) {
//        if (operation.getTags() == null) {
//            return;
//        }
//
//        for (String tag : operation.getTags()) {
//            Tag apiTag = openAPI.getTags()
//                    .stream()
//                    .filter(t -> t.getName().equals(tag))
//                    .findFirst()
//                    .orElse(null);
//
//            String apiGroupDescription = apiTag != null ? apiTag.getDescription() : "";
//
//            // Lưu API nhóm
//            API_GROUPS.put(tag, apiGroupDescription);
//
//            // Lưu quyền API
//            PERMISSIONS.put(String.format("%s:%s", tag, path), operation.getSummary());
//        }
//    }
}
