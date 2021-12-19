// TODO: Uncomment"{{ rootProject.name = providers.gradleProperty("project.name").forUseAtConfigurationTime().get() }}"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }

    versionCatalogs {
        create("pluginLibs") { from(files("gradle/pluginLibs.versions.toml")) }
    }
}

include(":library")
