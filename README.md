# Note
This is a fork of [Siphalor/amecs](https://github.com/Siphalor/amecs)

# Amecs
![logo](src/main/resources/assets/amecs/logo.png?raw=true)

## API
If you want to use the api provided by this mod you'll want to implement and include this mod:

[See here 😜](https://github.com/Klotzi111/amecs-api)

## DEV Runtime

If you're a modder and you want Amecs in you development build you can do so by including it like this in the `build.gradle`:

```groovy
repositories {
	maven {
		url "https://jitpack.io"
	}
}

dependencies {
	modRuntimeOnly("com.github.Klotzi111:amecs:multiversion-SNAPSHOT")
}
```

## License

This mod is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
