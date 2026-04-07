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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Redoc API documentation UI.
 *
 * <p>All properties are bound under the {@code springdoc.redoc} prefix.
 * Redoc-native options are converted to HTML attributes on the {@code <redoc>} element.</p>
 *
 * @author Chandan Veerabhadrappa (@chandanv89)
 */
@ConfigurationProperties(prefix = "springdoc.redoc")
public class RedocConfigProperties {

    /** Whether the Redoc UI endpoint is enabled. */
    private boolean enabled = true;

    /** The URL path where the Redoc UI is served. */
    private String path = "/redoc";

    /** The URL of the OpenAPI specification to render. */
    private String specUrl = "/v3/api-docs";

    /** The HTML page title for the Redoc documentation page. */
    private String title = "API Documentation";

    /**
     * Whether to load the Redoc JS from a CDN instead of the bundled version.
     * Set to {@code true} for CDN mode; {@code false} (default) uses the bundled JS.
     */
    private boolean useCdn = false;

    /** The CDN URL for the Redoc standalone JS file. Used only when {@code useCdn} is {@code true}. */
    private String cdnUrl = "https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js";

    /**
     * Google Fonts (or any web font provider) URL for loading custom fonts.
     * Defaults to Inter (body/headings) and JetBrains Mono (code).
     * Set to an empty string to disable external font loading.
     */
    private String fontUrl = "https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&family=JetBrains+Mono:wght@400;500;700&display=swap";

    /** When {@code true}, disables search indexing and hides the search box. */
    private Boolean disableSearch;

    /** When {@code true}, hides the 'Download' button for saving the API definition source file. */
    private Boolean hideDownloadButtons;

    /** When {@code true}, displays required properties first in schemas. */
    private Boolean sortRequiredPropsFirst;

    /**
     * Controls which responses to expand by default.
     * Specify response codes as a comma-separated list (e.g. {@code "200,201"}) or {@code "all"}.
     */
    private String expandResponses;

    /** When {@code true}, displays the path link and HTTP verb in the middle panel. */
    private Boolean pathInMiddlePanel;

    /** When {@code true}, hides the hostname in the operation definition. */
    private Boolean hideHostname;

    /** When {@code true}, hides the loading animation. */
    private Boolean hideLoading;

    /** When {@code true}, uses native browser scrollbars instead of custom ones. */
    private Boolean nativeScrollbars;

    /**
     * Sets the default expand level for JSON payload samples.
     * The default value is {@code "2"}. Use {@code "all"} to expand all levels.
     */
    private String jsonSamplesExpandLevel;

    /**
     * JSON string for Redoc theme configuration.
     * If not set, a default theme using Inter and JetBrains Mono fonts is applied.
     * Example: {@code {"sidebar": {"backgroundColor": "#fafafa"}, "colors": {"primary": {"main": "#0066cc"}}}}
     */
    private String theme;

    /**
     * Default Redoc theme JSON that sets Inter for body/headings and JetBrains Mono for code blocks.
     */
    private static final String DEFAULT_FONT_THEME =
            "{\"typography\":{" +
            "\"fontFamily\":\"Inter, sans-serif\"," +
            "\"headings\":{\"fontFamily\":\"Inter, sans-serif\"}," +
            "\"code\":{\"fontFamily\":\"\\\"JetBrains Mono\\\", monospace\"}" +
            "}}";

    /**
     * Returns the effective theme JSON to use. If the user has set a custom theme, it is used as-is.
     * Otherwise, the default font theme (Inter + JetBrains Mono) is returned.
     *
     * @return the theme JSON string
     */
    public String getEffectiveTheme() {
        return theme != null ? theme : DEFAULT_FONT_THEME;
    }

    /**
     * Builds a map of non-null Redoc-native options as kebab-case HTML attribute names to their values.
     *
     * @return a map of Redoc HTML attributes
     */
    public Map<String, String> buildRedocAttributes() {
        final var attributes = new LinkedHashMap<String, String>();
        addIfNotNull(attributes, "disable-search", disableSearch);
        addIfNotNull(attributes, "hide-download-buttons", hideDownloadButtons);
        addIfNotNull(attributes, "sort-required-props-first", sortRequiredPropsFirst);
        addIfNotNull(attributes, "expand-responses", expandResponses);
        addIfNotNull(attributes, "path-in-middle-panel", pathInMiddlePanel);
        addIfNotNull(attributes, "hide-hostname", hideHostname);
        addIfNotNull(attributes, "hide-loading", hideLoading);
        addIfNotNull(attributes, "native-scrollbars", nativeScrollbars);
        addIfNotNull(attributes, "json-samples-expand-level", jsonSamplesExpandLevel);
        attributes.put("theme", getEffectiveTheme());
        return attributes;
    }

    /**
     * Converts the Redoc-native options into an HTML attribute string for the {@code <redoc>} element.
     *
     * @return a string of HTML attributes, e.g. {@code disable-search="true" hide-loading="true"}
     */
    public String buildRedocAttributeString() {
        final var attributes = buildRedocAttributes();
        if (attributes.isEmpty()) {
            return "";
        }
        final var joiner = new StringJoiner(" ");
        attributes.forEach((key, value) -> {
            if ("theme".equals(key)) {
                joiner.add(key + "='" + value + "'");
            } else {
                joiner.add(key + "=\"" + value + "\"");
            }
        });
        return joiner.toString();
    }

    private void addIfNotNull(final Map<String, String> map, final String key, final Object value) {
        if (value != null) {
            map.put(key, value.toString());
        }
    }

    // --- Getters and setters ---

    /** @return whether the Redoc UI endpoint is enabled */
    public boolean isEnabled() {
        return enabled;
    }

    /** @param enabled whether the Redoc UI endpoint is enabled */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /** @return the URL path where the Redoc UI is served */
    public String getPath() {
        return path;
    }

    /** @param path the URL path where the Redoc UI is served */
    public void setPath(final String path) {
        this.path = path;
    }

    /** @return the URL of the OpenAPI specification to render */
    public String getSpecUrl() {
        return specUrl;
    }

    /** @param specUrl the URL of the OpenAPI specification to render */
    public void setSpecUrl(final String specUrl) {
        this.specUrl = specUrl;
    }

    /** @return the HTML page title */
    public String getTitle() {
        return title;
    }

    /** @param title the HTML page title */
    public void setTitle(final String title) {
        this.title = title;
    }

    /** @return whether to load Redoc JS from CDN */
    public boolean isUseCdn() {
        return useCdn;
    }

    /** @param useCdn whether to load Redoc JS from CDN */
    public void setUseCdn(final boolean useCdn) {
        this.useCdn = useCdn;
    }

    /** @return the CDN URL for the Redoc standalone JS */
    public String getCdnUrl() {
        return cdnUrl;
    }

    /** @param cdnUrl the CDN URL for the Redoc standalone JS */
    public void setCdnUrl(final String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }

    /** @return the web font CSS URL */
    public String getFontUrl() {
        return fontUrl;
    }

    /** @param fontUrl the web font CSS URL */
    public void setFontUrl(final String fontUrl) {
        this.fontUrl = fontUrl;
    }

    /** @return whether search is disabled */
    public Boolean getDisableSearch() {
        return disableSearch;
    }

    /** @param disableSearch whether search is disabled */
    public void setDisableSearch(final Boolean disableSearch) {
        this.disableSearch = disableSearch;
    }

    /** @return whether download buttons are hidden */
    public Boolean getHideDownloadButtons() {
        return hideDownloadButtons;
    }

    /** @param hideDownloadButtons whether download buttons are hidden */
    public void setHideDownloadButtons(final Boolean hideDownloadButtons) {
        this.hideDownloadButtons = hideDownloadButtons;
    }

    /** @return whether required properties are sorted first */
    public Boolean getSortRequiredPropsFirst() {
        return sortRequiredPropsFirst;
    }

    /** @param sortRequiredPropsFirst whether required properties are sorted first */
    public void setSortRequiredPropsFirst(final Boolean sortRequiredPropsFirst) {
        this.sortRequiredPropsFirst = sortRequiredPropsFirst;
    }

    /** @return response codes to expand by default */
    public String getExpandResponses() {
        return expandResponses;
    }

    /** @param expandResponses response codes to expand by default */
    public void setExpandResponses(final String expandResponses) {
        this.expandResponses = expandResponses;
    }

    /** @return whether path is shown in the middle panel */
    public Boolean getPathInMiddlePanel() {
        return pathInMiddlePanel;
    }

    /** @param pathInMiddlePanel whether path is shown in the middle panel */
    public void setPathInMiddlePanel(final Boolean pathInMiddlePanel) {
        this.pathInMiddlePanel = pathInMiddlePanel;
    }

    /** @return whether hostname is hidden */
    public Boolean getHideHostname() {
        return hideHostname;
    }

    /** @param hideHostname whether hostname is hidden */
    public void setHideHostname(final Boolean hideHostname) {
        this.hideHostname = hideHostname;
    }

    /** @return whether loading animation is hidden */
    public Boolean getHideLoading() {
        return hideLoading;
    }

    /** @param hideLoading whether loading animation is hidden */
    public void setHideLoading(final Boolean hideLoading) {
        this.hideLoading = hideLoading;
    }

    /** @return whether native scrollbars are used */
    public Boolean getNativeScrollbars() {
        return nativeScrollbars;
    }

    /** @param nativeScrollbars whether native scrollbars are used */
    public void setNativeScrollbars(final Boolean nativeScrollbars) {
        this.nativeScrollbars = nativeScrollbars;
    }

    /** @return the default expand level for JSON payload samples */
    public String getJsonSamplesExpandLevel() {
        return jsonSamplesExpandLevel;
    }

    /** @param jsonSamplesExpandLevel the default expand level for JSON payload samples */
    public void setJsonSamplesExpandLevel(final String jsonSamplesExpandLevel) {
        this.jsonSamplesExpandLevel = jsonSamplesExpandLevel;
    }

    /** @return the custom Redoc theme JSON, or {@code null} if using the default */
    public String getTheme() {
        return theme;
    }

    /** @param theme the custom Redoc theme JSON */
    public void setTheme(final String theme) {
        this.theme = theme;
    }
}
