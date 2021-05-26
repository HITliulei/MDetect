package com.ll.centralcontrol.mapper;

import com.ll.centralcontrol.dao.MInterfaceDao;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author SeptemberHX
 * @version 0.1
 * @date 2019/12/28
 */
public interface InterfacesMapper {

    @Select("SELECT * FROM interfaces")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patternUrl", column = "patternUrl"),
            @Result(property = "functionName", column = "functionName"),
            @Result(property = "requestMethod", column = "requestMethod"),
            @Result(property = "returnType", column = "returnType"),
            @Result(property = "serviceId", column = "serviceId")
    })
    List<MInterfaceDao> getAll();

    @Select("SELECT * FROM interfaces WHERE serviceId = #{serviceId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patternUrl", column = "patternUrl"),
            @Result(property = "functionName", column = "functionName"),
            @Result(property = "requestMethod", column = "requestMethod"),
            @Result(property = "returnType", column = "returnType"),
            @Result(property = "serviceId", column = "serviceId")
    })
    List<MInterfaceDao> getByServiceId(String serviceId);

    @Insert("INSERT INTO interfaces (id, patternUrl, functionName, requestMethod, returnType, serviceId)" +
            " VALUES(#{id}, #{patternUrl}, #{functionName}, #{requestMethod}, #{returnType}, #{serviceId});\n")
    void insert(MInterfaceDao interfaceDao);

    @Delete("DELETE FROM interfaces WHERE serviceId = #{serviceId}")
    void deleteByServiceId(String serviceId);
}
