# springdoc-openapi-starter-webmvc-redoc

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4%2B-brightgreen.svg)](https://spring.io/projects/spring-boot)

A Spring Boot starter that integrates [Redoc](https://redocly.com/redoc/) API documentation UI with [springdoc-openapi](https://springdoc.org/). Add the dependency, and get beautiful, three-panel API documentation at `/redoc` — zero configuration required.

**Default look**: Inter font for body/headings, JetBrains Mono for code blocks, with full theme customization support.

---

## Table of Contents

- [Features](#features)
- [Quick Start](#quick-start)
- [Configuration Reference](#configuration-reference)
  - [Core Properties](#core-properties)
  - [Font Customization](#font-customization)
  - [Redoc Display Options](#redoc-display-options)
  - [Theme Customization](#theme-customization)
- [Customization Examples](#customization-examples)
  - [Change the Endpoint Path](#change-the-endpoint-path)
  - [Use CDN Instead of Bundled JS](#use-cdn-instead-of-bundled-js)
  - [Custom Fonts](#custom-fonts)
  - [Dark Theme](#dark-theme)
  - [Disable Redoc](#disable-redoc)
  - [Override Beans](#override-beans)
- [Architecture](#architecture)
- [Running the Example App](#running-the-example-app)
- [Building from Source](#building-from-source)
- [Testing](#testing)
  - [Running Tests](#running-tests)
  - [Test Coverage](#test-coverage)
- [Requirements](#requirements)
- [License](#license)

---

## Features

- **Zero-config** — add the dependency, and Redoc is available at `/redoc`
- **Spring Boot 3.x** — built for Java 17+ and Spring Boot 3.4+
- **Bundled Redoc JS** — works in air-gapped / enterprise environments out of the box
- **CDN mode** — opt-in to load Redoc JS from a CDN instead
- **Modern fonts by default** — Inter (body/headings) + JetBrains Mono (code), fully overridable
- **Full Redoc theme support** — colors, sidebar, typography, spacing, all via `application.yml`
- **All Redoc display options** — search, download buttons, response expansion, scrollbars, etc.
- **Auto-configuration** — follows Spring Boot conventions with `@ConditionalOnProperty`, `@ConditionalOnMissingBean`
- **Hidden from OpenAPI spec** — the Redoc controller itself does not pollute your API documentation

---

## Quick Start

### 1. Add Dependencies

Your project needs both `springdoc-openapi` (provides the `/v3/api-docs` endpoint) and this starter (provides the Redoc UI).

**Gradle:**

```groovy
dependencies {
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.6'
    implementation 'io.github.chandanv89:springdoc-openapi-starter-webmvc-redoc:1.0.0-SNAPSHOT'
}
```

**Maven:**

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-api</artifactId>
    <version>2.8.6</version>
</dependency>
<dependency>
    <groupId>io.github.chandanv89</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-redoc</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 2. Start Your Application

```bash
./gradlew bootRun
```

### 3. Open Redoc

Visit [http://localhost:8080/redoc](http://localhost:8080/redoc)

That's it. No `@Configuration` classes, no bean definitions, no HTML files to create.

---

## Configuration Reference

All properties are optional and live under the `springdoc.redoc` prefix in `application.yml` or `application.properties`.

### Core Properties

| Property | Type | Default | Description |
|---|---|---|---|
| `springdoc.redoc.enabled` | `boolean` | `true` | Enable/disable the Redoc UI endpoint entirely |
| `springdoc.redoc.path` | `String` | `/redoc` | URL path where the Redoc page is served |
| `springdoc.redoc.spec-url` | `String` | `/v3/api-docs` | URL of the OpenAPI specification JSON |
| `springdoc.redoc.title` | `String` | `API Documentation` | HTML `<title>` of the Redoc page |
| `springdoc.redoc.use-cdn` | `boolean` | `false` | Load Redoc JS from CDN instead of bundled |
| `springdoc.redoc.cdn-url` | `String` | `https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js` | CDN URL (only used when `use-cdn=true`) |

### Font Customization

| Property | Type | Default | Description |
|---|---|---|---|
| `springdoc.redoc.font-url` | `String` | Google Fonts URL for Inter + JetBrains Mono | Web font CSS URL. Set to empty string `""` to disable external font loading |

The default value loads **Inter** (weights 300, 400, 600, 700) for body text and headings, and **JetBrains Mono** (weights 400, 500, 700) for code blocks.

The default theme automatically configures Redoc to use these fonts. If you override `font-url`, you should also set a matching `theme` to reference your chosen font families.

### Redoc Display Options

These map directly to [Redoc's configuration options](https://redocly.com/docs/redoc/config) and are rendered as HTML attributes on the `<redoc>` element.

| Property | Type | Default | Description |
|---|---|---|---|
| `springdoc.redoc.disable-search` | `Boolean` | _unset_ | Disable search indexing and hide the search box |
| `springdoc.redoc.hide-download-buttons` | `Boolean` | _unset_ | Hide the 'Download' button for the API definition |
| `springdoc.redoc.sort-required-props-first` | `Boolean` | _unset_ | Display required properties first in schemas |
| `springdoc.redoc.expand-responses` | `String` | _unset_ | Response codes to expand by default (`"200,201"` or `"all"`) |
| `springdoc.redoc.path-in-middle-panel` | `Boolean` | _unset_ | Show path and HTTP verb in the middle panel |
| `springdoc.redoc.hide-hostname` | `Boolean` | _unset_ | Hide the hostname in operation definitions |
| `springdoc.redoc.hide-loading` | `Boolean` | _unset_ | Hide the loading animation |
| `springdoc.redoc.native-scrollbars` | `Boolean` | _unset_ | Use native browser scrollbars instead of custom |
| `springdoc.redoc.json-samples-expand-level` | `String` | _unset_ | Default expand level for JSON samples (`"2"`, `"all"`) |

> **Note:** Properties marked _unset_ are not rendered unless explicitly configured. Redoc uses its own built-in defaults for those options.

### Theme Customization

| Property | Type | Default | Description |
|---|---|---|---|
| `springdoc.redoc.theme` | `String` (JSON) | Auto-generated Inter + JetBrains Mono theme | Full Redoc theme configuration as a JSON string |

When no custom theme is set, the starter automatically injects a theme that configures:

- `typography.fontFamily` → `Inter, sans-serif`
- `typography.headings.fontFamily` → `Inter, sans-serif`
- `typography.code.fontFamily` → `"JetBrains Mono", monospace`

Setting a custom `theme` **fully replaces** the default — there is no merging.

See the [Redoc theme documentation](https://redocly.com/docs/redoc/config) for all available theme options.

---

## Customization Examples

### Change the Endpoint Path

```yaml
springdoc:
  redoc:
    path: /api-docs
    title: My Service - API Reference
```

### Use CDN Instead of Bundled JS

```yaml
springdoc:
  redoc:
    use-cdn: true
    # Optionally pin a specific version:
    cdn-url: https://cdn.redoc.ly/redoc/v2.5.0/bundles/redoc.standalone.js
```

### Custom Fonts

To use Montserrat + Fira Code instead of the defaults:

```yaml
springdoc:
  redoc:
    font-url: "https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;700&family=Fira+Code:wght@400;500;700&display=swap"
    theme: >
      {
        "typography": {
          "fontFamily": "Montserrat, sans-serif",
          "headings": { "fontFamily": "Montserrat, sans-serif" },
          "code": { "fontFamily": "\"Fira Code\", monospace" }
        }
      }
```

To disable external font loading entirely (use system fonts):

```yaml
springdoc:
  redoc:
    font-url: ""
    theme: >
      {
        "typography": {
          "fontFamily": "-apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif",
          "code": { "fontFamily": "Consolas, 'Courier New', monospace" }
        }
      }
```

### Dark Theme

```yaml
springdoc:
  redoc:
    theme: >
      {
        "sidebar": {
          "backgroundColor": "#1a1a2e",
          "textColor": "#e0e0e0",
          "activeTextColor": "#e94560"
        },
        "colors": {
          "primary": { "main": "#e94560" },
          "text": { "primary": "#e0e0e0" },
          "http": {
            "get": "#61affe",
            "post": "#49cc90",
            "put": "#fca130",
            "delete": "#f93e3e"
          }
        },
        "typography": {
          "fontFamily": "Inter, sans-serif",
          "headings": { "fontFamily": "Inter, sans-serif" },
          "code": {
            "fontFamily": "\"JetBrains Mono\", monospace",
            "backgroundColor": "#2d2d44"
          }
        },
        "rightPanel": {
          "backgroundColor": "#1a1a2e"
        }
      }
```

### Disable Redoc

```yaml
springdoc:
  redoc:
    enabled: false
```

### Override Beans

All beans are registered with `@ConditionalOnMissingBean`, so you can replace any component:

```java
@Configuration
public class CustomRedocConfig {

    @Bean
    public RedocIndexTransformer redocIndexTransformer(RedocConfigProperties properties) {
        // Custom transformer with additional processing
        return new MyCustomRedocIndexTransformer(properties);
    }
}
```

---

## Architecture

This starter follows the same pattern as `springdoc-openapi-starter-webmvc-ui` (Swagger UI), but simpler:

```
┌─────────────────────────────────────────────────────────┐
│                  RedocAutoConfiguration                 │
│  @AutoConfiguration                                     │
│  @ConditionalOnWebApplication(SERVLET)                  │
│  @ConditionalOnProperty("springdoc.redoc.enabled")      │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌─────────────────────┐  ┌──────────────────────────┐  │
│  │ RedocConfigProperties│  │  RedocIndexTransformer   │  │
│  │                     │  │                          │  │
│  │ springdoc.redoc.*   │──│  Loads redoc.html        │  │
│  │ path, title, theme  │  │  Replaces {{placeholders}}│  │
│  │ fontUrl, specUrl    │  │  Returns rendered HTML   │  │
│  └─────────────────────┘  └──────────┬───────────────┘  │
│                                      │                  │
│  ┌─────────────────────┐  ┌──────────▼───────────────┐  │
│  │ RedocWebMvcConfigurer│  │ RedocWelcomeController   │  │
│  │                     │  │                          │  │
│  │ Serves bundled      │  │ @Hidden (not in OpenAPI) │  │
│  │ redoc.standalone.js │  │ GET /redoc → HTML        │  │
│  └─────────────────────┘  └──────────────────────────┘  │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

| Component | Class | Purpose |
|---|---|---|
| **Properties** | `RedocConfigProperties` | Binds `springdoc.redoc.*`, builds HTML attributes, manages default font theme |
| **Auto-Configuration** | `RedocAutoConfiguration` | Conditional bean registration, activates on servlet web app + enabled property |
| **Template Transformer** | `RedocIndexTransformer` | Loads HTML template, replaces `{{placeholders}}` with config values at request time |
| **Controller** | `RedocWelcomeController` | Serves rendered HTML at configurable path, hidden from OpenAPI spec via `@Hidden` |
| **Resource Configurer** | `RedocWebMvcConfigurer` | Registers `/webjars/redoc/**` resource handler for bundled JS |

### HTML Template Placeholders

The template (`META-INF/resources/redoc/redoc.html`) uses these placeholders:

| Placeholder | Source | Description |
|---|---|---|
| `{{title}}` | `springdoc.redoc.title` | Page `<title>` |
| `{{specUrl}}` | `springdoc.redoc.spec-url` | OpenAPI spec URL for the `<redoc>` element |
| `{{options}}` | All Redoc display options + theme | HTML attributes on the `<redoc>` element |
| `{{redocJsUrl}}` | Bundled path or CDN URL | `<script src>` for the Redoc JS |
| `{{fontUrl}}` | `springdoc.redoc.font-url` | `<link href>` for web fonts |

### Redoc JS Bundling

The Redoc standalone JS (v2.5.0, ~1.5MB) is downloaded during the Gradle build via the `downloadRedocJs` task and bundled into the jar at `META-INF/resources/webjars/redoc/redoc.standalone.js`. This ensures:

- **Air-gapped environments** work out of the box
- **No external network calls** at runtime (unless CDN mode is enabled)
- **Version pinning** — the JS version is locked to the build

The downloaded file is git-ignored and re-downloaded on clean builds.

---

## Running the Example App

The project includes a demo application with a `BookController` showcasing CRUD operations:

```bash
./gradlew :redoc-example-app:bootRun
```

Then open:
- **Redoc UI**: [http://localhost:8080/redoc](http://localhost:8080/redoc)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

The example app demonstrates:
- Zero-config Redoc integration
- `@Operation`, `@ApiResponse`, `@Tag`, `@Parameter` annotations
- Custom Redoc options via `application.yml` (`sort-required-props-first`, `expand-responses`)

---

## Building from Source

```bash
# Full build (downloads Redoc JS, compiles, runs tests)
./gradlew build

# Compile only
./gradlew :springdoc-openapi-starter-webmvc-redoc:compileJava

# Publish to local Maven repository
./gradlew publishToMavenLocal
```

---

## Testing

### Running Tests

```bash
# Run all tests
./gradlew :springdoc-openapi-starter-webmvc-redoc:test

# Run a specific test class
./gradlew :springdoc-openapi-starter-webmvc-redoc:test --tests "*.RedocConfigPropertiesTest"

# Run a specific test method
./gradlew :springdoc-openapi-starter-webmvc-redoc:test --tests "*.RedocConfigPropertiesTest.defaultValues"
```

### Test Coverage

The project has three test classes covering all major functionality:

#### `RedocConfigPropertiesTest`

Tests for the configuration properties class and attribute building logic.

| Test | What it verifies |
|---|---|
| `defaultValues` | All defaults: enabled, path, specUrl, title, fonts, CDN URL |
| `effectiveTheme_returnsDefaultFontThemeWhenNoCustomThemeSet` | Default theme contains Inter and JetBrains Mono font families |
| `effectiveTheme_returnsCustomThemeWhenSet` | Custom theme fully replaces the default |
| `buildRedocAttributes_alwaysIncludesTheme` | Theme is always present in attributes (default or custom) |
| `buildRedocAttributes_includesExplicitOptionsAndTheme` | Explicit options + theme are all included |
| `buildRedocAttributeString_includesDefaultThemeWhenNoOptionsSet` | Attribute string includes the default font theme |
| `buildRedocAttributeString_formatsCorrectly` | Boolean options formatted as `key="value"` |
| `buildRedocAttributeString_customThemeUsesSingleQuotes` | Theme JSON wrapped in single quotes |
| `fontUrl_defaultsToInterAndJetBrainsMono` | Default font URL points to Google Fonts |
| `fontUrl_canBeOverridden` | Custom font URL is accepted |
| `fontUrl_canBeSetToEmptyToDisable` | Empty string disables font loading |
| `setPath_updatesValue` | Parameterized test for path customization |
| `allRedocNativeOptionsMapping` | All 10 Redoc-native options produce correct kebab-case attributes |

#### `RedocAutoConfigurationTest`

Tests for Spring Boot auto-configuration behaviour using `WebApplicationContextRunner`.

| Test | What it verifies |
|---|---|
| `autoConfiguration_registersAllBeans` | All 4 beans are registered with defaults |
| `autoConfiguration_disabled_whenPropertySetToFalse` | No beans when `springdoc.redoc.enabled=false` |
| `autoConfiguration_bindsCustomProperties` | Property values from `application.properties` are bound correctly |
| `autoConfiguration_respectsConditionalOnMissingBean` | User-defined beans take precedence |

#### `RedocIndexTransformerTest`

Tests for the HTML template rendering logic.

| Test | What it verifies |
|---|---|
| `render_replacesTitle` | `{{title}}` placeholder is replaced |
| `render_replacesSpecUrl` | `{{specUrl}}` placeholder is replaced |
| `render_usesBundledJsByDefault` | Default JS source is `/webjars/redoc/redoc.standalone.js` |
| `render_usesCdnUrlWhenEnabled` | CDN URL is used when `use-cdn=true` |
| `render_includesRedocAttributes` | Display options appear as HTML attributes |
| `render_includesCustomThemeAttribute` | Custom theme JSON is rendered in the `<redoc>` tag |
| `render_includesDefaultFontThemeWhenNoCustomTheme` | Default Inter + JetBrains Mono theme is injected |
| `render_includesDefaultFontUrl` | Default Google Fonts URL is in the HTML |
| `render_customFontUrl` | Custom font URL replaces the default |
| `render_emptyFontUrlDisablesFontLoading` | Empty font URL results in empty `href` |
| `render_producesValidHtml` | Output is valid HTML structure |
| `render_defaultValues_producesExpectedOutput` | Full default rendering: title, spec-url, JS path, fonts |

---

## Requirements

- **Java 17+**
- **Spring Boot 3.x**
- **`springdoc-openapi-starter-webmvc-api`** (peer dependency — provides `/v3/api-docs`)

---

## License

Apache License 2.0 — see [LICENSE](LICENSE) for details.

This project bundles [Redoc](https://github.com/Redocly/redoc) (MIT License) — see [NOTICE](NOTICE) for attribution.
