package org.zwx.idea.right.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NewRightModel {

    /**
     * 类名
     */
    private String className;

    /**
     * 所选择的类的类型
     */
    private String json;

    /**
     * 创建java类所在的包
     */
    private String selectedPackage;


    private List<Class> classList;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getSelectedPackage() {
        return selectedPackage;
    }

    public void setSelectedPackage(String selectedPackage) {
        this.selectedPackage = selectedPackage;
    }

    public List<Class> getClassList() {
        return classList;
    }

    public void setClassList(List<Class> classList) {
        this.classList = classList;
    }

    public class Class {
        private String name;
        private String selectedPackage;
        private List<Field> fieldList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public List<Field> getFieldList() {
            return fieldList;
        }

        public void setFieldList(List<Field> fieldList) {
            this.fieldList = fieldList;
        }


        public void setSelectedPackage(String selectedPackage) {
            this.selectedPackage = selectedPackage;
        }

        public String getSelectedPackage() {
            return selectedPackage;
        }

        public boolean hasList() {
            if (CollectionUtils.isEmpty(fieldList)) {
                return false;
            }
            return fieldList.stream().anyMatch(field -> field.type.contains("List"));
        }
    }

    public class Field {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String type;
    }


    public void initClassList() {
        List<Class> cList = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(json);
        parse(className, jsonObject, cList);
        for (Class c : cList) {
            c.setSelectedPackage(selectedPackage);
        }
        this.classList = cList;
    }

    public void parse(String className, JSONObject jsonObject, List<Class> classList) {
        Class c = new Class();
        c.setName(className);
        if (jsonObject == null) {
            classList.add(c);
            return;
        }
        List<Field> fieldList = new ArrayList<>();
        for (Map.Entry<String, Object> en : jsonObject.entrySet()) {
            Field field = new Field();
            field.setName(en.getKey());
            Object value = en.getValue();
            if (value instanceof JSONObject) {
                String cName = captureName(en.getKey());
                field.setType(cName);
                parse(cName, (JSONObject) value, classList);
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                if (jsonArray.isEmpty()) {
                    field.setType("List<Object>");
                } else {
                    Object o = jsonArray.get(0);
                    if (o instanceof JSONObject) {
                        String cName = captureName(en.getKey());
                        field.setType("List<" + cName + ">");
                        parse(cName, jsonArray.getJSONObject(0), classList);
                    } else if (o instanceof String) {
                        field.setType("List<String>");
                    } else if (o instanceof Integer) {
                        field.setType("List<Integer>");
                    } else if (o instanceof Long) {
                        field.setType("List<Long>");
                    } else if (o instanceof Double) {
                        field.setType("List<Double>");
                    } else if (o instanceof Float) {
                        field.setType("List<Float>");
                    } else if (o instanceof Boolean) {
                        field.setType("List<Boolean>");
                    } else if (o instanceof Date) {
                        field.setType("List<Date>");
                    } else {
                        field.setType("List<Object>");
                    }
                }


            } else if (value instanceof Integer) {
                field.setType("Integer");
            } else if (value instanceof Long) {
                field.setType("Long");
            } else if (value instanceof Double) {
                field.setType("Double");
            }else if (value instanceof Float) {
                field.setType("Float");
            }  else if (value instanceof Date) {
                field.setType("Date");
            } else if (value instanceof String) {
                field.setType("String");
            } else if (value instanceof Boolean) {
                field.setType("Boolean");
            } else {
                field.setType("Object");
            }
            fieldList.add(field);
        }
        c.setFieldList(fieldList);
        classList.add(c);
    }

    //首字母大写
    public static String captureName(String name) {
        //     name = name.substring(0, 1).toUpperCase() + name.substring(1);
//        return  name;
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);

    }
}
