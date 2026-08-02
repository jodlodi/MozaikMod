package com.mod.mozaik.platform;

import com.mod.mozaik.Constants;
import com.mod.mozaik.platform.services.IModloaderHelper;
import com.mod.mozaik.platform.services.INetworkHelper;
import com.mod.mozaik.platform.services.IPlatformHelper;
import com.mod.mozaik.platform.services.IRegistryHelper;
import org.jspecify.annotations.NullMarked;

import java.util.ServiceLoader;

@NullMarked
public class Services {
    public static final IModloaderHelper MODLOADER = load(IModloaderHelper.class);
    public static final INetworkHelper NETWORK = load(INetworkHelper.class);
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}