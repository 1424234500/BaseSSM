package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * org.json.tools
 */


public class JsonUtil {


    public static String makeJson(Object obj) {
        String res = "";
        try {
            JSONObject jo = new JSONObject(obj);
            res = jo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public static String makeJson(List<Object> list) {
        String res = "";
        try {
            JSONArray ja = new JSONArray(list);
            res = ja.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String makeJson(Object cmd, Object obj) {
        String res = "";
        try {
            JSONObject jomap = new JSONObject(obj);

            JSONObject jo = new JSONObject();
            jo.put("cmd", cmd);
            jo.put("result", jomap);
            res = jo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    public static String makeJson(Object cmd, List<Object> list) {
        String res = "";
        try {
            JSONObject jo = new JSONObject();
            jo.put("cmd", cmd);
            jo.put("result", JsonUtil.listMapToJSONArray(list));
            res = jo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    // msg json {"cmd":"conn","value":"connok" }
    public static String makeJson(Object cmd, Object... values) {
        String res = "";
        try {
            JSONObject jo = new JSONObject();
            jo.put("cmd", cmd);
            int i = 0;
            for (Object value : values) {
                jo.put("value" + i++, value);
            }
            res = jo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static List<Map<String, Object>> getList(String jsonstr, String name) {
        try {
            JSONObject jo = new JSONObject(jsonstr);
            return JsonUtil.jsonArrayToListMap(jo.getJSONArray(name));
        } catch (Exception e) {
            out("json get list error from " + jsonstr + " exception：" + e.toString());
        }
        return null;
    }

    public static String getValue0(String jsonstr) {
        return getString(jsonstr, "value0");
    }

    public static String getValue1(String jsonstr) {
        return getString(jsonstr, "value1");
    }

    public static String getValue2(String jsonstr) {
        return getString(jsonstr, "value2");
    }

    public static String getValue3(String jsonstr) {
        return getString(jsonstr, "value3");
    }

    public static String getValueI(String jsonstr, int i) {
        return getString(jsonstr, "value" + i);
    }

    public static int getCmd(String jsonstr) {
        return getInt(jsonstr, "cmd");
    }

    public static String getString(String jsonstr, String name) {
        String res = "";
        try {
            JSONObject jo = new JSONObject(jsonstr);
            res = jo.getString(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int getInt(String jsonstr, String name) {
        int res = -1;
        try {
            JSONObject jo = new JSONObject(jsonstr);
            res = jo.getInt(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public static JSONArray listMapToJSONArray(List<Object> list) {
        if (list == null) return new JSONArray();
        return new JSONArray(list);
    }

    public static List<Map<String, Object>> jsonArrayToListMap(JSONArray jsonArray) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String name;

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                Iterator<String> nameItr = jo.keys();
                Map<String, Object> map = new HashMap<String, Object>();
                while (nameItr.hasNext()) {
                    name = nameItr.next();
                    map.put(name, jo.getString(name));
                }
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public static List<Map<String, Object>> getListType(String jsonstr) {
        List<Map<String, Object>> res = new ArrayList<>();

        try {
            JSONObject jo = new JSONObject(jsonstr);
            res = JsonUtil.jsonArrayToListMap(jo.getJSONArray("listtype"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static List<Map<String, Object>> getList(String jsonstr) {
        List<Map<String, Object>> res = new ArrayList<>();
        try {
            JSONObject jo = new JSONObject(jsonstr);
            res = JsonUtil.jsonArrayToListMap(jo.getJSONArray("list"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public static Map<String, Object> getMap(String jsonstr) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject jo = new JSONObject(jsonstr).getJSONObject("result");
            String name;
            Iterator<String> nameItr = jo.keys();
            while (nameItr.hasNext()) {
                name = nameItr.next();
                map.put(name, jo.getString(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    public static List<Map<?, ?>> orderListMap(List<Map<?, ?>> list) {
        return list;
    }


    public static void out(String str) {

    }
}
