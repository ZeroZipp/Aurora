package com.zerozipp.client;

import com.zerozipp.client.utils.Injector;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Aurora(Type.INJECT)
@SuppressWarnings("unused")
public class Invoker {
    public static final Injector injector = new Injector(Invoker.class, Events.class);
    public static final Client client = new Client("Cheatware", 2);
    public static final Logger logger = LogManager.getLogger();

    public static void onStart() {
        client.onStart();
    }

    public static void onKeyboard() {
        client.onKeyboard();
    }

    public static void onUpdate() {
        client.onUpdate();
    }

    public static boolean onEvent(Events event) {
        return client.onEvent(event);
    }
}
