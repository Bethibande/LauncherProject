package de.bethibande.launcher.events;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.bootstrap.IService;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class EventManager {

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

    public void runEvent(Event e) {
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
