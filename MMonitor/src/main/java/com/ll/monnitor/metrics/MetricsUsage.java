package com.ll.monnitor.metrics;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/29
 */

@Setter
@Getter
public class MetricsUsage {
    //获取的cpu和memory是带单位的字符串，加入转换逻辑，cpu统一单位为m,memory统一单位为Mi,如果不需要转换单位可以去掉相关逻辑
    private static Map<String, Double> UNIT_CPU = new HashMap<>();
    private static Map<String, Double> UNIT_MEMORY = new HashMap<>();

    static {
        UNIT_CPU.put("n", Math.pow(10, -6));
        UNIT_CPU.put("u", Math.pow(10, -3));
        UNIT_CPU.put("m", Math.pow(10, 0));
        UNIT_CPU.put("", Math.pow(10, 3));
        UNIT_CPU.put("k", Math.pow(10, 6));
        UNIT_CPU.put("M", Math.pow(10, 9));
        UNIT_CPU.put("G", Math.pow(10, 3*4));
        UNIT_CPU.put("T", Math.pow(10, 3*5));
        UNIT_CPU.put("P", Math.pow(10, 3*6));
        UNIT_CPU.put("E", Math.pow(10, 3*7));

        UNIT_MEMORY.put("Ki", Math.pow(1024, -1));
        UNIT_MEMORY.put("Mi", Math.pow(1024, 0));
        UNIT_MEMORY.put("Gi", Math.pow(1024, 1));
        UNIT_MEMORY.put("Ti", Math.pow(1024, 2));
        UNIT_MEMORY.put("Pi", Math.pow(1024, 3));
        UNIT_MEMORY.put("Ei", Math.pow(1024, 4));
    }

    private String cpu;

    private String memory;

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public static Long commomUnifiedUnit(String unitValStr, int unitLength, Map<String, Double> unitMap) {
        Long r = 0l;
        if(unitValStr == null) {
            return r;
        }
        String unit = unitValStr.substring(unitValStr.length()-unitLength, unitValStr.length());
        String val = unitValStr.substring(0,unitValStr.length()-unitLength);
        if(unit.matches("\\d")) {
            unit="";
            val=unitValStr;
        }
        Double factor = unitMap.get(unit);
        if(factor==null) {
            throw new IllegalArgumentException("无法解析单位:"+unitValStr);
        }
        r = Math.round(Long.parseLong(val)*factor);
        return r;
    }

    public Long getCpuInM() {
        return commomUnifiedUnit(cpu,1,UNIT_CPU);
    }

    public Long getMemoryInMi() {
        return commomUnifiedUnit(memory, 2, UNIT_MEMORY);
    }

    @Override
    public String toString() {
        return "{" +
                "\"cpu(n)\":\"" + cpu + "\"" +
                ", \"memory(ki)\":\"" + memory + "\"" +
                ", \"cpu(m)\":\"" + getCpuInM() + "\"" +
                ", \"memory(mi)\":\"" + getMemoryInMi() + "\"" +
                "}";
    }
}

