package de.bethibande.launcher.events;

import de.bethibande.launcher.bootstrap.IService;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HandlerList {

    @Getter
    private final HashMap<IService, List<Handler>> handlers = new HashMap<>();

    public void registerHandler(Listener l, Method m, IService service) {
        if(handlers.containsKey(service)) {
            handlers.get(service).add(new Handler(l, m));
        } else {
            List<Handler> listeners = new ArrayList<>();
            listeners.add(new Handler(l, m));
            handlers.put(service, listeners);
        }
    }

    public void unregisterHandlers(IService service) {
        handlers.remove(service);
    }

    public void unregisterHandler(IService service, Listener l) {
        List<Handler> remove = new ArrayList<>();
        for(Handler handler : handlers.get(service)) {
            if(handler.getListener() == l) {
                remove.add(handler);
            }
        }
        remove.forEach(handlers.get(service)::remove);

    }

}
