package de.bethibande.launcher.modules.loader;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.bootstrap.IService;
import de.bethibande.launcher.modules.IModule;
import de.bethibande.launcher.modules.IModuleDescription;
import de.bethibande.launcher.modules.Module;
import de.bethibande.launcher.modules.SimpleModuleDescription;
import de.bethibande.launcher.modules.configs.*;
import de.bethibande.launcher.utils.DataSerializer;
import de.bethibande.launcher.utils.FileUtils;
import lombok.Getter;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ModuleLoader implements IModuleLoader {

    // found modules with corresponding description/config
    private final HashMap<File, SimpleModuleDescription> collectedModules = new HashMap<>();

    // loaded and injected modules
    @Getter
    private final List<IModuleHandle> handles = new ArrayList<>();

    private static Field module_description_field;
    private static Field module_handle_field;
    private static Field module_manager_field;
    private static Field module_configManager_field;

    /*private static Field manager_module_field;

    private static Field gson_config_module_field;
    private static Field simple_config_module_field;
    private static Field gson_config_manager_field;
    private static Field simple_config_manager_field;*/

    @Getter
    private final File modulesPath;
    @Getter
    private final File moduleConfigPath;
    @Getter
    private final String moduleDescriptionFile;

    public ModuleLoader(File _modulesPath, File _moduleConfigPath, String _moduleDescriptionFile) {
        modulesPath = _modulesPath;
        moduleConfigPath = _moduleConfigPath;
        moduleDescriptionFile = _moduleDescriptionFile;
    }

    static {
        try {
            module_description_field = Module.class.getDeclaredField("description");
            module_handle_field = Module.class.getDeclaredField("handle");
            module_manager_field = Module.class.getDeclaredField("manager");
            module_configManager_field = Module.class.getDeclaredField("configManager");
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initModuleDirectories() {
        if(!modulesPath.exists()) {
            if(!modulesPath.mkdir()) {
                Core.loggerInstance.logError("Couldn't create modules directory!");
                System.exit(IService.EXIT_COULD_NOT_CREATE_MODULES_PATH);
            }
        }
        if(!moduleConfigPath.exists()) {
            if(!moduleConfigPath.mkdir()) {
                Core.loggerInstance.logError("Couldn't create modules directory!");
                System.exit(IService.EXIT_COULD_NOT_CREATE_MODULES_CONFIG_PATH);
            }
        }
    }

    @Override
    public void collectAvailableModules() {
        try {
            List<File> potentialModules = new ArrayList<>();
            for (File f : Objects.requireNonNull(modulesPath.listFiles())) {
                if (f.isFile()) {
                    if (f.getAbsolutePath().endsWith(".jar")) {
                        potentialModules.add(f);
                    }
                }
            }
            Core.loggerInstance.logMessage("Found a total of " + potentialModules.size() + " potential modules!");
            for (File f : potentialModules) {
                JarFile jar = new JarFile(f);
                ZipEntry entry = jar.getEntry(moduleDescriptionFile);
                if(entry != null) {
                    InputStream in = jar.getInputStream(entry);
                    try {
                        SimpleModuleDescription description = getModuleDescriptionFromInputStream(in);
                        if(description.getMainService() == null) {
                            collectedModules.put(f, description);
                        } else
                        if(Core.bootstrapInstance.getName().equalsIgnoreCase(description.getMainService())) {
                            collectedModules.put(f, description);
                        }
                    } catch(InvalidModuleDescriptionException e) {
                        Core.loggerInstance.logError("Invalid " + moduleDescriptionFileName + ": " + e.getMessage());
                    } catch(IOException e) { }
                } else Core.loggerInstance.logMessage("Couldn't load jar: " + f + "!");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static SimpleModuleDescription getModuleDescriptionFromInputStream(InputStream in) throws IOException {
        String name = null, version = null, author = null, mainClass = null, description = null, service = null;
        List<String> depend = new ArrayList<>();
        HashMap<String, String> customValues = new HashMap<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String buffer;
        while((buffer = reader.readLine()) != null) {
            buffer = buffer.replaceAll(" ", "");
            String key = buffer.split(":")[0];
            String value = buffer.split(":")[1];

            if(key.equalsIgnoreCase("name")) {
                name = value;
            } else
            if(key.equalsIgnoreCase("version")) {
                version = value;
            } else
            if(key.equalsIgnoreCase("main")) {
                mainClass = value;
            } else
            if(key.equalsIgnoreCase("author")) {
                author = value;
            } else
            if(key.equalsIgnoreCase("description")) {
                description = value;
            } else
            if(key.equalsIgnoreCase("service")) {
                service = value;
            } else
            if(key.equalsIgnoreCase("depend")) {
                    depend.add(value);
                } else {
                customValues.put(key, value);
            }
        }

        if(name == null || mainClass == null) {
            throw new InvalidModuleDescriptionException("Name and or main class not specified in " + moduleDescriptionFileName);
        }
        return new SimpleModuleDescription(name, version, author, mainClass, description, service, customValues, depend);
    }

    @Override
    // inject all the collected and prepared modules
    public void injectCollectedModules() {
        List<URL> urls = new ArrayList<>();
        for(File moduleFile : collectedModules.keySet()) {
            try {
                urls.add(moduleFile.toURI().toURL());
            } catch(MalformedURLException e) {
                Core.loggerInstance.logMessage("Error while loading module: " + moduleFile.getAbsolutePath());
                e.printStackTrace();
            }
        }
        //StaticClassloader classLoader = new StaticClassloader(urls.toArray(new URL[0]), ModuleLoader.class.getClassLoader());
        for(File moduleFile : collectedModules.keySet()) {
            try {
                SimpleModuleDescription description = collectedModules.get(moduleFile);
                StaticClassloader classLoader = new StaticClassloader(new URL[]{moduleFile.toURI().toURL()}, ModuleLoader.class.getClassLoader());

                Class c = Class.forName(description.getMainClass(), true, classLoader);
                Object instance = c.newInstance();
                if(instance instanceof Module) {
                    IModule module = (IModule)instance;
                    SimpleModuleHandle handle = new SimpleModuleHandle(description, module, classLoader, moduleFile, new File(moduleConfigPath + "/" + description.getName() + "/"));
                    handles.add(handle);
                    setModuleVars((Module)module, description, handle);
                    loadConfigManager(module);
                    Core.loggerInstance.logMessage("Injected module: " + description.getName());
                }
            } catch(MalformedURLException e) {
                Core.loggerInstance.logMessage("Error while loading module: " + moduleFile.getAbsolutePath());
                e.printStackTrace();
            } catch(ClassNotFoundException e) {
                Core.loggerInstance.logError("Error while loading module: " + moduleFile.getAbsolutePath() + " Main class not found");
            } catch(InstantiationException e) {
                Core.loggerInstance.logError("Error while loading module: " + moduleFile.getAbsolutePath() + " Cannot create a new instance of main class");
            } catch(IllegalAccessException e) {
                Core.loggerInstance.logError("Error while loading module: " + moduleFile.getAbsolutePath() + "");
                e.printStackTrace();
            }
        }
    }

    private void loadConfigManager(IModule module) {
        ModuleConfigManager manager = null;
        try {
            if (!module.getHandle().getModuleConfigPath().exists()) {
                manager = new ModuleConfigManager(module, new ArrayList<>());
            } else {
                File managerFile = new File(module.getHandle().getModuleConfigPath() + "/configs.ser");
                if (managerFile.exists()) {
                    String ser = FileUtils.readFile(managerFile).get(0);
                    ArrayList<IConfigReference> references = (ArrayList<IConfigReference>)DataSerializer.deserialize(ser);
                    manager = new ModuleConfigManager(module, references);
                } else manager = new ModuleConfigManager(module, new ArrayList<>());
            }
        } catch(Exception e) {
            Core.loggerInstance.logError("Error while loading module: " + module.getName() + ", couldn't load the module config manager.");
            e.printStackTrace();
        }
        if (manager == null) {
            Core.loggerInstance.logError("Error while loading module: " + module.getName() + ", couldn't load the module config manager.");
            unloadModule(module.getHandle());
            return;
        }

        try {
            module_configManager_field.setAccessible(true);
            module_configManager_field.set(module, manager);
            module_configManager_field.setAccessible(false);
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /*private void overrideModuleConfigFields(IModuleConfig config, IModuleConfigManager manager, IModule module) throws IllegalAccessException {
        if(config instanceof SimpleModuleConfig) {
            SimpleModuleConfig smc = (SimpleModuleConfig)config;
            simple_config_module_field.setAccessible(true);
            simple_config_manager_field.setAccessible(true);
            simple_config_module_field.set(smc, module);
            simple_config_manager_field.set(smc, manager);
            simple_config_manager_field.setAccessible(false);
            simple_config_module_field.setAccessible(false);
        } else if(config instanceof GsonModuleConfig) {
            GsonModuleConfig gmc = (GsonModuleConfig)config;
            gson_config_module_field.setAccessible(true);
            gson_config_manager_field.setAccessible(true);
            gson_config_module_field.set(gmc, module);
            gson_config_manager_field.set(gmc, manager);
            gson_config_module_field.setAccessible(false);
            gson_config_manager_field.setAccessible(false);
        }
    }*/

    @Override
    // calls the onEnable method of all the injected modules
    public void enableAllModules() {
        for(IModuleHandle handle : handles) {
            try {
                handle.getModule().onEnable();
                Core.loggerInstance.logMessage("Enabled module: " + handle.getDescription().getName());
            } catch(Exception e) {
                Core.loggerInstance.logError("Error while enabling module: " + handle.getDescription().getName());
                e.printStackTrace();
            }
        }
    }

    @Override
    // calls the onDisable method of all the running modules
    public void disableAllModules() {
        Core.loggerInstance.logMessage("Disabling all modules and saving configs..");
        for(IModuleHandle handle : handles) {
            Core.eventManager.unregisterListeners(handle.getModule());
            handle.getModule().onDisable();
            handle.getModule().getConfigManager().saveAll();
            handle.getModule().getConfigManager().save();
            Core.loggerInstance.logMessage("Disabled module: " + handle.getDescription().getName());
        }
    }

    @Override
    // not recommended, restart instead of using this to reload modules
    public void unloadAllModules() {
        boolean error = false;
        for(IModuleHandle handle : handles) {
            try {
                Core.eventManager.unregisterListeners(handle.getModule());
                handle.getClassLoader().close();
            } catch(IOException e) {
                Core.loggerInstance.logError("An error occurred while unloading the module: " + handle.getDescription().getName());
                error = true;
            }
        }
        handles.clear();
        System.gc();
        Core.loggerInstance.logMessage("Unloading all modules " + (error ? "not successful" : "successfully") +"!");
    }

    @Override
    // not recommended, restart instead of using this to reload modules
    public void unloadModule(IModuleHandle handle) {
        try {
            Core.eventManager.unregisterListeners(handle.getModule());
            handle.getClassLoader().close();
        } catch(IOException e) {
            Core.loggerInstance.logError("An error occurred while unloading the module: " + handle.getDescription().getName());
        }
        handles.remove(handle);
        System.gc();
    }

    private void setModuleVars(Module module, IModuleDescription description, IModuleHandle handle) {
        try {
            module_description_field.setAccessible(true);
            module_description_field.set(module, description);
            module_description_field.setAccessible(false);

            module_handle_field.setAccessible(true);
            module_handle_field.set(module, handle);
            module_handle_field.setAccessible(false);

            module_manager_field.setAccessible(true);
            module_manager_field.set(module, Core.moduleManager);
            module_manager_field.setAccessible(false);
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
