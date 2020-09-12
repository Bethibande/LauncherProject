package de.bethibande.marketplace.moduleloader;

import com.google.gson.Gson;
import de.bethibande.marketplace.Core;
import de.bethibande.marketplace.bootstrap.IService;
import de.bethibande.marketplace.modules.ModuleConfigManager;
import de.bethibande.marketplace.utils.FileUtils;
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

    public static final String moduleDescriptionFileName = "module.yml";

    // found modules with corresponding description/config
    private final HashMap<File, SimpleModuleDescription> collectedModules = new HashMap<>();

    // loaded and injected modules
    @Getter
    private final List<IModuleHandle> handles = new ArrayList<>();

    private static Field module_description_field;
    private static Field module_handle_field;
    private static Field module_manager_field;
    private static Field module_configManager_field;

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
        if(!IModuleLoader.modulesPath.exists()) {
            if(!IModuleLoader.modulesPath.mkdir()) {
                Core.loggerInstance.logError("Couldn't create modules directory!");
                System.exit(IService.EXIT_COULD_NOT_CREATE_MODULES_PATH);
            }
        }
        if(!IModuleLoader.moduleConfigPath.exists()) {
            if(!IModuleLoader.moduleConfigPath.mkdir()) {
                Core.loggerInstance.logError("Couldn't create modules directory!");
                System.exit(IService.EXIT_COULD_NOT_CREATE_MODULES_CONFIG_PATH);
            }
        }
    }

    @Override
    public void collectAvailableModules() {
        try {
            List<File> potentialModules = new ArrayList<>();
            for (File f : Objects.requireNonNull(IModuleLoader.modulesPath.listFiles())) {
                if (f.isFile()) {
                    if (f.getAbsolutePath().endsWith(".jar")) {
                        potentialModules.add(f);
                    }
                }
            }
            Core.loggerInstance.logMessage("Found a total of " + potentialModules.size() + " potential modules!");
            for (File f : potentialModules) {
                JarFile jar = new JarFile(f);
                ZipEntry entry = jar.getEntry("module.yml");
                if(entry != null) {
                    InputStream in = jar.getInputStream(entry);
                    try {
                        SimpleModuleDescription description = getModuleDescriptionFromInputStream(in);
                        collectedModules.put(f, description);
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
        String name = null, version = null, author = null, mainClass = null, description = null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String buffer;
        while((buffer = reader.readLine()) != null) {
            buffer = buffer.replaceAll(" ", "");
            String key = buffer.split(":")[0];
            String value = buffer.split(":")[1];

            if(key.equalsIgnoreCase("name")) name = value;
            if(key.equalsIgnoreCase("version")) version = value;
            if(key.equalsIgnoreCase("main")) mainClass = value;
            if(key.equalsIgnoreCase("author")) author = value;
            if(key.equalsIgnoreCase("description")) description = value;
        }

        if(name == null || mainClass == null) {
            throw new InvalidModuleDescriptionException("Name and or main class not specified in " + moduleDescriptionFileName);
        }
        return new SimpleModuleDescription(name, version, author, mainClass, description);
    }

    @Override
    // inject all the collected and prepared modules
    public void injectCollectedModules() {
        for(File moduleFile : collectedModules.keySet()) {
            try {
                SimpleModuleDescription description = collectedModules.get(moduleFile);
                StaticClassloader classLoader = new StaticClassloader(new URL[]{moduleFile.toURI().toURL()}, ModuleLoader.class.getClassLoader());

                Class c = Class.forName(description.getMainClass(), true, classLoader);
                Object instance = c.newInstance();
                if(instance instanceof Module) {
                    IModule module = (IModule)instance;
                    SimpleModuleHandle handle = new SimpleModuleHandle(description, module, classLoader, moduleFile, new File(IModuleLoader.moduleConfigPath + "/" + description.getName() + "/"));
                    handles.add(handle);
                    setModuleVars((Module)module, description, handle);
                    loadConfigManager(module);
                    Core.loggerInstance.logMessage("Injected module: " + description.getName());
                }
            } catch(MalformedURLException e) {
                Core.loggerInstance.logError("Error while loading module: " + moduleFile.getAbsolutePath());
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
                File managerFile = new File(module.getHandle().getModuleConfigPath() + "/configs.json");
                if (managerFile.exists()) {
                    String json = FileUtils.readFile(managerFile).get(0);
                    manager = new Gson().fromJson(json, ModuleConfigManager.class);
                } else manager = new ModuleConfigManager(module, new ArrayList<>());
            }
        } catch(Exception e) {
            Core.loggerInstance.logError("Error while loading module: " + module.getName() + ", couldn't load the module config manager.");
            e.printStackTrace();
        }
        if (manager == null) {
            Core.loggerInstance.logError("Error while loading module: " + module.getName() + ", couldn't load the module config manager.");
            unloadModule(module.getHandle());
        }

        try {
            module_configManager_field.setAccessible(true);
            module_configManager_field.set(module, manager);
            module_configManager_field.setAccessible(false);
        } catch(IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    // calls the onEnable method of all the injected modules
    public void enableAllModules() {
        for(IModuleHandle handle : handles) {
            handle.getModule().onEnable();
            Core.loggerInstance.logMessage("Enabled module: " + handle.getDescription().getName());
        }
    }

    @Override
    // calls the onDisable method of all the running modules
    public void disableAllModules() {
        Core.loggerInstance.logMessage("Disabling all modules and saving configs..");
        for(IModuleHandle handle : handles) {
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
    public void unloadModule(IModuleHandle handle) {
        try {
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
