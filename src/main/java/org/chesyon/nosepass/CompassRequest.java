package org.chesyon.nosepass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.json.JSONObject;

public class CompassRequest {
    private @Nonnull String region;
    private int address;
    private @Nullable String section;

    public CompassRequest(@Nonnull String r, int a, @Nullable String s) {
        region = r;
        address = a;
        section = s;
    }

    public String toJsonStr() {
        return toJson().toString();
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("region", region);
        obj.put("address", address);
        obj.put("section", section);
        return obj;
    }
}
