# Note
This is a fork of [Siphalor/amecs](https://github.com/Siphalor/amecs)

# Amecs
![logo](src/main/resources/assets/amecs/logo.png?raw=true)

## Compatibility (amecs latest release)

### Mods with cross interaction tested to be compatible

- [NMUK](https://github.com/Klotzi111/amecs)
- [KTIG](https://github.com/Klotzi111/ktig)
- [Controlling](https://github.com/jaredlll08/Controlling)

### Incompatible mods

- [interactic](https://github.com/gliscowo/interactic)  
  error: when applying mixins on method `ClientPlayerEntity.dropSelectedItem`

#### Solutions for incompatible mods

The general solution when having two incompatible mods is to remove one of them.

BUT often there is also another option:  
You can replace `amecs` with its dependency mod [amecs-api](https://github.com/Klotzi111/amecs-api).  
This will still give you the additional modifier keys for keybindings.  
But you will lose the extra keybindings that `amecs` adds.

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
