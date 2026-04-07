/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.chandanv89.springdoc.redoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot auto-configuration for the Redoc API documentation UI.
 *
 * <p>This configuration is activated when:
 * <ul>
 *   <li>The application is a servlet-based web application</li>
 *   <li>The property {@code springdoc.redoc.enabled} is {@code true} (default)</li>
 * </ul>
 *
 * <p>It registers the following beans:
 * <ul>
 *   <li>{@link RedocIndexTransformer} — renders the HTML template with configuration</li>
 *   <li>{@link RedocWelcomeController} — serves the Redoc HTML page at the configured path</li>
 *   <li>{@link RedocWebMvcConfigurer} — serves the bundled Redoc JS as a static resource</li>
 *   <li>{@link RedocActuatorEndpoint} — (optional) exposes Redoc on the actuator management port</li>
 * </ul>
 *
 * @author Chandan Veerabhadrappa @chandanv89
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(name = "springdoc.redoc.enabled", matchIfMissing = true)
@EnableConfigurationProperties(RedocConfigProperties.class)
public class RedocAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RedocAutoConfiguration.class);

    /**
     * Creates the {@link RedocIndexTransformer} bean that renders the HTML template.
     *
     * @param properties the Redoc configuration properties
     * @return a new {@link RedocIndexTransformer} instance
     */
    @Bean
    @ConditionalOnMissingBean
    public RedocIndexTransformer redocIndexTransformer(final RedocConfigProperties properties) {
        if (log.isInfoEnabled()) {
            log.info("Redoc UI is enabled at endpoint `{}`", properties.getPath());
        }
        return new RedocIndexTransformer(properties);
    }

    /**
     * Creates the {@link RedocWelcomeController} bean that serves the Redoc page.
     *
     * @param indexTransformer the transformer that renders the HTML
     * @return a new {@link RedocWelcomeController} instance
     */
    @Bean
    @ConditionalOnMissingBean
    public RedocWelcomeController redocWelcomeController(final RedocIndexTransformer indexTransformer) {
        return new RedocWelcomeController(indexTransformer);
    }

    /**
     * Creates the {@link RedocWebMvcConfigurer} bean that registers static resource handlers.
     *
     * @return a new {@link RedocWebMvcConfigurer} instance
     */
    @Bean
    @ConditionalOnMissingBean
    public RedocWebMvcConfigurer redocWebMvcConfigurer() {
        return new RedocWebMvcConfigurer();
    }

    /**
     * Nested configuration for Spring Boot Actuator support.
     * Only activated when the Actuator is on the classpath and
     * {@code springdoc.redoc.actuator.enabled=true}.
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.springframework.boot.actuate.endpoint.annotation.Endpoint")
    @ConditionalOnProperty(name = "springdoc.redoc.actuator.enabled", havingValue = "true")
    static class RedocActuatorConfiguration {

        /**
         * Creates the {@link RedocActuatorEndpoint} bean that exposes Redoc on the management port.
         *
         * @param indexTransformer the transformer that renders the HTML
         * @return a new {@link RedocActuatorEndpoint} instance
         */
        @Bean
        @ConditionalOnMissingBean
        public RedocActuatorEndpoint redocActuatorEndpoint(final RedocIndexTransformer indexTransformer) {
            return new RedocActuatorEndpoint(indexTransformer);
        }
    }
}
