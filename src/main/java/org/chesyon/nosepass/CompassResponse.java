package org.chesyon.nosepass;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class CompassResponse {
    private boolean succeeded;
    private ArrayList<String> sections;
    private ArrayList<Issue> issues;
    private MappingResult na;
    private MappingResult eu;
    private MappingResult jp;

    public static CompassResponse fromJsonStr(String s) {
        if (s == null)
            return null;
        return fromJson(new JSONObject(s));
    }

    public static CompassResponse fromJson(JSONObject json) {
        boolean suc = json.getBoolean("succeeded");
        JSONArray sections_arr = json.getJSONArray("sections");
        ArrayList<String> sec = new ArrayList<String>();
        for (int i = 0; i < sections_arr.length(); i++) {
            sec.add(sections_arr.getString(i));
        }
        JSONArray issues_arr = json.getJSONArray("issues");
        ArrayList<Issue> iss = new ArrayList<Issue>();
        for (int i = 0; i < issues_arr.length(); i++) {
            iss.add(Issue.valueOf(issues_arr.getString(i)));
        }
        // If succeeded, mapping results should not be null
        MappingResult n = null;
        MappingResult e = null;
        MappingResult j = null;
        if (suc) {
            n = new MappingResult(json.getJSONObject("na"));
            e = new MappingResult(json.getJSONObject("eu"));
            j = new MappingResult(json.getJSONObject("jp"));
        }
        return new CompassResponse(suc, sec, iss, n, e, j);
    }

    public CompassResponse(boolean suc, ArrayList<String> sec, ArrayList<Issue> iss, MappingResult n, MappingResult e,
            MappingResult j) {
        succeeded = suc;
        sections = sec;
        issues = iss;
        na = n;
        eu = e;
        jp = j;
    }

    public boolean getSucceeded() {
        return succeeded;
    }

    public ArrayList<String> getSections() {
        return sections;
    }

    public ArrayList<Issue> getIssues() {
        return issues;
    }

    public MappingResult getNa() {
        return na;
    }

    public MappingResult getEu() {
        return eu;
    }

    public MappingResult getJp() {
        return jp;
    }
}
