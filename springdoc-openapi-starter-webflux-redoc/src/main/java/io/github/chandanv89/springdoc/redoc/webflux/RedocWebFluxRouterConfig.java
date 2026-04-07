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

package io.github.chandanv89.springdoc.redoc.webflux;

import io.github.chandanv89.springdoc.redoc.RedocIndexTransformer;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Provides a reactive {@link RouterFunction} that serves the Redoc HTML page.
 *
 * <p>This is the WebFlux equivalent of the WebMVC {@code RedocWelcomeController}.
 * The route is hidden from the OpenAPI specification by default since it uses
 * functional routing (not annotated controllers).</p>
 *
 * @author Chandan Veerabhadrappa (@chandanv89)
 */
public class RedocWebFluxRouterConfig {

    private final RedocIndexTransformer indexTransformer;
    private final String path;

    /**
     * Creates a new router configuration.
     *
     * @param indexTransformer the transformer that renders the Redoc HTML
     * @param path the URL path to serve the Redoc page at
     */
    public RedocWebFluxRouterConfig(final RedocIndexTransformer indexTransformer, final String path) {
        this.indexTransformer = indexTransformer;
        this.path = path;
    }

    /**
     * Creates the {@link RouterFunction} that serves the Redoc page at the configured path.
     *
     * @return the router function for the Redoc endpoint
     */
    public RouterFunction<ServerResponse> redocRouterFunction() {
        return RouterFunctions.route()
                .GET(path, request -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .bodyValue(indexTransformer.render()))
                .build();
    }
}
