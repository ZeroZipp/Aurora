package com.zerozipp.client.utils.settings;

import com.zerozipp.client.utils.base.Setting;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import java.util.ArrayList;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Active extends Setting {
    public final ArrayList<Listing> listings;

    public Active(String name, ArrayList<Listing> listings) {
        super(name);
        this.listings = listings;
    }

    @Override
    public float getHeight() {
        float size = super.getHeight();
        for(Listing l : listings) size += l.getHeight();
        return size;
    }

    public static class Listing {
        public final String name;
        private boolean active;

        public Listing(String name, boolean active) {
            this.name = name;
            this.active = active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isActive() {
            return active;
        }

        public float getHeight() {
            return 11;
        }
    }
}
