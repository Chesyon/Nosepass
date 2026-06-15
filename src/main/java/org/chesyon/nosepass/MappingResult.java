package org.chesyon.nosepass;

import org.json.JSONObject;

public class MappingResult {
    private int result;
    private int minimum;
    private int maximum;

    public MappingResult(JSONObject json) {
        result = tryGetInt(json, "result");
        minimum = tryGetInt(json, "minimum");
        maximum = tryGetInt(json, "maximum");
    }

    // Modified version of json.getInt that will return return 0 for null instead of
    // exploding
    private int tryGetInt(JSONObject json, String key) {
        final Object object = json.get(key);
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }
        return 0;
    }

    public int getResult() {
        return result;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public String toString() {
        // literally a direct translation of offset_result_string from sky-compass CLI
        if (result != 0)
            return String.format("0x%X", result);
        if (minimum == 0) {
            if (maximum == 0)
                return "Both nearest symbols were missing for this region, so no information can be given";
            return String.format(
                    "The nearest lesser symbol was missing for this region, but the address should be no greater than 0x%X",
                    maximum);
        }
        if (maximum == 0)
            return String.format(
                    "The nearest greater symbol was missing for this region, but the address should be no less than 0x%X",
                    minimum);
        return String.format("Could not find exact offset. Should be between 0x%X and 0x%X", minimum, maximum);
    }
}