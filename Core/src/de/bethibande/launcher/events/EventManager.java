package de.bethibande.launcher.events;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.bootstrap.IService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private static final List<Class<? extends Event>> knownEvents = new ArrayList<>();

    public void registerListener(Listener l, IService service) {
        try {
            for(Method m : l.getClass().getDeclaredMethods()) {
                if(m.isAnnotationPresent(EventHandler.class)) {
                    if(m.getParameters().length == 1) {
                        Parameter p = m.getParameters()[0];
                        if(p.getType().getInterfaces().length >= 1 && p.getType().getInterfaces()[0] == Event.class) {
                            Event t = (Event)p.getType().newInstance();
                            t.getHandlers().registerHandler(l, m, service);
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            Core.loggerInstance.logError("Error while registering listener for service: " + service.getName());
        }
    }

    public void unregisterListeners(Listener l, IService service) {
        for(Class<? extends Event> event : knownEvents) {
            try {
                Event e = event.newInstance();
                HandlerList handlers = e.getHandlers();
                handlers.unregisterHandler(service, l);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void unregisterListeners(IService service) {
        for(Class<? extends Event> event : knownEvents) {
            try {
                Event e = event.newInstance();
                HandlerList handlers = e.getHandlers();
                handlers.unregisterHandlers(service);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void runEvent(Event e) {
        if(!knownEvents.contains(e.getClass())) knownEvents.add(e.getClass());
        for(IService s : e.getHandlers().getHandlers().keySet()) {
            for(Handler l : e.getHandlers().getHandlers().get(s)) {
                try {
                    l.getM().invoke(l.getListener(), e);
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
